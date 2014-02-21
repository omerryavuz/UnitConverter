package com.example.unitconverter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {  
    private Activity context;  
    private ListItemRow itemRow;  
    private List<String> list;  
    private LayoutInflater layoutInflater;  
    public CustomListAdapter(Activity context, List<String> list) {  
       super(context, R.layout.activity_main, list);  
       this.context = context;  
       this.list = list;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
       View rowView = convertView;  
       if (rowView == null) {  
          itemRow = new ListItemRow();  
          layoutInflater = context.getLayoutInflater();  
          rowView = layoutInflater.inflate(R.layout.list, null, true);  
 //         itemRow.photo = (ImageView) rowView.findViewById(R.id.photo);  
  //        itemRow.title = (TextView) rowView.findViewById(R.id.title);  
          rowView.setTag(itemRow);  
       } else {  
          itemRow = (ListItemRow) rowView.getTag();  
       }  
       // ------------ Satır elemanları dolduruluyor ------------ //  
       itemRow.photo.setImageResource(R.drawable.ic_launcher);  
       itemRow.title.setText(list.get(position).toString());  
       return rowView;  
    }  
    // ------------ Listedeki Satırların içinde bulunacak olan componentler ------------ //  
    private class ListItemRow {  
       private ImageView photo;  
       private TextView title;  
    }  
 }
