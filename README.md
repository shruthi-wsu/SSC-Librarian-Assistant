# Librarian Assistant: Comprehensive Project Documentation

## Executive Summary

The Librarian Assistant is a comprehensive **full-stack library management system** designed to help librarians efficiently manage common library operations and tasks. This application consists of a Spring Boot backend API (this repository) and a React frontend (separate repository) that work together to streamline the workflow of library staff by providing an intuitive interface for managing books, users, and transactions.

**Project Timeline**: 15 weeks (from requirements gathering to deployment)

This document combines the project overview with a detailed Quality Assurance Plan that defines quality goals aligned with ISO/IEC 25010 standards, establishes measurable metrics, and provides cost estimates for quality assurance activities. The compressed 15-week timeline requires intensive, focused execution while maintaining high quality standards through comprehensive testing and continuous integration practices.

---

## About This Project

This project is being developed as part of the **Software Quality** course at **Washington State University (WSU)**. The application is a **full-stack library management system** that aims to create a practical solution for real-world library management challenges by working closely with WSU librarians to understand their daily requirements and pain points.

## Project Architecture

This is a **full-stack application** consisting of two main components:

### Backend (This Repository)
- **Repository**: [library-assistant-backend](https://github.com/shruthi-wsu/SSC-Librarian-Assistant)
- **Technology**: Java, Spring Boot, PostgreSQL
- **Purpose**: RESTful API backend handling all business logic, data management, and authentication

### Frontend (Separate Repository)
- **Repository**: [library-assistant-frontend](https://github.com/sanjeevkrishnan02/library-assistant-frontend)
- **Technology**: React
- **Purpose**: User interface for librarians and patrons
- **Team Role**: Collaborative development with team members as contributors

Both repositories work together to provide a complete, modern library management solution.

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

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Key Features](#key-features)
3. [Technology Stack](#technology-stack)
4. [Requirements Elaboration](#1-initial-requirements-elaboration)
5. [Quality Goals](#2-quality-goals-definition)
6. [Quality Assurance Plan](#3-quality-assurance-plan)
7. [Quality Metrics](#4-quality-metrics)
8. [Quality Cost Estimation](#5-quality-cost-estimation)
9. [Getting Started](#getting-started)
10. [Development Guidelines](#development-guidelines)
11. [References & Standards](#12-references--standards)

---

## Project Overview

The Librarian Assistant is a **full-stack library management system** that supports efficient library operations through automation and intuitive interfaces. The system consists of a robust Spring Boot backend API (this repository) and a modern React frontend (separate repository), working together to reduce manual work and improve accuracy in common library tasks.

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

### Backend (This Repository)
- **Language:** Java 17
- **Framework:** Spring Boot
- **Build Tool:** Gradle
- **Database:** PostgreSQL
- **Security:** Spring Security with JWT Authentication
- **ORM:** JPA/Hibernate
- **Testing:** JUnit 5, Mockito
- **Version Control:** Git/GitHub

### Frontend (Separate Repository)
- **Framework:** React
- **Language:** JavaScript/TypeScript
- **Version Control:** Git/GitHub

This full-stack architecture provides a modern, scalable solution for library management.

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

---

## 1. Initial Requirements Elaboration

### 1.1 Stakeholder Analysis

**Primary Stakeholder: Librarians**
- Professional staff managing daily library operations
- Need efficient, reliable tools that reduce manual work
- Require minimal training time
- Value data accuracy and system reliability

**Secondary Stakeholders:**
- Library patrons (indirect users through librarian interactions)
- Library administrators (reports and analytics)
- IT support staff (maintenance and troubleshooting)

### 1.2 Functional Requirements

#### FR-1: User Management
- **FR-1.1**: Register new library patrons with required information (name, contact, library card number, address)
- **FR-1.2**: Update patron information
- **FR-1.3**: View patron history (checkouts, holds, fines)
- **FR-1.4**: Deactivate/suspend patron accounts
- **FR-1.5**: Search patrons by name, card number, or contact information

#### FR-2: Book Catalog Management
- **FR-2.1**: Add new books to the system with attributes:
    - ISBN, Title, Author(s), Publisher, Publication Date
    - Genre/Category, Dewey Decimal Classification
    - Physical location (shelf/section), Copy number
    - Condition status, Acquisition date, Cost
- **FR-2.2**: Update book information
- **FR-2.3**: Mark books as lost, damaged, or withdrawn
- **FR-2.4**: Search books by title, author, ISBN, genre, or keyword
- **FR-2.5**: Browse books by category or location
- **FR-2.6**: View detailed book information including availability status

#### FR-3: Hold Management
- **FR-3.1**: Place hold on checked-out books
- **FR-3.2**: View hold queue for specific books
- **FR-3.3**: Notify patrons when held books become available
- **FR-3.4**: Cancel holds
- **FR-3.5**: Set hold expiration dates

#### FR-4: Checkout Management
- **FR-4.1**: Check out books to registered patrons
- **FR-4.2**: Validate patron eligibility (no excessive fines, account active)
- **FR-4.3**: Set due dates based on book type and patron category
- **FR-4.4**: Renew checked-out books (if no holds exist)
- **FR-4.5**: Generate checkout receipts

#### FR-5: Return Management
- **FR-5.1**: Process book returns
- **FR-5.2**: Calculate overdue fines automatically
- **FR-5.3**: Update book availability status
- **FR-5.4**: Process next hold in queue if applicable
- **FR-5.5**: Record book condition upon return

#### FR-6: Reporting & Analytics
- **FR-6.1**: Generate circulation statistics
- **FR-6.2**: Track overdue items
- **FR-6.3**: View popular books and authors
- **FR-6.4**: Export data for administrative reporting

### 1.3 Non-Functional Requirements

#### NFR-1: Performance
- System response time < 2 seconds for 95% of transactions
- Support concurrent access by up to 10 librarians
- Database queries complete within 1 second for single-item lookups

#### NFR-2: Reliability
- System uptime of 99.5% during library operating hours
- Zero data loss in case of system failure
- Automatic backup every 24 hours

#### NFR-3: Usability
- New librarians can perform basic operations within 30 minutes of training
- Maximum 3 clicks to complete any primary task
- Clear error messages with actionable guidance

#### NFR-4: Security
- Role-based access control
- Encrypted storage of patron personal information
- Audit trail for all data modifications
- Compliance with patron privacy regulations

#### NFR-5: Maintainability
- Modular architecture for easy updates
- Comprehensive documentation
- Automated testing coverage > 80%

#### NFR-6: Compatibility
- Cross-platform support (Windows, macOS, Linux) if desktop
- Browser compatibility (Chrome, Firefox, Safari, Edge) if web-based
- Database migration capabilities

---

## 2. Quality Goals Definition

Based on ISO/IEC 25010 software quality model and stakeholder priorities, the following quality goals are established:

### 2.1 Primary Quality Goals

#### QG-1: Functional Correctness (Critical Priority)
**Definition**: The system accurately performs all library management operations without errors in business logic.

**Rationale**: Incorrect book checkouts, miscalculated fines, or lost patron records would severely impact library operations and patron trust.

**Success Criteria**:
- Zero critical defects in production after release
- 100% accuracy in financial calculations (fines)
- 100% data integrity in patron-book associations

---

#### QG-2: Reliability (Critical Priority)
**Definition**: The system maintains consistent availability and recovers gracefully from failures.

**Rationale**: Librarians depend on the system throughout operating hours; downtime directly impacts patron services.

**Success Criteria**:
- 99.5% uptime during library hours (8 AM - 9 PM, 7 days/week)
- Mean Time Between Failures (MTBF) > 720 hours
- Mean Time To Recovery (MTTR) < 1 hour
- Zero data loss incidents

---

#### QG-3: Usability (High Priority)
**Definition**: The system is intuitive, efficient, and requires minimal training for librarians.

**Rationale**: Librarians need to serve patrons quickly; a complex system would create bottlenecks and frustration.

**Success Criteria**:
- Task completion time within industry benchmarks (checkout: < 30 seconds)
- System Usability Scale (SUS) score ≥ 75
- New user proficiency achieved within 30 minutes
- User error rate < 5% during task execution

---

#### QG-4: Performance Efficiency (High Priority)
**Definition**: The system responds quickly and handles multiple concurrent operations efficiently.

**Rationale**: Delays during peak hours (lunch time, evenings) would create patron queues and dissatisfaction.

**Success Criteria**:
- 95th percentile response time ≤ 2 seconds for all transactions
- 99th percentile response time ≤ 5 seconds
- Support 10 concurrent librarian sessions without degradation
- Database search results returned within 1 second

---

#### QG-5: Security (High Priority)
**Definition**: The system protects patron data and prevents unauthorized access.

**Rationale**: Libraries handle sensitive patron information; breaches would violate privacy laws and damage institutional reputation.

**Success Criteria**:
- Zero unauthorized access incidents
- 100% of patron PII encrypted at rest and in transit
- Complete audit trail for all data modifications
- Compliance with relevant privacy regulations (FERPA for academic libraries)

---

### 2.2 Secondary Quality Goals

#### QG-6: Maintainability (Medium Priority)
**Definition**: The system can be easily modified, updated, and debugged.

**Success Criteria**:
- Cyclomatic complexity < 10 for 90% of functions
- Test coverage ≥ 80%
- Mean time to implement minor enhancements < 40 hours
- Code maintainability index > 70

#### QG-7: Compatibility (Medium Priority)
**Definition**: The system operates across different platforms and integrates with existing infrastructure.

**Success Criteria**:
- Support for Windows 10+, macOS 10.15+, Ubuntu 20.04+ (if desktop)
- Support for Chrome, Firefox, Safari, Edge (latest 2 versions) if web-based
- Successful integration with existing library databases

#### QG-8: Portability (Low Priority)
**Definition**: The system can be easily transferred to different environments.

**Success Criteria**:
- Installation time < 2 hours
- Configuration documentation complete and accurate
- Successful deployment across at least 3 different environments

---

## 3. Quality Assurance Plan

### 3.1 QA Organizational Structure

**Quality Assurance Team Composition**:
- QA Manager (1) - Overall QA strategy and coordination
- QA Engineers (2) - Test planning and execution
- Automation Engineers (1) - Test automation framework
- Performance Test Specialist (0.5 FTE) - Load and performance testing
- Security Analyst (0.5 FTE) - Security testing and compliance

### 3.2 QA Process Framework

We will adopt a **hybrid QA approach** combining:
- V-Model for traceability between requirements and tests
- Continuous Testing integrated into Agile/Scrum development
- Risk-Based Testing to prioritize critical functionality

### 3.3 QA Lifecycle Phases

**Phase 1: Requirements Analysis & Test Planning (Weeks 1-2)**
- Review and validate requirements with stakeholders
- Develop Test Strategy document
- Create Requirements Traceability Matrix (RTM)
- Setup test environments

**Phase 2: Test Design (Weeks 3-4)**
- Design test cases for functional requirements (≥300 test cases)
- Create test data sets
- Design automation framework
- Develop performance and security test plans

**Phase 3: Test Environment Setup (Week 4-5)**
- Configure development, staging, and production-like environments
- Setup test database with sample data
- Configure continuous integration pipeline

**Phase 4: Test Execution (Ongoing with each sprint/iteration)**
- Execute smoke, functional, and regression tests
- Perform usability, performance, and security testing
- Log and track defects

**Phase 5: Defect Management & Retesting (Continuous)**
- Triage and prioritize defects
- Verify defect fixes
- Perform regression testing

**Phase 6: Test Closure & Release Certification (Pre-release)**
- Execute complete regression suite
- Perform User Acceptance Testing (UAT) with librarians
- Generate test summary report
- Archive test artifacts

### 3.4 Testing Strategies Summary

- **Functional Correctness**: Requirements-based testing, boundary value analysis, state transition testing
- **Reliability**: Stability testing (72-hour continuous test), recovery testing, data integrity verification
- **Usability**: User testing sessions with librarians, System Usability Scale (SUS) measurement, task time analysis
- **Performance**: Load testing with 10 concurrent users, stress testing, database performance optimization
- **Security**: Authentication/authorization testing, encryption verification, vulnerability scanning, penetration testing
- **Maintainability**: Code quality analysis with SonarQube, test coverage measurement
- **Compatibility**: Cross-platform/browser testing, integration testing

---

## 4. Quality Metrics

### 4.1 Key Performance Indicators

#### Functional Correctness Metrics
- **Defect Density**: ≤ 1.0 defects per 1000 lines of code
- **Test Pass Rate**: ≥ 98% at release
- **Requirements Coverage**: 100%
- **Financial Calculation Accuracy**: 100%

#### Reliability Metrics
- **System Availability**: ≥ 99.5%
- **Mean Time Between Failures (MTBF)**: ≥ 720 hours
- **Mean Time To Recovery (MTTR)**: ≤ 60 minutes
- **Data Integrity Rate**: 100%

#### Usability Metrics
- **System Usability Scale (SUS) Score**: ≥ 75
- **Task Completion Rate**: ≥ 90%
- **User Error Rate**: ≤ 5%
- **Learning Time**: ≤ 30 minutes

#### Performance Metrics
- **95th Percentile Response Time**: ≤ 2 seconds
- **99th Percentile Response Time**: ≤ 5 seconds
- **Throughput**: ≥ 60 transactions/minute
- **Database Query Response Time**: ≤ 1 second (95th percentile)

#### Security Metrics
- **Vulnerability Count**: 0 High/Critical at release
- **Encryption Coverage**: 100% of PII
- **Audit Trail Completeness**: 100%
- **Access Control Violations**: 0 successful violations

#### Maintainability Metrics
- **Cyclomatic Complexity**: ≤ 10 for 90% of functions
- **Test Coverage**: ≥ 80% overall, ≥ 90% business logic
- **Code Duplication**: ≤ 5%
- **Maintainability Index**: ≥ 70

---

## 5. Quality Cost Estimation

### 5.1 Cost of Quality Summary

Based on the Prevention-Appraisal-Failure (PAF) model for a 15-week project:

| Cost Category | Total Cost | Percentage |
|---------------|------------|------------|
| **Prevention Costs** | $74,500 | 15.6% |
| **Appraisal Costs** | $235,000 | 49.2% |
| **Internal Failure Costs** | $108,600 | 22.7% |
| **External Failure Costs** | $59,550 | 12.5% |
| **Total Cost of Quality** | **$477,650** | **100%** |

### 5.2 Key Cost Components

**Prevention Activities** ($74,500):
- Requirements review and test planning (compressed schedule)
- Test design and automation framework
- Code reviews and developer training
- Process improvement activities

**Appraisal Activities** ($235,000):
- Unit, integration, and system testing (intensive execution)
- Performance and security testing
- Usability testing with librarians
- User Acceptance Testing (UAT)
- Test tools and licenses

**Internal Failure** ($108,600):
- Defect analysis and fixing
- Re-testing and regression testing
- Rework for failed inspections

**External Failure** ($59,550):
- Post-release hotfixes and support
- Customer support for issues
- Patch testing and release

### 5.3 Return on Quality Investment

**Project Cost Comparison**:
- Development costs (5 developers × 15 weeks × 40 hours × $95/hour): $285,000
- Quality assurance costs: $477,650
- **Total Project Cost**: $762,650
- **CoQ as % of Total**: 62.6%

**Cost Avoidance Analysis**:
Without comprehensive QA, external failure costs typically increase 5-10x. Estimated savings: ~$300,000 in avoided post-release issues.

**3-Year Cumulative ROI**: Estimated +180%
- Cost avoidance through early defect detection ($300,000+)
- Reduced maintenance costs (30% annual savings)
- Reusable automation framework for future releases
- Preserved reputation and user satisfaction
- Shorter time-to-market (15 weeks vs. 26 weeks) providing competitive advantage

---

## 6. Implementation Timeline

### 6.1 Project Schedule (15 Weeks)

```
Week 1-2:   Requirements Review & Test Planning
Week 3:     Test Design & Environment Setup
Week 4-6:   Sprint 1 - Core Functionality Development + Testing
Week 7-9:   Sprint 2 - Extended Features Development + Testing
Week 10-12: Sprint 3 - Integration & System Testing
Week 13:    Sprint 4 - Performance & Security Testing
Week 14:    User Acceptance Testing (UAT) & Release Preparation
Week 15:    Final Regression, Production Deployment & Post-Release Monitoring
```

### 6.2 Quality Gates

- **Gate 1 (End of Week 6 - Sprint 1)**: Requirements coverage 100%, Unit test coverage ≥ 70%, Zero P1 defects
- **Gate 2 (End of Week 9 - Sprint 2)**: Integration test pass rate ≥ 95%, Automation framework operational
- **Gate 3 (End of Week 12 - Sprint 3)**: System test pass rate ≥ 98%, Performance benchmarks met
- **Gate 4 (End of Week 13 - Sprint 4)**: All functional tests passed, Non-functional requirements verified
- **Gate 5 (End of Week 14 - Pre-Release)**: UAT sign-off, Zero P1/P2 defects, All quality goals achieved

---

## 7. Tools & Infrastructure

### 7.1 Quality Assurance Toolchain

| Tool Category | Selected Tool | Purpose | Estimated Cost |
|---------------|---------------|---------|----------------|
| **Test Management** | TestRail or Zephyr | Test case management | $3,000/year |
| **Test Automation** | Selenium WebDriver | UI automation | Free (open source) |
| **API Testing** | Postman + Newman | API testing | $1,500/year |
| **Performance Testing** | Apache JMeter | Load testing | Free (open source) |
| **Security Testing** | OWASP ZAP | Vulnerability scanning | Free (open source) |
| **Static Code Analysis** | SonarQube | Code quality analysis | $5,000/year |
| **CI/CD** | GitLab CI or Jenkins | Continuous integration | $2,000/year |
| **Defect Tracking** | Jira | Issue management | $2,000/year |
| **Total Tool Costs** | | | **$18,000/year** |

**Note**: Most tools require annual licenses. For the 15-week project, actual tool costs would be prorated (~$5,200 for 3.5 months), but annual licenses provide continued value for maintenance and future releases.

### 7.2 Test Environment Infrastructure

- **Development Environment**: Local development machines, shared development database
- **QA Environment**: Staging server (production-like), dedicated test database
- **Performance Testing Environment**: Load generation servers, monitoring infrastructure
- **CI/CD Environment**: Build server, automated test execution agents

**Estimated Infrastructure Costs**: $10,000 setup + $500/month ongoing (~$11,750 for 15-week project)

---

## Getting Started

### Prerequisites

#### Backend (This Repository)
- Java JDK 17+
- Gradle
- PostgreSQL
- Git for version control

#### Frontend (Separate Repository)
- Node.js and npm
- React development environment
- See [frontend repository](https://github.com/sanjeevkrishnan02/library-assistant-frontend) for detailed setup

### Installation

#### Backend Setup (This Repository)
```bash
# Clone the backend repository
git clone https://github.com/shruthi-wsu/SSC-Librarian-Assistant.git
cd "Librarian Assistant"

# Build the project
./gradlew build

# Run the application
./gradlew run

# Run tests
./gradlew test

# Generate test coverage report
./gradlew jacocoTestReport
```

#### Frontend Setup (Separate Repository)
```bash
# Clone the frontend repository
git clone https://github.com/sanjeevkrishnan02/library-assistant-frontend.git
cd library-assistant-frontend

# Install dependencies
npm install

# Run the development server
npm start
```

**Note**: Both backend and frontend need to be running for the complete application to function.

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
├── .gitlab-ci.yml         # CI/CD configuration
└── README.md             # Project documentation
```

## Development Guidelines

### Code Quality Standards
- Follow Java coding conventions (Oracle Style Guide)
- Maintain cyclomatic complexity < 10 for all functions
- Achieve minimum 80% test coverage (90% for business logic)
- Keep code duplication under 5%
- Document all public methods and classes with JavaDoc
- Use meaningful variable and method names

### Testing Requirements
- Write unit tests for all business logic
- Create integration tests for component interactions
- Implement end-to-end tests for critical user workflows
- Ensure all tests pass before committing
- Maintain test data in separate files/database

### Version Control Best Practices
- Use meaningful commit messages (follow Conventional Commits)
- Create feature branches for new development (feature/feature-name)
- Create bugfix branches for defects (bugfix/issue-number)
- Submit pull requests for code review
- Require at least one approval before merging
- Keep commits atomic and focused

### Code Review Process
1. Create a pull request with clear description
2. Self-review your code before requesting review
3. Address all reviewer comments
4. Ensure CI/CD pipeline passes
5. Merge only after approval

## Contributing

As this is a team project, all team members should:
1. Pull the latest changes before starting work
2. Create a feature branch for new work
3. Write tests for new functionality
4. Run all tests locally before pushing
5. Commit changes with clear messages
6. Push changes and create pull requests
7. Have at least one team member review before merging
8. Update documentation as needed

## Testing

### Testing Strategy

This project follows a comprehensive testing approach:

#### Unit Testing
- **Framework**: JUnit 5
- **Coverage Tool**: JaCoCo
- **Target**: ≥ 80% overall, ≥ 90% business logic
- **Run**: `./gradlew test`

#### Integration Testing
- **Framework**: JUnit 5 with Spring Test (if using Spring)
- **Database**: H2 in-memory database for testing
- **Run**: `./gradlew integrationTest`

#### Performance Testing
- **Tool**: Apache JMeter
- **Scenarios**: Load testing with 10 concurrent users
- **Location**: `/performance-tests` directory

#### Security Testing
- **Static Analysis**: SonarQube security rules
- **Dynamic Testing**: OWASP ZAP
- **Dependency Scanning**: OWASP Dependency-Check
- **Run**: `./gradlew dependencyCheckAnalyze`

#### Usability Testing
- **Method**: User testing sessions with librarians
- **Tool**: Manual observation and SUS questionnaire
- **Frequency**: End of each sprint

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests UserManagementTest

# Run integration tests
./gradlew integrationTest

# Run static code analysis
./gradlew sonarqube

# Run dependency vulnerability check
./gradlew dependencyCheckAnalyze
```

### Continuous Integration

All commits trigger automated CI/CD pipeline that:
1. Builds the project
2. Runs all unit and integration tests
3. Generates code coverage reports
4. Performs static code analysis
5. Checks for security vulnerabilities
6. Deploys to staging environment (on main branch)

---

## 8. Risk Management

### Quality Risks

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| Inadequate requirements | Medium | High | Early QA involvement in requirements review |
| Scope creep affecting schedule | High | Medium | Strict change control; prioritize testing |
| Test environment instability | Medium | Medium | Automated environment setup |
| Key staff turnover | Low | High | Knowledge documentation; cross-training |
| Performance bottlenecks | Medium | High | Early performance testing |
| Security vulnerabilities | Low | Critical | Third-party pen testing; continuous scanning |

---

## 9. Success Criteria

The project will be considered successful if:

1. All quality goals achieved (Section 2) at release
2. Quality metrics meet targets at release date
3. Zero critical defects in production for first 30 days post-release
4. System Usability Scale (SUS) score ≥ 75 achieved in UAT
5. UAT sign-off received from library stakeholders
6. Post-release support incidents ≤ 5 per month (first 3 months)
7. 99.5% system availability during library operating hours
8. Stakeholder satisfaction with QA process ≥ 4/5

---

## 10. Continuous Improvement

### Quality Retrospectives
- **Frequency**: End of each sprint + post-release
- **Participants**: QA team, development team, stakeholders
- **Focus**: What worked well, what needs improvement, process bottlenecks

### Metrics-Driven Improvement
- Quarterly quality reviews
- Defect root cause analysis
- Process optimization based on data

### Test Automation Enhancement
- Increase automation coverage from 60% (Year 1) to 80% (Year 2)
- Reduce regression test execution time by 50%
- Implement automated performance regression tests

---

## 11. Stakeholder Communication

### Quality Reporting Structure

**Daily**:
- Stand-up updates on testing progress
- Critical defect alerts

**Weekly**:
- Quality metrics dashboard update
- Test execution summary report
- Defect trend analysis

**Bi-Weekly (Sprint End)**:
- Sprint quality report
- Demo of test automation progress
- Risk and issue escalation

**Monthly**:
- Comprehensive quality status report
- Metrics trend analysis with recommendations
- Cost tracking vs. budget

**Pre-Release**:
- Final quality assessment report
- UAT results summary
- Release recommendation with risk assessment

---

## 12. References & Standards

### Quality Standards
- ISO/IEC 25010:2011 - Systems and software Quality Requirements and Evaluation (SQuaRE)
- ISO/IEC 25023:2016 - Measurement of system and software product quality
- ISO 9241-11:2018 - Ergonomics of human-system interaction - Usability
- IEEE 829-2008 - Standard for Software and System Test Documentation
- IEEE 982.1-2005 - Standard Dictionary of Measures of the Software Aspects of Dependability

### Security Standards
- OWASP Top 10 (2021) - Top 10 Web Application Security Risks
- CWE Top 25 - Most Dangerous Software Weaknesses
- NIST SP 800-53 - Security and Privacy Controls
- WCAG 2.1 Level AA - Web Content Accessibility Guidelines

### Academic References
- Bangor, A., Kortum, P., & Miller, J. (2009). "Determining what individual SUS scores mean: Adding an adjective rating scale." *Journal of Usability Studies*, 4(3), 114-123.
- Crosby, P. B. (1979). *Quality is Free: The Art of Making Quality Certain*. McGraw-Hill.
- Feigenbaum, A. V. (1956). "Total quality control." *Harvard Business Review*, 34(6), 93-101.
- Kan, S. H. (2002). *Metrics and Models in Software Quality Engineering* (2nd ed.). Addison-Wesley.
- McCabe, T. J. (1976). "A complexity measure." *IEEE Transactions on Software Engineering*, SE-2(4), 308-320.
- Nielsen, J. (1993). *Usability Engineering*. Academic Press.
- Pressman, R. S., & Maxim, B. R. (2014). *Software Engineering: A Practitioner's Approach* (8th ed.). McGraw-Hill.

---

## 13. Appendices

### Appendix A: Defect Severity & Priority Definitions

**Severity Levels**:
- **Critical**: System crash, data loss, security breach
- **High**: Major functionality broken, no workaround
- **Medium**: Functionality impaired, workaround exists
- **Low**: Minor issue, cosmetic defect

**Priority Levels**:
- **P1**: Fix immediately, blocks release
- **P2**: Fix before release
- **P3**: Fix if time permits
- **P4**: Consider for future release

### Appendix B: Quality Gate Checklist

**Release Quality Gate**:
- [ ] All P1/P2 defects resolved
- [ ] Test pass rate ≥ 98%
- [ ] Code coverage ≥ 80%
- [ ] Performance benchmarks met
- [ ] Security scan clean (no high/critical vulnerabilities)
- [ ] UAT sign-off received
- [ ] Documentation complete
- [ ] Deployment plan approved
- [ ] Rollback plan tested
- [ ] Support team trained

### Appendix C: Glossary

- **APM**: Application Performance Monitoring
- **CoQ**: Cost of Quality
- **MTBF**: Mean Time Between Failures
- **MTTR**: Mean Time To Recovery
- **PAF**: Prevention-Appraisal-Failure (cost model)
- **PII**: Personally Identifiable Information
- **RTM**: Requirements Traceability Matrix
- **SUS**: System Usability Scale
- **UAT**: User Acceptance Testing
- **WCAG**: Web Content Accessibility Guidelines

---

## License

This project is developed for educational purposes as part of a Software Quality course at Washington State University.

## Contact

For questions or concerns about this project, please contact any team member:

**Team Members:**
- Shruthi Mallesh - shruthi.mallesh@wsu.edu
- Sanjeev Sreekumar Krishnan - s.krishnan@wsu.edu
- Chenhua Fan - chenhua.fan@wsu.edu

**Course Instructor:**
- Parteek Kumar - Washington State University

## Acknowledgments

- Washington State University Library Staff for their insights and requirements
- WSU Librarians for collaboration in requirements gathering and usability testing
- Course Instructor Parteek Kumar for guidance and support
- Quality Assurance community for established standards and best practices

---

## Document Control

**Version**: 1.1 (Updated for 15-Week Timeline)
**Last Updated**: February 5, 2026
**Authors**: Project Team Members
**Status**: Living Document
**Next Review Date**: End of Sprint 1 (Week 6)
**Project Duration**: 15 weeks

---

**Note:** This README combines the project overview with the comprehensive Quality Assurance Plan. It will be updated throughout the project lifecycle as features are implemented, requirements are refined, and quality processes are executed. The document serves as both a project introduction for new team members and a detailed quality assurance reference for the development and QA teams.

---

## Quick Reference Links

- [Project Overview](#project-overview)
- [Getting Started](#getting-started)
- [Quality Goals](#2-quality-goals-definition)
- [Quality Metrics](#4-quality-metrics)
- [Testing Strategy](#testing)
- [Development Guidelines](#development-guidelines)
- [Quality Cost Estimation](#5-quality-cost-estimation)
- [References & Standards](#12-references--standards)

---

**End of Document**
