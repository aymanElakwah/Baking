package com.akwah.baking.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.akwah.baking.details.ingredients.IngredientsActivity;
import com.akwah.baking.details.ingredients.IngredientsFragment;
import com.akwah.baking.details.step_details.StepDetailsActivity;
import com.akwah.baking.details.step_details.StepDetailsFragment;

public class RecipeActivity extends AppCompatActivity implements StepsFragment.OnListFragmentInteractionListener {

    public static final String RECIPE_KEY = "recipe_key";
    private boolean splitScreen;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        splitScreen = findViewById(R.id.split_screen) != null;
        mRecipe = getIntent().getParcelableExtra(RECIPE_KEY);
        if (mRecipe != null) {
            if (savedInstanceState == null) {
                Fragment stepsFragment = StepsFragment.newInstance(mRecipe.getSteps());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_steps, stepsFragment).commit();
            }
            setTitle(mRecipe.getName());
        }
    }

    @Override
    public void onIngredientClick() {
        if (splitScreen) {
            Fragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe.getIngredients());
            getSupportFragmentManager().beginTransaction().replace(R.id.right_side, ingredientsFragment).commit();
        } else {
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(IngredientsActivity.RECIPE_KEY, mRecipe);
            startActivity(intent);
        }
    }

    @Override
    public void onStepClick(int number) {
        if (splitScreen) {
            Fragment stepDetailsFragment = StepDetailsFragment.newInstance(mRecipe.getSteps().get(number));
            getSupportFragmentManager().beginTransaction().replace(R.id.right_side, stepDetailsFragment).commit();
        } else {
            Intent intent = StepDetailsActivity.newInstance(this, mRecipe, number);
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Recipe recipe = intent.getParcelableExtra(RECIPE_KEY);
        if(recipe == null)
            return;
        setIntent(intent);
        mRecipe = recipe;
        if (mRecipe != null) {
            Fragment stepsFragment = StepsFragment.newInstance(mRecipe.getSteps());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_steps, stepsFragment).commit();
            setTitle(mRecipe.getName());
        }
    }
}
