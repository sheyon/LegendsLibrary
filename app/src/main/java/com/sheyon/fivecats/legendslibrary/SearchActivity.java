package com.sheyon.fivecats.legendslibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

public class SearchActivity extends AppCompatActivity
{
    int categoryId;
    String categoryName;
    String loreTitle;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String searchString = getIntent().getStringExtra("query");
        String modString = "%"+searchString+"%";
        String[] selectionArgs = { modString, modString, modString };

        ListView searchListView = (ListView) findViewById(R.id.search_list_view);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.findViewById(R.id.lore_category_text_view);

                categoryName = tv.getText().toString();
                categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(LegendsContract.LoreLibrary.COLUMN_CATEGORY_ID));
                loreTitle = cursor.getString(cursor.getColumnIndexOrThrow(LegendsContract.LoreLibrary.COLUMN_TITLE));

                startLoreActivity();
            }
        });

        cursor = legendsDB.rawQuery(Queries.SEARCH, selectionArgs);
        LegendsListAdapter listAdapter = new LegendsListAdapter(this, cursor);
        searchListView.setAdapter(listAdapter);
    }

    private void startLoreActivity()
    {
        Intent intent = new Intent(SearchActivity.this, LoreActivity.class);
        intent.putExtra("catPosition", categoryId);
        intent.putExtra("catName", categoryName);
        //Not really the search param, but it is being repurposed here so that it can smoothly launch the lore into the next activity
        intent.putExtra("searchParam", loreTitle);

        closeCursor();
        startActivity(intent);
    }

    private void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }
}
