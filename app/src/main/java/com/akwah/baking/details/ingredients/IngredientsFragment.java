package com.akwah.baking.details.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {

    public static final String INGREDIENTS_KEY = "ingredients_key";
    private ArrayList<Recipe.Ingredient> mIngredients;

    public IngredientsFragment() {
    }

    public static IngredientsFragment newInstance(ArrayList<Recipe.Ingredient> ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS_KEY, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIngredients = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new IngredientsRecyclerViewAdapter(mIngredients));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INGREDIENTS_KEY, true);
    }
}
