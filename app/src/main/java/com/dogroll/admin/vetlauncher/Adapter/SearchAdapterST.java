package com.dogroll.admin.vetlauncher.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dogroll.admin.vetlauncher.Models.stockItems;
import com.dogroll.admin.vetlauncher.R;
import com.dogroll.admin.vetlauncher.Stocktake;
import java.util.ArrayList;


public class SearchAdapterST extends RecyclerView.Adapter<SearchAdapterST.SearchViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<stockItems> stockModelArrayList;
    private final ArrayList<stockItems> arrayList;

    public SearchAdapterST(Context context, ArrayList<stockItems> stockModelArrayList){
        inflater = LayoutInflater.from(context);
        this.stockModelArrayList = stockModelArrayList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(Stocktake.stockModelArrayList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.st_layout,viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.productName.setText(stockModelArrayList.get(i).getProduct());
        searchViewHolder.grouping.setText(stockModelArrayList.get(i).getGrouping());
        searchViewHolder.onHand.setText(stockModelArrayList.get(i).getOnHand());
    }

    @Override
    public int getItemCount() {
        return stockModelArrayList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView productName, grouping, onHand;

        SearchViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            grouping = itemView.findViewById(R.id.grouping);
            onHand = itemView.findViewById(R.id.onHand);
        }
    }

    public void filter (String filterText) {
        filterText = filterText.toLowerCase();
        Stocktake.stockModelArrayList.clear();

        if (filterText.length() == 0 ){
            Stocktake.stockModelArrayList.addAll(arrayList);
        } else {
            for (stockItems suggestion : arrayList) {
                if (suggestion.getProduct().toLowerCase().contains(filterText)){
                    Stocktake.stockModelArrayList.add(suggestion);
                }
                if (suggestion.getBarcode().toLowerCase().contains(filterText)){
                    Stocktake.stockModelArrayList.add(suggestion);
                }
                if (suggestion.getGrouping().toLowerCase().contains(filterText)){
                    Stocktake.stockModelArrayList.add(suggestion);
                }
            }
        }
        notifyDataSetChanged();
    }

}
