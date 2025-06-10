package com.pluralsight;

import java.sql.*;

public class NorthwindApp {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // load the MySQL Driver, next line no longer needed in newer versions of Java
//         Class.forName("com.mysql.cj.jdbc.Driver");
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];
        // 1. open a connection to the database
        // use the database URL to point to the correct database
        Connection connection;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

        // create statement
        // the statement is tied to the open connection
        String query = """
                         SELECT ProductId,
                                ProductName,
                                UnitPrice,
                                UnitsInStock
                         FROM Products
                         """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);


        // 2. Execute the statement/your query
        ResultSet results = preparedStatement.executeQuery();

        System.out.printf("%-4s %-40s %15s %10s%n", "Id", "Product Name", "Unit Price", "Stock");
        System.out.println("---------------------------------------------------------------------------");

        // process the results
        while (results.next()) {
            int productId = results.getInt("productid");
            String productName = results.getString("productname");
            Double unitPrice = results.getDouble("unitPrice");
            int unitsInStock = results.getInt("unitsInStock");

            System.out.printf("%-4d %-40s %15.2f %10d%n", productId, productName, unitPrice, unitsInStock);
        }

        // 3. Close resources
        results.close();
        preparedStatement.close();
        connection.close();
    }
}
