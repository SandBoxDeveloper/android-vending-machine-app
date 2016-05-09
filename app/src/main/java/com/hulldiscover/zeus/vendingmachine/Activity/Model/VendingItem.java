package com.hulldiscover.zeus.vendingmachine.Activity.Model;

import android.graphics.Bitmap;

/**
 * Created by Zeus on 07/05/16.
 */
public class VendingItem {


    private Bitmap imageBitmap;
    private String image;
    private String description;
    private String price;
    private int qauntity;


    public VendingItem() {
        super();
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQauntity() {
        return qauntity;
    }

    public void setQauntity(int qauntity) {
        this.qauntity = qauntity;
    }

    public void updateQuantity(int newQuantity) {
        this.qauntity -= newQuantity;
    }

}
