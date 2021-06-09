package com.nirwal.gailconnect.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Word;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WordListAdaptor extends RecyclerView.Adapter<WordListAdaptor.WordHolder> {
    private List<Word> _wordList;
    private IonRowClick _listener;

    public WordListAdaptor(List<Word> wordList, IonRowClick listener) {
        this._wordList = wordList;
        this._listener = listener;
    }

    public void updateDataSet(List<Word> words){
        this._wordList = words;
        notifyDataSetChanged();
    }



    @NonNull
    @NotNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new WordHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word,parent,false),_listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WordHolder holder, int position) {
        Word word = _wordList.get(position);
        holder.english.setText(word.getEnglish());
        holder.hindi.setText(word.getHindi());
    }

    @Override
    public int getItemCount() {
        return _wordList.size();
    }

    public static class WordHolder extends RecyclerView.ViewHolder {
        TextView english, hindi;
        public WordHolder(@NonNull @NotNull View itemView, IonRowClick listener) {
            super(itemView);
            english = itemView.findViewById(R.id.txt_english);
            hindi = itemView.findViewById(R.id.txt_hindi);
            itemView.setOnClickListener(v -> listener.onRowClick(getBindingAdapterPosition()));
            itemView.findViewById(R.id.btn_copy).setOnClickListener(v-> listener.onCopyBtnClick(getBindingAdapterPosition()));
        }
    }

    public interface IonRowClick{
        void onRowClick(int position);
        void onCopyBtnClick(int position);
    }
}
