package com.hulldiscover.zeus.vendingmachine.Activity;

/**
 * Created by Zeus on 07/05/16.
 */
public class VendingItem {

    private String image;
    private String description;
    private int price;

    public VendingItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
