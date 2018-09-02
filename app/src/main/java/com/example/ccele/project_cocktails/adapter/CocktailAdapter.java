package com.example.ccele.project_cocktails.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ccele.project_cocktails.CocktailDetailActivity;
import com.example.ccele.project_cocktails.CocktailDetailFragment;
import com.example.ccele.project_cocktails.CocktailListActivity;
import com.example.ccele.project_cocktails.R;
import com.example.ccele.project_cocktails.models.Cocktail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccele on 8/5/2018.
 */

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>
        implements Filterable {

    private final CocktailAdapterOnClickHandler mClickHandler;

    public interface CocktailAdapterOnClickHandler {
        void onClick(Cocktail cocktail);
    }

    private List<Cocktail> cocktailList;
    private List<Cocktail> cocktailListFiltered;

    public CocktailAdapter(CocktailAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        cocktailList = new ArrayList<>();
        cocktailListFiltered = cocktailList;

    }

    class CocktailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mThumbView;
        TextView mNameView;

        CocktailViewHolder(View view) {
            super(view);
            mThumbView = view.findViewById(R.id.thumb);
            mNameView = view.findViewById(R.id.name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int adapterPosition = getAdapterPosition();
            Cocktail cocktail = cocktailListFiltered.get(adapterPosition);
            mClickHandler.onClick(cocktail);
        }
    }

    @Override
    public CocktailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_list_content, parent, false);
        return new CocktailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CocktailViewHolder holder, final int position) {
        final Cocktail cocktail = cocktailListFiltered.get(position);
        holder.mNameView.setText(cocktail.getName());
        Picasso.get().load(cocktail.getImageUrl()).into(holder.mThumbView);
    }

    @Override
    public int getItemCount() {
        if (cocktailListFiltered == null) return 0;
        return cocktailListFiltered.size();
    }


    public void setCocktailData(List<Cocktail> cocktailList){
        this.cocktailListFiltered.clear();
        this.cocktailListFiltered.addAll(cocktailList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    cocktailListFiltered = cocktailList;
                } else {
                    List<Cocktail> filteredList = new ArrayList<>();
                    for (Cocktail row : cocktailList){
                        if(row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    cocktailListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cocktailListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cocktailListFiltered = (ArrayList<Cocktail>) filterResults.values;
                setCocktailData(cocktailListFiltered);
            }
        };
    }

}
