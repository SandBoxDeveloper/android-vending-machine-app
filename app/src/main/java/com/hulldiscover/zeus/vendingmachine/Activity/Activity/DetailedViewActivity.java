package com.hulldiscover.zeus.vendingmachine.Activity.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.vendingmachine.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zeus on 08/05/16.
 */
public class DetailedViewActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView itemPrice;
    private TextView userValue;
    private TextView insufficientFundsMessage;
    private EditText userCoinInput;
    private ImageView imageView;
    private ImageView chosenItem;
    private ImageView customDialogImage;
    private ImageButton insertAmount;
    private Button purchaseItem;


    int mInteger = 0;
    int mQuantity = 0;
    String mPrice = "0";
    String mChosenItem;
    public static double mInsertedAmount = 0.00;

    //change denominations
    //double[] denominations = {0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00};
    List<Double> denominations = new ArrayList<>(Arrays.asList(0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00));

    List<Double> changeCount = new ArrayList<>(Arrays.asList(0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00));
    DecimalFormat df = new DecimalFormat("#.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting details screen layout
        setContentView(R.layout.activity_details_view);

        //retrieves the selected item data
        Bundle bundle = getIntent().getExtras();

        String description = bundle.getString("description");
        mPrice = bundle.getString("price");
        String image = bundle.getString("image");
        mQuantity = bundle.getInt("quantity");

        //set chosen item image for later use
        mChosenItem = image;

        //initialize and set the image description
        titleTextView = (TextView) findViewById(R.id.stock_item_description);
        titleTextView.setText(description);

        itemPrice = (TextView) findViewById(R.id.stock_item_price);
        itemPrice.setText("£" +mPrice);


        //Set image url
        imageView = (ImageView) findViewById(R.id.item_image);
        Picasso.with(this).load(image).into(imageView);

        Animation translatebu= AnimationUtils.loadAnimation(this, R.anim.accelerate_decelerate);
        userValue = (TextView) findViewById(R.id.user_value);
        userValue.setText("Balance: £" +mInsertedAmount);
        userValue.startAnimation(translatebu);

        //initialize insert money button
        insertAmount = (ImageButton) findViewById(R.id.insertCoinButton);
        insertAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(DetailedViewActivity.this)).inflate(R.layout.alert_dialog, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailedViewActivity.this);
                alertBuilder.setView(view);
                userCoinInput = (EditText) view.findViewById(R.id.editTextDialogUserInput);

                double totalCostToPayForItem = 0; // total cost for item

                //check quantity
                if(mInteger > 0) { // if quantity user wants is greater than 0
                    totalCostToPayForItem = (Double.parseDouble(mPrice)) * mInteger; // times mPrice by quantity
                } else { // normal mPrice stands
                    totalCostToPayForItem = Double.parseDouble(mPrice);
                }

                //show to user how much it will cost to pay for item(s)
                userCoinInput.setHint("Insert amount: £" + totalCostToPayForItem);

                //insert button for dialog
                alertBuilder.setCancelable(true)
                        .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = userCoinInput.getText().toString();
                                mInsertedAmount += Double.parseDouble(input); // update machine system with the amount the user has inserted
                                df.format(mInsertedAmount); // format amount to two decimal places
                                userValue.setText("Balance: " + mInsertedAmount);
                            }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

        //initialize purchase button
        purchaseItem = (Button) findViewById(R.id.purchase_button);
        purchaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get mPrice of item
                // get how much money the user has inserted into machine
                // calculate
                //check quantity
                Double calculation = 0.00;
                double costOfItem = 0.00;
                if(mInteger > 0) { // if quantity user wants is greater than 0
                    calculation = mInsertedAmount - ((Double.parseDouble(mPrice)) * mInteger); // times mPrice by quantity
                    costOfItem = (Double.parseDouble(mPrice)) * mInteger;
                } else { // normal mPrice stands
                    calculation = mInsertedAmount - (Double.parseDouble(mPrice));
                    costOfItem = Double.parseDouble(mPrice);
                }
                //Double calculation = mInsertedAmount - (Double.parseDouble(mPrice) * mInteger);
                Log.d("PURCHASE BUTTON", " Calculation is: " + calculation);

                if(costOfItem > mInsertedAmount) { // user doesn't have enough money
                    // alert user insufficient funds, insert more mo mo money
                    customAlertDialog();
                    Log.d("PURCHASE BUTTON", " Price of item cost more than Inserted Money " + calculation);
                }

                if (mInsertedAmount == costOfItem) { // exact amount needed to purchase item
                    // dispense item to user
                    // with image view
                    chosenItemImageDialog();
                    Log.d("Quantity", "old value is:" + mQuantity);
                    mQuantity -= mInteger; // minus quantity
                    Log.d("Quantity", "new value is:" + mQuantity);
                    mInsertedAmount = 0; // reset inserted amount
                    Log.d("PURCHASE BUTTON", " Calculation == 0.00 " + calculation);
                    Log.d("PURCHASE BUTTON", "Item Dispensed");
                }

                /*if (mInsertedAmount > Double.parseDouble(mPrice)) { // user is owed change
                    // dispense user's change
                    double remainder = 0;
                    for(int i = 8; i > 1; i--) {
                        remainder = calculation % denominations.get(i-1);
                        if(remainder < calculation) {
                            changeCount.set(i, ((calculation - remainder) / denominations.get(i)));
                            //Log.d("PURCHASE BUTTON", " Calculation > 0.00 " + calculation);
                        }
                        double change = Double.parseDouble(df.format(remainder));

                    }
                    Log.d("PURCHASE BUTTON", " Change is " + remainder);
                }*/
            }
        });
    }

    public void increaseInteger(View view) {
        mInteger = mInteger + 1;
        display(mInteger);

    }public void decreaseInteger(View view) {
        mInteger = mInteger - 1;
        if(mInteger < 0) {
            mInteger = 0; // reset value to 0 to prevent quantity less than 0
        }
        display(mInteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }

    public void chosenItemImageDialog() {
        View view = (LayoutInflater.from(DetailedViewActivity.this)).inflate(R.layout.alert_dialog_collect_item, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailedViewActivity.this);
        alertBuilder.setView(view);
        chosenItem = (ImageView) view.findViewById(R.id.chosen_item_image);
        Picasso.with(this).load(mChosenItem).into(chosenItem);

        alertBuilder.setCancelable(true)
                .setPositiveButton(R.string.collect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }

    //alert dialog to alert user they have insufficient funds
    public void customAlertDialog() {
        View view = (LayoutInflater.from(DetailedViewActivity.this)).inflate(R.layout.alert_dialog_insufficient_funds, null);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailedViewActivity.this);
        alertBuilder.setView(view);
        insufficientFundsMessage = (TextView) view.findViewById(R.id.insufficientFundTextDialog);
        // check quantity chosen
        double costOfItem;
        if(mInteger > 0) { // if quantity user wants is greater than 0
            costOfItem = (Double.parseDouble(mPrice)) * mInteger; // times mPrice by quantity
        } else { // normal mPrice stands
            costOfItem = Double.parseDouble(mPrice);
        }
        insufficientFundsMessage.setText(R.string.insufficient_funds_alert_message + "\n" + "Insert: £" +costOfItem);
        //customDialogImage = (ImageView) view.findViewById(R.id.insufficient_funds_image);
        //Picasso.with(this).load(R.drawable.img_insufficient_funds).into(customDialogImage);

        alertBuilder.setCancelable(true)
                .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mInsertedAmount = 0;
                        alertBuilder.setCancelable(true);
                    }
                });

        Dialog dialog = alertBuilder.create();
        dialog.show();
        //dialog.getWindow().setLayout(900, 800);
    }
}
