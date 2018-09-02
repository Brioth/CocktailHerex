package com.example.ccele.project_cocktails.repositories;

import com.example.ccele.project_cocktails.models.Cocktail;
import com.example.ccele.project_cocktails.util.ServerCallback;

import java.util.List;

/**
 * Created by ccele on 8/4/2018.
 */

public interface ICocktailRepository {
    void GetAllCocktails(ServerCallback callback);
    void GetCocktailById(int id, ServerCallback callback);
}
