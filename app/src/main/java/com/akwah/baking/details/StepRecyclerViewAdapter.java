package com.akwah.baking.details;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.akwah.baking.details.StepsFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {

    private static final int INGREDIENTS = 0;
    private static final int STEP = 1;
    private int SELECTED_COLOR;
    //    private Drawable DEFAULT_BACKGROUND;
    private final ArrayList<Recipe.Step> mSteps;
    private final OnListFragmentInteractionListener mListener;
    private int mPosition = -1;
    private View mSelectedView;
    private static final String POSITION_KEY = "position_key";

    public StepRecyclerViewAdapter(Context context, ArrayList<Recipe.Step> steps, OnListFragmentInteractionListener listener, Bundle savedInstanceState) {
        mSteps = steps;
        mListener = listener;
        SELECTED_COLOR = ContextCompat.getColor(context, R.color.selected_item);
        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(POSITION_KEY);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        if (DEFAULT_BACKGROUND == null)
//            DEFAULT_BACKGROUND = holder.mView.getBackground();
        holder.mIdView.setText(String.valueOf(position + 1));
        if (position == mPosition) {
            mSelectedView = holder.mView;
            holder.mView.setBackgroundColor(SELECTED_COLOR);
            holder.mView.setEnabled(false);
        }
        switch (getItemViewType(position)) {
            case INGREDIENTS:
                holder.mContentView.setText(R.string.show_ingredients);
                break;
            case STEP:
                holder.mContentView.setText(mSteps.get(position - 1).getShortDescription());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return INGREDIENTS;
        return STEP;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, mPosition);
    }

    public void onResume() {
        restoreView();
    }

//    private void restoreView() {
//        if (mSelectedView != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                mSelectedView.setBackground(DEFAULT_BACKGROUND);
//            else
//                mSelectedView.setBackgroundDrawable(DEFAULT_BACKGROUND);
//            mSelectedView = null;
//            mPosition = -1;
//        }
//    }

    private void restoreView() {
        if (mSelectedView != null) {
            mSelectedView.setBackgroundColor(Color.TRANSPARENT);
            mSelectedView.setEnabled(true);
            mSelectedView = null;
            mPosition = -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    restoreView();
                    mSelectedView = view;
                    view.setBackgroundColor(SELECTED_COLOR);
                    view.setEnabled(false);
                    mPosition = getAdapterPosition();
                    switch (getItemViewType()) {
                        case INGREDIENTS:
                            mListener.onIngredientClick();
                            break;
                        case STEP:
                            mListener.onStepClick(mPosition - 1);
                            break;
                    }
                }
            });
        }
    }
}
