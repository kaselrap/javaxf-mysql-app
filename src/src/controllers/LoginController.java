package src.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import src.home.Main;
import src.models.User;
import src.utils.ConnectionUtil;
import java.util.prefs.Preferences;
import java.lang.String;
/**
 *
 * @author oXCToo
 */
public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    TableView tblDataCategorie;

    @FXML
    TableView tblDataCompanie;
    /// -- 
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleButtonRegisterAction (MouseEvent event) {
        if ( event.getSource() == btnSignup ) {
            try {

                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();

                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/src/fxml/Register.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignin) {
            //login here
            if (logIn().equals("Success")) {
                try {
                    //add you loading or delays - ;-)
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/src/fxml/OnBoard.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
            fetColumnList(tblDataCompanie,
                    "SELECT" +
                            "       COUNT(*) as countProductsInCompany," +
                            "       companies.name as company_name" +
                            " FROM products" +
                            " INNER JOIN companies ON products.company_id = companies.id" +
                            " GROUP BY companies.name");
            fetRowList(tblDataCompanie,
                    "SELECT" +
                            "       COUNT(*) as countProductsInCompany," +
                            "       companies.name as company_name" +
                            " FROM products" +
                            " INNER JOIN companies ON products.company_id = companies.id" +
                            " GROUP BY companies.name");

            fetColumnList(tblDataCategorie,
                    "SELECT" +
                            "       COUNT(*) as countProductsInCategory," +
                            "       categories.name as category_name" +
                            " FROM products" +
                            " INNER JOIN categories ON products.category_id = categories.id" +
                            " GROUP BY categories.name");
            fetRowList(tblDataCategorie,
                    "SELECT" +
                            "       COUNT(*) as countProductsInCategory," +
                            "       categories.name as category_name" +
                            " FROM products" +
                            " INNER JOIN categories ON products.category_id = categories.id" +
                            " GROUP BY categories.name");
        }
    }

    public LoginController() {
        con = (Connection) ConnectionUtil.conDB();
    }

    //we gonna use string to check for status
    private String logIn() {

        String email = txtUsername.getText();
        String password = txtPassword.getText();
        //query
        String sql = "SELECT * FROM user Where email = ? OR name = ? and password = ?";
        int primkey = 0 ;
        Preferences userPreferences = Preferences.userRoot();
        try {
            if ( con.isClosed() ) {
                con = (Connection) ConnectionUtil.conDB();
            }
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                lblErrors.setTextFill(Color.TOMATO);
                lblErrors.setText("Enter Correct Email/Password");
                System.err.println("Wrong Logins --///");
                return "Error";

            } else {
                Main.setUserId(resultSet.getInt("id"));
                lblErrors.setTextFill(Color.GREEN);
                lblErrors.setText("Login Successful..Redirecting..");
                System.out.println("Successfull Login");
                return "Success";
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return "Exception";
        }

    }

    private ObservableList<ObservableList> data;

    //only fetch columns
    private void fetColumnList(TableView tblData, String SQL) {
//        String SQL = "SELECT " +
//                "       wip_users.firstname," +
//                "       wip_users.lastname," +
//                "       wip_users.email," +
//                "       wip_users.dob," +
//                "       wip_users.gender," +
//                "       user.name as name_user" +
//                " FROM wip_users" +
//                " LEFT JOIN user ON wip_users.user_username = user.name";
        try {
            if ( con.isClosed() ) {
                con = (Connection) ConnectionUtil.conDB();
            }
            ResultSet rs = con.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList(TableView tblData, String SQL) {

//        String SQL = "SELECT" +
//                "       wip_users.firstname," +
//                "       wip_users.lastname," +
//                "       wip_users.email," +
//                "       wip_users.dob," +
//                "       wip_users.gender," +
//                "       user.name as name_user" +
//                " FROM wip_users" +
//                " LEFT JOIN user ON wip_users.user_username = user.name";
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            if ( con.isClosed() ) {
                con = (Connection) ConnectionUtil.conDB();
            }
            rs = con.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
