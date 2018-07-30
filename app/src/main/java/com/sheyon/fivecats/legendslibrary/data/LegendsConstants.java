package com.sheyon.fivecats.legendslibrary.data;

import android.provider.BaseColumns;

public final class LegendsConstants
{
    //Version 7 = App Version 1.7
    public static final int DATABASE_VERSION = 7;
    public static final String DB_EN = "lore_library.db";
    public static final String DB_DE = "lore_library_DE.db";
    public static final String DB_FR = "lore_library_FR.db";

    private LegendsConstants() {
        //do nothing
    }

    public static final class LoreLibrary implements BaseColumns
    {
        public static final String LORE_TABLE_NAME = "lore";
        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String SUBCAT_TABLE_NAME = "subcat";
        public static final String IMAGE_TABLE_NAME = "image";

        //THE STRING NAMES ARE CASE-SENSITIVE. DOUBLE-CHECK WITH THE SQLITE .DB FILE!
        public static final String _ID = "_id";

        public static final String BASE_LORE_ID = "lore._id";
        public static final String BASE_CATEGORY_ID = "category.categoryId";
        public static final String BASE_SUBCAT_ID = "subcat.subcatId";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PREFIX = "prefix";
        public static final String COLUMN_CATEGORY_NAME = "categoryName";
        public static final String COLUMN_SUBCAT_NAME = "subcatName";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_SUBCAT_ID = "subcatId";
        public static final String COLUMN_BUZZING = "legend";
        public static final String COLUMN_BLACK_SIGNAL = "blackLore";
        public static final String COLUMN_FAVED = "faved";
        public static final String COLUMN_IMAGE = "resourceLink";
    }

    //VALUES FOR SPINNER SELECTION
    public static final class Categories {
        public static final int CAT_0 = 0;
        public static final int CAT_1_SOL = 1;
        public static final int CAT_2_EGY = 2;
        public static final int CAT_3_TRN = 3;
        public static final int CAT_4_TOK = 4;
        public static final int CAT_5_GBL = 5;
        public static final int CAT_6_BES = 6;
        public static final int CAT_7_EVN = 7;
        public static final int CAT_8_ISU = 8;
        public static final int CAT_9_AFR = 9;
    }

    //VALUES FOR LANGUAGE SPINNER
    public static final class Languages {
        public static final int LANG_EN = 0;
        public static final int LANG_DE = 1;
        public static final int LANG_FR = 2;
    }

    public static final class Queries {

        //THIS REPLACES UNION 1+2 to return Uncategorized Lore and Unique Subcats to populate the Expandable View
        public static final String THIS_FUCKIN_QUERY = "select * from\n" +
                "( select lore._id AS _id, lore.categoryId, title, prefix, subcatName, lore.subcatId AS subcatId\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId IS NOT NULL and lore.categoryId = ?\n" +
                "group by lore.subcatId\n" +
                "UNION\n" +
                "select lore._id AS _id, lore.categoryId, title, prefix, subcatName, lore.subcatId AS subcatId\n" +
                "from lore\n" +
                "left outer join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId IS NULL AND lore.categoryId = ?\n" +
                "group by title )\n" +
                "order by subcatId is null, subcatId asc, title\n" +
                "collate nocase";

        //UNION 1 and UNION 2 returns Uncategorized Lore and Unique Subcats to populate the Expandable View
//        public static final String UNION_1 = "select lore._id AS _id, lore.categoryId, title, prefix, subcatName, lore.subcatId AS subcatId\n" +
//                "from lore\n" +
//                "left outer join subcat\n" +
//                "on lore.subcatId = subcat.subcatId\n" +
//                "where lore.subcatId IS NOT NULL and lore.categoryId = ?\n" +
//                "group by lore.subcatId";
//
//        public static final String UNION_2 = "select lore._id AS _id, lore.categoryId, title, prefix, subcatName, lore.subcatId AS subcatId\n" +
//                "from lore\n" +
//                "left outer join subcat\n" +
//                "on lore.subcatId = subcat.subcatId\n" +
//                "where lore.subcatId IS NULL AND lore.categoryId = ?\n" +
//                "order by lore.categoryId, title;";

        //Returns all other lore
        public static final String LORES = "select lore._id as _id, title, prefix, lore.categoryId, subcatName, lore.subcatId\n" +
                "from lore\n" +
                "join subcat\n" +
                "on lore.subcatId = subcat.subcatId\n" +
                "where lore.subcatId = ?\n" +
                "order by title\n" +
                "collate nocase";

        //Returns CatID given a Title and a Category Name Part 1
        public static final String GET_CAT_ID_UNION_1 = "select lore._id AS _id, title, lore.categoryId, category.categoryName\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore.prefix || lore.title like ? and category.categoryName like ?";

        //Returns CatID given a Title and a Category Name Part 2
        public static final String GET_CAT_ID_UNION_2 = "select lore._id AS _id, title, lore.categoryId, category.categoryName\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore.title like ? and category.categoryName like ?";

        //Returns info for the LoreActivity Part 1
        public static final String SINGLE_LORE_UNION_1 = "select lore._id AS _id, title, prefix, lore.categoryId, category.categoryName, legend, blackLore, faved\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore.categoryId = ? and title LIKE ?";

        //Returns info for the LoreActivity Part 2
        public static final String SINGLE_LORE_UNION_2 = "select lore._id AS _id, title, prefix, lore.categoryId, category.categoryName, legend, blackLore, faved\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore.categoryId = ? and prefix || title LIKE ?";

        //Returns dropdown results for the Search ExpandableViewList
        public static final String SEARCH_CHILD_TABLE = "select lore._id AS _id, category.categoryName, lore.Title, lore.legend, lore.blackLore\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where lore._id = ?";

        public static final String ALPHABETICAL = "select lore._id AS _id, lore.title, lore.prefix, lore.categoryId, category.categoryName, lore.faved\n" +
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

        public static final String GET_ALL_FAVES = "select lore._id AS _id, title, prefix, lore.categoryId, category.categoryName, faved\n" +
                "from lore\n" +
                "join category\n" +
                "on lore.categoryId = category.categoryId\n" +
                "where faved = 1\n" +
                "order by title asc";

        public static final String CHECK_FOR_FAVED_LORE = "select title from lore\n" +
                "where faved = 1";

        //CREATE TABLE WITH DEFAULT COLUMNS
        static final String CREATE_DEFAULT_TABLE = "CREATE VIRTUAL TABLE LoreSearch USING fts4 (_id, prefix, title, legend, blackLore, categoryName, faved);";

        //CREATE TABLE NORMALIZED COLUMNS
        static final String CREATE_ASCII_TABLE = "CREATE VIRTUAL TABLE LoreSearch USING fts4 (_id, prefix, title, ASCII_title, legend, ASCII_legend, blackLore, ASCII_blackLore, categoryName, faved);";

        //BUILD THE VIRTUAL TABLE FOR FTS
        static final String POPULATE_VIRTUAL_TABLE = "INSERT INTO LoreSearch\n" +
                "SELECT _id, prefix, title, legend, blackLore, category.categoryName, faved\n" +
                "FROM lore\n" +
                "JOIN category\n" +
                "on lore.categoryId = category.CategoryID;";

        //BUILD THE VIRTUAL TABLE FOR FTS (OMITS DIACRITICS)
        static final String POPULATE_VIRTUAL_TABLE_FR_DE_NORMALIZED = "INSERT INTO LoreSearch\n" +
                "SELECT _id, prefix, title, ASCII_title, legend, ASCII_legend, blackLore, ASCII_blacklore, category.categoryName, faved\n" +
                "FROM lore\n" +
                "JOIN category\n" +
                "on lore.categoryId = category.CategoryID;";

        //FOR QUERYING THE DATABASE
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

        //FOR REPLACING THE DEFAULT IMAGE ON THE LORE PAGE
        public static final String GET_IMAGE = "select * from "+ LoreLibrary.IMAGE_TABLE_NAME +
                " where title = ?";

        //NULL CATCH FOR LORE ACTIVITY (ON CREATE)
        public static final String CAT_ID_CATCH =
                "select lore._id AS _id, lore.categoryId AS categoryId, lore.title\n" +
                "from lore\n" +
                "where title LIKE ?\n" +
                "UNION\n" +
                "select lore._id AS _id, lore.categoryId AS categoryId, lore.title\n" +
                "from lore\n" +
                "where prefix || title LIKE ?";
    }
}