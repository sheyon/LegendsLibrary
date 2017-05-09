package com.sheyon.fivecats.legendslibrary.data;

import android.provider.BaseColumns;

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
        public static final String _ID = "rowid _id";
        public static final String LORE_TITLE = "Title";
        public static final String CATEGORY_ID = "CategoryID";
        public static final String CATEGORY_NAME = "CategoryName";
        public static final String SUBCAT_ID = "SubCatID";
        public static final String SUBCAT_NAME = "SubCatName";

    }
}
