package Model;


import java.util.ArrayList;
import java.util.List;

public class Cakes {
    private List<Cake> cakeList = new ArrayList<Cake>();

    public Cakes() {

    }

    public Cakes(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

    public Cakes(Cakes cakes) {
        this.cakeList = cakes.cakeList;
    }

    public List<Cake> getCakeList() {
        return cakeList;
    }

    public void setCakeList(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

    public String toString() {
        String result = "";
        for (Cake cake : cakeList) {
            result += cake + "\n";
        }
        return result;
    }
}