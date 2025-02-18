package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.grocerynotifier.converters.DateFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "products")
@TypeConverters(DateFormatter.class)
public class Product {
    @PrimaryKey
    @NotNull
    private String barcode;
    @NotNull
    private String name;
    private Date boughtDate;
    @NotNull
    private Date expirationDate;
    private double price;
    private int weight;

    public Product() {
    }

    public Product(String barcode, int weight, double price, Date expirationDate, String name, Date boughtDate) {
        this.barcode = barcode;
        this.weight = weight;
        this.price = price;
        this.expirationDate = expirationDate;
        this.name = name;
        this.boughtDate = boughtDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Date boughtDate) {
        this.boughtDate = boughtDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
