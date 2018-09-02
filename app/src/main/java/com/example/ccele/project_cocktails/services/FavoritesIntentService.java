package com.example.ccele.project_cocktails.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.ccele.project_cocktails.models.Cocktail;
import com.example.ccele.project_cocktails.repositories.FavoriteCocktailDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_RESULT_RECEIVER;

public class FavoritesIntentService extends IntentService {
    private static final String TAG = "FavoritesIntentService";
    private static List<Cocktail> favoriteCocktails;

    private static final String ACTION_ADD = "ADD";
    private static final String ACTION_REMOVE = "REMOVE";
    private static final String ACTION_GET = "GET";

    private static final String EXTRA_COCKTAIL = "COCKTAIL";
    private static final String EXTRA_RESULT_RECEIVER = "RESULT_RECEIVER";

    public FavoritesIntentService() {
        super("FavoritesIntentService");
    }

    public static void startServiceAddFavorite(Context context, Cocktail cocktail){
        Intent intent = new Intent(context, FavoritesIntentService.class);
        intent.setAction(ACTION_ADD);
        intent.putExtra(EXTRA_COCKTAIL, cocktail);
        context.startService(intent);
    }

    public static void startServiceRemoveFavorite(Context context, Cocktail cocktail){
        Intent intent = new Intent(context, FavoritesIntentService.class);
        intent.setAction(ACTION_REMOVE);
        intent.putExtra(EXTRA_COCKTAIL, cocktail);
        context.startService(intent);
    }
    public static void startServiceGetFavorites(Context context, ResultReceiver resultReceiver){
        Intent intent = new Intent(context, FavoritesIntentService.class);
        intent.setAction(ACTION_GET);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        favoriteCocktails = FavoriteCocktailDbHelper.getInstance(getApplicationContext()).getAllFavoriteCocktails();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FavoriteCocktailDbHelper.getInstance(getApplicationContext()).saveAllFavoriteCocktails(favoriteCocktails);
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(ACTION_ADD)) {
                final Cocktail cocktailParam = (Cocktail) intent.getParcelableExtra(EXTRA_COCKTAIL);
                handleActionAdd(cocktailParam);
            } else if (action.equals(ACTION_REMOVE)) {
                final Cocktail cocktailParam = (Cocktail) intent.getParcelableExtra(EXTRA_COCKTAIL);
                handleActionRemove(cocktailParam);
            } else if (action.equals(ACTION_GET)) {
                final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
                handleActionGet(resultReceiver);
            }
        }
    }

    private void handleActionAdd(Cocktail cocktail) {
        favoriteCocktails.add(cocktail);
        Log.d(TAG, "added to favoritesService: " + cocktail.getName());
    }

    private void handleActionRemove(Cocktail cocktail) {
        favoriteCocktails.remove(cocktail);
        Log.d(TAG, "removed from favoritesService: " + cocktail.getName());;
    }

    private void handleActionGet(ResultReceiver resultReceiver){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("cocktailList",(ArrayList)favoriteCocktails);
        if(resultReceiver != null){
            resultReceiver.send(0, bundle);
        }
    }

}
