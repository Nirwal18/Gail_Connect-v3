package com.nirwal.gailconnect.adaptors;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BirthdayAdaptor extends RecyclerView.Adapter<BirthdayAdaptor.BirthdayViewHolder> {
    private final List<Contact> _list;
    private final IOnListItemClickListener _listener;

    public BirthdayAdaptor(@NonNull Context context, List<Contact> birthdayList, IOnListItemClickListener listener) {
        this._list = birthdayList;
        this._listener = listener;
    }




    @NonNull
    @NotNull
    @Override
    public BirthdayViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_birthday_person_frag,parent,false);
        return new BirthdayViewHolder(view, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BirthdayViewHolder holder, int position) {
        Contact contact = _list.get(position);

        ImageLoader.with((Application) holder.itemView.getContext().getApplicationContext())
                .load(MyApp.IMG_URL+contact.getIMAGE())
                .into(holder.photo);

//        SpannableStringBuilder sb = new SpannableStringBuilder()
//                .append().;

        holder.title.setText(Html.fromHtml("<b> <font color='#FFA000'>"+contact.getEmp_Name()+"</font></b><br>("+
                contact.getGrade()+")"
                + contact.getDesignation()+", " +contact.getDepartment()
                + contact.getLocation())
        );
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public void updateDataSet(List<Contact> contacts){
        _list.clear();
        _list.addAll(contacts);
        notifyDataSetChanged();
    }


    public static class BirthdayViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView title;
        public BirthdayViewHolder(@NonNull @NotNull View itemView, IOnListItemClickListener listener) {
            super(itemView);
            photo = itemView.findViewById(R.id.birthday_photo);
            title= itemView.findViewById(R.id.birthday_name);
            itemView.setOnClickListener(v->{
                listener.onListItemClicked(getBindingAdapterPosition());
            });

        }
    }

}
