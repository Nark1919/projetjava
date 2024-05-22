package App.presentation.controllers;

import App.dao.*;
import App.IntroPageAdmin;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {

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
    public JFXButton addNewClientBtn;
    public JFXTextField clientIdField;
    public JFXTextField addressField;
    public JFXTextField phoneField;
    public JFXTextField clientNameField;
    public JFXTextField contactPersonField;
    public Label confirmationMsg;

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
    }

    public void viewAllClientAction(ActionEvent event) {
        if(event.getSource() == viewAllClient){
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../views/allclient.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AllClientController allClientController = Loader.getController();
            allClientController.setUserRole(getUserRole());
            allClientController.setAdminId(getAdminId());

            Parent p = Loader.getRoot();
            stage = (Stage) AddClientBtn.getScene().getWindow();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void addNewClientAction(ActionEvent event) {
        if(event.getSource() == addNewClientBtn){
            int clientId = Integer.parseInt(clientIdField.getText());
            String clientName = clientNameField.getText();
            String contactPerson = contactPersonField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if(clientName.trim().isEmpty() || contactPerson.trim().isEmpty() || phone.trim().isEmpty() || address.trim().isEmpty()){
                confirmationMsg.setText("Veuillez remplir le formulaire correctement.");
                return;
            }

            SingletonConnexionDB singletonConnexionDB = new SingletonConnexionDB();
            Connection connection = singletonConnexionDB.getConnection();
            PreparedStatement ps = null;

            try {
                String sql = "INSERT INTO CLIENT(name, contact_person, phone, address) values (?,?,?,?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, clientName);
                ps.setString(2, contactPerson);
                ps.setString(3, phone);
                ps.setString(4, address);
                ps.executeUpdate();

                ps.close();

                confirmationMsg.setStyle("-fx-text-fill: #24bb71");
                confirmationMsg.setText("Le client "+ clientName + " est ajouté avec succès.");

            } catch (SQLException e) {
                e.printStackTrace();
                confirmationMsg.setText(e.getMessage());
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int id = 0;



        try {
            SingletonConnexionDB singletonConnexionDB = new SingletonConnexionDB();
            Connection connection = singletonConnexionDB.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT MAX(id) as id FROM CLIENT";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = (int) rs.getInt("id");
            }

            statement.close();
            connection.close();
            id += 1;
            clientIdField.setText(String.valueOf(id));
            clientIdField.setEditable(false);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
