package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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

    private SQLiteDatabase legendsDB;
    private ExpandableListView legendsExpandableView;
    private Cursor cursor;
    private int spinnerCatNumber;

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

        legendsExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                LinearLayout ll = (LinearLayout) v;
                TextView tv = (TextView) ll.findViewById(R.id.subcategory_text_view);
                String clickedSubcatText = tv.getText().toString();

                Log.v("***STRING: " , "" + clickedSubcatText);
                Log.v("***GROUP:CHILD POS" , "" + groupPosition + " : " + childPosition);

                Intent intent = new Intent(MainActivity.this, LoreActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    private void setupSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Solomon Island")) {
                        spinnerCatNumber = LoreLibrary.CAT_1_SOL;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Valley of the Sun God")) {
                        spinnerCatNumber = LoreLibrary.CAT_2_EGY;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Transylvania")) {
                        spinnerCatNumber = LoreLibrary.CAT_3_TRN;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Tokyo")) {
                        spinnerCatNumber = LoreLibrary.CAT_4_TOK;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Global")) {
                        spinnerCatNumber = LoreLibrary.CAT_5_GBL;
                        displayCategoryScreen();
                    }
                    if (selection.equals("The Bestiary")) {
                        spinnerCatNumber = LoreLibrary.CAT_6_BES;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Events")) {
                        spinnerCatNumber = LoreLibrary.CAT_7_EVN;
                        displayCategoryScreen();
                    }
                    if (selection.equals("Issues")) {
                        spinnerCatNumber = LoreLibrary.CAT_8_ISU;
                        displayCategoryScreen();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //Do nothing
            }
        });
    }

    private void displayCategoryScreen()
    {
        closeCursor();

        String[] selectionArgs = { Integer.toString(spinnerCatNumber), Integer.toString(spinnerCatNumber) };
        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        cursor = legendsDB.rawQuery(unionQuery, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, this);
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);
    }

    private void displaySubcategoryScreen(int groupPosition, int childPosition)
    {
        closeCursor();

        int adjustedGroupPos = groupPosition + 1;
        String[] selectionArgs = { Integer.toString(adjustedGroupPos), Integer.toString(adjustedGroupPos) };
        String[] mergedQuery = { Queries.UNION_1, Queries.UNION_2};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String unionQuery = qb.buildUnionQuery(mergedQuery, null, null);

        cursor = legendsDB.rawQuery(unionQuery, selectionArgs);

        LegendsCursorTreeAdapter legendsCursorTreeAdapter = new LegendsCursorTreeAdapter(cursor, this);
        legendsExpandableView.setAdapter(legendsCursorTreeAdapter);

        //EXPAND THE GROUP THE USER CLICKED ON
        legendsExpandableView.expandGroup(childPosition);
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
    }

    private void closeCursor()
    {
        //USED TO CLOSE THE PREVIOUS CURSOR WHEN SWAPPING BETWEEN CATEGORY AND SUBCAT VIEWS
        if (cursor != null)
        {
            cursor.close();
        }
    }
}