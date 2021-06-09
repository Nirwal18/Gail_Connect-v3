package com.nirwal.gailconnect.tasks;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nirwal.gailconnect.MyApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static MyApp _app;
    private static ImageLoader _loader;
    private static final String CACHE_DIR = "image_loader";

    public ImageLoader(MyApp app){
        _app = app;
    }

    private static ImageLoader getInstance(Application application){
        if(_loader==null){
            _loader = new ImageLoader((MyApp) application);
            return _loader;
        }
        return _loader;
    }

    public static ImageLoader with(Application application){
        return getInstance(application);
    }

    public RequestManager load(String url){
     return new RequestManager(url);
    }


    public String  getFileNameFromUrl(String url){
        return URLUtil.guessFileName(url,null, null);
    }

    public Bitmap getImage(String url){
       String filename = getFileNameFromUrl(url);
//        Log.d(TAG, "getImage: "+filename);

        File f =  new File(getFolder(),filename);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);

        return bitmap;
    }

    public boolean isImageCached(String url){

        return new File(
                getFolder(),
                getFileNameFromUrl(url)
        )
                .exists();
    }

    public File getFolder(){
        return new File(_app.getCacheDir(),CACHE_DIR);
    }

    public void putImageInCache(String imageName, Bitmap bitmap){
        if(bitmap==null)return;

        File folder=  getFolder();
        if(!folder.exists()){
            folder.mkdir();
        }

        File file = new File(folder, imageName);

        try {
            FileOutputStream out = new FileOutputStream(
                    file);
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    50, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void downloadImageAsync(String url, ImageView view, OnLoadListener listener){

        _app.executorService.execute(() -> {
            Bitmap bitmap = downloadFromUrl(url);
            postImageToUi(view, bitmap,listener);
            String filename = URLUtil.guessFileName(url,null, null);
            putImageInCache(filename, bitmap);
        });

    }

    private Bitmap downloadFromUrl(String urlAdd){
        try {
            URL url = new URL(urlAdd);
            // This user agent is for if the server wants real humans to visit
          //  String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            //connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void postImageToUi(ImageView view, Bitmap bitmap, OnLoadListener listener){
        if(bitmap==null){
            return;
        }
        _app.mainThreadHandler.post(() -> {
            view.setImageBitmap(bitmap);

            if(listener!=null){
                listener.onSuccess();
            }

        });
    }

    private void postError(String errorMsg, OnLoadListener listener){
        if(listener!=null){
            _app.mainThreadHandler.post(()->listener.onError(errorMsg));
        }
    }
    private void getImageAsync(String url, ImageView view, OnLoadListener listener){
        _app.executorService.execute(()->{
            Bitmap bitmap = getImage(url);
            postImageToUi(view, bitmap,listener);
        });
    }



    public class RequestManager{
        private final String _url;
        private  OnLoadListener _listener;

        public RequestManager(String url){
            this._url = url;
        }

        public void into(ImageView view){
            if(isImageCached(_url)){
                getImageAsync(_url,view, _listener);
            }
            else {
                downloadImageAsync(_url,view,_listener);
            }
        }

        public RequestManager addListener(OnLoadListener listener){
            _listener = listener;
            return this;
        }

    }

    public static class Utils{
        public  static byte[] bitmapToBytes(Bitmap bmp){
            if(bmp==null)return null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bmp.recycle();
            return byteArray;
        }

        public static byte[] downloadImageBytes(String imageUrl) {

            byte[] data;
            // This will get input data from the server
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                // This will open a socket from client to server
                URL url = new URL(imageUrl);

                // This user agent is for if the server wants real humans to visit
                String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

                // This socket type will allow to set user_agent
                URLConnection con = url.openConnection();

                // Setting the user agent
                con.setRequestProperty("User-Agent", USER_AGENT);
                //Getting content Length
                int contentLength = con.getContentLength();

                // Requesting input data from server
                inputStream = con.getInputStream();



                // Limiting byte written to file per loop
                byte[] buffer = new byte[2048];

                // Increments file size
                int length;
                int downloaded = 0;

                // Looping until server finishes
                while ((length = inputStream.read(buffer)) != -1)
                {
                    // Writing data
                    outputStream.write(buffer, 0, length);
                    downloaded+=length;

                }

                data = outputStream.toByteArray();
                outputStream.close();
                inputStream.close();

                return data;

            } catch (Exception ex) {
                Log.d(TAG, "image error: "+ex.getMessage());
                //Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }



    }


    public interface OnLoadListener{
        void onSuccess();
        void onError(String msg);
    }


}
