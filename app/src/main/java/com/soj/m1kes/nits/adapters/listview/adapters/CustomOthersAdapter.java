package com.soj.m1kes.nits.adapters.listview.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soj.m1kes.nits.R;



public class CustomOthersAdapter extends BaseAdapter {

    private String [] result;
    private String [] descriptions;
    private Context context;
    private int [] imageId;
    private static LayoutInflater inflater=null;
    private Class[] optionMenus;
    private OnClickListItem callback;


    public interface OnClickListItem{
        public void onClick(Class aClass);
    }



    public CustomOthersAdapter(Context context, String[] prgmNameList, int[] prgmImages,Class[] optionMenus,OnClickListItem callback,
                               String []descriptions) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.context=context;
        this.descriptions = descriptions;
        imageId=prgmImages;
        this.optionMenus = optionMenus;
        this.callback = callback;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView othersTextDescription;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        if(convertView==null){
            convertView = inflater.inflate(R.layout.list_others_item, null);
            holder.tv=(TextView) convertView.findViewById(R.id.othersText1);
            holder.img=(ImageView) convertView.findViewById(R.id.othersImage1);
            holder.othersTextDescription=(TextView)convertView.findViewById(R.id.othersTextDescription);
            holder.tv.setText(result[position]);
            holder.othersTextDescription.setText(descriptions[position]);
            holder.img.setImageResource(imageId[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    callback.onClick(optionMenus[position]);
                }
            });
            return convertView;
        }else{
            return convertView;
        }

    }

}
