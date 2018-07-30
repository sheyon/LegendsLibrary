package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Categories;
import com.sheyon.fivecats.legendslibrary.data.LegendsConstants.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

public class CategoriesFragment extends Fragment {

    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    private Spinner spinner;
    private LegendsPreferences legendsPreferences;
    private SQLiteDatabase db;

    private int catNumber;
    private int groupNumber = -1;
    private String loreTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        legendsPreferences = LegendsPreferences.getInstance(getContext());
        db = LegendsDatabase.getInstance(getContext());

        setupSpinner(view);
        setupExpandableView(view);
    }

    private void setupSpinner(View view) {
        //THE SPINNER SEEMS TO BE PRODUCING A WINDOW ALREADY FOCUSED ERROR. FIX LATER
        spinner = view.findViewById(R.id.category_spinner);
        setupNewSpinnerAdapter();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_choose))) {
                    catNumber = Categories.CAT_0;
                    legendsPreferences.setSpinnerPosition(position);
                    closeCursor();
                    return;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_solomon))) {
                    catNumber = Categories.CAT_1_SOL;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_egypt))) {
                    catNumber = Categories.CAT_2_EGY;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_trans))) {
                    catNumber = Categories.CAT_3_TRN;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_tokyo))) {
                    catNumber = Categories.CAT_4_TOK;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_africa))) {
                    catNumber = Categories.CAT_9_AFR;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_global))) {
                    catNumber = Categories.CAT_5_GBL;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_bestiary))) {
                    catNumber = Categories.CAT_6_BES;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_events))) {
                    catNumber = Categories.CAT_7_EVN;
                }
                else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.spinner_issues))) {
                    catNumber = Categories.CAT_8_ISU;
                }
                legendsPreferences.setSpinnerPosition(position);
                displayCategoryScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void setupNewSpinnerAdapter() {
        ArrayAdapter<CharSequence> spinnerAdapter;
        if (legendsPreferences.isUsingTswSorting()) {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array_tsw, R.layout.spinner_custom_layout);
        } else {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array_swl, R.layout.spinner_custom_layout);
        }
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_custom_dropdown_text);
        spinner.setAdapter(spinnerAdapter);
    }

    private void setupExpandableView(View view) {
        legendsExpandableView = view.findViewById(R.id.legends_expandable_list);
        legendsExpandableView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            LinearLayout ll = (LinearLayout) v;
            TextView tv = ll.findViewById(R.id.category_text_view);
            int style = tv.getTypeface().getStyle();

            //IF THE TEXT STYLE IS BOLDED, EXPAND THE CATEGORY
            if ( style == 1 ) {
                groupNumber = groupPosition;
                return false;
            }
            //IF NOT, LAUNCH THE LORE PAGE
            else {
                loreTitle = tv.getText().toString();
                startLoreActivity();
                return true;
            }
        });

        legendsExpandableView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            LinearLayout ll = (LinearLayout) v;
            TextView tv = ll.findViewById(R.id.subcategory_text_view);
            loreTitle = tv.getText().toString();

            groupNumber = groupPosition;

            startLoreActivity();
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //THIS ENSURES THE CATEGORIES MATCH TSW OR SWL SORTING PREFERENCES
        setupNewSpinnerAdapter();

        //THIS ENSURES SWL-MODE DOESN'T CRASH PERMANENTLY IF STUCK ON ISSUES CATEGORY
        resetSpinnerPosition();

        if (groupNumber != -1) {
            ViewTreeObserver vto = legendsExpandableView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
                //WTF REALLY?!
                @Override
                public void onGlobalLayout() {
                    legendsExpandableView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    legendsExpandableView.post(() -> legendsExpandableView.expandGroup(groupNumber));
                }
            });
        }
    }

    private void startLoreActivity() {
        Intent intent = new Intent(getContext(), LoreActivity.class);
        intent.putExtra("loreTitle", loreTitle);

        //FAILSAFE
        legendsPreferences.setLoreTitle(loreTitle);

        closeCursor();
        startActivity(intent);
    }

    private void displayCategoryScreen() {
        closeCursor();

        String[] selectionArgs = { Integer.toString(catNumber), Integer.toString(catNumber) };
//        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        //ALWAYS GET NEW DATABASE VARIABLE IN CASE SETTINGS WERE CHANGED
        db = LegendsDatabase.getInstance(getContext());
        cursor = db.rawQuery(Queries.THIS_FUCKIN_QUERY, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, getContext());
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);
    }

    private void resetSpinnerPosition() {
        if (!legendsPreferences.isUsingTswSorting() && legendsPreferences.getSpinnerPosition() == Categories.CAT_8_ISU) {
            legendsPreferences.setSpinnerPosition(0);
        }
        spinner.setSelection(legendsPreferences.getSpinnerPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        resetSpinnerPosition();
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}