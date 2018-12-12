package src.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import src.utils.ConnectionUtil;

public class RegisterController  implements Initializable{

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPasswordRepeat;

    @FXML
    private Button btnSignup;

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public RegisterController() {
        con = ConnectionUtil.conDB();
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignup) {
            //login here
            if (register().equals("Success")) {
                try {

                    //add you loading or delays - ;-)
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/src/fxml/Login.fxml")));
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
        }
    }

    private String register () {

        String name = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String passwordRepeat = txtPasswordRepeat.getText();

        if (!password.equals(passwordRepeat)) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Password don't match, check pls");
            System.err.println("Wrong, Register --///");
            return "Error";
        } else {

            //query
            String sql = "INSERT INTO user ( name, email, password) VALUES (?,?,?)";

            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    lblErrors.setTextFill(Color.TOMATO);
                    lblErrors.setText("Enter Correct Email/Password");
                    System.err.println("Wrong Register --///");
                    return "Error";

                } else {
                    lblErrors.setTextFill(Color.GREEN);
                    lblErrors.setText("Register Successful..Redirecting..");
                    System.out.println("Successfull Register");
                    return "Success";
                }


            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return "Exception";
            }
        }
    }

}
