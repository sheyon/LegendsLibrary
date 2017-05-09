package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    private ListView legendsListView;

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

        String[] projection = {
                LoreLibrary._ID,
                LoreLibrary.LORE_TITLE,
                LoreLibrary.CATEGORY_ID,
                LoreLibrary.SUBCAT_ID
        };

        Cursor cursor = legendsDB.query(LoreLibrary.LORE_TABLE_NAME, projection, null, null, null, null, null);

        LegendsAdapter legendsAdapter = new LegendsAdapter(this, cursor);
        legendsListView.setAdapter(legendsAdapter);

        cursor.close();
    }
}
