package com.jesussoto.android.bakingtime.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.jesussoto.android.bakingtime.db.dao.BakingStepDao;
import com.jesussoto.android.bakingtime.db.dao.IngredientDao;
import com.jesussoto.android.bakingtime.db.dao.RecipeDao;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;
import com.jesussoto.android.bakingtime.db.entity.Recipe;

@Database(
    entities = {
        Recipe.class,
        Ingredient.class,
        BakingStep.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "baking.db";

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract BakingStepDao bakingStepDao();
}
