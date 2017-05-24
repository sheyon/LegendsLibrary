package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment
{
    private SearchView searchView;
    private ListView listView;
    private LegendsListAdapter adapter;
    private Cursor cursor;

    private String searchString;
    private String modString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem clearSearch = menu.findItem(R.id.menu_search_clear);
        clearSearch.setVisible(true);

        MenuItem clearFavorites = menu.findItem(R.id.menu_favorites_remove);
        clearFavorites.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_layout, container, false);

        setupSearchBar(view);
        setupListView(view);

        return view;
    }

    private void setupSearchBar(View view)
    {
        // Get the SearchView and set the searchable configuration
        //  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        // Assumes current activity is the searchable activity
        //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //CLEAR FOCUS IS NEEDED TO PREVENT THE QUERY FROM FIRING TWICE IN CASE OF PHYSICAL KEYBOARDS [ ANDROID BUG :( ]
                searchString = searchView.getQuery().toString().toLowerCase().trim();
                searchView.clearFocus();

                runQuery();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupListView(View view) {
        listView = (ListView) view.findViewById(R.id.search_list_view);
    }

    private void runQuery() {
        //CLOSE THE PREVIOUS QUERY IF IT EXISTS
        closeCursor();

        modString = "%"+searchString+"%";
        String [] selectionArgs = { modString, modString, modString };

        cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        adapter = new LegendsListAdapter(getContext(), cursor, searchString, this);
        listView.setAdapter(adapter);
    }

    public void refreshCursor() {
        if (cursor == null) {
            return;
        }

        String[] selectionArgs = { modString, modString, modString };
        Cursor newCursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);
        adapter.swapCursor(newCursor);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_search_clear:
                clearSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearSearch()
    {
        searchView.setQuery("", false);
        //CODE HOW TO CLEAR RESULTS
    }
}

