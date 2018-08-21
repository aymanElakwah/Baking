package com.akwah.baking.details.step_details;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";
    public static final String STEP_NUMBER = "step_number";
    private Button mNext;
    private Button mPrevious;
    private Recipe mRecipe;
    private int mNumber;
    private boolean mLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean land = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            land = true;
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_step_details);
        mNext = (Button) findViewById(R.id.next_button);
        mPrevious = (Button) findViewById(R.id.previous_button);
        if (land) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();
            mPrevious.setVisibility(View.GONE);
            mNext.setVisibility(View.GONE);
        }
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Recipe recipe = getIntent().getParcelableExtra(RECIPE_KEY);
        if(recipe == null)
            return;
        mRecipe = recipe;
        mNumber = getIntent().getIntExtra(STEP_NUMBER, -1);
        if (mRecipe != null) {
            ArrayList<Recipe.Step> steps = mRecipe.getSteps();
            if (mNumber == 0) {
                mPrevious.setEnabled(false);
            } else {
                mPrevious.setEnabled(true);
            }
            if (mNumber == steps.size() - 1) {
                mLast = true;
                mNext.setText(R.string.finish);
            } else {
                mLast = false;
                mNext.setText(R.string.next);
            }
            if (savedInstanceState == null) {
                Fragment stepDetailsFragment = StepDetailsFragment.newInstance(steps.get(mNumber));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_step_details, stepDetailsFragment).commit();
            }
            setTitle(steps.get(mNumber).getShortDescription());
        }
    }

    public void next(View view) {
        if (mLast)
            finish();
        else
            startActivity(StepDetailsActivity.newInstance(this, mRecipe, mNumber + 1));
    }

    public void previous(View view) {
        startActivity(StepDetailsActivity.newInstance(this, mRecipe, mNumber - 1));
    }

    public static Intent newInstance(Context context, Recipe recipe, int number) {
        Intent intent = new Intent(context, StepDetailsActivity.class);
        intent.putExtra(RECIPE_KEY, recipe);
        intent.putExtra(STEP_NUMBER, number);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init(null);
    }
}
