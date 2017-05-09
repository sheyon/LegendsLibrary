package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    private ListView legendsListView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        legendsListView = (ListView) findViewById(R.id.legends_listView);

        displayDatabaseInfo();

        //OPTIONAL LATER
        //set up Content Provider
        //set up legendsCursorAdaptor
        //set up legendsCursorLoader
        //set up legendsListView.onItemClickListener
    }

    private void displayDatabaseInfo()
    {
        LegendsHelper legendsHelper = new LegendsHelper(this);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        cursor = legendsDB.rawQuery(LegendsContract.MY_QUERY, null, null);

        LegendsAdapter legendsAdapter = new LegendsAdapter(this, cursor);
        legendsListView.setAdapter(legendsAdapter);
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
        if (cursor != null)
        {
            cursor.close();
        }
    }
}