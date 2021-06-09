package com.nirwal.gailconnect.adaptors;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.List;


public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ContactViewHolder> implements SectionTitleProvider {
    private static final String TAG = "ContactAdaptor";
    private List<Contact> _contactList;
    private final IOnClickListener _onClickListener;

    public ContactAdaptor(List<Contact> contactList, IOnClickListener listener) {
        this._contactList = contactList;
        this._onClickListener = listener;
    }

    public void updateDataSet(List<Contact> contacts){
        _contactList = contacts;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact,parent,false),
                _onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = _contactList.get(position);
        holder.nameInitial.setText(String.valueOf(contact.getEmp_Name().trim().charAt(0)));
        holder.nameInitial.setVisibility(View.VISIBLE);
        holder.username.setText(contact.getEmp_Name());
        holder.departmentAndPlaceOfPosting.setText(contact.getGrade()+" "+contact.getLocation());
        holder.userImage.setImageResource(R.drawable.color_1_bg);

        if(!contact.getIMAGE().trim().isEmpty()){
            ImageLoader.with((Application) holder.itemView.getContext().getApplicationContext())
                    .load(MyApp.IMG_URL+contact.getIMAGE())
                    .addListener(new ImageLoader.OnLoadListener() {
                        @Override
                        public void onSuccess() {
                            final ContactViewHolder vh = new WeakReference<ContactViewHolder>(holder).get();
                            vh.nameInitial.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    })
                    .into(holder.userImage);
        }


    }

    @Override
    public int getItemCount() {
        return _contactList==null? 0: _contactList.size();
    }

    @Override
    public String getSectionTitle(int position) {
        return _contactList.get(position).getEmp_Name().substring(0,1);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        public TextView nameInitial, username, departmentAndPlaceOfPosting, cpf;
        public ImageView userImage;
        public ContactViewHolder(@NonNull View itemView, IOnClickListener listener) {
            super(itemView);
            nameInitial = itemView.findViewById(R.id.name_initail);
            userImage = itemView.findViewById(R.id.img_user);
            username = itemView.findViewById(R.id.txt_persion_name);
            departmentAndPlaceOfPosting = itemView.findViewById(R.id.txt_dep_and_pop);
            itemView.setOnClickListener(v -> listener.onRowClick(getBindingAdapterPosition()));
        }
    }

    public interface IOnClickListener{
        void onRowClick(int position);
    }
}
