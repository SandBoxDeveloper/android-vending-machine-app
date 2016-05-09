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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private TextView collectChangeTextView;
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
    Double mChange = 0.00;
    Double mChangeCountTwoPound, mChangeCountOnePound, mChangeCountOneFiftyPence, mChangeCountTwentyPence, mChangeCountTenPence, mChangeCountFivePence, mChangeCountTwoPence, mChangeCountOnePence = 0.00;

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
                double costOfItem = 0.00;
                if(mInteger > 0) { // if quantity user wants is greater than 0
                    mChange = mInsertedAmount - ((Double.parseDouble(mPrice)) * mInteger); // times mPrice by quantity
                    costOfItem = (Double.parseDouble(mPrice)) * mInteger; // true cost of items
                } else { // normal mPrice stands
                    mChange = mInsertedAmount - (Double.parseDouble(mPrice));
                    costOfItem = Double.parseDouble(mPrice);
                }
                double changeOwed = mChange;
                //Double mChange = mInsertedAmount - (Double.parseDouble(mPrice) * mInteger);
                Log.d("PURCHASE BUTTON", " Change owed is: " + changeOwed);

                if(costOfItem > mInsertedAmount) { // user doesn't have enough money
                    // alert user insufficient funds, insert more mo mo money
                    customAlertDialog();
                    Log.d("PURCHASE BUTTON", " Price of item cost more than Inserted Money " + mChange);
                }

                if (mInsertedAmount == costOfItem) { // user has exact amount needed to purchase item
                    // dispense item to user
                    // with image view
                    chosenItemImageDialog();
                    Log.d("Quantity", "old value is:" + mQuantity);
                    mQuantity -= mInteger; // minus quantity
                    Log.d("Quantity", "new value is:" + mQuantity);
                    // no change needed
                    mInsertedAmount = 0; // reset inserted amount
                    Log.d("PURCHASE BUTTON", " Calculation == 0.00 " + mChange);
                    Log.d("PURCHASE BUTTON", "Item Dispensed");
                }

                if (mInsertedAmount > costOfItem) { // user is owed change
                    // dispense user's change
                    double twoPound = mChange / 2.00;
                    if (twoPound > 0) {

                        mChange = mChange / 2.00;

                        Log.d("CHANGE NORMAL", mChange + " £2 (s)");

                        mChange = Math.floor(mChange);

                        twoPound = mChange * 2.00;

                        Log.d("CHANGE", mChange + " £2 (s)");

                        mChangeCountTwoPound = twoPound; // keep record of how many two pounds to give in change to user
                    }

                    changeOwed = changeOwed - twoPound;
                    Log.d("AMOUNT LEFT", "" +changeOwed);

                    double onePound = changeOwed / 1.00;
                    if (onePound > 0) {
                        mChange = changeOwed / 1.00;
                        mChange = Math.floor(mChange);
                        onePound = mChange * 1.00;
                        Log.d("CHANGE", mChange + " £1 (s)");
                        mChangeCountOnePound = onePound; // keep record of how many one pounds to give in change to user

                        if (mChange > 0) { // check to see if any £1 coins have been given as change
                            changeOwed = changeOwed - onePound;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    double fiftyPence = changeOwed / 0.50;
                    if (fiftyPence > 0) {
                        mChange = changeOwed / 0.50;
                        mChange = Math.floor(mChange);
                        fiftyPence = mChange * 0.50; // how many fifty pence go into remaining change
                        Log.d("CHANGE", mChange + " 50p (s)");
                        mChangeCountOneFiftyPence = fiftyPence; // keep record of how many 50p to give in change to user

                        if (mChange > 0) { // check to see if any 50p coins have been given as change
                            changeOwed = changeOwed - fiftyPence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    double twentyPence = changeOwed / 0.20;
                    if (twentyPence > 0) {
                        mChange = mChange / 0.20;
                        mChange = Math.floor(mChange);
                        twentyPence = mChange * 0.20;
                        Log.d("CHANGE", mChange + " 20p (s)");
                        mChangeCountTwentyPence = twentyPence; // keep record of how many 20p to give in change to user

                        if (mChange > 0) { // check to see if any 20p coins have been given as change
                            changeOwed = changeOwed - twentyPence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    Log.d("20p CHECK", "" +changeOwed);
                    double tenPence = changeOwed / 0.10;
                    if (tenPence > 0) {
                        mChange = changeOwed / 0.10;
                        mChange = Math.floor(mChange);
                        tenPence = mChange * 0.10;
                        Log.d("CHANGE", mChange + " 10p (s)");
                        mChangeCountTenPence = tenPence; // keep record of how many 10p to give in change to user

                        if (mChange > 0) { // check to see if any 10p coins have been given as change
                            changeOwed = changeOwed - tenPence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }

                    }

                    double fivePence = changeOwed / 0.05;
                    if (fivePence > 0) {
                        mChange = changeOwed / 0.05;
                        mChange = Math.floor(mChange);
                        fivePence = mChange * 0.05;
                        Log.d("CHANGE", mChange + " 5p (s)");
                        mChangeCountFivePence = fivePence; // keep record of how many 5p to give in change to user

                        if (mChange > 0) { // check to see if any 5p coins have been given as change
                            changeOwed = changeOwed - fivePence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    double twoPence = changeOwed / 0.02;
                    if (twoPence > 0) {
                        mChange = changeOwed / 0.02;
                        mChange = Math.floor(mChange);
                        twoPence = mChange * 0.02;
                        Log.d("CHANGE", mChange + " 2p (s)");
                        mChangeCountTwoPence = twoPence; // keep record of how many 2p to give in change to user

                        if (mChange > 0) { // check to see if any 2p coins have been given as change
                            changeOwed = changeOwed - twoPence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    double onePence = mChange / 0.01;
                    if (onePence > 0) {
                        mChange = changeOwed / 0.01;
                        mChange = Math.floor(mChange);
                        onePence = mChange * 0.01;
                        Log.d("CHANGE", mChange + " 1p (s)");
                        mChangeCountOnePence = onePence; // keep record of how many 1p to give in change to user

                        if (mChange > 0) { // check to see if any 1p coins have been given as change
                            changeOwed = changeOwed - onePence;
                            Log.d("AMOUNT LEFT", "" +changeOwed);
                        }
                    }

                    giveChangeAlertDialog();
                    //reset inserted coins amount
                    mInsertedAmount = 0;
                    Log.d("PURCHASE BUTTON", " User is owed change " + mChange);
                    Log.d("PURCHASE BUTTON", " User is owed change " + mChangeCountTwoPound + "" + mChangeCountOnePound + "" + mChangeCountOneFiftyPence + "" +mChangeCountTwentyPence + mChangeCountTenPence + mChangeCountFivePence + mChangeCountTwoPence + mChangeCountOnePence);
                }
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

    //function to round double
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

    public void giveChangeAlertDialog() {
        View view = (LayoutInflater.from(DetailedViewActivity.this)).inflate(R.layout.alert_dialog_collect_change, null);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailedViewActivity.this);
        alertBuilder.setView(view);
        collectChangeTextView = (TextView) view.findViewById(R.id.collectChangeTextView);

        collectChangeTextView.setText("You coins\n" + "£"+mChangeCountTwoPound + "\n£" + mChangeCountOnePound + "\n" + mChangeCountOneFiftyPence + "p\n" +mChangeCountTwentyPence+ "p\n"+ mChangeCountTenPence + "p\n" + mChangeCountFivePence +"p\n" +mChangeCountTwoPence + "p\n" +mChangeCountOnePence + "p");
        //customDialogImage = (ImageView) view.findViewById(R.id.insufficient_funds_image);
        //Picasso.with(this).load(R.drawable.img_insufficient_funds).into(customDialogImage);

        alertBuilder.setCancelable(true)
                .setNegativeButton(R.string.collect_change, new DialogInterface.OnClickListener() {
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
