package com.hulldiscover.zeus.vendingmachine.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hulldiscover.zeus.vendingmachine.Activity.Adapter.ListAdapter;
import com.hulldiscover.zeus.vendingmachine.Activity.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.vendingmachine.Activity.Model.VendingItem;
import com.hulldiscover.zeus.vendingmachine.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // members
    ListView mListview;
    ListAdapter mListAdapter;
    private ArrayList<VendingItem> mListData;

    // list of images to draw to screen
    private ArrayList<Bitmap> bitmapImagesToDraw = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // find listView layout
        mListview = (ListView)findViewById(R.id.listView);

        // list of vending stock
        final List<VendingItem> vendingItems;
        ArrayList<String> imageURLs = new ArrayList<String>();

        //mListAdapter = new ListAdapter(this, R.layout.listview_item);
        //mListview.setAdapter(mListAdapter);

        // gather vending items stock from XML file
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            vendingItems = parser.parse(getAssets().open("vending_stock.xml"));
            // add url of each vending item
            for(int i = 0; i < vendingItems.size(); i++) {
                imageURLs.add(vendingItems.get(i).getImage());
                vendingItems.get(i).setImage(imageURLs.get(i));
            }
            // download images
            /*downloadBitmapsToDevice(imageURLs);
            VendingItem item;
            for(int index = 0; index < bitmapImagesToDraw.size(); index++){
                vendingItems.get(index).setImageBitmap(bitmapImagesToDraw.get(index));
            }*/

            //ArrayAdapter<VendingItem> adapter = new ArrayAdapter<VendingItem>(this, android.R.layout.simple_list_item_2, vendingItems);
            //mListview.setAdapter(adapter);
            mListAdapter = new ListAdapter(this, R.layout.listview_item, vendingItems);
            //mListAdapter.add(vendingItems);
            mListview.setAdapter(mListAdapter);

            /*for(int index = 0; index < imageURLs.size(); index++){
                item = new VendingItem();
                item.setImage(imageURLs.get(index));
                vendingItems.add(item);
            }*/


            //Grid view click event
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //Get item at position
                    VendingItem item = (VendingItem) parent.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailedViewActivity.class);
                    ImageView imageView = (ImageView) v.findViewById(R.id.item_image);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("description", item.getDescription())
                            .putExtra("price", item.getPrice())
                            .putExtra("image", item.getImage())
                            .putExtra("quantity", item.getQauntity());

                    //Start details activity
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void downloadBitmapsToDevice(ArrayList<String> arrayList) {
        for(int i = 0; i < arrayList.size(); i++) {
            try {
                bitmapImagesToDraw.add(new downloadImage().execute(arrayList.get(i)).get());
                Log.i("MainActivity", "Image is Downloading...");
            } catch (Exception e) {
                e.getMessage();
            }
            Log.i("MainActivity", "Image Download Completed: " +i+ " " +arrayList.get(i));
        }
    }

    private class downloadImage extends AsyncTask<String, String, Bitmap>
    {

        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            try {
                //bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                FileInputStream reader = new FileInputStream (new File(args[0]));
                //FileInputStream reader = getApplicationContext().openFileInput(args[0]);
                bitmap = BitmapFactory.decodeStream(reader);
            }
            catch (FileNotFoundException fileNotFound)
            {
                try
                {
                    //e.printStackTrace();
                    String url = args[0];
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                    FileOutputStream writer = null;
                    try
                    {
                        writer = getApplicationContext().openFileOutput(args[0], Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, writer);
                    }
                    catch (Exception e)
                    {
                        Log.i("MyError", e.getMessage());
                    }
                    finally
                    {
                        writer.close();
                    }
                }
                catch (Exception e)
                {
                    Log.i("MyError", e.getMessage());
                }

            }
            return bitmap;
        }

    }
}
