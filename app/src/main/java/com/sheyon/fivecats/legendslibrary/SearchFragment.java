package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment
{
//    private ExpandableListView searchExpandableView;
//    private FragmentTransaction fragmentTransaction;

    private ListView listView;
    private Cursor cursor;
    private String searchString;

    private int catNumber;
    private String categoryName;
    private String loreTitle;

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

    private void setupListView(View view)
    {
        listView = (ListView) view.findViewById(R.id.search_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout ll = (LinearLayout) view;
                TextView tvt = (TextView) ll.findViewById(R.id.lore_title_text_view);
                TextView tvc = (TextView) ll.findViewById(R.id.lore_category_text_view);
                loreTitle = tvt.getText().toString();
                categoryName = tvc.getText().toString();

                if (categoryName.equals("Solomon Island")) {
                    catNumber = LoreLibrary.CAT_1_SOL;
                }
                if (categoryName.equals("Valley of the Sun God")) {
                    catNumber = LoreLibrary.CAT_2_EGY;
                }
                if (categoryName.equals("Transylvania")) {
                    catNumber = LoreLibrary.CAT_3_TRN;
                }
                if (categoryName.equals("Tokyo")) {
                    catNumber = LoreLibrary.CAT_4_TOK;
                }
                if (categoryName.equals("Global")) {
                    catNumber = LoreLibrary.CAT_5_GBL;
                }
                if (categoryName.equals("The Bestiary")) {
                    catNumber = LoreLibrary.CAT_6_BES;
                }
                if (categoryName.equals("Events")) {
                    catNumber = LoreLibrary.CAT_7_EVN;
                }
                if (categoryName.equals("Issues")) {
                    catNumber = LoreLibrary.CAT_8_ISU;
                }

                startLoreActivity();

//                OLD FRAGMENT LAUNCHER CODE | EXPERIMENTAL
//                LoreFragment nextFrag= new LoreFragment();
//                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, nextFrag);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });
    }

    private void runQuery()
    {
        closeCursor();

        String modString = "%"+searchString+"%";
        String[] selectionArgs = { modString, modString, modString };

        cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        LegendsListAdapter adapter = new LegendsListAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        //ExpandableSearchAdapter adapter = new ExpandableSearchAdapter(cursor, getContext(), searchString);
        //searchExpandableView.setAdapter(adapter);
    }

    private void startLoreActivity() {
        Intent intent = new Intent(getContext(), LoreActivity.class);
        intent.putExtra("catNumber", catNumber);
        intent.putExtra("catName", categoryName);
        intent.putExtra("loreTitle", loreTitle);
        intent.putExtra("searchString", searchString);

        startActivity(intent);
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

