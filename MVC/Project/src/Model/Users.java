package Model;

import java.util.*;

public class Users {

    private List<User> usersList = new ArrayList<User>();

    public Users() {

    }

    public Users(List<User> usersList) {
        this.usersList = usersList;
    }

    public Users(Users users) {
        this.usersList = users.usersList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public String toString() {
        String result = "";
        for (User user : usersList) {
            result += user.toString() + "\n";
        }
        return result;
    }
}
