package com.akwah.baking;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> mList;
    private RecipeClickListener mRecipeClickListener;

    public MainAdapter(RecipeClickListener recipeClickListener, ArrayList<Recipe> recipes) {
        mRecipeClickListener = recipeClickListener;
        mList = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public void swapData(ArrayList<Recipe> list) {
        mList = list;
        if (mList != null)
            notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private CardView mRecipeCard;
        private TextView mRecipeName;
        private ImageView mRecipeImage;

        RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeCard = itemView.findViewById(R.id.recipe_card_view);
            mRecipeName = itemView.findViewById(R.id.recipe_name);
            mRecipeImage = itemView.findViewById(R.id.recipe_image);
            mRecipeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    mRecipeClickListener.onClick(mList.get(position));
                }
            });
        }

        void bind(Recipe recipe) {
            mRecipeName.setText(recipe.getName());
            String image = recipe.getImage();
            if (image != null && !image.equals(""))
                Picasso.get().load(image).into(mRecipeImage);
            else
                mRecipeImage.setImageResource(Utils.getRecipeImage(recipe.getName()));
        }
    }

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }
}