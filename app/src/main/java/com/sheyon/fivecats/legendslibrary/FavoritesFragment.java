package com.sheyon.fivecats.legendslibrary;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class FavoritesFragment extends Fragment implements FragmentVisibilityListener
{
    private Cursor cursor;
    private Cursor refreshedCursor;
    private LegendsListAdapter adapter;

    private ListView listView;
    private View emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem clearSearch = menu.findItem(R.id.menu_search_clear);
        clearSearch.setVisible(false);

        MenuItem clearFavorites = menu.findItem(R.id.menu_favorites_remove);
        clearFavorites.setVisible(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //THIS FRAGMENT REUSES THE ALPHABETICAL LAYOUT; THIS IS FINE
        View view = inflater.inflate(R.layout.fragment_alphabetical, container, false);

        cursor = legendsDB.rawQuery(Queries.GET_ALL_FAVES, null);
        cursor.moveToFirst();
        adapter = new LegendsListAdapter(getContext(), cursor, this);

        setupListView(view);

        return view;
}

    private void setupListView(View view) {
        //THIS FRAGMENT REUSES THE ALPHABETICAL LAYOUT; THIS IS FINE
        listView = (ListView) view.findViewById(R.id.alphabetical_list_view);
        emptyView = view.findViewById(R.id.empty_view);

        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
    }

    public void refreshCursor() {
        closeCursor();
        refreshedCursor = legendsDB.rawQuery(Queries.GET_ALL_FAVES, null);
        adapter.swapCursor(refreshedCursor);
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
        if (refreshedCursor != null) {
            refreshedCursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_favorites_remove:
                checkForFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkForFavorites() {
        Cursor check = legendsDB.rawQuery(Queries.CHECK_FOR_FAVED_LORE, null);
        check.moveToFirst();
        int i = check.getCount();
        check.close();

        if (i == 0) {
            Toast.makeText(getContext(), "This list is already empty.", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmRemoveAll();
        }
    }

    private void confirmRemoveAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to remove all favorites?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeAllFavorites();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void removeAllFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoreLibrary.COLUMN_FAVED, "0");
        String [] whereArgs = { "1" };
        legendsDB.update(LoreLibrary.LORE_TABLE_NAME, contentValues, LoreLibrary.COLUMN_FAVED + " = ?", whereArgs);

        Toast.makeText(getContext(), "All favorites removed.", Toast.LENGTH_SHORT).show();
        refreshCursor();
    }

    @Override
    public void onFragmentInvisible() {
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
                emptyView.setVisibility(View.VISIBLE);
                listView.setEmptyView(emptyView);
            }
        }, 100);
    }
}
