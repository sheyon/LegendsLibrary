package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment
{
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_layout, container, false);

        setupSearchBar(view);
        setupExpandableView(view);
        return view;
    }

    private void setupExpandableView(View view){

//            String searchString = getIntent().getStringExtra("query");
//            String modString = "%"+searchString+"%";
//            String[] selectionArgs = { modString, modString, modString };
//
//            ExpandableListView searchExpandableView = (ExpandableListView) view.findViewById(R.id.search_expandable_view);
//
//            cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);
//            cursor.moveToFirst();
//
//            ExpandableSearchAdapter adapter = new ExpandableSearchAdapter(cursor, getContext(), searchString);
//            searchExpandableView.setAdapter(adapter);
    }

    private void setupSearchBar(View view)
    {
        // Get the SearchView and set the searchable configuration
        //  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        // Assumes current activity is the searchable activity
        //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //CLEAR FOCUS IS NEEDED TO PREVENT THE QUERY FROM FIRING TWICE IN CASE OF PHYSICAL KEYBOARDS [ ANDROID BUG :( ]
                String searchString = searchView.getQuery().toString().toLowerCase().trim();
                searchView.clearFocus();

//                Intent intent = new Intent(CategoriesFragment.this, SearchActivity.class);
//                intent.putExtra("query", searchString);
//
//                closeCursor();
//                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}

