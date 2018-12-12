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

            String sql_stmt = "CREATE TABLE IF NOT EXISTS `wip_users` (\n" +
                    "    `id` SMALLINT(5) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                    "    `firstname` VARCHAR(45) NOT NULL,\n" +
                    "    `lastname` VARCHAR(45) NOT NULL,\n" +
                    "    `email` VARCHAR(50) DEFAULT NULL,\n" +
                    "    `gender` VARCHAR(10) DEFAULT NULL,\n" +
                    "    `dob` VARCHAR(10) DEFAULT NULL,\n" +
                    "    PRIMARY KEY (`id`)\n" +
                    ");";

            preparedStatement = (PreparedStatement) con.prepareStatement(sql_stmt);
            preparedStatement.executeUpdate();

            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : "+ex.getMessage());
           return null;
        }
    }

    //make sure you add the lib
}
