package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    private ListView legendsListView;
    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //legendsListView = (ListView) findViewById(R.id.legends_listView);
        legendsExpandableView = (ExpandableListView) findViewById(R.id.legends_expandable_list);

        displayDatabaseInfo();

        //OPTIONAL LATER
        //set up Content Provider
        //set up legendsCursorAdaptor
        //set up legendsCursorLoader
        //set up legendsListView.onItemClickListener
    }

    private void displayDatabaseInfo()
    {
        if (cursor != null)
        {
            cursor.close();
        }

        LegendsHelper legendsHelper = new LegendsHelper(this);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        cursor = legendsDB.rawQuery(LegendsContract.CAT_QUERY, null, null);

        LegendsCursorTreeAdaptor legendsCursorTreeAdaptor = new LegendsCursorTreeAdaptor(cursor, this);
        legendsExpandableView.setAdapter(legendsCursorTreeAdaptor);

        //TO UTILISE THE CURSOR ADAPTOR
        //LegendsAdapter legendsAdapter = new LegendsAdapter(this, cursor);
        //legendsListView.setAdapter(legendsAdapter);
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