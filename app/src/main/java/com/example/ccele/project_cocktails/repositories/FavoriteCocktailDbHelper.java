package com.example.ccele.project_cocktails.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ccele.project_cocktails.models.Cocktail;

import java.util.ArrayList;
import java.util.List;

import static com.example.ccele.project_cocktails.repositories.FavoriteCocktailContract.CocktailEntry.*;

/**
 * Created by ccele on 8/5/2018.
 */

public class FavoriteCocktailDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "FavoriteCDbHelper";
    private static FavoriteCocktailDbHelper mInstance;

    private static final String DATABASE_NAME = "FavoriteCocktail.db";
    private static final int DATABASE_VERSION = 1;

    private FavoriteCocktailDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized FavoriteCocktailDbHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new FavoriteCocktailDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME_COCKTAIL_ID + " INTEGER, "
                        + COLUMN_NAME_COCKTAIL_NAME + " TEXT, "
                        + COLUMN_NAME_IMAGE + " TEXT, "
                        + "UNIQUE(" + COLUMN_NAME_COCKTAIL_ID + "))";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "updated dbversion");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Cocktail> getAllFavoriteCocktails(){
            List<Cocktail> favoriteCocktails = new ArrayList<>();

            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            while (cursor.moveToNext()){
                Cocktail cocktail = new Cocktail();
                cocktail.setId(cursor.getInt((cursor.getColumnIndex(COLUMN_NAME_COCKTAIL_ID))));
                cocktail.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COCKTAIL_NAME)));
                cocktail.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IMAGE)));
                cocktail.setFavorite(true);

                favoriteCocktails.add(cocktail);
            }

            cursor.close();
            db.close();

            return favoriteCocktails;
    }

    public void saveAllFavoriteCocktails(List<Cocktail> cocktails){
        removeAllRowsFromFavoriteCocktailTable();
        for (Cocktail cocktail: cocktails ) {
            insertFavoriteCocktail(cocktail);
        }

    }

    public void removeAllRowsFromFavoriteCocktailTable(){
        Log.d(TAG, "clear data");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public long insertFavoriteCocktail(Cocktail cocktail){
        Log.d(TAG, "insert cocktail: " + cocktail.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_COCKTAIL_ID, cocktail.getId());
        values.put(COLUMN_NAME_COCKTAIL_NAME, cocktail.getName());
        values.put(COLUMN_NAME_IMAGE, cocktail.getImageUrl());

        long id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        db.close();

        return id;
    }

    public void deleteFavoriteCocktail(Cocktail cocktail) {
        Log.d(TAG, "delete cocktail: " + cocktail.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_NAME_COCKTAIL_ID
        + " = ?", new String[]{String.valueOf(cocktail.getId())});

        db.close();
    }
}
