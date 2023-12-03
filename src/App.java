import java.util.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSetMetaData;

public class App {
    public static int choice = 0;
    public static Scanner sc;

    public static void main(String[] args) {
        // Database connection parameters
        String connectionUrl = "jdbc:sqlserver://cxp-sql-03\\ltk30;"
                + "database=Bookstore;"
                + "user=htv5;"
                + "password=HarryVuCSDS341;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=15;";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * from Book";
            resultSet = statement.executeQuery(selectSql);
            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
            // MENU
            int choice = -1;
            boolean exit = false;
            while (exit == false) {
                sc = new Scanner(System.in);
                System.out.println("Menu option \n");
                System.out.println("1. Login \n");
                System.out.println("2. Register a new customer \n");
                System.out.println("3. Search book by cover \n");
                System.out.println("4. List all authors description \n");
                System.out.println("5. Add new book \n");
                System.out.println("6. Count books \n");
                System.out.println("7. Update book prices \n");
                System.out.println("8. Create a new order \n");
                System.out.println("9. List top selling books\n");
                System.out.println("10. Find orders within a specific date range\n");
                System.out.println("11. Retrieve all books in a specific genre\n");
                System.out.println("12. Calculate total revenue from a specific date range\n");
                System.out.println("13. Display books with low stock \n");
                System.out.println("14. Exit the program \n\n");
                System.out.print("Choose your option number: ");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        option1(connectionUrl);
                        break;
                    case 2:
                        option2(connectionUrl);
                        break;
                    case 3:
                        option3(connectionUrl);
                        break;
                    case 4:
                        option4(connectionUrl);
                        break;
                    case 5:
                        option5(connectionUrl);
                        break;
                    case 6:
                        option6(connectionUrl);
                        break;
                    case 7:
                        option7(connectionUrl);
                        break;
                    case 8:
                        option8(connectionUrl);
                        break;
                    case 9:
                        option9(connectionUrl);
                        break;
                    case 10:
                        option10(connectionUrl);
                        break;
                    case 11:
                        option11(connectionUrl);
                        break;
                    case 12:
                        option12(connectionUrl);
                        break;
                    case 13:
                        option13(connectionUrl);
                        break;
                }
                if (choice == 14) {
                    System.out.println("The program exit successfully");
                    break;
                }
                System.out.println("\n");
                System.out.print("Press 'E' to exit the program, press 'M' to back to Menu option: "); // ask the user
                                                                                                       // whether to
                                                                                                       // return to menu
                                                                                                       // or not
                String back = sc.next();
                if (back.equals("E")) // exit condition
                {
                    exit = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    // Option 1: Login
    public static void option1(String connectionUrl) {
        sc = new Scanner(System.in);
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Constructing the SQL query
        String query = "SELECT COUNT(*) FROM Customers WHERE email = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Login successfully.");
                // User is logged in, proceed with further actions
            } else {
                System.out.println("Login failed. Incorrect email or password.");
                // Handle failed login
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Option 2: Register a new customer
    public static void option2(String connectionUrl) {
        sc = new Scanner(System.in);

        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        if (!isValidName(firstName)) {
            System.out.println("Invalid first name.");
            return;
        }

        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();
        if (!isValidName(lastName)) {
            System.out.println("Invalid last name.");
            return;
        }

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Insert the new customer into the database
        String insertSql = "INSERT INTO Customers (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement pstmt = connection.prepareStatement(insertSql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("New customer registered successfully.");
            } else {
                System.out.println("Customer registration failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during customer registration.");
            e.printStackTrace();
        }
    }

    // Option 3: Search book by cover
    public static void option3(String connectionUrl) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the title of the book: ");
        String bookTitle = scanner.nextLine();

        try {
            // Establishing a connection
            Connection conn = DriverManager.getConnection(connectionUrl);

            // Creating a statement
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM book WHERE title LIKE ?");
            stmt.setString(1, bookTitle);

            // Executing the query
            ResultSet rs = stmt.executeQuery();

            // Processing the result
            while (rs.next()) {
                System.out.println("Book Title: " + rs.getString("title"));
                // Add more print statements for other book details
            }

            // Closing the connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Option 4: List all authors description
    public static void option4(String connectionUrl) {
        try {
            // Establishing a connection
            Connection conn = DriverManager.getConnection(connectionUrl);

            // Creating a statement
            Statement stmt = conn.createStatement();

            // Executing the query
            ResultSet rs = stmt.executeQuery("SELECT concat(firstname, ' ', lastname) as author_name from author");

            // Processing the result
            while (rs.next()) {
                System.out.println("Author Name: " + rs.getString("author_name"));
            }

            // Closing the connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Option 5: Add new book
    public static void option5(String connectionUrl) {

    }

    // Option 6: Count books
    public static void option6(String connectionUrl) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the title of the book: ");
        String title = scanner.nextLine();
        System.out.println("Enter the ID of the book: ");
        int id = scanner.nextInt();

        try {
            // Establishing a connection
            Connection conn = DriverManager.getConnection(connectionUrl);

            // Creating a statement
            PreparedStatement stmt = conn
                    .prepareStatement("select stock_quantity from book where title = ? and id = ?");
            stmt.setString(1, title);
            stmt.setInt(2, id);

            // Executing the query
            ResultSet rs = stmt.executeQuery();

            // Processing the result
            if (rs.next()) {
                System.out.println("Stock Quantity: " + rs.getInt("stock_quantity"));
            }

            // Closing the connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Option 7: Update book prices
    public static void option7(String connectionUrl) {
        sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        int bookID;
        while (true) {
            if (sc.hasNextInt()) {
                bookID = sc.nextInt();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid book ID (integer).");
                sc.next(); // Clear the invalid input
            }
        }

        System.out.print("Enter New Price: ");
        double newPrice;
        while (true) {
            if (sc.hasNextDouble()) {
                newPrice = sc.nextDouble();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid price (decimal).");
                sc.next(); // Clear the invalid input
            }
        }

        // Call the stored procedure
        String callSql = "{call UpdateBookPrice(?, ?)}";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                CallableStatement cstmt = connection.prepareCall(callSql)) {

            cstmt.setInt(1, bookID);
            cstmt.setDouble(2, newPrice);

            int affectedRows = cstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book price updated successfully.");
            } else {
                System.out.println("Book price update failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during updating book price.");
            e.printStackTrace();
        }
    }

    // Option 8: Create a new order
    public static void option8(String connectionUrl) {
        sc = new Scanner(System.in);

        System.out.print("Enter Customer ID: ");
        int customerID = sc.nextInt();

        // Step 1: Create a new order
        String createOrderSql = "{call CreateNewOrder(?)}";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                CallableStatement cstmt = connection.prepareCall(createOrderSql)) {

            cstmt.setInt(1, customerID);
            cstmt.executeUpdate();
            System.out.println("New order created successfully.");

        } catch (SQLException e) {
            System.out.println("Error occurred during order creation.");
            e.printStackTrace();
            return;
        }

        // Step 2: Add items to the order
        // Assuming that the user knows the order ID.
        // In a real application, you might want to retrieve and display the order ID
        // after creation.
        System.out.print("Enter the Order ID: ");
        int orderID = sc.nextInt();

        String addItemsSql = "{call InsertOrderItem(?, ?, ?)}";
        boolean addMoreItems;
        do {
            System.out.print("Enter Book ID to add to the order: ");
            int bookID = sc.nextInt();

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();

            try (Connection connection = DriverManager.getConnection(connectionUrl);
                    CallableStatement cstmt = connection.prepareCall(addItemsSql)) {

                cstmt.setInt(1, orderID);
                cstmt.setInt(2, bookID);
                cstmt.setInt(3, quantity);
                cstmt.executeUpdate();

                System.out.println("Item added to order successfully.");
            } catch (SQLException e) {
                System.out.println("Error occurred while adding item to order.");
                e.printStackTrace();
            }

            System.out.print("Do you want to add more items to this order? (yes/no): ");
            addMoreItems = "yes".equalsIgnoreCase(sc.next());
        } while (addMoreItems);
    }

    // Option 9: List top selling books
    public static void option9(String connectionUrl) {
        sc = new Scanner(System.in);

        System.out.print("Enter the number of top selling books to display: ");
        int topN;
        while (true) {
            if (sc.hasNextInt()) {
                topN = sc.nextInt();
                if (topN > 0) {
                    break;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // Clear the invalid input
            }
        }

        // Call the stored procedure
        String callSql = "{call ListTopSellingBooks(?)}";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                CallableStatement cstmt = connection.prepareCall(callSql)) {

            cstmt.setInt(1, topN);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("ID");
                    String title = rs.getString("title");
                    int quantitySold = rs.getInt("TotalQuantitySold");
                    System.out.printf("Book ID: %d, Title: %s, Total Sold: %d\n", bookId, title, quantitySold);
                }
            }

            System.out.println("Top " + topN + " best-selling books listed successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred while listing top selling books.");
            e.printStackTrace();
        }
    }

    // Option 10: Find orders within a specific date range
    public static void option10(String connectionUrl) {
        String fromDate = sc.next();
        String toDate = sc.next();
        if (!validateDate(fromDate) || !validateDate(toDate)) {
            System.out.println("The input dates are invalid!");
            return;
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM BookOrder" + "WHERE date BETWEEN" + fromDate + "AND" + toDate + ";";
            fromDate = sc.nextLine();
            toDate = sc.nextLine();
            statement.execute(selectSql);
            printTable(connectionUrl, selectSql);
            // Print results from select statement
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    // Option 11: Retrieve all books in a specific genre
    public static void option11(String connectionUrl) {
        System.out.print("Pick your poison: ");
        String genreString = sc.next();
        if (!isValidName(genreString)) {
            System.out.println("The input position is invalid!");
            return;
        }

        genreString = formatInput(genreString);

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM Book" + "INNER JOIN Genre ON Book.genreID = Genre.ID WHERE Genre.name ="
                    + genreString;
            System.out.println("Books that are in the " + genreString + "category: ");
            statement.execute(selectSql);

            printTable(connectionUrl, selectSql);
            // Print results from select statement
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    // Option 12: Calculate total revenue from a specific date range
    public static void option12(String connectionUrl) {
        String from = sc.next();
        String to = sc.next();
        if (!validateDate(from) || !validateDate(to)) {
            System.out.println("The input dates are invalid!");
            return;
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT SUM(total_price) AS TotalRevenue" + "FROM BookOrder WHERE date BETWEEN" + from
                    + "AND" + to + ";";
            statement.execute(selectSql);
            // Print results from select statement
        } catch (Exception e) {
            // System.out.println("No sales were made during this period");
            System.out.println(e);
            return;
        }
    }

    // Option 13: Display books with low stock
    public static void option13(String connectionUrl) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM Book WHERE stock_quantity < 10;";
            statement.execute(selectSql);
            System.out.println("New email has successfully updated");
            // Print results from select statement
        } catch (Exception e) {
            // System.out.println("No book is in low stock");
            System.out.println(e);
            return;
        }
    }

    private static String formatInput(String input) {
        return "\'" + input + "\'";
    }

    private static void printTable(String connectionUrl, String selectSql) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(selectSql);

            // Get the number of columns in the table
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            // Get the column names and maximum width of each column
            String[] columnNames = new String[columnCount];
            int[] columnWidths = new int[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = resultSetMetaData.getColumnName(i);
                columnWidths[i - 1] = columnNames[i - 1].length();
            }

            // Find the maximum width of each column
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    if (value != null) {
                        columnWidths[i - 1] = Math.max(columnWidths[i - 1], value.length());
                    }
                }
            }

            // Print the column names
            for (int i = 0; i < columnCount; i++) {
                System.out.print(String.format("%-" + (columnWidths[i] + 2) + "s", columnNames[i]));
            }
            System.out.println();

            // Print a separator line
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < columnWidths[i] + 2; j++) {
                    System.out.print("-");
                }
            }
            System.out.println();

            // Create and execute the SELECT statement again to get a new ResultSet object
            resultSet = statement.executeQuery(selectSql);

            // Print the data
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(String.format("%-" + (columnWidths[i - 1] + 2) + "s", resultSet.getString(i)));
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.print("Input is not allowed!");
            return;
        }
    }

    public static boolean validateDate(String inputDate) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        return inputDate.matches(dateRegex);
    }

    public static boolean validateNumber(String inputNumber) {
        String numberRegex = "^\\d+$";
        return inputNumber.matches(numberRegex);
    }

    public static boolean isValidName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return false;
        }
        for (int i = 0; i < firstName.length(); i++) {
            char c = firstName.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
