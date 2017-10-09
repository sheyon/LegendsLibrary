//package com.sheyon.fivecats.legendslibrary;
//
//import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//class LegendsLocationAdapter extends ArrayAdapter<LegendsLocation>{
//
//    private Context mContext;
//    private ArrayList<LegendsLocation> locations;
//
//    LegendsLocationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<LegendsLocation> objects) {
//        super(context, 0, objects);
//        mContext = context;
//        locations = objects;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.location_list_item, parent, false);
//        }
//
//        TextView titleTextView = view.findViewById(R.id.location_title_text_view);
//        TextView zoneTextView = view.findViewById(R.id.location_zone_text_view);
//        TextView notesTextView = view.findViewById(R.id.location_notes_text_view);
//
//        titleTextView.setText("# " + Integer.toString(locations.get(position).getLegendNumber()));
//        zoneTextView.setText(locations.get(position).getLegendZone() + " ( " + locations.get(position).getXcoord() + ", " + locations.get(position).getYCoord() + " )");
//        notesTextView.setText(locations.get(position).getLegendNote());
//
//        return view;
//    }
//}
