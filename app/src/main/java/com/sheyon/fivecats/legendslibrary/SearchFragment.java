package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment implements FragmentVisibilityListener, View.OnClickListener
{
    private LinearLayout searchLayout;
    private SearchView searchView;
    private ListView listView;
    private LegendsListAdapter adapter;

    private Cursor cursor;
    private Cursor refreshedCursor;

    private String modString;
    private String searchString;

    private static class ViewHolder {
        private LinearLayout mClickableLayout;
    }

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

                //QUOTATION MARKS CRASH THE SEARCH
                if (searchString.contains("\"")) {
                    Toast.makeText(getContext(), "Do not include quotation marks in your queries.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                //PEOPLE CAN STILL SEARCH FOR 'UTA' OR 'BEE'; ANYTHING LESS WILL RETURN NOTHING USEFUL
                if (searchString.length() < 3) {
                    Toast.makeText(getContext(), "Queries must have a length of at least 3 characters.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else  {
                    runQuery();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupListView(View view) {
        searchLayout = (LinearLayout) view.findViewById(R.id.search_layout);
        listView = (ListView) view.findViewById(R.id.search_list_view);

        LinearLayout infoClickable = (LinearLayout) view.findViewById(R.id.search_info_clickable);
        ViewHolder holder = new ViewHolder();
        holder.mClickableLayout = infoClickable;
        holder.mClickableLayout.setOnClickListener(this);
    }

    private void runQuery() {
        //CLOSE ANY OF THE PREVIOUS QUERIES IF THEY EXIST
        closeCursor();

        modString = "'"+searchString+"'";
        String [] selectionArgs = { modString, modString, modString };

        cursor = legendsDB.rawQuery(Queries.QUERY_FTS, selectionArgs);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        adapter = new LegendsListAdapter(getContext(), cursor, searchString, this);
        listView.setAdapter(adapter);
    }

    public void refreshCursor() {
        //NULL CATCH; NPE OCCURS IF THERE IS A NULL CURSOR TO SWAP
        if (cursor == null) {
            return;
        }

        //IF USER HAS NOT SEARCHED YET, DO NOT REFRESH
        if (modString == null) {
            return;
        }

        closeCursor();
        String[] selectionArgs = { modString, modString, modString };
        refreshedCursor = legendsDB.rawQuery(Queries.QUERY_FTS, selectionArgs);
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
        if (refreshedCursor != null){
            refreshedCursor.close();
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

    private void clearSearch() {
        //EMPTY THE SEARCH FIELD
        searchView.setQuery("", false);

        //NULL CATCH; IF THERE IS A NULL, DO NOTHING
        if (cursor == null && refreshedCursor == null){
            return;
        }
        //OTHERWISE IT IS SAFE TO PROCEED
        else {
            adapter.swapCursor(null);
            modString = null;
        }
    }

    @Override
    public void onFragmentInvisible() {
        searchLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentVisible() {
        refreshCursor();

        // ACTION TO RUN AFTER 1/10TH OF A SECOND
        // THIS IS NECESSARY TO ENSURE A SMOOTH TRANSITION WHEN FAVING A LORE FROM ANOTHER TAB AND THEN NAVIGATING BACK
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                searchLayout.setVisibility(View.VISIBLE);
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_info_clickable) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_search_info_layout, null);

            //DUMMY VIEW TO DARKEN AND LOCK THE BACKGROUND
            View dummyView = new View(getContext());
            dummyView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black));
            dummyView.setAlpha(.6f);

            final PopupWindow mDummyPopupWindow = new PopupWindow(dummyView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mDummyPopupWindow.showAtLocation(listView, Gravity.CENTER, 0, 0);

            final PopupWindow mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.showAtLocation(listView, Gravity.CENTER, 0, 0);
            if( Build.VERSION.SDK_INT >= 21){
                mPopupWindow.setElevation(5);
            }
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mDummyPopupWindow.dismiss();
                }
            });

            Button closeButton = (Button) popupView.findViewById(R.id.popup_button_dismiss);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
        }
    }
}