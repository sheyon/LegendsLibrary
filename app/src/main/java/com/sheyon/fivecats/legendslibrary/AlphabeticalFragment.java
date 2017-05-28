package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class AlphabeticalFragment extends Fragment implements FragmentVisibilityListener
{
    private Cursor cursor;
    private Cursor refreshedCursor;

    private ListView listView;
    private View emptyView;

    private LegendsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alphabetical, container, false);

        cursor = legendsDB.rawQuery(Queries.ALPHABETICAL, null);
        cursor.moveToFirst();
        adapter = new LegendsListAdapter(getContext(), cursor, this);

        setupListView(view);

        return view;
    }

    private void setupListView(View view) {
        listView = (ListView) view.findViewById(R.id.alphabetical_list_view);
        emptyView = view.findViewById(R.id.empty_view);

        listView.setAdapter(adapter);
    }

    public void refreshCursor() {
        closeCursor();
        refreshedCursor = legendsDB.rawQuery(Queries.ALPHABETICAL, null);
        if (cursor != null){
            adapter.swapCursor(refreshedCursor);
        }
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
        if (refreshedCursor != null){
            refreshedCursor.close();
        }
    }

    @Override
    public void onFragmentInvisible() {
        //THE ALPHABETICAL LIST WILL NEVER BE EMPTY
        //BUT THE EMPTY VIEW IS SET TO GONE SO IT SHOULD NEVER COME UP
        listView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentVisible() {
        refreshCursor();

        // ACTION TO RUN AFTER 1/10TH OF A SECOND
        // THIS IS NECESSARY TO ENSURE A SMOOTH TRANSITION WHEN FAVING A LORE FROM ANOTHER TAB AND THEN NAVIGATING BACK
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                listView.setVisibility(View.VISIBLE);
            }
        }, 100);
    }
}
