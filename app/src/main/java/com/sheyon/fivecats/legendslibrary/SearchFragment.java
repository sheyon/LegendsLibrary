package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment
{
    private ListView listView;
    private Cursor cursor;
    private String searchString;
//    private ExpandableListView searchExpandableView;
//    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_layout, container, false);

        setupSearchBar(view);
        setupListView(view);
        //setupExpandableList(view);

        return view;
    }

    private void setupSearchBar(View view)
    {
        // Get the SearchView and set the searchable configuration
        //  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
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

//    private void setupExpandableList(View view) {
//        searchExpandableView = (ExpandableListView) view.findViewById(R.id.search_expandable_view);
//    }

    private void setupListView(View view) {
        listView = (ListView) view.findViewById(R.id.search_list_view);
    }

    private void runQuery() {
        closeCursor();

        String modString = "%"+searchString+"%";
        String[] selectionArgs = { modString, modString, modString };

        cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        LegendsListAdapter adapter = new LegendsListAdapter(getContext(), cursor, searchString);
        listView.setAdapter(adapter);

        //ExpandableSearchAdapter adapter = new ExpandableSearchAdapter(cursor, getContext(), searchString);
        //searchExpandableView.setAdapter(adapter);
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

