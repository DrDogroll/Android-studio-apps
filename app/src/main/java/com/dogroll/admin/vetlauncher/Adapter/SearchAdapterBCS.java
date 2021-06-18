package com.dogroll.admin.vetlauncher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dogroll.admin.vetlauncher.BCSLaunch;
import com.dogroll.admin.vetlauncher.Models.bcsRecord;
import com.dogroll.admin.vetlauncher.R;
import java.util.ArrayList;

public class SearchAdapterBCS extends RecyclerView.Adapter<SearchAdapterBCS.SearchViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<bcsRecord> bcsRecordsFullArrayList;
    private final ArrayList<bcsRecord> arrayList;

    public SearchAdapterBCS(Context context, ArrayList<bcsRecord> bcsRecordsFullArrayList){
        inflater = LayoutInflater.from(context);
        this.bcsRecordsFullArrayList = bcsRecordsFullArrayList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(BCSLaunch.bcsRecordsFullArrayList);
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.bcs_layout,viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.date.setText(bcsRecordsFullArrayList.get(i).getDate());
        searchViewHolder.farm.setText(bcsRecordsFullArrayList.get(i).getFarm());
        searchViewHolder.herd.setText(bcsRecordsFullArrayList.get(i).getHerd());
    }

    @Override
    public int getItemCount() {
        return bcsRecordsFullArrayList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView date, farm, herd;

        SearchViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tvBCSRecycleDate);
            farm = itemView.findViewById(R.id.tvBCSRecycleFarm);
            herd = itemView.findViewById(R.id.tvBCSRecycleHerd);
        }
    }

    public void filter (String filterText) {
        filterText = filterText.toLowerCase();
        BCSLaunch.bcsRecordsFullArrayList.clear();

        if (filterText.length() == 0 ){
            BCSLaunch.bcsRecordsFullArrayList.addAll(arrayList);

        } else {
            for (bcsRecord suggestion : arrayList) {
                if (suggestion.getDate().toLowerCase().contains(filterText)){
                    BCSLaunch.bcsRecordsFullArrayList.add(suggestion);
                }
                if (suggestion.getFarm().toLowerCase().contains(filterText)){
                    BCSLaunch.bcsRecordsFullArrayList.add(suggestion);
                }
                if (suggestion.getHerd().toLowerCase().contains(filterText)){
                    BCSLaunch.bcsRecordsFullArrayList.add(suggestion);
                }
            }
        }
        notifyDataSetChanged();
    }

}
