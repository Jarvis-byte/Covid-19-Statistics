package com.example.covid_19.AlertDialouge;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    long DURATION = 500;
    String number, formatted;
    double amount;
    DecimalFormat formatter;
    private List<ListItem> listItems;
    private List<ListItem> exampleListFull;
    private Context context;
    private boolean on_attach = true;
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ListItem item : exampleListFull) {
                    if (item.getState().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems.clear();
            listItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        exampleListFull = new ArrayList<>(listItems);
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

        number = listItem.getInfected();
        amount = Double.parseDouble(number);
        formatter = new DecimalFormat("#,###");
        formatted = formatter.format(amount);
        holder.infected_num.setText(formatted);


        number = listItem.getRecovered();
        amount = Double.parseDouble(number);
        formatter = new DecimalFormat("#,###");
        formatted = formatter.format(amount);
        holder.recovered_num.setText(formatted);


        holder.death_num.setText(listItem.getDeath());
        number = listItem.getRecovered();
        amount = Double.parseDouble(number);
        formatter = new DecimalFormat("#,###");
        formatted = formatter.format(amount);
        holder.death_num.setText(formatted);


        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void updateList(List<ListItem> newList) {
        listItems = new ArrayList<>();
        listItems.addAll(newList);
        notifyDataSetChanged();
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView State, infected_num, recovered_num, death_num;

        GridLayout Grid_Layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            State = itemView.findViewById(R.id.State);
            infected_num = itemView.findViewById(R.id.infected_num);
            recovered_num = itemView.findViewById(R.id.recovered_num);
            death_num = itemView.findViewById(R.id.death_num);

            Grid_Layout = itemView.findViewById(R.id.Grid_Layout);


        }
    }
}
