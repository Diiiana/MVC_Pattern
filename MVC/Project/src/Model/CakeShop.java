package Model;

public class CakeShop {
    private String name;
    private Cakes menu = new Cakes();
    private Users users = new Users();

    public CakeShop() {

    }

    public CakeShop(String name, Cakes menu, Users users) {
        this.name = name;
        this.menu = menu;
        this.users = users;
    }

    public CakeShop(CakeShop cakeShop) {
        this.name = cakeShop.name;
        this.menu = cakeShop.menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cakes getMenu() {
        return menu;
    }

    public void setMenu(Cakes menu) {
        this.menu = menu;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String toString() {
        return this.name + " " + this.menu;
    }
}
