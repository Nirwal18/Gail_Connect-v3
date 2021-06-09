package com.nirwal.gailconnect.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFile extends AsyncTask<String, Void, Void> {

    private final Context _context;
    private IOnCompleteListener _listener;

    public DownloadFile(Context context){
        this._context = context;
    }

    public void addOnCompleteListener(IOnCompleteListener listener) {
        this._listener = listener;
    }

    @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/
            String fileName = strings[1];  // -> maven.pdf

            String extStorageDirectory = _context.getCacheDir().toString();
            File folder = new File(extStorageDirectory, "PDF");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(_listener!=null){
            _listener.onComplete();
        }
    }

    public interface IOnCompleteListener{
            void onComplete();
        }
    }




