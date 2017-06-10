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
        public static final String BASE_CATEGORY_ID = "category.categoryId";
        public static final String BASE_SUBCAT_ID = "subcat.subcatId";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORY_NAME = "categoryName";
        public static final String COLUMN_SUBCAT_NAME = "subcatName";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_SUBCAT_ID = "subcatId";
        public static final String COLUMN_BUZZING = "legend";
        public static final String COLUMN_BLACK_SIGNAL = "blackLore";
        public static final String COLUMN_FAVED = "faved";

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

        //VALUES FOR LANGUAGE SPINNER
        public static final int LANG_ENG = 0;
        public static final int LANG_GMN = 1;
        public static final int LANG_FRN = 2;
    }

    public static final class Queries {
        //UNION 1 and UNION 2 returns Uncategorized Lore and Unique Subcats to populate the Expandable View
        public static final String UNION_1 = "select lore._id AS _id, lore.categoryId, title, subcatName, lore.subcatId AS subcatId\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId IS NOT NULL and lore.categoryId = ?\n" +
                "group by lore.subcatId";

        public static final String UNION_2 = "select lore._id AS _id, lore.categoryId, title, subcatName, lore.subcatId AS subcatId\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId IS NULL AND lore.categoryId = ?\n" +
                "order by lore.categoryId;";

        //Returns all other lore
        public static final String LORES = "select lore._id as _id, title, lore.categoryId, subcatName, lore.subcatId\n" +
                "from lore\n" +
                "join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId = ?\n" +
                "order by lore.subcatId";

        //Returns CatID given a Title and a Category Name
        public static final String GET_CAT_ID = "select lore._id AS _id, title, lore.categoryId, category.categoryName\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where title like ? and category.categoryName like ?";

        //Returns info for the LoreActivity
        public static final String SINGLE_LORE = "select lore._id AS _id, title, lore.categoryId, legend, blackLore, faved\n" +
                "from lore\n" +
                "where lore.categoryId = ? and title LIKE ? ";

//        Returns results from the SearchView
//        public static final String SEARCH = "select lore._id AS _id, lore.categoryId, category.categoryName, lore.title, lore.legend, lore.blackLore, lore.faved\n" +
//                "from lore\n" +
//                "join category\n" +
//                "on lore.categoryId = category.categoryId\n" +
//                "where (lore.title like ?) or (lore.legend like ?) or (lore.blackLore like ?)";

        //Returns dropdown results for the Search ExpandableViewList
        public static final String SEARCH_CHILD_TABLE = "select lore._id AS _id, category.categoryName, lore.Title, lore.legend, lore.blackLore\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore._id = ?";

        public static final String ALPHABETICAL = "select lore._id AS _id, lore.title, lore.categoryId, category.categoryName, lore.faved\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "order by title\n" +
                "COLLATE NOCASE";

        public static final String UPDATE_FAVE = "UPDATE lore\n" +
                "SET faved = CASE\n" +
                "WHEN faved = 0 THEN 1\n" +
                "WHEN faved = 1 THEN 0\n" +
                "ELSE 0 END\n" +
                "WHERE title = ";

        public static final String GET_FAVE = "select lore._id AS _id, title, faved\n" +
                "from lore\n" +
                "where title = ?";

        public static final String GET_ALL_FAVES = "select lore._id AS _id, title, lore.categoryId, category.categoryName, faved\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where faved = 1\n" +
                "order by title asc";

        public static final String CHECK_FOR_FAVED_LORE = "select title from lore\n" +
                "where faved = 1";

        static final String POPULATE_VIRTUAL_TABLE_ENGLISH = "INSERT INTO LoreSearch\n" +
                "SELECT _id, title, legend, blackLore, category.categoryName, faved\n" +
                "FROM lore\n" +
                "JOIN category\n" +
                "on lore.categoryId = category.CategoryID;";

        //FOR BUILDING THE FTS IN THE PRIMARY LANGUAGE OF THE USER
        static final String POPULATE_VIRTUAL_TABLE_FR_DE_NATIVE = "INSERT INTO LoreSearch\n" +
                "SELECT _id, prefix, title, legend, blackLore, category.categoryName, faved\n" +
                "FROM lore\n" +
                "JOIN category\n" +
                "on lore.categoryId = category.CategoryID;";

        //FOR BUILDING THE FTS IN THE NON-PRIMARY LANGUAGE OF THE USER (OMITS DIACRITICS)
        static final String POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED = "INSERT INTO LoreSearch\n" +
                "SELECT _id, prefix, title, ASCII_title, legend, ASCII_legend, blackLore, ASCII_blacklore, category.categoryName, faved\n" +
                "FROM lore\n" +
                "JOIN category\n" +
                "on lore.categoryId = category.CategoryID;";

        //FOR QUERYING THE DATABASE IN THE LANGUAGE OF THE USER
        public static final String QUERY_FTS = "SELECT * FROM LoreSearch\n" +
                "WHERE title MATCH ?\n" +
                "UNION\n" +
                "SELECT * FROM LoreSearch\n" +
                "WHERE legend MATCH ?\n" +
                "UNION\n" +
                "SELECT * FROM LoreSearch\n" +
                "WHERE blackLore MATCH ?";

        //FOR QUERYING THE DATABASE WITHOUT DIACRITICS
        public static final String QUERY_FTS_NORMALIZED = "SELECT * FROM LoreSearch\n" +
                "WHERE ASCII_title MATCH ?\n" +
                "UNION\n" +
                "SELECT * FROM LoreSearch\n" +
                "WHERE ASCII_legend MATCH ?\n" +
                "UNION\n" +
                "SELECT * FROM LoreSearch\n" +
                "WHERE ASCII_blacklore MATCH ?";
    }
}