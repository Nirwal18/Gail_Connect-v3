package com.nirwal.gailconnect.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Link;

import java.util.List;

public class LinkAdaptor extends RecyclerView.Adapter<LinkAdaptor.MyViewHolder> {

    private List<Link> _listItems;
    private IOnListItemClickListener _listener;

    public LinkAdaptor(List<Link> listItems, IOnListItemClickListener listener) {
        this._listItems = listItems;
        this._listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_link,parent,false),
                _listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Link item = _listItems.get(position);
        holder.title.setText(item.getTitle());
        Glide.with(holder.itemView)
                .load(item.getLogo())
                .into(holder.logo);



    }

    @Override
    public int getItemCount() {
        return _listItems ==null ? 0 : _listItems.size();
    }

    public void updateDataSet(List<Link> data){
        this._listItems.clear();
        this._listItems.addAll(data);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView logo;

        public MyViewHolder(@NonNull View itemView, IOnListItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.list_title);
            logo = itemView.findViewById(R.id.list_image_logo);

            itemView.setOnClickListener(v -> listener.onListItemClicked(getBindingAdapterPosition()));
        }
    }



}
