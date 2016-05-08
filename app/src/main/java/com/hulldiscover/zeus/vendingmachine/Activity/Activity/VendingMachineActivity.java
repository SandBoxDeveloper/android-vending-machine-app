package com.hulldiscover.zeus.vendingmachine.Activity.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.hulldiscover.zeus.vendingmachine.Activity.Adapter.VendingMachineGridAdapter;
import com.hulldiscover.zeus.vendingmachine.Activity.Model.VendingItem;
import com.hulldiscover.zeus.vendingmachine.R;

import java.util.ArrayList;

/**
 * Created by Zeus on 07/05/16.
 */
public class VendingMachineActivity extends AppCompatActivity {
    private static final String TAG = VendingMachineActivity.class.getSimpleName();

    private GridView mGridView;

    private VendingMachineGridAdapter mVendingMachineGridAdapter;
    private ArrayList<VendingItem> mGridData;
    private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_machine_gridview);

        mGridView = (GridView) findViewById(R.id.gridView);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mVendingMachineGridAdapter = new VendingMachineGridAdapter(this, R.layout.vending_item, mGridData);
        mGridView.setAdapter(mVendingMachineGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                VendingItem item = (VendingItem) parent.getItemAtPosition(position);

                //Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.vending_item_image);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                /*intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("title", item.getDescription()).
                        putExtra("image", item.getImage());*/

                //Start details activity
                //startActivity(intent);
            }
        });

        //Start download
        //new AsyncHttpTask().execute(FEED_URL);
    }


    //Downloading data asynchronously





    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
   /* private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new GridItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
