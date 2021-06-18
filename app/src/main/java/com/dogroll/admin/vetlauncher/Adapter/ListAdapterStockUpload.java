package com.dogroll.admin.vetlauncher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dogroll.admin.vetlauncher.DB2Handler;
import com.dogroll.admin.vetlauncher.Models.uploadItems;
import com.dogroll.admin.vetlauncher.R;
import java.util.ArrayList;

public class ListAdapterStockUpload extends RecyclerView.Adapter<ListAdapterStockUpload.ViewHolder> {

    private final ArrayList<uploadItems> uploadData;
    private final LayoutInflater inflater;
    Context c;

    public ListAdapterStockUpload(Context context, ArrayList<uploadItems> data){
        c = context;
        this.inflater = LayoutInflater.from(context);
        this.uploadData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.upload_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.productName.setText(uploadData.get(position).getProductName());
        holder.count.setText(uploadData.get(position).getCount());
        holder.delete.setOnClickListener(v -> {
            DB2Handler PaidDB = new DB2Handler(c);
            PaidDB.UpdateCountedStockItem(uploadData.get(position).getId());
            uploadData.remove(uploadData.get(position));
            notifyDataSetChanged();
            Toast.makeText(c, "Counted stock record removed", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount(){
        return uploadData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName, count;
        ImageButton delete;

        ViewHolder(View itemView){
            super(itemView);
            productName = itemView.findViewById(R.id.uploadProductName);
            count = itemView.findViewById(R.id.uploadCount);
            delete = itemView.findViewById(R.id.stockUploadDeleteBtn);
        }
    }

}
