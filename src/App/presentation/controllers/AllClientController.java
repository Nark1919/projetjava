package App.presentation.controllers;

import App.dao.entities.Client;
import App.dao.*;
import App.IntroPageAdmin;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AllClientController implements Initializable {

    Stage stage;

    private String userRole;
    private int adminId;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }


    public JFXButton homeBackBtn;
    public JFXButton AddClientBtn;
    public JFXButton viewAllClient;
    public TableView<Client> clientTableView;
    public Label clientCountLabel;
    public TableColumn<Client, String> clientId;
    public TableColumn<Client, String> clientName;
    public TableColumn<Client, String> contactPerson;
    public TableColumn<Client, String> phone;
    public TableColumn<Client, String> address;

    private void getClientTableData(){
        //Clear the all column data from table
        clientTableView.getItems().clear();
        int clientCount = 0;

        try {
            SingletonConnexionDB singletonConnexionDB =new SingletonConnexionDB();
            Connection connection= singletonConnexionDB.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM CLIENT";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                clientCount++;
                String clientId = rs.getString("id");
                String clientName = rs.getString("name");
                String contactPerson = rs.getString("contact_person");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                Client singleClient = new Client(clientId,clientName,contactPerson,phone,address);
                clientTableView.getItems().add(singleClient);
            }

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        clientCountLabel.setText("Total des clients : " + String.valueOf(clientCount) );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        contactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientTableView.setEditable(true);

        getClientTableData();

    }

    public void homeBackBtnAction(ActionEvent event) {
        if(event.getSource() == homeBackBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../views/intropageadmin.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            IntroPageAdmin introPageAdmin = Loader.getController();
            introPageAdmin.setAdminId(getAdminId());
            introPageAdmin.setUserRole(getUserRole());
            introPageAdmin.getAdminName(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) homeBackBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void AddClientBtnAction(ActionEvent event) {
        if(event.getSource() == AddClientBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../views/addclient.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AddClientController addClientController = Loader.getController();
            addClientController.setAdminId(getAdminId());
            addClientController.setUserRole(getUserRole());

            Parent p = Loader.getRoot();
            stage = (Stage) AddClientBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void viewAllClientAction(ActionEvent event) {
    }
}
