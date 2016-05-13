package com.hulldiscover.zeus.vendingmachine.Activity.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.vendingmachine.Activity.Adapter.VendingMachineGridAdapter;
import com.hulldiscover.zeus.vendingmachine.Activity.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.vendingmachine.Activity.Model.VendingItem;
import com.hulldiscover.zeus.vendingmachine.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Zeus on 07/05/16.
 */
public class VendingMachineActivity extends AppCompatActivity {
    private static final String TAG = VendingMachineActivity.class.getSimpleName();

    //members
    private GridView mGridView;
    int mInteger = 1; // used to select quantity of how many items wanted
    int mQuantity = 0; // stock level of items
    String mPrice = "0"; // price of item
    double totalCostToPayForItem = 0;
    public static double insertedAmount = 0.00;
    private VendingMachineGridAdapter mVendingMachineGridAdapter;
    private ArrayList<VendingItem> mGridData;
    DecimalFormat df = new DecimalFormat("#.00");

    private TextView itemPrice;
    private Button insertCoinsButton;
    private TextView userCoinInput;


    ArrayList<String> imageURLs = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_machine_gridview);

        mGridView = (GridView) findViewById(R.id.gridView);

        //Initialize with empty data
        mGridData = new ArrayList<>();

        // gather vending items stock from XML file
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            mGridData = parser.parse(getAssets().open("vending_stock.xml"));
            // add url of each vending item
            for(int i = 0; i < mGridData.size(); i++) {
                imageURLs.add(mGridData.get(i).getImage());
                mGridData.get(i).setImage(imageURLs.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        mVendingMachineGridAdapter = new VendingMachineGridAdapter(this, R.layout.vending_item, mGridData);
        mGridView.setAdapter(mVendingMachineGridAdapter);




        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }

        });

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //mGridView.setVerticalScrollBarEnabled(false);
                //Get item at position
                VendingItem item = (VendingItem) parent.getItemAtPosition(position);

                //Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.vending_item_image);

                //retrieves the selected item data
                String description = item.getDescription();
                mPrice = item.getPrice();
                String image = item.getImage();
                mQuantity = item.getQauntity();

                double totalCostToPayForItem = 0; // total cost for item

                //TextView for the items price
                itemPrice = (TextView) findViewById(R.id.purchaseTotal);
                //check quantity
                // and update view of item price
                if (mInteger > 0) { // if quantity user wants is greater than 0
                    totalCostToPayForItem = (Double.parseDouble(mPrice)) * mInteger; // times mPrice by quantity
                    itemPrice.setText("£" + new DecimalFormat("#.##").format(totalCostToPayForItem)); // update view to display update price
                } else { // normal mPrice stands
                    totalCostToPayForItem = Double.parseDouble(mPrice); // else don't change price, as quantity has not changed
                }


            }
        });

        //initialize insert money button
        insertCoinsButton = (Button) findViewById(R.id.insertCoinButton);
        insertCoinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(VendingMachineActivity.this)).inflate(R.layout.alert_dialog, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(VendingMachineActivity.this);
                alertBuilder.setView(view);
                userCoinInput = (TextView) view.findViewById(R.id.add_balance);

                double totalCostToPayForItem = 0; // total cost for item

                //check quantity
                if (mInteger > 0) { // if quantity user wants is greater than 0
                    totalCostToPayForItem = (Double.parseDouble(mPrice)) * mInteger; // times mPrice by quantity
                } else { // normal mPrice stands
                    totalCostToPayForItem = Double.parseDouble(mPrice);
                }


                //insert button for dialog
                alertBuilder.setCancelable(true)
                        .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = userCoinInput.getText().toString();
                                insertedAmount += Double.parseDouble(input); // update machine system with the amount the user has inserted
                                df.format(insertedAmount); // format amount to two decimal places
                                userCoinInput.setText("Balance: " + insertedAmount);
                            }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

    }

    //methods to change quantity values
    //increase quantity
    public void increaseInteger(View view) {
        mInteger = mInteger + 1;
        display(mInteger);
    }
    //decrease quantity
    public void decreaseInteger(View view) {
        mInteger = mInteger - 1;
        if (mInteger < 0) {
            mInteger = 0; // reset value to 0 to prevent quantity less than 0
        }
        display(mInteger);
    }
    //update, display quantity
    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
        if (mInteger > 0) { // if quantity user wants is greater than 0
            totalCostToPayForItem = (Double.parseDouble(mPrice)) * mInteger; // times mPrice by quantity
            //if (totalCostToPayForItem != 0) {
                itemPrice.setText("£" + new DecimalFormat("#.##").format(totalCostToPayForItem)); // update view to display update price
            //}
        } else { // normal mPrice stands
            totalCostToPayForItem = Double.parseDouble(mPrice); // else don't change price, as quantity has not changed
        }
    }







}
