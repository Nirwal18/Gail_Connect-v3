package com.nirwal.gailconnect.ui.hindi;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.nirwal.gailconnect.modal.Link;

import java.util.ArrayList;
import java.util.List;

public class HindiViewModel extends ViewModel {
    private static final String TAG = "HindiViewModel";

    private final MutableLiveData<List<Link>> _linkList;

    public HindiViewModel() {
        _linkList = new MutableLiveData<>();
        fetchLinkFromFireStore(Source.CACHE);
        fetchLinkFromFireStore(Source.DEFAULT);
    }

    public MutableLiveData<List<Link>> get_linkList() {
        return _linkList;
    }

    public void set_linkList(List<Link> linkList) {
        this._linkList.setValue(linkList);
    }



    private void fetchLinkFromFireStore(Source source){
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("hindi")
                .orderBy("title", Query.Direction.ASCENDING)
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Link link;
                            List<Link> linkList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              //  Log.d(TAG, document.getId() + " => " + document.getData());
                                link = document.toObject(Link.class);
                                linkList.add(link);
                            }
                            if(linkList.size()<=0){
                                fetchLinkFromFireStore(Source.SERVER);
                            }

                            if(_linkList.getValue()==null){
                                _linkList.setValue(linkList);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }



}