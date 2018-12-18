/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.utils;

import java.sql.*;

/**
 *
 * @author oXCToo
 */
public class ConnectionUtil {
    Connection conn = null;
    static PreparedStatement preparedStatement = null;
    public static Connection conDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/extra-project", "root", "");

            String sql_user = "CREATE TABLE IF NOT EXISTS `user` (" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "    `name` VARCHAR(45) NOT NULL," +
                    "    `email` VARCHAR(45) NOT NULL," +
                    "    `password` VARCHAR(45) NOT NULL," +
                    "    PRIMARY KEY (`id`)" +
                    ")";

            String sql_products = "CREATE TABLE IF NOT EXISTS `products` (" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "    `name` VARCHAR(45) NOT NULL," +
                    "    `description` VARCHAR(255) NOT NULL," +
                    "    `price` VARCHAR(10) NOT NULL," +
                    "    `company_id` SMALLINT(5) UNSIGNED DEFAULT NULL," +
                    "    `category_id` SMALLINT(5) UNSIGNED DEFAULT NULL," +
                    "    `user_id` SMALLINT(5) UNSIGNED DEFAULT NULL," +
                    "    PRIMARY KEY (`id`)" +
                    ")";
            String sql_categories = " CREATE TABLE IF NOT EXISTS `categories` (" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "    `name` VARCHAR(45) NOT NULL," +
                    "    PRIMARY KEY (`id`)" +
                    ")";

            String sql_companies = " CREATE TABLE IF NOT EXISTS `companies` (" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "    `name` VARCHAR(45) NOT NULL," +
                    "    PRIMARY KEY (`id`)" +
                    ")";
            String sql_cart_shop = " CREATE TABLE IF NOT EXISTS `cart_shop` (" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "    `product_id` SMALLINT(5) UNSIGNED DEFAULT NULL," +
                    "    `user_id` SMALLINT(5) UNSIGNED DEFAULT NULL," +
                    "    PRIMARY KEY (`id`)" +
                    ")";

            Statement statement = (Statement) con.createStatement();
            statement.execute(sql_user);
            statement.execute(sql_products);
            statement.execute(sql_categories);
            statement.execute(sql_companies);
            statement.execute(sql_cart_shop);

            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : "+ex.getMessage());
           return null;
        }
    }

    //make sure you add the lib
}
