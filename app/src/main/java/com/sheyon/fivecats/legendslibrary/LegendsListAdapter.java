package com.sheyon.fivecats.legendslibrary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.sheyon.fivecats.legendslibrary.data.LegendsDatabase;
import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;

class LegendsListAdapter extends CursorAdapter implements View.OnClickListener
{
    private static class ViewHolder {
        private LinearLayout mTextLayout;
        private ImageView mImageLayout;
    }

    private Context mContext;
    private String mSearchString;
    private Fragment mFragment;

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
        return LayoutInflater.from(context).inflate(R.layout.lore_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //SET UP THE CLICKABLE LAYOUTS
        LinearLayout textLayout = view.findViewById(R.id.container_text_views);
        ImageView imageLayout = view.findViewById(R.id.lore_favorites_image_view);

        ViewHolder holder = new ViewHolder();
        holder.mTextLayout = textLayout;
        holder.mImageLayout = imageLayout;

        holder.mTextLayout.setOnClickListener(this);
        holder.mImageLayout.setOnClickListener(this);

        //IF COMING FROM THE SEARCH PAGE, HIDE THE STAR
        //LORES FROM THE SEARCH VIEW DON'T SYNC PROPERLY BECAUSE OF VIRTUAL TABLES
        if (mFragment.getClass() == SearchFragment.class) {
            imageLayout.setVisibility(View.INVISIBLE);
        }

        TextView loreTitle_TV = view.findViewById(R.id.lore_title_text_view);
        TextView loreCategory_TV = view.findViewById(R.id.lore_category_text_view);

        //START UNFAVED
        imageLayout.setImageResource(R.drawable.ic_star_border_white_48dp);

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
            imageLayout.setImageResource(R.drawable.ic_star_white_48dp);
        }
    }

    @Override
    public void onClick(View v) {
        //GET NEW DATABASE VARIABLE IN CASE SETTINGS WERE CHANGED
        SQLiteDatabase mDb = LegendsDatabase.getInstance(mContext);

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] union = { Queries.GET_CAT_ID_UNION_1, Queries.GET_CAT_ID_UNION_2 };
        String joinedQuery = qb.buildUnionQuery(union, null, null);

        TextView loreCategory_TV;

        switch (v.getId())
        {
            case R.id.container_text_views:
                TextView loreTitle_TV = v.findViewById(R.id.lore_title_text_view);
                String clickedTitle = loreTitle_TV.getText().toString();

//                STOP PASSING A CAT NUMBER; THE LORE PAGE WILL NOW FIND IT
//                loreCategory_TV = v.findViewById(R.id.lore_category_text_view);
//                String clickedCategory = loreCategory_TV.getText().toString();
//
//                String[] catIdArgs = { clickedTitle, clickedCategory, clickedTitle, clickedCategory };
//                Cursor catIdCursor = mDb.rawQuery(joinedQuery, catIdArgs);
//
//                catIdCursor.moveToFirst();
//                int clickedCatId = catIdCursor.getInt(catIdCursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_CATEGORY_ID));
//                catIdCursor.close();

                Intent intent = new Intent(mContext, LoreActivity.class);
                //intent.putExtra("catNumber", clickedCatId);
                intent.putExtra("loreTitle", clickedTitle);
                intent.putExtra("searchString", mSearchString);

                //FAILSAFE
                LegendsPreferences.getInstance(mContext).setLoreTitle(clickedTitle);

                mContext.startActivity(intent);

                break;

            case R.id.lore_favorites_image_view:
                //IF ON THE SEARCH PAGE, PREVENT THE USER FROM MAKING A FAVE
                if (mFragment.getClass() == SearchFragment.class) {
                    break;
                }
                //GET PARENT VIEW TO CATCH THE TITLE STRING
                View parentView = (View) v.getParent();
                loreTitle_TV = parentView.findViewById(R.id.lore_title_text_view);
                loreCategory_TV = parentView.findViewById(R.id.lore_category_text_view);

                String faveTitle = loreTitle_TV.getText().toString();
                String faveCategory = loreCategory_TV.getText().toString();

                //FULL TITLE STRING IS USED FOR FAVE TOASTS; SAVE SO YOU CAN USE IT LATER
                String fullTitleString = faveTitle;

                //REMOVE THE PREFIX FROM THE TITLE STRING
                String[] plainTitleArgs = { faveTitle, faveCategory, faveTitle, faveCategory };
                Cursor plainTitleCursor = mDb.rawQuery(joinedQuery, plainTitleArgs);
                if (plainTitleCursor != null) {
                    plainTitleCursor.moveToFirst();
                    faveTitle = plainTitleCursor.getString(plainTitleCursor.getColumnIndexOrThrow(LoreLibrary.COLUMN_TITLE));
                    plainTitleCursor.close();
                }

                String modFaveTitle = "\"" + faveTitle + "\"";

                //EXECUTE UPDATE QUERY
                mDb.execSQL(Queries.UPDATE_FAVE + modFaveTitle + ";");

                //GET UPDATED CURSOR
                String[] selectionArgs = { faveTitle };
                Cursor cursor = mDb.rawQuery(Queries.GET_FAVE, selectionArgs);
                cursor.moveToFirst();
                int faved = cursor.getInt(cursor.getColumnIndex(LoreLibrary.COLUMN_FAVED));

                ImageView loreFavorite_IV = v.findViewById(R.id.lore_favorites_image_view);

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
