package com.hulldiscover.zeus.vendingmachine.Activity.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.vendingmachine.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Zeus on 08/05/16.
 */
public class DetailedViewActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView itemPrice;
    private ImageView imageView;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private FrameLayout frameLayout;
    private ColorDrawable colorDrawable;

    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;

    private String description;
    private int price;

    int minteger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting details screen layout
        setContentView(R.layout.activity_details_view);

        //retrieves the selected item data
        Bundle bundle = getIntent().getExtras();

        String description = bundle.getString("description");
        String price = bundle.getString("price");
        String image = bundle.getString("image");

        //initialize and set the image description
        titleTextView = (TextView) findViewById(R.id.stock_item_description);
        titleTextView.setText(description);

        itemPrice = (TextView) findViewById(R.id.stock_item_price);
        itemPrice.setText(price);


        //Set image url
        imageView = (ImageView) findViewById(R.id.item_image);
        Picasso.with(this).load(image).into(imageView);
    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger - 1;
        if(minteger < 0) {
            minteger = 0; // reset value to 0 to prevent quantity less than 0
        }
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }
}
