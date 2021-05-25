package Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cake implements Comparable<Cake> {
    private String id;
    private String name;
    private int disponibility;
    private Date availability;
    private float price;

    public Cake() {

    }

    public Cake(String id, String name, int disponibility, Date availability, float price) {
        this.id = id;
        this.name = name;
        this.disponibility = disponibility;
        this.availability = availability;
        this.price = price;
    }

    public Cake(String name, int disponibility, Date availability, float price) {
        this.name = name;
        this.disponibility = disponibility;
        this.availability = availability;
        this.price = price;
    }

    public Cake(Cake cake) {
        this.name = cake.name;
        this.disponibility = cake.disponibility;
        this.availability = cake.availability;
        this.price = cake.price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAvailability() {
        return availability;
    }

    public void setAvailability(Date availability) {
        this.availability = availability;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(int disponibility) {
        this.disponibility = disponibility;
    }

    public String toString() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(this.getAvailability());
        return this.name + ", " + this.disponibility + " pieces, available until " + date + ", price: " + this.price + ".";
    }

    @Override
    public int compareTo(Cake o) {
        if (availability.compareTo(o.availability) < 0) {
            return -1;
        } else {
            if (availability.compareTo(o.availability) > 0) {
                return 1;
            }
        }
        return 0;
    }
}
