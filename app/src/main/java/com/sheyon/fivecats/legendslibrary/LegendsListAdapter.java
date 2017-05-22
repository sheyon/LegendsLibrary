package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;

public class LegendsListAdapter extends CursorAdapter implements View.OnClickListener
{
    protected static class ViewHolder
    {
        private LinearLayout mTextLayout;
        private LinearLayout mImageLayout;
    }

    private Context mContext;
    private String mSearchString;

    private ImageView loreFavorite_IV;
    private TextView loreTitle_TV;
    private TextView loreCategory_TV;

    public LegendsListAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
        mContext = context;
    }

    public LegendsListAdapter(Context context, Cursor c, String searchString)
    {
        super(context, c, 0);
        mContext = context;
        mSearchString = searchString;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.lore_list_item,parent, false);

        LinearLayout textLayout = (LinearLayout) view.findViewById(R.id.container_text_views);
        LinearLayout imageLayout = (LinearLayout) view.findViewById(R.id.container_fave_clickable);

        ViewHolder holder = new ViewHolder();
        holder.mTextLayout = textLayout;
        holder.mImageLayout = imageLayout;

        holder.mTextLayout.setOnClickListener(this);
        holder.mImageLayout.setOnClickListener(this);

        return view;
        //return LayoutInflater.from(context).inflate(R.layout.lore_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        loreTitle_TV = (TextView) view.findViewById(R.id.lore_title_text_view);
        loreCategory_TV = (TextView) view.findViewById(R.id.lore_category_text_view);

        //START UNFAVED
        loreFavorite_IV = (ImageView) view.findViewById(R.id.lore_favorites_image_view);
        loreFavorite_IV.setImageResource(R.drawable.ic_star_border_white_48dp);

        String titleText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        String categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));
        int faved = cursor.getInt(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_FAVED));

        loreTitle_TV.setText(titleText);
        loreCategory_TV.setText(categoryText);

        if (faved == 1) {
            loreFavorite_IV.setImageResource(R.drawable.ic_star_border_white_48dp);
        }
    }

    private int getCategoryId(String categoryText) {
        int catNumber = 0;

        if (categoryText.equals("Solomon Island")) {
            catNumber = LoreLibrary.CAT_1_SOL;
        }
        if (categoryText.equals("Valley of the Sun God")) {
            catNumber = LoreLibrary.CAT_2_EGY;
        }
        if (categoryText.equals("Transylvania")) {
            catNumber = LoreLibrary.CAT_3_TRN;
        }
        if (categoryText.equals("Tokyo")) {
            catNumber = LoreLibrary.CAT_4_TOK;
        }
        if (categoryText.equals("Global")) {
            catNumber = LoreLibrary.CAT_5_GBL;
        }
        if (categoryText.equals("The Bestiary")) {
            catNumber = LoreLibrary.CAT_6_BES;
        }
        if (categoryText.equals("Events")) {
            catNumber = LoreLibrary.CAT_7_EVN;
        }
        if (categoryText.equals("Issues")) {
            catNumber = LoreLibrary.CAT_8_ISU;
        }
        return catNumber;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.container_text_views:
                loreTitle_TV = (TextView) v.findViewById(R.id.lore_title_text_view);
                loreCategory_TV = (TextView) v.findViewById(R.id.lore_category_text_view);

                String clickedTitle = loreTitle_TV.getText().toString();
                String clickedCategory = loreCategory_TV.getText().toString();
                int clickedCatId = getCategoryId(clickedCategory);

                Intent intent = new Intent(mContext, LoreActivity.class);
                intent.putExtra("catNumber", clickedCatId);
                intent.putExtra("catName", clickedCategory);
                intent.putExtra("loreTitle", clickedTitle);
                intent.putExtra("searchString", mSearchString);

                mContext.startActivity(intent);
                break;

            case R.id.container_fave_clickable:
                loreFavorite_IV = (ImageView) v.findViewById(R.id.lore_favorites_image_view);
                loreFavorite_IV.setImageResource(R.drawable.ic_star_white_48dp);
                break;
        }
    }

    private void setFave()
    {

    }
}
