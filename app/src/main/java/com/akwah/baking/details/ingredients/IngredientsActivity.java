package com.akwah.baking.details.ingredients;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.akwah.baking.details.step_details.StepDetailsActivity;
import com.akwah.baking.details.step_details.StepDetailsFragment;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        mRecipe = getIntent().getParcelableExtra(RECIPE_KEY);
        ArrayList<Recipe.Ingredient> ingredients = mRecipe.getIngredients();
        if (ingredients != null) {
            if(savedInstanceState == null) {
                Fragment ingredientsFragment = IngredientsFragment.newInstance(ingredients);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_ingredients, ingredientsFragment).commit();
            }
        }
    }

    public void showSteps(View view) {
        startActivity(StepDetailsActivity.newInstance(this, mRecipe, 0));
        finish();
    }
}
