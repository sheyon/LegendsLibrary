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
        public static final String _ID = "_id";

        public static final String BASE_LORE_ID = "lore._id";
        public static final String BASE_CATEGORY_ID = "category._id";
        public static final String BASE_SUBCAT_ID = "subcat._id";

        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_CATEGORY_NAME = "CategoryName";
        public static final String COLUMN_SUBCAT_NAME = "SubCatName";
        public static final String COLUMN_CATEGORY_ID = "CategoryID";
        public static final String COLUMN_SUBCAT_ID = "SubCatID";
        public static final String COLUMN_BUZZING = "Legend";
        public static final String COLUMN_BLACK_SIGNAL = "BlackLore";

        //VALUES FOR SPINNER SELECTION
        public static final int CAT_0 = 0;
        public static final int CAT_1_SOL = 1;
        public static final int CAT_2_EGY = 2;
        public static final int CAT_3_TRN = 3;
        public static final int CAT_4_TOK = 4;
        public static final int CAT_5_GBL = 5;
        public static final int CAT_6_BES = 6;
        public static final int CAT_7_EVN = 7;
        public static final int CAT_8_ISU = 8;
    }

    public static final class Queries
    {
        //UNION 1 and UNION 2 returns Uncategorized Lore and Unique Subcats to populate the Expandable View
        public static final String UNION_1 = "select lore._id AS _id, lore.CategoryID, Title, SubCatName, lore.SubCatID AS SubCatID\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.SubCatID = subcat._id\n" +
                "where lore.SubCatID IS NOT NULL and lore.CategoryID = ?\n" +
                "group by lore.SubCatID";

        public static final String UNION_2 = "select lore._id AS _id, lore.CategoryID, Title, SubCatName, lore.SubCatID AS SubCatID\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.SubCatID = subcat._id\n" +
                "where lore.SubCatID IS NULL AND lore.CategoryID = ?\n" +
                "order by lore.CategoryID;";

        //Returns all other lore
        public static final String LORES = "select lore._id as _id, Title, lore.CategoryID, SubCatName, lore.SubCatID\n" +
                "from lore\n" +
                "join subcat\n" +
                "on lore.SubCatID = subcat._id\n" +
                "where lore.SubCatID = ?\n" +
                "order by lore.SubCatID";

        //Returns info for the LoreActivity
        public static final String SINGLE_LORE = "select lore._id AS _id, Title, lore.CategoryID, Legend, BlackLore\n" +
                "from lore\n" +
                "where lore.CategoryID = ? and Title LIKE ? ";

        //Returns results from the SearchView
        public static final String SEARCH = "select lore._id AS _id, lore.CategoryID, category.CategoryName, lore.Title, lore.Legend, lore.BlackLore\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.CategoryID = category._id\n" +
                "where (lore.Title like ?) or (lore.Legend like ?) or (lore.BlackLore like ?)";

        //Returns dropdown results for the Search ExpandableViewList
        public static final String SEARCH_CHILD_TABLE = "select lore._id AS _id, category.CategoryName, lore.Title, lore.Legend, lore.BlackLore\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.CategoryID = category._id\n" +
                "where lore._id = ?";
    }

}
