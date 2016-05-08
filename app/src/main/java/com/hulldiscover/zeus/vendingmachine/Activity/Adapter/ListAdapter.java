package com.hulldiscover.zeus.vendingmachine.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.vendingmachine.Activity.Model.VendingItem;
import com.hulldiscover.zeus.vendingmachine.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Zeus on 07/05/16.
 */
public class ListAdapter extends ArrayAdapter {

    //List list = new ArrayList();
    List<VendingItem> vending;
    private Context mContext;

    public ListAdapter(Context context, int resource, List<VendingItem> item) {
        super(context, resource, item);
        this.mContext = context;
    }

    public void setGridData(List<VendingItem> mGridData) {
        this.vending = mGridData;
        notifyDataSetChanged();
    }
   /* public void add(VendingItem object) {
        super.add(object);
        // add all objects into list
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }*/

    /*@Override
    public Object getItem(int position) {
        return list.get(position); // get each object from the list
    }*/

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View row;
        row = convertView;
        final ViewHolder viewHolder;

        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.listview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stockDescription = (TextView) row.findViewById(R.id.txt_itemDescription);
            viewHolder.stockPrice = (TextView) row.findViewById(R.id.txt_itemPrice);
            viewHolder.itemImage = (ImageView) row.findViewById(R.id.stock_image);

            row.setTag(viewHolder);
        }
        else { // if row is already available
            viewHolder = (ViewHolder)row.getTag();
        }

        VendingItem item = (VendingItem)this.getItem(position);
        // set resource for TextViews
        //final VendingItem vendingItems = (VendingItem)this.getItem(position);

        //final String newPictureId = Integer.toString(downloadedPuzzles.getPictureId());
        // set the resource for the DownloadedPuzzlesHolder
        viewHolder.stockDescription.setText(item.getDescription());
        viewHolder.stockPrice.setText("£" +item.getPrice());
        //VendingItem stock_item = vending.get(position);
        Picasso.with(mContext)
                .load(item.getImage())
                .into(viewHolder.itemImage);


        return row;
    }


    public class ViewHolder {
        private TextView stockDescription, stockPrice;
        ImageView itemImage;

    }

}
