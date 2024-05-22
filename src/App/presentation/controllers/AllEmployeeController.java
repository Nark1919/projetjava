package App.presentation.controllers;

import App.dao.entities.Employee;
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

public class AllEmployeeController implements  Initializable{
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
    public JFXButton AddEmployeeBtn;
    public JFXButton viewAllEmployee;
    public TableView<Employee> employeeTableView;
    public Label employeeCountLabel;
    public TableColumn<Employee, String> empId;
    public TableColumn<Employee, String> empName;
    public TableColumn<Employee, String> empDept;
    public TableColumn<Employee, String> empDesignation;
    public TableColumn<Employee, String> empEmail;
    public TableColumn<Employee, String> empPhone;

    private void getEmployeeTableData(){
        //Clear the all column data from table
        employeeTableView.getItems().clear();
        int employeeCount = 0;

        try {
            SingletonConnexionDB singletonConnexionDB =new SingletonConnexionDB();
            Connection connection= singletonConnexionDB.getConnection();

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM EMPLOYEE";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                employeeCount++;
                String empID = rs.getString("id");
                String employeeName = rs.getString("name");
                String empDesig = rs.getString("designation");
                String empDep = rs.getString("department");
                String empPhone = rs.getString("contact");
                String empMail = rs.getString("email");

                Employee singleEmployee = new Employee(empID, employeeName, empDep, empDesig, empPhone, empMail);
                employeeTableView.getItems().add(singleEmployee);
            }

            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        employeeCountLabel.setText("Total des employés : " + String.valueOf(employeeCount));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        empName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        empDept.setCellValueFactory(new PropertyValueFactory<>("empDept"));
        empDesignation.setCellValueFactory(new PropertyValueFactory<>("empDesignation"));
        empEmail.setCellValueFactory(new PropertyValueFactory<>("empEmail"));
        empPhone.setCellValueFactory(new PropertyValueFactory<>("empPhone"));
        employeeTableView.setEditable(true);

        getEmployeeTableData();
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

    public void AddEmployeeBtnAction(ActionEvent event) {
        if(event.getSource() == AddEmployeeBtn) {
            FXMLLoader Loader = new FXMLLoader();

            Loader.setLocation(getClass().getResource("../views/addemployee.fxml"));

            try {
                Loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AddEmployeeController addEmpController = Loader.getController();
            addEmpController.setAdminId(getAdminId());
            addEmpController.setUserRole(getUserRole());

            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void viewAllEmployeeAction(ActionEvent event) {
    }
}
