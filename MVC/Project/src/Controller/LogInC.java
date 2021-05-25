package Controller;

import Model.User;
import Model.UserPersistence;
import Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class LogInC {
    private static String loggedUser;
    @FXML
    private Label invalidData;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;

    public LogInC() {
    }

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(String loggedUser) {
        LogInC.loggedUser = loggedUser;
    }

    public int logIn2() {
        UserPersistence userPersistence = new UserPersistence();
        Users users = userPersistence.readUsers();
        for (User user : users.getUsersList()) {
            if (usernameField.getText().equals(user.getName())) {
                if (passwordField.getText().equals(user.getPassword())) {
                    loggedUser = user.getId();
                    if (user.getRole().equals("employee")) {
                        return 1;
                    } else {
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    public void logIn(ActionEvent actionEvent) {
        int userType = logIn2();
        if (userType == 1) {
            try {
                invalidData.setText("");
                Parent employeeAccess = FXMLLoader.load(getClass().getResource("../View/userView.fxml"));
                Scene scene = new Scene(employeeAccess, 930, 780);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Image img = new Image("./c3.png");
                window.setResizable(true);
                window.getIcons().add(img);
                window.setTitle("Employee page");
                window.setScene(scene);
                window.show();
            } catch (Exception e) {
            }
        } else {
            if (userType == 2) {
                try {
                    invalidData.setText("");
                    Parent adminAccess = FXMLLoader.load(getClass().getResource("../View/adminView.fxml"));
                    Scene scene = new Scene(adminAccess, 682, 728);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Image img = new Image("./c3.png");
                    window.setResizable(true);
                    window.getIcons().add(img);
                    window.setTitle("Admin page");
                    window.setScene(scene);
                    window.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                invalidData.setText("Invalid data. Please try again!");
            }
        }
    }
}
