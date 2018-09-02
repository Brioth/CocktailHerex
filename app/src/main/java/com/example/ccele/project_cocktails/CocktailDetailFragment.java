package com.example.ccele.project_cocktails;

import android.app.Activity;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ccele.project_cocktails.models.Cocktail;
import com.example.ccele.project_cocktails.models.CocktailApiResponse;
import com.example.ccele.project_cocktails.repositories.CocktailApiRepository;
import com.example.ccele.project_cocktails.services.FavoritesIntentService;
import com.example.ccele.project_cocktails.util.ServerCallback;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a single Cocktail detail screen.
 * This fragment is either contained in a {@link CocktailListActivity}
 * in two-pane mode (on tablets) or a {@link CocktailDetailActivity}
 * on handsets.
 */
public class CocktailDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Cocktail mCocktail;
    private CollapsingToolbarLayout appBarLayout;
    private ImageView thumb_view;
    private TextView instructions_view;
    private LinearLayout ingredients_table;
    private ToggleButton isFavorite_view;
    private Map<String, String> ingredients;
    private List<Cocktail> favoritesList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CocktailDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("Loading");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cocktail_detail, container, false);
        thumb_view = rootView.findViewById(R.id.detail_thumb);
        instructions_view = rootView.findViewById(R.id.detail_instructions);
        ingredients_table = rootView.findViewById(R.id.detail_ingredientsTable);
        isFavorite_view = rootView.findViewById(R.id.detail_isFavorite);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF,0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        isFavorite_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCocktail.setFavorite(isChecked);
                if(isChecked){
                    compoundButton.startAnimation(scaleAnimation);
                    FavoritesIntentService.startServiceAddFavorite(getContext(), mCocktail);
                } else {
                    FavoritesIntentService.startServiceRemoveFavorite(getContext(), mCocktail);
                }
            }
        });

        ingredients = new HashMap<>();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            CocktailApiRepository.getInstance(this.getContext()).GetCocktailById(getArguments().getInt(ARG_ITEM_ID), new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    mCocktail = gson.fromJson(result, CocktailApiResponse.class).getCocktails().get(0);

                    appBarLayout.setTitle(mCocktail.getName());
                    instructions_view.setText(mCocktail.getInstructions());
                    Picasso.get().load(mCocktail.getImageUrl()).into(thumb_view);

                    FavoritesIntentService.startServiceGetFavorites(getContext(), new ResultReceiver(new Handler()){
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            favoritesList = resultData.getParcelableArrayList("cocktailList");
                            mCocktail.setFavorite(favoritesList.contains(mCocktail));
                            isFavorite_view.setChecked(mCocktail.isFavorite());
                        }
                    });

                    getIngredients();
                    buildIngredients();

                }
            });
        }
    }

    private void getIngredients(){
        if(mCocktail.getIngredient1()!=null)
            ingredients.put(mCocktail.getIngredient1(), mCocktail.getMeasure1());

        if(mCocktail.getIngredient2()!=null)
            ingredients.put(mCocktail.getIngredient2(), mCocktail.getMeasure1());

        if(mCocktail.getIngredient3()!=null)
            ingredients.put(mCocktail.getIngredient3(), mCocktail.getMeasure3());

        if(mCocktail.getIngredient4()!=null)
            ingredients.put(mCocktail.getIngredient4(), mCocktail.getMeasure4());

        if(mCocktail.getIngredient5()!=null)
            ingredients.put(mCocktail.getIngredient5(), mCocktail.getMeasure5());

        if(mCocktail.getIngredient6()!=null)
            ingredients.put(mCocktail.getIngredient6(), mCocktail.getMeasure6());
    }

    private void buildIngredients(){
        for (Map.Entry<String, String> ingredient : ingredients.entrySet()) {
            LinearLayout row = new LinearLayout(this.getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);

            TextView ingredientView = new TextView(this.getContext());
            ingredientView.setLayoutParams(params);
            ingredientView.setText(ingredient.getKey());

            TextView measureView = new TextView(this.getContext());
            measureView.setLayoutParams(params);
            measureView.setText(ingredient.getValue());

            row.addView(ingredientView);
            row.addView(measureView);

            ingredients_table.addView(row);

        }
    }


}
