package my.edu.tarc.foodorderingapp;

import java.io.Serializable;

public class Orders implements Serializable{
    int menuID;
    int quantity;
    double total;

    public Orders() {

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Orders(int menuID, int quantity, double total) {
        this.menuID = menuID;
        this.quantity = quantity;
        this.total = total;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
