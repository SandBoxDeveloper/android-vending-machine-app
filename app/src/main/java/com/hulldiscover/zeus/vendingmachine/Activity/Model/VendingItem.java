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

    public VendingItem() {
        super();
         /*this.setImage(image);
        this.setDescription(description);
        this.setPrice(price);*/
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

   /* @Override
    public String toString () {
        return description + ": " + "\n" + price + ": '";
    }*/
}
