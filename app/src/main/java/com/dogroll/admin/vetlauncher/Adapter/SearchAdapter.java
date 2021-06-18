package com.dogroll.admin.vetlauncher.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dogroll.admin.vetlauncher.Models.vVisits;
import com.dogroll.admin.vetlauncher.R;
import com.dogroll.admin.vetlauncher.diary;

import java.util.ArrayList;




public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<vVisits> visitModelArrayList;
    private final ArrayList<vVisits> arrayList;

    public SearchAdapter (Context context, ArrayList<vVisits> visitModelArrayList){
        inflater = LayoutInflater.from(context);
        this.visitModelArrayList = visitModelArrayList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(diary.visitModelArrayList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.vv_layout,viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.date.setText(visitModelArrayList.get(i).getDate());
        searchViewHolder.client.setText(visitModelArrayList.get(i).getClient());
        searchViewHolder.animal.setText(visitModelArrayList.get(i).getAnimal());
        searchViewHolder.CDU.setText(visitModelArrayList.get(i).getIsCD());
    }

    @Override
    public int getItemCount() {
        return visitModelArrayList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView date, client, animal, CDU;

        SearchViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTV);
            client = itemView.findViewById(R.id.clientTV);
            animal = itemView.findViewById(R.id.animalTV);
            CDU = itemView.findViewById(R.id.CDUTV);
        }
    }
    public void filter (String charText) {
        charText = charText.toLowerCase();
        diary.visitModelArrayList.clear();
        if (charText.length() == 0){
            diary.visitModelArrayList.addAll(arrayList);
        } else {
            for (vVisits suggestion : arrayList) {
                if (suggestion.getClient().toLowerCase().contains(charText)){
                    diary.visitModelArrayList.add(suggestion);
                }
                if (suggestion.getAnimal().toLowerCase().contains(charText)){
                    diary.visitModelArrayList.add(suggestion);
                }
                if (suggestion.getDate().toLowerCase().contains(charText)){
                    diary.visitModelArrayList.add(suggestion);
                }
                if (suggestion.getCR().toLowerCase().contains(charText)){
                    diary.visitModelArrayList.add(suggestion);
                }
            }
        }
        notifyDataSetChanged();
    }
}