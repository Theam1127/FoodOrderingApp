package my.edu.tarc.foodorderingapp;

import java.io.Serializable;
import java.util.Date;

public class PaymentHistoryClass implements Serializable{
    private double amountPaid;
    private double change;
    private double grandTotal;
    private int orderID;
    private String date;
    private String time;
    private int paymentID;
    private String staffID;

    public PaymentHistoryClass(){}

    public PaymentHistoryClass(double amountPaid, double change, double grandTotal, int orderID, String date, String time, int paymentID, String staffID) {
        this.amountPaid = amountPaid;
        this.change = change;
        this.grandTotal = grandTotal;
        this.orderID = orderID;
        this.date = date;
        this.time = time;
        this.paymentID = paymentID;
        this.staffID = staffID;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
}
