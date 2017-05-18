package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchActivity extends AppCompatActivity
{
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String searchString = getIntent().getStringExtra("query");
        String modString = "%"+searchString+"%";
        String[] selectionArgs = { modString, modString, modString };

        ExpandableListView searchExpandableView = (ExpandableListView) findViewById(R.id.search_expandable_view);

        cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);
        cursor.moveToFirst();

        ExpandableSearchAdapter adapter = new ExpandableSearchAdapter(cursor, this, searchString);
        searchExpandableView.setAdapter(adapter);
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

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}
