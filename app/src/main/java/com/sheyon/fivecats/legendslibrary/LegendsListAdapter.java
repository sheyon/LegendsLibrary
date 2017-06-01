package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract.Queries;
import com.sheyon.fivecats.legendslibrary.data.LegendsContract.LoreLibrary;

import static com.sheyon.fivecats.legendslibrary.MainActivity.legendsDB;

class LegendsListAdapter extends CursorAdapter implements View.OnClickListener
{
    private static class ViewHolder {
        private LinearLayout mTextLayout;
        private LinearLayout mImageLayout;
    }

    private Context mContext;
    private String mSearchString;
    private Fragment mFragment;

    private ImageView loreFavorite_IV;
    private TextView loreTitle_TV;
    private TextView loreCategory_TV;

    public LegendsListAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    LegendsListAdapter(Context context, Cursor c, Fragment fragment) {
        super(context, c, 0);
        mContext = context;
        mFragment = fragment;
    }

    LegendsListAdapter(Context context, Cursor c, String searchString, Fragment fragment) {
        super(context, c, 0);
        mContext = context;
        mSearchString = searchString;
        mFragment = fragment;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.lore_list_item,parent, false);

        LinearLayout textLayout = (LinearLayout) view.findViewById(R.id.container_text_views);
        LinearLayout imageLayout = (LinearLayout) view.findViewById(R.id.container_fave_clickable);

        ViewHolder holder = new ViewHolder();
        holder.mTextLayout = textLayout;
        holder.mImageLayout = imageLayout;

        holder.mTextLayout.setOnClickListener(this);
        holder.mImageLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        loreTitle_TV = (TextView) view.findViewById(R.id.lore_title_text_view);
        loreCategory_TV = (TextView) view.findViewById(R.id.lore_category_text_view);

        //START UNFAVED
        loreFavorite_IV = (ImageView) view.findViewById(R.id.lore_favorites_image_view);
        loreFavorite_IV.setImageResource(R.drawable.ic_star_border_white_48dp);

        //IF COMING FROM THE SEARCH PAGE, HIDE THE STAR
        //WITH FTS, IMPLEMENTING FAVES WILL BE A MESS
        if (mFragment.getClass() == SearchFragment.class) {
            loreFavorite_IV.setVisibility(View.INVISIBLE);
        }

        String titleText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        String categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));
        int faved = cursor.getInt(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_FAVED));

        loreTitle_TV.setText(titleText);
        loreCategory_TV.setText(categoryText);

        if (faved == 1) {
            loreFavorite_IV.setImageResource(R.drawable.ic_star_white_48dp);
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
                //GET PARENT VIEW TO CATCH THE TITLE STRING
                View parentView = (View) v.getParent();
                loreTitle_TV = (TextView) parentView.findViewById(R.id.lore_title_text_view);
                String faveTitle = loreTitle_TV.getText().toString();
                String modFaveTitle = "\"" + faveTitle + "\"";

                //EXECUTE UPDATE QUERY
                legendsDB.execSQL(Queries.UPDATE_FAVE + modFaveTitle + ";");

                //GET UPDATED CURSOR
                String[] selectionArgs = { faveTitle };
                Cursor cursor = legendsDB.rawQuery(Queries.GET_FAVE, selectionArgs);
                cursor.moveToFirst();
                int faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));

                loreFavorite_IV = (ImageView) v.findViewById(R.id.lore_favorites_image_view);

                //SET FAVED OR UNFAVED
                String convertedTitle = new TitleRenamer().convertTitle(faveTitle);
                if (faved == 0){
                    loreFavorite_IV.setImageResource(R.drawable.ic_star_border_white_48dp);
                    Toast.makeText(mContext, convertedTitle + " removed from Favorites.", Toast.LENGTH_SHORT).show();
                }
                if (faved == 1){
                    loreFavorite_IV.setImageResource(R.drawable.ic_star_white_48dp);
                    Toast.makeText(mContext, convertedTitle + " set to Favorites.", Toast.LENGTH_SHORT).show();
                }

                //FIND OUT WHICH FRAGMENT CALLED THE ADAPTER AND REFRESH THE CURSOR
                if (mFragment.getClass() == AlphabeticalFragment.class) {
                    AlphabeticalFragment af = (AlphabeticalFragment) mFragment;
                    af.refreshCursor();
                }
//                DO NOT CALL REFRESH FOR THE FAVORITES FRAGMENT. USERS MAY MISCLICK OR RESELECT
//                if (mFragment.getClass() == FavoritesFragment.class) {
//                    FavoritesFragment ff = (FavoritesFragment) mFragment;
//                    ff.refreshCursor();
//                }
//                WITH FTS, IT IS NOW IMPOSSIBLE TO FAVE A LORE FROM THE SEARCH TAB
//                if (mFragment.getClass() == SearchFragment.class) {
//                    SearchFragment sf = (SearchFragment) mFragment;
//                    sf.refreshCursor();
//                }

                cursor.close();
                break;
        }
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
        notifyDataSetChanged();
    }
}
