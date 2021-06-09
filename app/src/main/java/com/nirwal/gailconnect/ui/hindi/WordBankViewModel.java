package com.nirwal.gailconnect.ui.hindi;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Link;
import com.nirwal.gailconnect.modal.Word;

import java.util.ArrayList;
import java.util.List;

public class WordBankViewModel extends ViewModel {
    private static final String TAG = "WordBankViewModel";

    private MyApp _app;
    private final List<Word> _wordList = new ArrayList<>();
    private final List<Word> _tempWordList = new ArrayList<>();
    private final MutableLiveData<String> _errorTxt = new MutableLiveData<>();
    private final MutableLiveData<List<Word>> _wordListLiveData = new MutableLiveData<>();


    public MutableLiveData<String> get_errorTxt() {
        return _errorTxt;
    }

    public LiveData<List<Word>> get_wordListLiveData(MyApp app,String path){
        if(_wordList.size()<=0) {
            fetchWordListFromFireStore(path);
        }
        this._app = app;
        return _wordListLiveData;
    }

    public void performSearch(String query){
        if(query.isEmpty()){
            _wordListLiveData.postValue(_wordList);
            return;
        }

        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {


                _tempWordList.clear();
                for(Word word : _wordList){
                    if(word.getEnglish().toLowerCase().contains(query)){
                        _tempWordList.add(word);
                    }

                }
                _app.mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _wordListLiveData.postValue(_tempWordList);
                    }
                });

            }
        });


    }

    public void fetchWordListFromFireStore(String path){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Source source = Source.DEFAULT;

        db.collection(path)
                .orderBy("english", Query.Direction.ASCENDING)
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Word word;
                            _wordList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                word = document.toObject(Word.class);
                                _wordList.add(word);
                            }



                            performSearch("");
                        } else {
                           // Log.d(TAG, "Error getting documents.", task.getException());
                            get_errorTxt().setValue("Error getting documents.\n"+ task.getException());
                        }
                    }
                });
    }




}