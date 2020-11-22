package com.example.covid_19.AlertDialouge;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.R;

import java.util.List;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    long DURATION = 500;
    private List<ListItem> listItems;
    private Context context;
    private boolean on_attach = true;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout_states, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.State.setText(listItem.getState());
        holder.infected_num.setText(listItem.getInfected());
        holder.recovered_num.setText(listItem.getRecovered());
        holder.death_num.setText(listItem.getDeath());
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private void setAnimation(View itemView, int i) {
        if (!on_attach) {
            i = -1;
        }
        boolean isNotFirstItem = i == -1;
        i++;
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", 0.f, 0.5f, 1.0f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animator.setStartDelay(isNotFirstItem ? DURATION / 2 : (i * DURATION / 3));
        animator.setDuration(500);
        animatorSet.play(animator);
        animator.start();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                Log.d("onScrollStateChanged", String.valueOf(newState));
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView State, infected_num, recovered_num, death_num;
        FrameLayout frameLayout_separator, frameLayout_separator_State_line;
        GridLayout Grid_Layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            State = itemView.findViewById(R.id.State);
            infected_num = itemView.findViewById(R.id.infected_num);
            recovered_num = itemView.findViewById(R.id.recovered_num);
            death_num = itemView.findViewById(R.id.death_num);
            //    frameLayout_separator = itemView.findViewById(R.id.frameLayout_separator);
            Grid_Layout = itemView.findViewById(R.id.Grid_Layout);


        }
    }
}
