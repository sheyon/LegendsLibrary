package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchFragment extends Fragment implements FragmentVisibilityListener, View.OnClickListener
{
    private LinearLayout searchLayout;
    private SearchView searchView;
    private ListView listView;
    private View loadingView;
    private LegendsListAdapter adapter;

    private int prefsLang;
    private boolean prefsNormalization;
    private boolean prefsWildcardOn;

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

    private void getPrefs(){
        LegendsPreferences legendsPrefs = LegendsPreferences.getInstance(getContext());
        prefsLang = legendsPrefs.getLangPref();
        prefsNormalization = legendsPrefs.getNormalizationPref();
        prefsWildcardOn = legendsPrefs.getWildcardAlwaysOnPref();
    }

    private void setupSearchBar(View view) {
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //CLEAR FOCUS IS NEEDED TO PREVENT THE QUERY FROM FIRING TWICE IN CASE OF PHYSICAL KEYBOARDS [ ANDROID BUG :( ]
                searchString = searchView.getQuery().toString().toLowerCase().trim();
                searchView.clearFocus();

                //QUOTATION MARKS CRASH THE SEARCH
                if (searchString.contains("\"")) {
                    Toast.makeText(getContext(), R.string.toast_no_quotes, Toast.LENGTH_SHORT).show();
                    return false;
                }
                //PEOPLE CAN STILL SEARCH FOR 'UTA' OR 'BEE'; ANYTHING LESS WILL RETURN NOTHING USEFUL
                if (searchString.length() < 3) {
                    Toast.makeText(getContext(), R.string.toast_three_chars, Toast.LENGTH_SHORT).show();
                    return false;
                }
                //ARTICLES CAN RETURN FALSE SEARCH RESULTS
                if (searchString.startsWith("les ") || searchString.startsWith("la ") || searchString.startsWith("le ") || searchString.startsWith("l' ") ||
                        searchString.startsWith("des ") || searchString.startsWith("de ") || searchString.startsWith("du ") || searchString.startsWith("d' ") ||
                        searchString.startsWith("the ") || searchString.startsWith("a ") || searchString.startsWith("an ") ||
                        searchString.startsWith("der ") || searchString.startsWith("die ") || searchString.startsWith("das ") ||
                        searchString.startsWith("dem ") || searchString.startsWith("den ") ||
                        searchString.startsWith("ein ") || searchString.startsWith("eine ") || searchString.startsWith("einen ") ||
                        searchString.startsWith("einem ") || searchString.startsWith("einer ") || searchString.startsWith("eines ") ) {
                    Toast.makeText(getContext(), R.string.toast_no_articles, Toast.LENGTH_SHORT).show();
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
        loadingView = view.findViewById(R.id.search_loading_view);

        LinearLayout infoClickable = (LinearLayout) view.findViewById(R.id.search_info_clickable);
        ViewHolder holder = new ViewHolder();
        holder.mClickableLayout = infoClickable;
        holder.mClickableLayout.setOnClickListener(this);
    }

    private void runQuery() {
        //CLOSE ANY OF THE PREVIOUS QUERIES IF THEY EXIST
        closeCursor();
        getPrefs();

        //PREF OVERRIDE CHECK
        if (prefsWildcardOn) {
            searchString = searchString + "*";
        }

        //FOR DE and FR (NORMALIZED); ENGLISH DOES NOT SUPPORT A NORMALIZATION QUERY
        if (prefsNormalization && prefsLang != 0) {
            searchString = normalizeSearchString(searchString);

            modString = "'"+searchString+"'";
            String [] selectionArgs = { modString, modString, modString };

            cursor = legendsDB.rawQuery(Queries.QUERY_FTS_NORMALIZED, selectionArgs);
        }
        else {
            //FOR EN, DE, FR (UN-NORMALIZED); CHECK FOR ROMANIAN WORDS FIRST
            searchString = checkForRomanianWords(searchString);

            modString = "'"+searchString+"'";
            String [] selectionArgs = { modString, modString, modString };

            cursor = legendsDB.rawQuery(Queries.QUERY_FTS, selectionArgs);
        }

        if (cursor != null) {
            cursor.moveToFirst();
        }

        adapter = new LegendsListAdapter(getContext(), cursor, searchString, this);
        listView.setAdapter(adapter);
    }

    String checkForRomanianWords(String search) {
        Pattern patternDrac = Pattern.compile("dr.cule.ti");
        Pattern patternHarb = Pattern.compile("h.rb.bure.ti");
        Pattern patternBacas = Pattern.compile("baca.");
        Pattern patternMosul = Pattern.compile("mo.ul");
        Pattern patternIaz = Pattern.compile("iazm.*");
        Pattern patternMoarta = Pattern.compile("moart.");

        Matcher matchDrac = patternDrac.matcher(search);
        Matcher matchHarb = patternHarb.matcher(search);
        Matcher matchBacas = patternBacas.matcher(search);
        Matcher matchMosul = patternMosul.matcher(search);
        Matcher matchIaz = patternIaz.matcher(search);
        Matcher matchMoarta = patternMoarta.matcher(search);

        if (matchDrac.matches()){ search = "drăculești"; }
        if (matchHarb.matches()){ search = "harbaburești"; }
        if (matchBacas.matches()){ search = "bacaș"; }
        if (matchMosul.matches()){ search = "moșul"; }
        if (matchIaz.matches()){ search = "iazmăciune"; }
        if (matchMoarta.matches()){ search = "moartă"; }
        return search;
    }

    //THIS FUNCTION EXISTS IN CASE A USER PUTS DIACRITICS INTO A DIACRITIC-INSENSITIVE QUERY
    String normalizeSearchString(String query){
        String normalizedQuery = Normalizer.normalize(query, Normalizer.Form.NFD);
        normalizedQuery = normalizedQuery.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalizedQuery;
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
        getPrefs();
        String[] selectionArgs = { modString, modString, modString };

        //ENGLISH DOES NOT SUPPORT A NORMALIZATION QUERY
        if (prefsNormalization && prefsLang != 0) {
            refreshedCursor = legendsDB.rawQuery(Queries.QUERY_FTS_NORMALIZED, selectionArgs);
        }
        else {
            refreshedCursor = legendsDB.rawQuery(Queries.QUERY_FTS, selectionArgs);
        }

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
        searchView.setQuery(null, false);

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

//    GETTING LOG SPAM ON PHYSICAL DEVICE ABOUT EXPIRED INPUT-CONNECTION; NOT SURE HOW TO FIX; OTHERWISE HARMLESS
//    private void restartInput() {
//        if (inputMethodManager != null) {
//            inputMethodManager.restartInput(searchLayout);
//        }
//    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onFragmentInvisible() {
        searchLayout.setVisibility(View.GONE);
        loadingView.setAlpha(1f);
        hideKeyboard();
    }

    @Override
    public void onFragmentVisible() {
        refreshCursor();

        Crossfader crossfader = new Crossfader();
        crossfader.crossfadeView(searchLayout, loadingView);
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