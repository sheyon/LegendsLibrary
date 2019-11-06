package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class DrawerAdapter extends ArrayAdapter<String> {

    DrawerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, parent, false);
        }

        TextView drawerSelection = listItemView.findViewById(R.id.drawer_selection);
        drawerSelection.setText(getItem(position));
        drawerSelection.setAllCaps(false);

        return listItemView;
    }
}