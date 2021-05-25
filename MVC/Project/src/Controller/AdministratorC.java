package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdministratorC {
    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField roleField;

    @FXML
    private Label invalidDataLabel;
    @FXML
    private ComboBox selectUser;

    @FXML
    private TextField newUsernameField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField newRoleField;

    @FXML
    private Label updateInvalidData;
    @FXML
    private ComboBox deleteUserComboBox;

    @FXML
    private ComboBox workplace;

    public AdministratorC() {
    }

    public void logOut(ActionEvent actionEvent) {
        try {
            Parent logInAccess = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
            Scene scene = new Scene(logInAccess, 658, 366);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Image img = new Image("./c3.png");
            window.setResizable(true);
            window.getIcons().add(img);
            window.setTitle("Cake Shop");
            window.setScene(scene);
            window.show();
        } catch (Exception exception) {

        }
    }

    public void viewAllUsers() {
        try {
            for (User user : getObservableUsers()) {
                usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
                passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
                roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
            }
            tableView.setItems(getObservableUsers());
        } catch (Exception e) {

        }
    }

    public ObservableList<User> getObservableUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        UserPersistence userPersistence = new UserPersistence();
        for (User user : userPersistence.readUsers().getUsersList()) {
            String mew = user.getName() + " works at " + workPlaceForUser(user.getId());
            users.add(new User(user.getId(), mew, user.getPassword(), user.getRole()));
        }
        return users;
    }

    public void addUser() {
        try {
            if (usernameField.getText().length() < 5 || passwordField.getText().length() < 5 || (!roleField.getText().equals("admin") && !roleField.getText().equals("employee"))) {
                invalidDataLabel.setText("Invalid data! Role should be admin or employee. \n Name and password longer than 5 characters.");
            }
            UserPersistence userPersistence = new UserPersistence();
            userPersistence.addUser(new User(usernameField.getText(), passwordField.getText(), roleField.getText()), workplace.getValue().toString());
            invalidDataLabel.setText("");
            usernameField.setText("");
            passwordField.setText("");
            roleField.setText("");
            invalidDataLabel.setVisible(false);
        } catch (Exception e) {
            invalidDataLabel.setVisible(true);
            invalidDataLabel.setText("Invalid data! Role should be admin or employee. \n Name and password longer than 5 characters.");
        }
    }

    public void fillSelectUser() {
        UserPersistence userPersistence = new UserPersistence();
        Users users = userPersistence.readUsers();
        selectUser.getItems().clear();
        for (User user : users.getUsersList()) {
            selectUser.getItems().add(user.getName());
        }
    }

    public void setSelectedUser() {
        UserPersistence userPersistence = new UserPersistence();
        if (selectUser != null && selectUser.getValue() != null) {
            String name = selectUser.getValue().toString().trim();
            User user = userPersistence.getUserByName(name);
            if (user != null) {
                newUsernameField.setText(user.getName());
                newPasswordField.setText(user.getPassword());
                newRoleField.setText(user.getRole());
            }
        }
    }

    public void updateUser() {
        UserPersistence userPersistence = new UserPersistence();
        String name = newUsernameField.getText();
        String password = newPasswordField.getText();
        String role = newRoleField.getText();
        if (name.length() < 5 || password.length() < 5 || (!role.equals("admin") && !role.equals("employee"))) {
            updateInvalidData.setText("Invalid data! Role should be admin or employee. \n Name and password longer than 5 characters.");
        } else {
            User user = new User(name, password, role);
            if (userPersistence.updateUser(selectUser.getValue().toString(), user)) {
                updateInvalidData.setText("");
                newUsernameField.setText("");
                newPasswordField.setText("");
                newRoleField.setText("");
            } else {
                updateInvalidData.setText("Invalid data! Role should be admin or employee. \n Name and password longer than 5 characters.");
            }
        }
    }

    public void fillDeleteUser() {
        UserPersistence userPersistence = new UserPersistence();
        Users users = userPersistence.readUsers();
        deleteUserComboBox.getItems().clear();
        for (User user : users.getUsersList()) {
            deleteUserComboBox.getItems().add(user.getName());
        }
    }

    public void deleteUser() {
        UserPersistence userPersistence = new UserPersistence();
        if (deleteUserComboBox.getValue().equals("")) {
            //
        } else {
            userPersistence.deleteUser(deleteUserComboBox.getValue().toString().trim());
            deleteUserComboBox.setValue("");
        }
    }

    public void fillWorkplace() {
        CakePersistence cakePersistence = new CakePersistence();
        CakeShops cakeShops = cakePersistence.getCakeShops();
        workplace.getItems().clear();
        for (CakeShop cakeShop : cakeShops.getCakeShopList()) {
            workplace.getItems().add(cakeShop.getName());
        }
    }

    public String workPlaceForUser(String id) {
        CakePersistence cakePersistence = new CakePersistence();
        if (cakePersistence.cakeShopWhereUserWorks(id) != null) {
            return cakePersistence.cakeShopWhereUserWorks(id).getName();
        } else return " ";
    }
}
