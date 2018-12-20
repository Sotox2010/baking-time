package com.jesussoto.android.bakingtime.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jesussoto.android.bakingtime.db.entity.Recipe;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    LiveData<Recipe> getById(long recipeId);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    Maybe<Recipe> getByIdAsMaybe(long recipeId);

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();

    @Insert
    long insert(Recipe recipe);

    @Insert
    List<Long> insert(List<Recipe> recipes);

    @Delete
    void delete(Recipe recipe);

    @Query("DELETE FROM recipe;")
    void clearTable();
}
