package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase legendsDB;
    private ExpandableListView legendsExpandableView;
    private Cursor cursor;
    private int spinnerSelection;

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
                displaySubcategoryScreen(groupPosition, childPosition);

                Log.v("***STRING: " , "" + clickedSubcatText);
                Log.v("***GROUP:CHILD POS" , "" + groupPosition + " : " + childPosition);
                return false;
            }
        });

        displayCategoryScreen();
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayCategoryScreen()
    {
        closeCursor();
        cursor = legendsDB.rawQuery(Queries.CAT_QUERY, null, null);

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

    @Override
    public void onBackPressed()
    {
        closeCursor();
        displayCategoryScreen();
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