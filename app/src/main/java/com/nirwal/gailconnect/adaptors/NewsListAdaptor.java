package com.nirwal.gailconnect.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.News;

import java.util.List;

public class NewsListAdaptor extends RecyclerView.Adapter<NewsListAdaptor.NewsViewHolder> {

    private List<News> _newsList;
    private IOnClickListener _listener;

    public NewsListAdaptor(List<News> newsList) {
        this._newsList = newsList;
    }


    public void updateDataSet(List<News> data){
        _newsList.clear();
        _newsList.addAll(data);
        notifyDataSetChanged();
    }

    public void set_IOnClickListener(IOnClickListener listener) {
        this._listener =listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = _newsList.get(position);
        holder.title.setText(news.evenT_DATE);
        holder.itemView.setOnClickListener(v -> {
            if(_listener!=null){
                _listener.onNewsItemClick(news.body);
            }
        });

    }

    @Override
    public int getItemCount() {
        return _newsList.size();
    }

    public static final class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView logo;
        public TextView title;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.list_title);
            logo = itemView.findViewById(R.id.list_image_logo);
        }
    }

    public interface IOnClickListener{
        void onNewsItemClick(String pdfFileName);
    }
}
