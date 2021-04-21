package com.example.covid_19.Adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.covid_19.ModelClass.NewsItems;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {
    private List<NewsItems> old, baru;

    public CardStackCallback(List<NewsItems> old, List<NewsItems> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getTitle().equalsIgnoreCase(baru.get(newItemPosition).getTitle());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
