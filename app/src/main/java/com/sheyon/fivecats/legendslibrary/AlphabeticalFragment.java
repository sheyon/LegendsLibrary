package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class AlphabeticalFragment extends Fragment
{
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alphabetical, container, false);

        setupListView(view);

        return view;
    }

    private void setupListView(View view)
    {
        ListView listView = (ListView) view.findViewById(R.id.alphabetical_list_view);

        cursor = legendsDB.rawQuery(Queries.ALPHABETICAL, null);
        cursor.moveToFirst();

        LegendsListAdapter adapter = new LegendsListAdapter(getContext(), cursor);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCursor();
    }

    private void closeCursor() {
        //DO NOT OVERRIDE "ONPAUSE" OR "ONSTOP" TO CLOSE THE CURSOR!
        //YOU NEED THE CURSOR TO NAVIGATE BACK TO HERE FROM THE LORE ACTIVITY!
        if (cursor != null) {
            cursor.close();
        }
    }
}
