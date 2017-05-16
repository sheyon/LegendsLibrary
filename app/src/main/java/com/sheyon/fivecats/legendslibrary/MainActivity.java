package com.sheyon.fivecats.legendslibrary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase legendsDB;
    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    private int spinnerCatNumber;
    private String categoryName;
    private String clickedText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSpinner();

        //OPEN DATABASE
        LegendsHelper legendsHelper = new LegendsHelper(this);
        legendsDB = legendsHelper.getReadableDatabase();

        legendsExpandableView = (ExpandableListView) findViewById(R.id.legends_expandable_list);

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
                    clickedText = tv.getText().toString();
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
                clickedText = tv.getText().toString();

                startLoreActivity();
                return false;
            }
        });
    }

    private void startLoreActivity() {
        Intent intent = new Intent(MainActivity.this, LoreActivity.class);
        intent.putExtra("catPosition", spinnerCatNumber);
        intent.putExtra("catName", categoryName);
        intent.putExtra("searchParam", clickedText);

        closeCursor();
        startActivity(intent);
    }

    private void setupSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, R.layout.spinner_custom_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_custom_dropdown_text);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("[Choose a category:]")) {
                        spinnerCatNumber = LoreLibrary.CAT_0;
                        cursor.close();
                    }
                    if (selection.equals("Solomon Island")) {
                        categoryName = "Solomon Island";
                        spinnerCatNumber = LoreLibrary.CAT_1_SOL;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Valley of the Sun God")) {
                        categoryName = "Valley of the Sun God";
                        spinnerCatNumber = LoreLibrary.CAT_2_EGY;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Transylvania")) {
                        categoryName = "Transylvania";
                        spinnerCatNumber = LoreLibrary.CAT_3_TRN;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Tokyo")) {
                        categoryName = "Tokyo";
                        spinnerCatNumber = LoreLibrary.CAT_4_TOK;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Global")) {
                        categoryName = "Global";
                        spinnerCatNumber = LoreLibrary.CAT_5_GBL;
                        displayCategoryScreen();
                    }
                    if (selection.equals("The Bestiary")) {
                        categoryName = "The Bestiary";
                        spinnerCatNumber = LoreLibrary.CAT_6_BES;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Events")) {
                        categoryName = "Events";
                        spinnerCatNumber = LoreLibrary.CAT_7_EVN;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Issues")) {
                        categoryName = "Issues";
                        spinnerCatNumber = LoreLibrary.CAT_8_ISU;
                        displayCategoryScreen();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void displayCategoryScreen() {
        closeCursor();

        String[] selectionArgs = { Integer.toString(spinnerCatNumber), Integer.toString(spinnerCatNumber) };
        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        cursor = legendsDB.rawQuery(unionQuery, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, this, legendsDB);
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCategoryScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCursor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCursor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCursor();
        legendsDB.close();
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}