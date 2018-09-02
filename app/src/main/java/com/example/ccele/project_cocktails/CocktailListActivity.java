package com.example.ccele.project_cocktails;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.example.ccele.project_cocktails.adapter.CocktailAdapter;
import com.example.ccele.project_cocktails.models.Cocktail;
import com.example.ccele.project_cocktails.models.CocktailApiResponse;
import com.example.ccele.project_cocktails.repositories.CocktailApiRepository;
import com.example.ccele.project_cocktails.services.FavoritesIntentService;
import com.example.ccele.project_cocktails.util.ServerCallback;
import com.google.gson.Gson;

import java.util.List;

/**
 * An activity representing a list of Cocktails. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CocktailDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CocktailListActivity extends AppCompatActivity implements CocktailAdapter.CocktailAdapterOnClickHandler {

    private final String LIST_STATE_KEY = "recycler_list_state";
    private static Bundle mBundleRecyclerViewState;

    private RecyclerView mRecyclerView;
    private CocktailAdapter mAdapter;

    private boolean mTwoPane;
    private boolean showFavorites;

    private MenuItem showFavoritesMenu;
    private MenuItem settingsMenu;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("Theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cocktail_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.cocktail_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerView = findViewById(R.id.cocktail_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CocktailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        setCocktails();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mBundleRecyclerViewState!=null){
            Parcelable listState = mBundleRecyclerViewState.getParcelable(LIST_STATE_KEY);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

   @Override
    public void onClick(Cocktail cocktail) {
        // Cocktail cocktail = (Cocktail) view.getTag();
        if(mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(CocktailDetailFragment.ARG_ITEM_ID, cocktail.getId());
            CocktailDetailFragment fragment = new CocktailDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cocktail_detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, CocktailDetailActivity.class);
            intent.putExtra(CocktailDetailFragment.ARG_ITEM_ID, cocktail.getId());

            context.startActivity(intent);
        }
    }

    // --- Menu handling -- //
    /* Menu events in onCreate Because of searchManager */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cocktail_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        showFavoritesMenu = menu.findItem(R.id.action_favorite);
        showFavoritesMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                toggleSearchFavorite();
                return false;
            }
        });

        settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, SettingsActivity.class);

                context.startActivity(intent);
                return false;
            };
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void toggleSearchFavorite(){
        showFavorites = !showFavorites;
        setCocktails();
        setIcons();
    }

    private void setIcons(){
        if(showFavorites){
            showFavoritesMenu.setIcon(R.drawable.ic_favorite);
        } else {
            showFavoritesMenu.setIcon(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    // -- Cocktail Setters --
    private void setCocktails(){
        if(showFavorites){
            getFavoriteCocktails();
        } else {
            getAllCocktails();
        }
    }

    private void getFavoriteCocktails() {
                FavoritesIntentService.startServiceGetFavorites(this, new ResultReceiver(new Handler()){
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        List<Cocktail> favoriteCocktailList = resultData.getParcelableArrayList("cocktailList");
                        UpdateAdapterList(favoriteCocktailList);
                    }
                });

    }

    private void getAllCocktails() {
        CocktailApiRepository.getInstance(this).GetAllCocktails(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CocktailApiResponse responseObj = gson.fromJson(result, CocktailApiResponse.class);
                List<Cocktail> newCocktailList  = responseObj.getCocktails();

                UpdateAdapterList(newCocktailList);
            }
        });
    }

    private void UpdateAdapterList(List<Cocktail> newCocktailList) {
        mAdapter.setCocktailData(newCocktailList);
    }


}
