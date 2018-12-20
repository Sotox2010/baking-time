package com.jesussoto.android.bakingtime.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jesussoto.android.bakingtime.db.entity.Ingredient;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient WHERE id = :ingredientId")
    LiveData<Ingredient> getById(long ingredientId);

    @Query("SELECT * FROM ingredient WHERE recipeId = :recipeId")
    LiveData<List<Ingredient>> getAllByRecipeId(long recipeId);

    @Query("SELECT * FROM ingredient WHERE recipeId = :recipeId")
    List<Ingredient> blockingGetAllByRecipeId(long recipeId);

    @Query("SELECT * FROM ingredient WHERE recipeId = :recipeId")
    Maybe<List<Ingredient>> getAllByRecipeIdAsMaybe(long recipeId);

    @Insert
    long insert(Ingredient ingredient);

    @Insert
    List<Long> insert(List<Ingredient> ingredients);

    @Delete
    void delete(Ingredient ingredient);

    @Query("DELETE FROM ingredient")
    void clearTable();
}
