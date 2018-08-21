package com.akwah.baking;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.akwah.baking.details.RecipeActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.RecipeClickListener, Response.Listener<String>, Response.ErrorListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MainAdapter mAdapter;
    private ArrayList<Recipe> mRecipes;
    private static final String SAVED_RECIPES = "saved_recipes_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getSpanCount()));
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_RECIPES)) {
            mRecipes = savedInstanceState.getParcelableArrayList(SAVED_RECIPES);
            mAdapter = new MainAdapter(this, mRecipes);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mAdapter = new MainAdapter(this, null);
            Utils.requestData(this, this, this);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipes != null)
            outState.putParcelableArrayList(SAVED_RECIPES, mRecipes);
    }

    public int getSpanCount() {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int width = displayMetrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.main_recyclerview_padding);
        int item_width = resources.getDimensionPixelSize(R.dimen.image_food_width)
                + 2 * resources.getDimensionPixelSize(R.dimen.image_food_padding)
                + 2 * resources.getDimensionPixelSize(R.dimen.frame_padding);
        return width / item_width;
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mRecipes = Utils.parseJSON(Utils.readLocalJsonFile(this));
        mAdapter.swapData(mRecipes);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(String response) {
        mRecipes = Utils.parseJSON(response);
        mAdapter.swapData(mRecipes);
        mProgressBar.setVisibility(View.GONE);
    }
}
