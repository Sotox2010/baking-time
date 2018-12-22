package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;

import butterknife.BindView;
import butterknife.ButterKnife;

class BakingStepViewHolder implements View.OnClickListener {

    interface OnStepSelectedListener {
        void onStepSelected(BakingStep step);
    }

    public static BakingStepViewHolder create(@NonNull ViewGroup parent, OnStepSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_step, parent, false);
        parent.addView(itemView);

        return new BakingStepViewHolder(itemView, listener);
    }

    @NonNull
    private View itemView;

    @Nullable
    private OnStepSelectedListener mStepSelectedListener;

    private BakingStep mBakingStep;

    @BindView(R.id.item_step_number)
    TextView mStepNumberView;

    @BindView(R.id.item_step_short_description)
    TextView mStepShortDescriptionView;

    private BakingStepViewHolder(@NonNull View itemView, @Nullable OnStepSelectedListener listener) {
        this.itemView = itemView;
        this.mStepSelectedListener = listener;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    void bindStep(BakingStep step) {
        mBakingStep = step;
        mStepNumberView.setText("Step " + step.getStepNumber());
        mStepShortDescriptionView.setText(step.getShortDescription());
    }

    @Override
    public void onClick(View v) {
        if (mStepSelectedListener != null) {
            mStepSelectedListener.onStepSelected(mBakingStep);
        }
    }
}
