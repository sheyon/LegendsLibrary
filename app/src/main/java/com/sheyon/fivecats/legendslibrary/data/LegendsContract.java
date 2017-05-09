package com.sheyon.fivecats.legendslibrary.data;

import android.provider.BaseColumns;

import static android.R.attr.category;

public final class LegendsContract
{
    private LegendsContract()
    {
        //do nothing
    }

    public static final class LoreLibrary implements BaseColumns
    {
        public static final String LORE_TABLE_NAME = "lore";
        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String SUBCAT_TABLE_NAME = "subcat";

        //THE STRING NAMES ARE CASE-SENSITIVE. DOUBLE-CHECK WITH THE SQLITE .DB FILE!
        public static final String _ID = "_id";

        public static final String BASE_LORE_ID = "lore._id";
        public static final String BASE_CATEGORY_ID = "category._id";
        public static final String BASE_SUBCAT_ID = "subcat._id";

        public static final String LORE_TITLE = "Title";
        public static final String CATEGORY_NAME = "CategoryName";
        public static final String SUBCAT_NAME = "SubCatName";
        public static final String CATEGORY_ID = "CategoryID";
        public static final String SUBCAT_ID = "SubCatID";
    }

    //Returns ALL Lore + their Categories
    public static final String MY_QUERY = "SELECT " + LoreLibrary.BASE_LORE_ID + " AS " + LoreLibrary._ID + " , " + LoreLibrary.LORE_TITLE + " , " + LoreLibrary.CATEGORY_NAME +
            " FROM " + LoreLibrary.LORE_TABLE_NAME +
            " INNER JOIN " + LoreLibrary.CATEGORY_TABLE_NAME +
            " ON " + LoreLibrary.LORE_TABLE_NAME + "." + LoreLibrary.CATEGORY_ID +
            " = " + LoreLibrary.CATEGORY_TABLE_NAME + "." + LoreLibrary._ID;

    //Returns Categories + Subcategories for Expandable List View
    public static final String CAT_SUBCAT_QUERY = "SELECT " + LoreLibrary.BASE_CATEGORY_ID + " AS " + LoreLibrary._ID + " , " + LoreLibrary.CATEGORY_NAME + " , " + LoreLibrary.SUBCAT_NAME +
            " FROM " + LoreLibrary.CATEGORY_TABLE_NAME +
            " INNER JOIN " + LoreLibrary.SUBCAT_TABLE_NAME +
            " ON " + LoreLibrary.CATEGORY_TABLE_NAME + "." + LoreLibrary._ID +
            " = " + LoreLibrary.SUBCAT_TABLE_NAME + "." + LoreLibrary.CATEGORY_ID;

    //Returns all Categories
    public static final String CAT_QUERY = "SELECT " + LoreLibrary.BASE_CATEGORY_ID + " AS " + LoreLibrary._ID + " , " + LoreLibrary.CATEGORY_NAME +
            " FROM " + LoreLibrary.CATEGORY_TABLE_NAME;
}
