package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
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
        //LORES FROM THE SEARCH VIEW DON'T SYNC PROPERLY BECAUSE OF VIRTUAL TABLES
        if (mFragment.getClass() == SearchFragment.class) {
            loreFavorite_IV.setVisibility(View.INVISIBLE);
        }

        String prefixText = cursor.getString(cursor.getColumnIndex(LoreLibrary.COLUMN_PREFIX));
        String titleText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
        String categoryText = cursor.getString(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_NAME));
        int faved = cursor.getInt(cursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_FAVED));

        //SOMETHING ABOUT THIS BLOCK IS CAUSING LOG SPAM ON A PHYSICAL DEVICE; OTHERWISE HARMLESS
        if (prefixText != null) {
            loreTitle_TV.setText("" + prefixText + titleText);
        }
        else {
            loreTitle_TV.setText(titleText);
        }
        loreCategory_TV.setText(categoryText);

        if (faved == 1) {
            loreFavorite_IV.setImageResource(R.drawable.ic_star_white_48dp);
        }
    }

    @Override
    public void onClick(View v) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] union = { Queries.GET_CAT_ID_UNION_1, Queries.GET_CAT_ID_UNION_2 };
        String joinedQuery = qb.buildUnionQuery(union, null, null);

        switch (v.getId())
        {
            case R.id.container_text_views:
                loreTitle_TV = (TextView) v.findViewById(R.id.lore_title_text_view);
                loreCategory_TV = (TextView) v.findViewById(R.id.lore_category_text_view);

                String clickedTitle = loreTitle_TV.getText().toString();
                String clickedCategory = loreCategory_TV.getText().toString();

                String[] catIdArgs = { clickedTitle, clickedCategory, clickedTitle, clickedCategory };
                Cursor catIdCursor = legendsDB.rawQuery(joinedQuery, catIdArgs);

                catIdCursor.moveToFirst();
                int clickedCatId = catIdCursor.getInt(catIdCursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_ID));
                catIdCursor.close();

                Intent intent = new Intent(mContext, LoreActivity.class);
                intent.putExtra("catNumber", clickedCatId);
                intent.putExtra("loreTitle", clickedTitle);
                intent.putExtra("searchString", mSearchString);

                mContext.startActivity(intent);
                break;

            case R.id.container_fave_clickable:
                //IF ON THE SEARCH PAGE, PREVENT THE USER FROM CONTINUING
                if (mFragment.getClass() == SearchFragment.class) {
                    break;
                }
                //GET PARENT VIEW TO CATCH THE TITLE STRING
                View parentView = (View) v.getParent();
                loreTitle_TV = (TextView) parentView.findViewById(R.id.lore_title_text_view);
                loreCategory_TV = (TextView) parentView.findViewById(R.id.lore_category_text_view);

                String faveTitle = loreTitle_TV.getText().toString();
                String faveCategory = loreCategory_TV.getText().toString();

                //FULL TITLE STRING IS USED FOR FAVE TOASTS; SAVE SO YOU CAN USE IT LATER
                String fullTitleString = faveTitle;

                //REMOVE THE PREFIX FROM THE TITLE STRING
                String[] plainTitleArgs = { faveTitle, faveCategory, faveTitle, faveCategory };
                Cursor plainTitleCursor = legendsDB.rawQuery(joinedQuery, plainTitleArgs);
                if (plainTitleCursor != null) {
                    plainTitleCursor.moveToFirst();
                    faveTitle = plainTitleCursor.getString(plainTitleCursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
                    plainTitleCursor.close();
                }

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
                if (faved == 0){
                    loreFavorite_IV.setImageResource(R.drawable.ic_star_border_white_48dp);
                    Toast.makeText(mContext, fullTitleString + " " + mContext.getString(R.string.fave_removed), Toast.LENGTH_SHORT).show();
                }
                if (faved == 1){
                    loreFavorite_IV.setImageResource(R.drawable.ic_star_white_48dp);
                    Toast.makeText(mContext, fullTitleString + " " + mContext.getString(R.string.fave_added), Toast.LENGTH_SHORT).show();
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
