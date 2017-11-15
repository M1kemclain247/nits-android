package com.soj.m1kes.nits.adapters.gridview.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soj.m1kes.nits.R;


public class MenuAdapter extends BaseAdapter {

    Context context;
    Integer[]imageIds;
    String[]keyWords;

    public MenuAdapter(Context context, Integer[]imageIds, String[] keyWords){

        this.context = context;
        this.imageIds = imageIds;
        this.keyWords = keyWords;
    }


    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.menu_item, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        TextView txtview = (TextView)rowView.findViewById(R.id.txt_GridItem);
        imageView.setImageResource(imageIds[position]);
        txtview.setText(keyWords[position]);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "RobotoCondensed-Regular.ttf");
        txtview.setTypeface(face);
        return rowView;
    }




}
