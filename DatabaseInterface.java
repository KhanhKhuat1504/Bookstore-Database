import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSetMetaData;

public class DatabaseInterface {
    public static int choice = 0;
    public static Scanner sc;
    public static void main(String[] args) {
        // Database connection parameters
        String connectionUrl = 
        "jdbc:sqlserver://cxp-sql-03\\ltk30;"
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
            while(exit == false){
                sc = new Scanner(System.in);
                System.out.println("Menu option \n");
                System.out.println("1. Search book by cover \n");
                System.out.println("2. List all authors description \n");
                System.out.println("3. Add new book \n");
                System.out.println("4. Count books \n"); 
                System.out.println("5. Update book prices \n");
                System.out.println("6. Create a new order \n");
                System.out.println("7. Register a new customer \n");
                System.out.println("8. List top %user_input& top selling books\n");
                System.out.println("9. Find orders within a specific date range\n");
                System.out.println("10. Retrieve all books in a specific genre\n");
                System.out.println("11. Calculate total revenue from a specific date range\n");
                System.out.println("12. Display books with low stock \n");
                System.out.println("13. Exit the program \n\n");
                System.out.print("Choose your option number: ");
                choice = sc.nextInt();
                switch (choice) {
                    case 1 -> option1(connectionUrl);
                    case 2 -> option2(connectionUrl);
                    case 3 -> option3(connectionUrl);
                    case 4 -> option4(connectionUrl);
                    case 5 -> option5(connectionUrl);
                    case 6 -> option6(connectionUrl);
                    case 7 -> option7(connectionUrl);
                    case 8 -> option8(connectionUrl);
                    case 9 -> option9(connectionUrl);
                    case 10 -> option10(connectionUrl);
                    case 11 -> option11(connectionUrl);
                    case 12 -> option12(connectionUrl);
                }
                if (choice == 13) {
                    System.out.println("The program exit successfully");
                    break;
                }
                System.out.println("\n");
                System.out.print("Press 'E' to exit the program, press 'M' to back to Menu option: "); // ask the user whether to return to menu or not
                String back = sc.next();
                if (back.equals("E")) // exit condition
                {
                    exit = true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    // employee
    public static void option1(String connectionUrl){
        

    }
    
     // employee
    public static void option2(String connectionUrl){
        String selectSql = "SELECT * from employee";
        printTable(connectionUrl, selectSql);
    }

    public static void option3(String connectionUrl){
        System.out.print("Please enter employee first name: ");
        String employeeFirstName = sc.next();
        if(!isValidName(employeeFirstName)) {
            System.out.println("The input first name is invalid!");
            return;
        }
        System.out.print("Please enter employee last name: ");
        String employeeLastName = sc.next(); 
        if(!isValidName(employeeLastName)) {
            System.out.println("The input last name is invalid!");
            return;
        }       
        String findEmployeeByName = "Select * from employee where firstname = " + formatInput(employeeFirstName) + "and lastname = " + formatInput(employeeLastName);
        printTable(connectionUrl, findEmployeeByName);
        System.out.print("Select the employee ID that you want to add the job: ");
        int employeeID = sc.nextInt();
        System.out.print("Please enter position name: ");
        String positionName = sc.next();
        if(!isValidName(positionName)){
            System.out.println("The input position name is invalid!");
            return;
        }    
        String findPositionByName = "Select * from position where position_name = " + formatInput(positionName);
        printTable(connectionUrl, findPositionByName);
        System.out.print("Select the position ID that you want to " + employeeFirstName + " "+ employeeLastName+ ": ");
        int positionID = sc.nextInt();

        System.out.print("Please specify the salary: ");
        int salary = sc.nextInt();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "INSERT INTO employee_position VALUES " + "(" + employeeID + ','+ positionID + "," + salary + ")";
            statement.execute(selectSql);
            System.out.println("Successfully add job to the specified employee!");
            // Print results from select statement
        }catch(Exception e){
            System.out.println("Input is not allowed!");
            return;
        }
    }

    private static String formatInput(String input){
        return "\'" + input + "\'";
    }

    private static void printTable(String connectionUrl, String selectSql){
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
        }catch(Exception e){
            System.out.print("Input is not allowed!");
            return;
        }
    }
    public static void option4(String connectionUrl){
        System.out.print("Please enter employee first name: ");
        String employeeFirstName = sc.next();
        if(!isValidName(employeeFirstName)) {
            System.out.println("The input first name is invalid!");
            return;
        }
        System.out.print("Please enter employee last name: ");
        String employeeLastName = sc.next();        
        if(!isValidName(employeeLastName)) {
            System.out.println("The input last name is invalid!");
            return;
        }
        String findEmployeeByName = "Select * from employee where firstname = " + formatInput(employeeFirstName) + "and lastname = " + formatInput(employeeLastName);
        printTable(connectionUrl, findEmployeeByName);
        System.out.print("Select the employee ID that you want to generate the payroll: ");
        int employeeID = sc.nextInt();
        System.out.print("Enter the start date of the payroll (yyyy-mm-dd): ");
        String startDate = formatInput(sc.next());
        if(!validateDate(startDate)){
            System.out.println("The input start date is invalid!");
            return;
        }
        System.out.print("Enter the end date of the payroll (yyyy-mm-dd): ");
        String endDate = formatInput(sc.next());
        if(!validateDate(endDate)){
            System.out.println("The input end date is invalid!");
            return;
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "EXEC generatePayRollForEmployee " + employeeID + ", " + startDate + "," + endDate;
            statement.execute(selectSql);
            System.out.println("Payroll for " + employeeFirstName + " " + employeeLastName +" " + "has successfully created");
            // Print results from select statement
        }catch(Exception e){
            System.out.println("Payroll not successfully created");
            return;
        }        
    }
    public static void option5(String connectionUrl){
        System.out.print("Enter the start date of the payroll (yyyy-mm-dd): ");
        String startDate = sc.next();
        if(!validateDate(startDate)){
            System.out.println("The input start date is invalid!");
            return;
        }

        startDate = formatInput(startDate);

        System.out.print("Enter the end date of the payroll (yyyy-mm-dd): ");
        String endDate = sc.next();
        if(!validateDate(endDate)){
            System.out.println("The input end date is invalid!");
            return;
        }

        endDate = formatInput(endDate);
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "EXEC generatePayrollForAllEmployees2 " + startDate + "," + endDate;
            statement.execute(selectSql);
            System.out.println("Payroll for all of the employees have successfully been created.");
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }


    }

    public static void option6(String connectionUrl){
        System.out.print("Enter the name of the position: ");
        String positionName = sc.next();
        if(!isValidName(positionName)){
            System.out.println("The input position is invalid!");
            return;
        }

        positionName = formatInput(positionName);

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql ="select position.position_name, employee.email, employee.firstname, employee.lastname, employee_position.salary from employee inner join employee_position on employee.id = employee_position.employee_id inner join position on position.id = employee_position.position_id where position.position_name = " + positionName;
            System.out.println("List all of the employees working as " + positionName + ": ");
            statement.execute(selectSql);

            printTable(connectionUrl, selectSql);
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }

    public static void option7(String connectionUrl){
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql ="SELECT employee_position.employee_id, employee.firstname, employee.lastname, SUM(salary) AS total_salary FROM employee_position INNER JOIN employee ON employee_position.employee_id = employee.id GROUP BY employee_position.employee_id, employee.firstname, employee.lastname;";
            statement.execute(selectSql);

            printTable(connectionUrl, selectSql);
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }
    public static void option8(String connectionUrl){
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql ="SELECT employee.firstname, employee.lastname, SUM(payroll.payroll_amount) as total_payroll_amount FROM employee INNER JOIN payroll ON employee.id = payroll.employee_id GROUP BY employee.firstname, employee.lastname;";
            statement.execute(selectSql);
            
            printTable(connectionUrl, selectSql);
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }

    public static void option9(String connectionUrl){
        String fromDate = sc.next();
        String toDate = sc.next();
        if(!validateDate(fromDate) ||!validateDate(toDate)) {
            System.out.println("The input dates are invalid!");
            return;
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql ="SELECT * FROM BookOrder" + "WHERE date BETWEEN" + fromDate + "AND" + toDate + ";";
            fromDate = sc.nextLine();
            toDate = sc.nextLine();
            statement.execute(selectSql);
            printTable(connectionUrl, selectSql);
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }

    public static void option10(String connectionUrl){
        System.out.print("Pick your poison: ");
        String genreString = sc.next();
        if(!isValidName(genreString)){
            System.out.println("The input position is invalid!");
            return;
        }

        genreString = formatInput(genreString);

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql ="SELECT * FROM Book" + "INNER JOIN Genre ON Book.genreID = Genre.ID WHERE Genre.name =" + genreString;
            System.out.println("Books that are in the " + genreString + "category: ");
            statement.execute(selectSql);

            printTable(connectionUrl, selectSql);
            // Print results from select statement
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }

    public static void option11(String connectionUrl){
        String from = sc.next();
        String to = sc.next();
        if(!validateDate(from) ||!validateDate(to)) {
            System.out.println("The input dates are invalid!");
            return;
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT SUM(total_price) AS TotalRevenue" + "FROM BookOrder WHERE date BETWEEN" + from + "AND" + to + ";";
            statement.execute(selectSql);
            // Print results from select statement
        }catch(Exception e){
            // System.out.println("No sales were made during this period");
            System.out.println(e);
            return;
        }        
    }
    public static void option12(String connectionUrl){
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM Book WHERE stock_quantity < 10;";
            statement.execute(selectSql);
            System.out.println("New email has successfully updated");
            // Print results from select statement
        }catch(Exception e){
            // System.out.println("No book is in low stock");
            System.out.println(e);
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
