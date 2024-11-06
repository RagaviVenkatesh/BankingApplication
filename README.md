# BankingApplication
**CONSOLE BASED BANKING APPLICATION**
The application simulates essential banking operations, allowing users to create and manage accounts, perform transactions, view account details, and generate reports. The application is structured to follow standard software design practices, including layers for data access (DAO), business logic (service), and user interaction (controller). It leverages Java's core OOP principles—such as encapsulation, inheritance, and polymorphism—along with multithreading, exception handling, file I/O, and JDBC for database interactions.

**Key Features**
1.	User Account Management:
o	Create different types of accounts: Savings and Current.
o	View account details, such as account type, balance, and other information.
o	Update account information, such as contact details.
2.	Transaction Management:
o	Deposit and Withdraw funds from accounts.
o	Transfer funds between accounts.
o	Ensure thread-safe transactions using multithreading and synchronization, so deposits and withdrawals don’t conflict.
3.	Transaction History:
o	Save a log of all transactions to a file, allowing users to view a history of their account activities.
o	Retrieve and display transaction history for specific accounts from the saved file.
4.	Database Operations (Using JDBC):
o	Store account and transaction details in a relational database.
o	Perform CRUD operations for account management, ensuring data persistence.
o	Generate reports based on the data stored in the database.
5.	Reports:
o	Generate various reports, such as:
	Account details for each customer.
	Total balance across all accounts.
	Number of accounts by type (savings or current).
	Daily summaries of deposits, withdrawals, and transfers.

**PROJECT STRUCTURE**

BankingApplication/
├── src/                         # Source directory for application code
│   ├── model/                # Contains the entity classes
│   ├── dao/                   # Data Access Object layer
│   ├── service/              # Service layer handling business logic
│   ├── controller/           # Controller layer for handling user interactions
│   ├── utility/                # Utility classes for common functions
│   ├── resource/            # Configuration files and static resources
└── test/                         # Contains JUnit test cases for testing

1. Model Layer (model/)
The model package contains entity classes that represent the core objects or data structures in the application, such as Bank, Account, SavingsAccount, CurrentAccount, and Transaction. These classes often correspond to database tables, mapping attributes to columns.
•	Purpose: Encapsulate the data and define the structure of objects used throughout the application.
•	Responsibilities: Hold the state of entities and provide getters and setters for accessing and modifying data.
2. Data Access Object Layer (dao/)
The dao (Data Access Object) package handles interaction with the database. DAO classes provide methods for CRUD (Create, Read, Update, Delete) operations on the database and are the only classes that should directly access the database.
•	Purpose: Encapsulate database interactions, ensuring separation between the application’s logic and data persistence.
•	Responsibilities: Execute SQL queries and manage connections, statements, and result sets.
3. Service Layer (service/)
The service package contains the business logic of the application. Service classes use DAO classes to retrieve and manipulate data, applying business rules and validation as needed.
•	Purpose: Implement the core business processes of the application, acting as an intermediary between the controller and DAO layers.
•	Responsibilities: Validate data, handle exceptions, apply business rules, and coordinate data flow between the controller and DAO.
4. Controller Layer (controller/)
The controller package is the user interaction layer. Controllers handle user inputs, invoke services to process requests, and display output back to the user. In this console-based application, the BankController interacts with users through the console.
•	Purpose: Act as an entry point for user interactions and connect the user interface with the underlying business logic.
•	Responsibilities: Display menus, gather user inputs, handle responses, and navigate between different parts of the application.
5. Utility Layer (utility/)
The utility package includes helper classes and reusable functions used throughout the application. These classes perform common tasks that are not specific to any single business process.
•	Purpose: Provide common utilities that simplify code by encapsulating frequently used operations.
•	Responsibilities: Simplify repetitive tasks, manage configurations, and provide reusable methods that other classes can call.
6. Resource Layer (resource/)
The resource package holds configuration files, properties, and static resources needed by the application. It might contain files such as database configuration properties and templates.
•	Purpose: Centralize static resources and configuration files to keep them separate from business logic.
•	Responsibilities: Provide configurations and static resources in a structured and centralized way, making it easy to manage and update.
7. Test Layer (test/)
The test directory includes JUnit test cases for testing each layer of the application. Tests ensure the correctness of each function and are particularly useful for validating business logic in the service layer and database interactions in the DAO layer.
•	Purpose: Verify the functionality and correctness of the code by testing individual components and end-to-end processes.
•	Responsibilities: Ensure that methods perform as expected, catch bugs early, and maintain the stability of the application over time.

**Project Flow and Integration**
1.	Controller Layer: Acts as the entry point and interfaces with the user.
2.	Service Layer: Processes user requests by coordinating between the controller and DAO layers.
3.	DAO Layer: Interacts with the database to persist or retrieve data.
4.	Model Layer: Represents data objects that are passed between layers.
5.	Utility Layer: Provides reusable code, such as database connections or file handling.
6.	Resource Layer: Supplies configuration and static data.
7.	Test Layer: Validates each layer's functionality through unit tests.
   
**SCHEMA DESIGN:**
The schema consists of the following tables:
1.	Bank: Stores information about each bank.
2.	Account: Stores account details, associated with a particular bank.
3.	SavingsAccount and CurrentAccount: Specialized tables for different account types, inheriting from the Account table.
4.	Transaction: Records all transactions (deposits, withdrawals, and transfers) linked to accounts.
5.	DepositTransaction and WithdrawalTransaction: Specialized tables for different transaction types, inheriting from the Transaction table.
<img width="354" alt="image" src="https://github.com/user-attachments/assets/c1e49bf6-6922-4234-81af-845a1a258ffc">

**Complete Project Structure**

BankingApplication/
├── src/
│   ├── model/                     # Entity classes
│   │   ├── Bank.java
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CurrentAccount.java
│   │   ├── Transaction.java
│   │   ├── DepositTransaction.java
│   │   ├── WithdrawalTransaction.java
│   ├── dao/                       # Data Access Object (DAO) layer
│   │   ├── AccountDAO.java
│   │   ├── AccountDAOImpl.java
│   │   ├── BankDAO.java
│   │   ├── BankDAOImpl.java
│   │   ├── TransactionDAO.java
│   │   ├── TransactionDAOImpl.java
│   ├── service/                   # Service layer for business logic
│   │   ├── AccountService.java
│   │   ├── TransactionService.java
│   │   ├── BankService.java
│   ├── controller/                # Controller layer for user interactions
│   │   ├── BankController.java
│   ├── utility/                   # Utility classes
│   │   ├── DBConnection.java
│   │   ├── TransactionHistoryUtil.java
│   ├── resource/                  # SQL schema and configuration files
│   │   ├── sql.schema
├── test/                          # Test directory
│   ├── dao/                       # Tests for DAO classes
│   │   ├── AccountDAOTest.java
│   │   ├── BankDAOTest.java
│   │   ├── TransactionDAOTest.java
│   ├── service/                   # Tests for Service classes
│   │   ├── AccountServiceTest.java
│   │   ├── TransactionServiceTest.java
│   │   ├── BankServiceTest.java
│   ├── controller/                # Tests for Controller classes
│   │   ├── BankControllerTest.java
├── README.md                      # Documentation file


![image](https://github.com/user-attachments/assets/8af988c2-d74a-49df-9da9-17030b3b6815)


