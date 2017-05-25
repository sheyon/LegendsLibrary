package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
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

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class CategoriesFragment extends Fragment {

    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    private int spinnerCatNumber;
    private String categoryName;
    private String loreTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_layout, container, false);

        setupSpinner(view);
        setupExpandableView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayCategoryScreen();
    }

    private void setupSpinner(View view)
    {
        //THE SPINNER SEEMS TO BE PRODUCING A WINDOW ALREADY FOCUSED ERROR. FIX LATER
        final Spinner spinner = (Spinner) view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, R.layout.spinner_custom_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_custom_dropdown_text);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("[Choose a category:]")) {
                        spinnerCatNumber = LoreLibrary.CAT_0;
                        closeCursor();
                        return;
                    }
                    if (selection.equals("Solomon Island")) {
                        categoryName = "Solomon Island";
                        spinnerCatNumber = LoreLibrary.CAT_1_SOL;
                    }
                    if (selection.equals("Valley of the Sun God")) {
                        categoryName = "Valley of the Sun God";
                        spinnerCatNumber = LoreLibrary.CAT_2_EGY;
                    }
                    if (selection.equals("Transylvania")) {
                        categoryName = "Transylvania";
                        spinnerCatNumber = LoreLibrary.CAT_3_TRN;
                    }
                    if (selection.equals("Tokyo")) {
                        categoryName = "Tokyo";
                        spinnerCatNumber = LoreLibrary.CAT_4_TOK;
                    }
                    if (selection.equals("Global")) {
                        categoryName = "Global";
                        spinnerCatNumber = LoreLibrary.CAT_5_GBL;
                    }
                    if (selection.equals("The Bestiary")) {
                        categoryName = "The Bestiary";
                        spinnerCatNumber = LoreLibrary.CAT_6_BES;
                    }
                    if (selection.equals("Events")) {
                        categoryName = "Events";
                        spinnerCatNumber = LoreLibrary.CAT_7_EVN;
                    }
                    if (selection.equals("Issues")) {
                        categoryName = "Issues";
                        spinnerCatNumber = LoreLibrary.CAT_8_ISU;
                    }
                displayCategoryScreen();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void setupExpandableView(View view)
    {
        legendsExpandableView = (ExpandableListView) view.findViewById(R.id.legends_expandable_list);

        legendsExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                LinearLayout ll = (LinearLayout) v;
                TextView tv = (TextView) ll.findViewById(R.id.category_text_view);
                int style = tv.getTypeface().getStyle();

                //IF THE TEXT STYLE IS BOLDED, EXPAND THE CATEGORY
                if ( style == 1 ) {
                    return false;
                }
                //IF NOT, LAUNCH THE LORE PAGE
                else {
                    loreTitle = tv.getText().toString();
                    startLoreActivity();
                    return false;
                }
            }
        });

        legendsExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LinearLayout ll = (LinearLayout) v;
                TextView tv = (TextView) ll.findViewById(R.id.subcategory_text_view);
                loreTitle = tv.getText().toString();

                startLoreActivity();
                return false;
            }
        });
    }

    private void startLoreActivity() {
        Intent intent = new Intent(getContext(), LoreActivity.class);
        intent.putExtra("catNumber", spinnerCatNumber);
        intent.putExtra("catName", categoryName);
        intent.putExtra("loreTitle", loreTitle);

        closeCursor();
        startActivity(intent);
    }

    private void displayCategoryScreen() {
        closeCursor();

        String[] selectionArgs = { Integer.toString(spinnerCatNumber), Integer.toString(spinnerCatNumber) };
        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        cursor = legendsDB.rawQuery(unionQuery, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, getContext());
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        closeCursor();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeCursor();
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}