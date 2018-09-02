package com.example.ccele.project_cocktails.repositories;

import android.provider.BaseColumns;

import com.android.volley.toolbox.StringRequest;


/**
 * Created by ccele on 8/5/2018.
 */

public final class FavoriteCocktailContract {

    private FavoriteCocktailContract() {};

    public static class CocktailEntry implements BaseColumns {
        public static final String TABLE_NAME = "favoriteCocktails";

        public static final String COLUMN_NAME_COCKTAIL_ID = "cocktail_id";
        public static final String COLUMN_NAME_COCKTAIL_NAME = "name";
        public static final String COLUMN_NAME_IMAGE = "thumb";
    }
}
