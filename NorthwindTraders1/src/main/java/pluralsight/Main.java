package pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];

        Scanner scanner = new Scanner(System.in);
        System.out.print("Search for products that start with: ");
        String searchLetter = scanner.nextLine() + "%";

        //create a SQL statement/query
        String sql = """
                 select *
                 from products
                 where ProductName like ?;
                """;


        try (
                //create a connection
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);
                // create a SQL statement
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, searchLetter);

            try (//execute the statement/query
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                //print header row
                System.out.printf("%-4s %-40s %15s %10s%n", "Id", "Product Name", "Price", "Stock");
                System.out.println("_________________________________________________________________________________");

                //loop through the results and display them
                while (resultSet.next()) {
                    int productId = resultSet.getInt("productid");
                    String productName = resultSet.getString("productname");
                    Double unitPrice = resultSet.getDouble("unitprice");
                    int unitsInStock = resultSet.getInt("unitsinstock");

                    //print row
                    System.out.printf("%-4d %-40s %15.2f %10d%n", productId, productName, unitPrice, unitsInStock);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            //display user friendly error message
            System.out.println("There was an error retrieving the products. Please try again or contact support");
            //display error message for developer
            e.printStackTrace();
        }

    }
}
