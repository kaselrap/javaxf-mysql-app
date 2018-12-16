/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.controllers;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import src.home.Main;
import src.utils.ConnectionUtil;
import java.lang.NullPointerException;
/**
 * FXML Controller class
 *
 * @author oXCToo
 */
public class HomeController implements Initializable {

    @FXML
    private TextField userId;
    @FXML
    private Pane productsPane;
    @FXML
    private TextField txtNameProduct;
    @FXML
    private TextArea txtDescriptionProduct;
    @FXML
    private TextField txtPriceProduct;
    @FXML
    private Button btnSaveProduct;
    @FXML
    private ComboBox<String> txtCompaniesProduct;
    @FXML
    private ComboBox<String> txtCategoriesProduct;
    @FXML
    private Label lblStatus;
    @FXML
    private Pane categoriesPane;
    @FXML
    private TextField txtNameCategory;
    @FXML
    private Button btnSaveCategory;
    @FXML
    private Pane companiesPane;
    @FXML
    private TextField txtNameCompany;
    @FXML
    private Button btnSaveCompany;
    @FXML
    private Pane productsPaneUpdate;
    @FXML
    private TextField txtProductId;
    @FXML
    private TextField txtNameProductUpdate;
    @FXML
    private TextArea txtDescriptionProductUpdate;
    @FXML
    private TextField txtPriceProductUpdate;
    @FXML
    private Button btnUpdateProduct;
    @FXML
    private ComboBox<String> txtCompaniesProductUpdate;
    @FXML
    private ComboBox<String> txtCategoriesProductUpdate;
    @FXML
    private Button btnLogout;
    @FXML
    TableView tblData;
    @FXML
    TableView tblCategories;
    @FXML
    TableView tblCompanies;
    @FXML
    TableView tblCartShop;

    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    Connection connection;
    Integer getUserId = null;

    public HomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getUserId = Main.getUserId();
        // TODOa
        comboBoxCompanies(txtCompaniesProduct, "None");
        comboBoxCategories(txtCategoriesProduct, "None");

        fetColumnList(tblData,
                "SELECT    products.id," +
                        "       products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM products" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);
        fetRowList(tblData,
                "SELECT    products.id," +
                        "       products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM products" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);
        fetColumnList(tblCategories,
                "SELECT * from categories");
        fetRowList(tblCategories,
                "SELECT * from categories");
        fetColumnList(tblCompanies,
                "SELECT * from companies");
        fetRowList(tblCompanies,
                "SELECT * from companies");

        fetColumnList(tblCartShop,
                "SELECT products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM cart_shop" +
                        " INNER JOIN products ON products.id = cart_shop.product_id" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);

        fetRowList(tblCartShop,
                "SELECT products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM cart_shop" +
                        " INNER JOIN products ON products.id = cart_shop.product_id" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);

        MenuItem miAddToCart = new MenuItem("Add to Cart");
        MenuItem miEditProduct = new MenuItem("Edit Product");
        MenuItem miDeleteProduct = new MenuItem("Delete Product");
        miAddToCart.setOnAction((ActionEvent event) -> {
            try {
                if( tblData.getSelectionModel().getSelectedItem() != null ) {
                    TableView.TableViewSelectionModel selectionModel = tblData.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();

                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);


                    tablePosition.getTableView().getSelectionModel().getTableView().getId();
                    String prodct_id = tblData.getSelectionModel().getSelectedItem().toString();
                    //gives you first column value..
                    Integer products_id = Integer.parseInt(prodct_id.toString().split(",")[0].substring(1));
                    String st = "INSERT INTO cart_shop ( product_id, user_id) VALUES (?,?)";
                    preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                    preparedStatement.setInt(1, products_id);
                    preparedStatement.setInt(2, getUserId);

                    preparedStatement.executeUpdate();
                    lblStatus.setTextFill(Color.GREEN);
                    lblStatus.setText("Added Successfully");

                    fetColumnList(tblData,
                            "SELECT    products.id," +
                                    "       products.name," +
                                    "       products.description," +
                                    "       products.price," +
                                    "       companies.name as companyName," +
                                    "       categories.name as categoryName" +
                                    " FROM products" +
                                    " INNER JOIN companies ON products.company_id = companies.id" +
                                    " INNER JOIN categories ON products.category_id = categories.id" +
                                    " WHERE products.user_id = "+ getUserId);
                    //clear fields
                    clearFields();
                }
            } catch (SQLException ex) {

            }
        });
        miEditProduct.setOnAction((ActionEvent event) -> {
            if( tblData.getSelectionModel().getSelectedItem() != null ) {
                TableView.TableViewSelectionModel selectionModel = tblData.getSelectionModel();
                ObservableList selectedCells = selectionModel.getSelectedCells();

                TablePosition tablePosition = (TablePosition) selectedCells.get(0);

                tablePosition.getTableView().getSelectionModel().getTableView().getId();
                String product = tblData.getSelectionModel().getSelectedItem().toString();
                String productSplit[] = product.toString().split(",");
                System.out.println(productSplit[0].substring(1));
                System.out.println(productSplit[1].substring(1));
                System.out.println(productSplit[2].substring(1));
                System.out.println(productSplit[3].substring(1));
                System.out.println(productSplit[4].substring(1));
                System.out.println(productSplit[5].substring(1).replace("]", ""));

                productsPane.setVisible(false);
                productsPaneUpdate.setVisible(true);
                txtProductId.setText(productSplit[0].substring(1));
                txtNameProductUpdate.setText(productSplit[1].substring(1));
                txtDescriptionProductUpdate.setText(productSplit[2].substring(1));
                txtPriceProductUpdate.setText(productSplit[3].substring(1));
                comboBoxCompanies(txtCompaniesProductUpdate, productSplit[4].substring(1));
                comboBoxCategories(txtCategoriesProductUpdate, productSplit[5].substring(1).replace("]", ""));
            }
        });
        miDeleteProduct.setOnAction((ActionEvent event) -> {
            try {
                if( tblData.getSelectionModel().getSelectedItem() != null ) {
                    TableView.TableViewSelectionModel selectionModel = tblData.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();

                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);


                    tablePosition.getTableView().getSelectionModel().getTableView().getId();
                    String prodct_id = tblData.getSelectionModel().getSelectedItem().toString();
                    //gives you first column value..
                    Integer products_id = Integer.parseInt(prodct_id.toString().split(",")[0].substring(1));
                    String st = "DELETE FROM products WHERE id = " + products_id;
                    preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                    preparedStatement.executeUpdate();
                    lblStatus.setTextFill(Color.GREEN);
                    lblStatus.setText("Deleted Successfully product by id = " + products_id);

                    fetColumnList(tblData,
                            "SELECT    products.id," +
                                    "       products.name," +
                                    "       products.description," +
                                    "       products.price," +
                                    "       companies.name as companyName," +
                                    "       categories.name as categoryName" +
                                    " FROM products" +
                                    " INNER JOIN companies ON products.company_id = companies.id" +
                                    " INNER JOIN categories ON products.category_id = categories.id" +
                                    " WHERE products.user_id = "+ getUserId);
                    //clear fields
                    clearFields();
                }
            } catch (SQLException ex) {

            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(miAddToCart);
        menu.getItems().add(miEditProduct);
        menu.getItems().add(miDeleteProduct);
        tblData.setContextMenu(menu);

    }

    private String comboBoxCompanies(ComboBox combobox, String defaultSelect) {
        combobox.getItems().clear();
        combobox.getSelectionModel().select(defaultSelect);
        String SQL = "SELECT * from companies";
        ResultSet rs;
        try {
            if ( connection.isClosed() ) {
                connection = (Connection) ConnectionUtil.conDB();
            }
            rs = connection.createStatement().executeQuery(SQL);
            int index = 0;
            while (rs.next()) {
                combobox.getItems().add(rs.getInt("id") - 1, rs.getString("name"));
                index++;
            }
            return "Success";
        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private String comboBoxCategories(ComboBox combobox, String defaultSelect) {
        combobox.getItems().clear();
        combobox.getSelectionModel().select(defaultSelect);
        String SQL = "SELECT * from categories";
        ResultSet rs;
        try {
            if ( connection.isClosed() ) {
                connection = (Connection) ConnectionUtil.conDB();
            }
            rs = connection.createStatement().executeQuery(SQL);
            int index = 0;
            while (rs.next()) {
                combobox.getItems().add(rs.getInt("id") - 1, rs.getString("name"));
                index++;
            }
            return "Success";
        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private void refreshData(){

        tblData.getItems().removeAll();
        tblCategories.getItems().removeAll();
        tblCompanies.getItems().removeAll();
        tblCartShop.getItems().removeAll();

        comboBoxCompanies(txtCompaniesProduct, "None");
        comboBoxCategories(txtCategoriesProduct, "None");
        fetRowList(tblData,
                "SELECT    products.id," +
                        "       products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM products" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);
        fetRowList(tblCategories,
                "SELECT * from categories");
        fetRowList(tblCompanies,
                "SELECT * from companies");

        fetRowList(tblCartShop,
                "SELECT products.name," +
                        "       products.description," +
                        "       products.price," +
                        "       companies.name as companyName," +
                        "       categories.name as categoryName" +
                        " FROM cart_shop" +
                        " INNER JOIN products ON products.id = cart_shop.product_id" +
                        " INNER JOIN companies ON products.company_id = companies.id" +
                        " INNER JOIN categories ON products.category_id = categories.id" +
                        " WHERE products.user_id = "+ getUserId);
    }

    @FXML
    private void HandleEvents(MouseEvent event) {

        if (event.getSource() == btnSaveProduct) {
            if (txtNameProduct.getText().isEmpty() || txtDescriptionProduct.getText().isEmpty() || txtPriceProduct.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                saveData("products");
            }
        } else if (event.getSource() == btnSaveCategory) {
            if (txtNameCategory.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                saveData("categories");
            }
        } else if (event.getSource() == btnSaveCompany) {
            if (txtNameCompany.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                saveData("companies");
            }
        } else if (event.getSource() == btnUpdateProduct) {
            if (txtNameProductUpdate.getText().isEmpty() || txtDescriptionProductUpdate.getText().isEmpty() || txtPriceProductUpdate.getText().isEmpty()) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Enter all details");
            } else {
                productsPane.setVisible(true);
                productsPaneUpdate.setVisible(false);
                saveData("productsUpdate");
            }
        }
        refreshData();

    }


    @FXML
    public void handleLogoutAction(MouseEvent event) {
        if(event.getSource() == btnLogout) {
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

    private void clearFields() {
        txtNameProduct.clear();
        txtDescriptionProduct.clear();
        txtPriceProduct.clear();
        txtNameCategory.clear();
        txtNameCompany.clear();
    }

    private String saveData(String type) {

        try {
            if ( connection.isClosed() ) {
                connection = (Connection) ConnectionUtil.conDB();
            }
//            String st = "INSERT INTO products ( name, description, price, company_id, categorie_id, user_id) VALUES (?,?,?,?,?,?)";
            String st = null;
            if( type == "products" ) {
                st = "INSERT INTO products ( name, description, price, company_id, category_id, user_id) VALUES (?,?,?,?,?,?)";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.setString(1, txtNameProduct.getText());
                preparedStatement.setString(2, txtDescriptionProduct.getText());
                preparedStatement.setString(3, txtPriceProduct.getText());
                preparedStatement.setInt(4, txtCompaniesProduct.getSelectionModel().getSelectedIndex() + 1);
                preparedStatement.setInt(5, txtCategoriesProduct.getSelectionModel().getSelectedIndex() + 1);
                preparedStatement.setInt(6, getUserId);

                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Added Successfully");

                fetRowList(tblData,
                        "SELECT    products.id," +
                                "       products.name," +
                                "       products.description," +
                                "       products.price," +
                                "       companies.name as companyName," +
                                "       categories.name as categoryName" +
                                " FROM products" +
                                " INNER JOIN companies ON products.company_id = companies.id" +
                                " INNER JOIN categories ON products.category_id = categories.id" +
                                " WHERE products.user_id = "+ getUserId);
                //clear fields
                clearFields();
                return "Success";
            } else if ( type == "productsUpdate" ) {
                st = "UPDATE products SET name = ?, description = ?, price = ?, company_id = ?, category_id = ? WHERE id = ?";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.setString(1, txtNameProductUpdate.getText());
                preparedStatement.setString(2, txtDescriptionProductUpdate.getText());
                preparedStatement.setString(3, txtPriceProductUpdate.getText());
                preparedStatement.setInt(4, txtCompaniesProductUpdate.getSelectionModel().getSelectedIndex() + 1);
                preparedStatement.setInt(5, txtCategoriesProductUpdate.getSelectionModel().getSelectedIndex() + 1);
                preparedStatement.setInt(6, Integer.parseInt(txtProductId.getText()));

                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Updated Successfully");

                fetRowList(tblData,
                        "SELECT    products.id," +
                                "       products.name," +
                                "       products.description," +
                                "       products.price," +
                                "       companies.name as companyName," +
                                "       categories.name as categoryName" +
                                " FROM products" +
                                " INNER JOIN companies ON products.company_id = companies.id" +
                                " INNER JOIN categories ON products.category_id = categories.id" +
                                " WHERE products.user_id = "+ getUserId);
                //clear fields
                clearFields();
                return "Success";
            } else if ( type == "categories" ) {
                st = "INSERT INTO categories ( name ) VALUES (?)";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.setString(1, txtNameCategory.getText());
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Added Successfully");

                fetRowList(tblData,
                        "SELECT * from categories");
                //clear fields
                clearFields();
                return "Success";
            } else if ( type == "companies" ) {
                st = "INSERT INTO companies ( name ) VALUES (?)";
                preparedStatement = (PreparedStatement) connection.prepareStatement(st);
                preparedStatement.setString(1, txtNameCompany.getText());
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Added Successfully");

                fetRowList(tblData,
                        "SELECT * from companies");
                //clear fields
                clearFields();
                return "Success";
            }


        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
        return type;
    }
    private ObservableList<ObservableList> data;

    //only fetch columns
    private void fetColumnList(TableView tblData, String SQL) {
        try {
            if ( connection.isClosed() ) {
                connection = (Connection) ConnectionUtil.conDB();
            }
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

//                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
//            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList(TableView tblData, String SQL) {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            if ( connection.isClosed() ) {
                connection = (Connection) ConnectionUtil.conDB();
            }
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
//                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            tblData.setItems(data);
        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
        }
    }

    private boolean tabSelection = false;
    @FXML void tabProductsAction(Event e) {
        if (checkTab()) {
            productsPane.setVisible(true);
            categoriesPane.setVisible(false);
            companiesPane.setVisible(false);
        }
    }
    @FXML void tabCategoriesAction(Event e) {
        if (checkTab()) {
            productsPane.setVisible(false);
            categoriesPane.setVisible(true);
            companiesPane.setVisible(false);
        }
    }
    @FXML void tabCompaniesAction(Event e) {
        if (checkTab()) {
            productsPane.setVisible(false);
            categoriesPane.setVisible(false);
            companiesPane.setVisible(true);
        }
    }
    @FXML void tabCartShopAction(Event e) {
        if (checkTab()) {
            productsPane.setVisible(true);
            categoriesPane.setVisible(false);
            companiesPane.setVisible(false);
        }
    }

    private boolean checkTab() {
        tabSelection = !tabSelection;
        return tabSelection;
    }

}
