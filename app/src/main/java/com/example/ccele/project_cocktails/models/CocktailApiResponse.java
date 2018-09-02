package com.example.ccele.project_cocktails.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ccele on 8/4/2018.
 *
 * this class is needed for accessing thecocktaildb-api.
 * It returns an object drinks with an array of objects (cocktails)
 */

public class CocktailApiResponse {
    @SerializedName("drinks")
    private List<Cocktail> cocktails;

    public CocktailApiResponse() {}

    public List<Cocktail> getCocktails() {
        return cocktails;
    }

    public void setCocktails(List<Cocktail> cocktails) {
        this.cocktails = cocktails;
    }
}
