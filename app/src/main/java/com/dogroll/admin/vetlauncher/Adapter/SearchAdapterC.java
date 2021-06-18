package com.dogroll.admin.vetlauncher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dogroll.admin.vetlauncher.Models.client;
import com.dogroll.admin.vetlauncher.R;
import com.dogroll.admin.vetlauncher.clients;
import java.util.ArrayList;

public class SearchAdapterC extends RecyclerView.Adapter<SearchAdapterC.SearchViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<client> clientFullArrayList;
    private final ArrayList<client> arrayList;

    public SearchAdapterC(Context context, ArrayList<client> clientFullArrayList){
        inflater = LayoutInflater.from(context);
        this.clientFullArrayList = clientFullArrayList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(clients.clientFullArrayList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.c_layout,viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.dairyNumber.setText(clientFullArrayList.get(i).getDairyNumber());
        searchViewHolder.farmName.setText(clientFullArrayList.get(i).getFarmName());
    }

    @Override
    public int getItemCount() {
        return clientFullArrayList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{
                    //I have made this class static, because Android studio wanted me too. Change if it malfunctions.
        TextView dairyNumber, farmName;

        SearchViewHolder(View itemView) {
            super(itemView);
            dairyNumber = itemView.findViewById(R.id.dairyNumber);
            farmName = itemView.findViewById(R.id.farmName);
        }
    }

    public void filter (String filterText) {
        filterText = filterText.toLowerCase();
        clients.clientFullArrayList.clear();

        if (filterText.length() == 0 ){
            clients.clientFullArrayList.addAll(arrayList);
        } else {
            for (client suggestion : arrayList) {
                if (suggestion.getDairyNumber().toLowerCase().contains(filterText)){
                    clients.clientFullArrayList.add(suggestion);
                }
                if (suggestion.getFarmName().toLowerCase().contains(filterText)){
                    clients.clientFullArrayList.add(suggestion);
                }
                if (suggestion.getContactName().toLowerCase().contains(filterText)){
                    clients.clientFullArrayList.add(suggestion);
                }
            }
        }
        notifyDataSetChanged();
    }

}
