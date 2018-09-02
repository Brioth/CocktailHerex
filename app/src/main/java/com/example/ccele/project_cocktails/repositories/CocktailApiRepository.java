package com.example.ccele.project_cocktails.repositories;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ccele.project_cocktails.util.ServerCallback;

/**
 * Created by ccele on 8/4/2018.
 *
 * - Volley request only accepts url's as strings, not uri-objects.
 * - Callback methods needed because volley works async
 *      and result of request must be processed in calling class.
 * - Repository & requestqueue are Singleton
 * - thecocktaildb doesn't provide a get all drinks, instead we filter by category Cocktail
 */

public class CocktailApiRepository implements ICocktailRepository {
    private static CocktailApiRepository mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private static final String TAG = "CocktailApiRepository";
    private static final String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";

    private CocktailApiRepository(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CocktailApiRepository getInstance(Context context){
        if(mInstance == null){
            mInstance = new CocktailApiRepository(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    public void GetAllCocktails(final ServerCallback callback) {

        String url = BASE_URL + "filter.php?c=Cocktail";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "GetAllCocktails - onErrorResponse: " + error.getMessage());
            }
        });

        getRequestQueue().add(request);
    }

    @Override
    public void GetCocktailById(int id, final ServerCallback callback) {
        String url = BASE_URL + "lookup.php?i=" + id;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "GetCocktailById - onErrorResponse: " + error.getMessage());
            }
        });

        getRequestQueue().add(request);
    }

}
