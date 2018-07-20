package my.edu.tarc.foodorderingapp;

import java.io.Serializable;

public class Menu implements Serializable{
    public String menuID;
    public String name;
    public String type;
    public boolean available;
    public double price;

    public Menu(){}

    public Menu(String menuID, String name, String type, boolean available, double price) {
        this.menuID = menuID;
        this.name = name;
        this.type = type;
        this.available = available;
        this.price = price;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuID='" + menuID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", available=" + available +
                ", price=" + price +
                '}';
    }
}
