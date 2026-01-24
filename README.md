# Librarian Assistant

## Project Overview

The Librarian Assistant is a comprehensive library management system designed to help librarians efficiently manage common library operations and tasks. This application streamlines the workflow of library staff by providing an intuitive interface for managing books, users, and transactions.

## About This Project

This project is being developed as part of the **Software Quality** course at **Washington State University (WSU)**. The application aims to create a practical solution for real-world library management challenges by working closely with WSU librarians to understand their daily requirements and pain points.

## Repositories
- Backend - This repository handles backend
- Front end - [front end repo url]

## Team Information

**Team Members:**
- Shruthi Mallesh - shruthi.mallesh@wsu.edu
- Sanjeev Sreekumar Krishnan - s.krishnan@wsu.edu
- Chenhua Fan - chenhua.fan@wsu.edu

**Course Information:**
- **Course:** Software Quality
- **Institution:** Washington State University
- **Semester:** Spring 2026
- **Instructor:** Parteek Kumar

## Key Features

The Librarian Assistant supports the following core functionalities:

- **User Registration & Management**
  - Register new library patrons
  - Manage user profiles and information
  - Track user borrowing history

- **Book Search & Browse**
  - Search books by title, author, ISBN, genre, etc.
  - Browse complete library catalog
  - View detailed information about each book

- **Book Management**
  - Add new books to the catalog
  - Update book information
  - Remove books from inventory
  - Track book availability status

- **Hold Management**
  - Place holds on unavailable books
  - Manage hold queues
  - Notify users when books become available

- **Check-Out System**
  - Check out books to registered users
  - Set due dates automatically
  - Track borrowed books

- **Return Processing**
  - Process book returns
  - Calculate and manage late fees
  - Update book availability

- **Reporting & Analytics**
  - Generate reports on library usage
  - Track popular books and trends
  - Monitor overdue items

## Technology Stack

- **Language:** Java
- **Build Tool:** Gradle
- **Database:** [To be determined - SQL]
- **UI Framework:** Web Portal
- **Version Control:** Git/GitHub3

## Book Attributes

Each book in the system includes the following properties:
- ISBN (International Standard Book Number)
- Title
- Author(s)
- Publisher
- Publication Date
- Genre/Category
- Number of Pages
- Language
- Physical Description (hardcover, paperback, etc.)
- Location in Library (shelf/section)
- Availability Status
- Number of Copies (total and available)
- Book Summary/Description
- Cover Image

## Project Goals

1. Create an intuitive and efficient user interface for librarians
2. Ensure data integrity and reliability
3. Implement robust error handling and validation
4. Follow software quality best practices
5. Conduct thorough testing (unit, integration, system)
6. Document code and maintain clean architecture
7. Deliver a maintainable and scalable solution

## Requirements Gathering

This project involves collaboration with WSU librarians to:
- Understand real-world library workflows
- Identify pain points in current systems
- Validate feature requirements
- Ensure the solution addresses actual needs

## Getting Started

### Prerequisites
- Java JDK
- Gradle
- [Database system] TBD
- Front end - separate repository

### Installation
```bash
# Clone the repository
git clone [repository-url]

# Navigate to project directory
cd "Librarian Assistant"

# Build the project
./gradlew build

# Run the application
./gradlew run
```

## Project Structure

```
Librarian Assistant/
├── src/
│   ├── main/
│   │   ├── java/          # Java source files
│   │   └── resources/     # Configuration and resource files
│   └── test/
│       └── java/          # Test files
├── build.gradle           # Gradle build configuration
├── settings.gradle        # Gradle settings
└── README.md             # Project documentation
```

## Development Guidelines

- Follow Java coding conventions
- Write unit tests for all major functionality
- Document all public methods and classes
- Use meaningful commit messages
- Create feature branches for new development
- Submit pull requests for code review

## Contributing

As this is a team project, all team members should:
1. Pull the latest changes before starting work
2. Create a feature branch for new work
3. Commit changes with clear messages
4. Push changes and create pull requests
5. Have at least one team member review before merging

## Testing

[Details about testing strategy, frameworks used, and how to run tests will be added]

## License

This project is developed for educational purposes as part of a Software Quality course at Washington State University.

## Contact

For questions or concerns about this project, please contact any team member listed above.

## Acknowledgments

- Washington State University Library Staff for their insights and requirements
- Course Instructor for guidance and support

---

**Note:** This README will be updated throughout the project lifecycle as features are implemented and requirements are refined.
