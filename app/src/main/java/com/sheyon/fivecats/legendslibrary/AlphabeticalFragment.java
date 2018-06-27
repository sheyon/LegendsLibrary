package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

public class AlphabeticalFragment extends Fragment implements FragmentVisibilityListener
{
    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor refreshedCursor;

    private ListView listView;
    private View emptyView;
    private View loadingView;

    private LegendsListAdapter adapter;

    boolean haveExitedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alphabetical, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.alphabetical_list_view);
        emptyView = view.findViewById(R.id.empty_view);
        loadingView = view.findViewById(R.id.loading_view);

        db = LegendsDatabase.getInstance(getContext());

        cursor = db.rawQuery(Queries.ALPHABETICAL, null);
        if (cursor != null) {
            cursor.moveToFirst();
            adapter = new LegendsListAdapter(getContext(), cursor, this);
        }

        listView.setAdapter(adapter);
        listView.setVerticalScrollbarPosition(LegendsPreferences.getInstance(getContext()).getAlphabeticalPosition());
    }

    public void refreshCursor() {
        closeCursor();

        refreshedCursor = db.rawQuery(Queries.ALPHABETICAL, null);
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
        //SAVE POSITION IN LISTVIEW
        LegendsPreferences.getInstance(getContext()).setAlphabeticalPosition(listView.getFirstVisiblePosition());

        //THE ALPHABETICAL LIST WILL NEVER BE EMPTY
        //BUT THE EMPTY VIEW IS SET TO GONE SO IT SHOULD NEVER COME UP
        listView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        loadingView.setAlpha(1f);
        haveExitedOnce = true;
    }

    @Override
    public void onFragmentVisible() {
        //TO ENSURE THIS BLOCK NEVER FIRES ON START-UP; CAUSES LAYOUT SPAM
        if (haveExitedOnce) {
            //ALWAYS GET NEW DATABASE IN CASE SETTINGS WERE CHANGED
            db = LegendsDatabase.getInstance(getContext());

            refreshCursor();

            Crossfader.crossfadeView(listView, loadingView);

            //JUMP TO LAST SAVED POSITION; PERSISTS AFTER APP CLOSE
            listView.setSelection(LegendsPreferences.getInstance(getContext()).getAlphabeticalPosition());
        }
    }
}
