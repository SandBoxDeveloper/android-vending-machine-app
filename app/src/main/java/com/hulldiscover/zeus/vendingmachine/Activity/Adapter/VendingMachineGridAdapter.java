package com.hulldiscover.zeus.vendingmachine.Activity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.vendingmachine.Activity.VendingItem;
import com.hulldiscover.zeus.vendingmachine.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Zeus on 07/05/16.
 */
public class VendingMachineGridAdapter extends ArrayAdapter<VendingItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<VendingItem> mGridData = new ArrayList<VendingItem>();

    public VendingMachineGridAdapter(Context mContext, int layoutResourceId, ArrayList<VendingItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData grid data items
     */
    public void setGridData(ArrayList<VendingItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.vending_item_description);
            holder.imageView = (ImageView) row.findViewById(R.id.vending_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        VendingItem item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getDescription()));

        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }

}
