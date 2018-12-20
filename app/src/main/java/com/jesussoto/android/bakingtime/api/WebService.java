package com.jesussoto.android.bakingtime.api;

import com.jesussoto.android.bakingtime.db.entity.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface WebService {

    String PATH_RECIPES = "/topher/2017/May/59121517_baking/baking.json";

    @GET(PATH_RECIPES)
    Single<List<Recipe>> getRecipes();

}
