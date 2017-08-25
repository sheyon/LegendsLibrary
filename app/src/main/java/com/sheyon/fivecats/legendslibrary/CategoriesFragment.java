package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

public class CategoriesFragment extends Fragment {

    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    private Spinner spinner;
    private LegendsPreferences legendsPreferences;
    private SQLiteDatabase db;

    private int spinnerCatNumber;
    private int groupNumber = -1;
    private String loreTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        legendsPreferences = LegendsPreferences.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_layout, container, false);

        db = LegendsDatabase.getInstance(getContext());

        setupSpinner(view);
        setupExpandableView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayCategoryScreen();

        if (groupNumber != -1) {
            legendsExpandableView.expandGroup(groupNumber);
        }
    }

    private void setupSpinner(View view)
    {
        //THE SPINNER SEEMS TO BE PRODUCING A WINDOW ALREADY FOCUSED ERROR. FIX LATER
        spinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, R.layout.spinner_custom_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_custom_dropdown_text);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinnerCatNumber = LoreLibrary.CAT_0;
                    legendsPreferences.setSpinnerCatNumber(spinnerCatNumber);
                    closeCursor();
                    return;
                }
                if (position == 1) {
                    spinnerCatNumber = LoreLibrary.CAT_1_SOL;
                }
                if (position == 2) {
                    spinnerCatNumber = LoreLibrary.CAT_2_EGY;
                }
                if (position == 3) {
                    spinnerCatNumber = LoreLibrary.CAT_3_TRN;
                }
                if (position == 4) {
                    spinnerCatNumber = LoreLibrary.CAT_4_TOK;
                }
                if (position == 5) {
                    spinnerCatNumber = LoreLibrary.CAT_5_GBL;
                }
                if (position == 6) {
                    spinnerCatNumber = LoreLibrary.CAT_6_BES;
                }
                if (position == 7) {
                    spinnerCatNumber = LoreLibrary.CAT_7_EVN;
                }
                if (position == 8) {
                    spinnerCatNumber = LoreLibrary.CAT_8_ISU;
                }
                legendsPreferences.setSpinnerCatNumber(spinnerCatNumber);
                displayCategoryScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void setupExpandableView(View view)
    {
        legendsExpandableView = view.findViewById(R.id.legends_expandable_list);

        legendsExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
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
            }
        });

        legendsExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LinearLayout ll = (LinearLayout) v;
                TextView tv = ll.findViewById(R.id.subcategory_text_view);
                loreTitle = tv.getText().toString();

                groupNumber = groupPosition;

                startLoreActivity();
                return false;
            }
        });
    }

    private void startLoreActivity() {
        Intent intent = new Intent(getContext(), LoreActivity.class);
        intent.putExtra("catNumber", spinnerCatNumber);
        intent.putExtra("loreTitle", loreTitle);

        //FAILSAFE
        legendsPreferences.setLoreTitle(loreTitle);

        closeCursor();
        startActivity(intent);
    }

    private void displayCategoryScreen() {
        closeCursor();

        String[] selectionArgs = { Integer.toString(spinnerCatNumber), Integer.toString(spinnerCatNumber) };
        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        //ALWAYS GET NEW DATABASE VARIABLE IN CASE SETTINGS WERE CHANGED
        db = LegendsDatabase.getInstance(getContext());
        cursor = db.rawQuery(unionQuery, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, getContext());
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        legendsPreferences.setSpinnerCatNumber(spinnerCatNumber);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        spinner.setSelection(legendsPreferences.getSpinnerCatNumber());
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}