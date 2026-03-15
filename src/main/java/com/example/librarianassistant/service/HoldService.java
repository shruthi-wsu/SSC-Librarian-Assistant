package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.HoldResponse;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.exception.ResourceNotFoundException;
import com.example.librarianassistant.model.Book;
import com.example.librarianassistant.model.Hold;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.BookRepository;
import com.example.librarianassistant.repository.CheckoutRepository;
import com.example.librarianassistant.repository.HoldRepository;
import com.example.librarianassistant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HoldService {

    private final HoldRepository holdRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    @Transactional
    public HoldResponse placeHold(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        if (checkoutRepository.findByUserIdAndBookIdAndStatus(
                userId, bookId, com.example.librarianassistant.model.Checkout.CheckoutStatus.ACTIVE).isPresent()) {
            throw new BusinessException("User already has this book checked out");
        }

        if (holdRepository.findByUserIdAndBookIdAndStatus(userId, bookId, Hold.HoldStatus.PENDING).isPresent()) {
            throw new BusinessException("User already has an active hold for this book");
        }

        long pendingCount = holdRepository.countByBookIdAndStatus(bookId, Hold.HoldStatus.PENDING);
        int queuePosition = (int) pendingCount + 1;
        LocalDate holdDate = LocalDate.now();
        LocalDate expirationDate = holdDate.plusDays(7);

        Hold hold = Hold.builder()
                .user(user)
                .book(book)
                .holdDate(holdDate)
                .expirationDate(expirationDate)
                .queuePosition(queuePosition)
                .build();

        HoldResponse response = toResponse(holdRepository.save(hold));
        log.info("Hold placed: holdId={}, userId={}, book='{}', queuePosition={}",
                response.getId(), userId, book.getTitle(), queuePosition);
        return response;
    }

    @Transactional
    public void cancelHold(Long holdId, String requestingEmail) {
        Hold hold = holdRepository.findById(holdId)
                .orElseThrow(() -> new ResourceNotFoundException("Hold not found: " + holdId));

        User requester = userRepository.findByEmail(requestingEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + requestingEmail));

        boolean isLibrarian = requester.getRole() == User.Role.LIBRARIAN;
        boolean isOwner = hold.getUser().getId().equals(requester.getId());

        if (!isLibrarian && !isOwner) {
            throw new BusinessException("Not authorized to cancel this hold");
        }

        hold.setStatus(Hold.HoldStatus.CANCELLED);
        holdRepository.save(hold);
        log.info("Hold cancelled: holdId={}, cancelledBy={}", holdId, requestingEmail);
    }

    @Transactional(readOnly = true)
    public List<HoldResponse> getUserHolds(Long userId) {
        return holdRepository.findByUserIdOrderByHoldDateAsc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HoldResponse> getHoldsByBook(Long bookId) {
        return holdRepository.findByBookIdAndStatusOrderByHoldDateAsc(bookId, Hold.HoldStatus.PENDING).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void processNextHold(Long bookId) {
        holdRepository.findTopByBookIdAndStatusOrderByHoldDateAsc(bookId, Hold.HoldStatus.PENDING)
                .ifPresent(hold -> {
                    hold.setStatus(Hold.HoldStatus.NOTIFIED);
                    holdRepository.save(hold);
                    log.info("Hold notified: holdId={}, bookId={}, userId={}",
                            hold.getId(), bookId, hold.getUser().getId());
                });
    }

    private HoldResponse toResponse(Hold h) {
        return HoldResponse.builder()
                .id(h.getId())
                .userId(h.getUser().getId())
                .userName(h.getUser().getName())
                .bookId(h.getBook().getId())
                .bookTitle(h.getBook().getTitle())
                .holdDate(h.getHoldDate())
                .expirationDate(h.getExpirationDate())
                .queuePosition(h.getQueuePosition())
                .status(h.getStatus())
                .build();
    }
}
