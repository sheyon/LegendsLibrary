package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<String> {

    public DrawerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, parent, false);
        }

        TextView drawerSelection = (TextView) listItemView.findViewById(R.id.drawer_selection);
        drawerSelection.setText(getItem(position));

        return listItemView;
    }
}