package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by alexey.simakov on 06.06.2015.
 */
public class Foundation extends Activity {

    ProgressBar myProgressBar;
    RelativeLayout mainLayout;
    ScrollView scrollView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foundation);
        new ParseTask().execute();
        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);
    }

    public void printException(Exception e){
        String stackTrace = "";
        for (StackTraceElement element : e.getStackTrace()){
            stackTrace += element.toString() + "\t\n";
        }
        addWidget("textview", stackTrace, 0);

    }

    private class ParseTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJson = "";
            try {
                URL url = new URL("http://api.pandem.pro/healthcheck/w/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                printException(e);
            }
            return resultJson;
        }


        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONObject dataJsonObj = null;
            myProgressBar.setProgress(100);
            myProgressBar.setVisibility(View.INVISIBLE);
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray widgets = dataJsonObj.getJSONArray("widgets");
                for (int i = 0; i < widgets.length(); i++) {
                    JSONObject widget = widgets.getJSONObject(i);
                    if (widget.has("type") && widget.getString("type").equals("person") ){
                        Person person = new Person(widget);
                        printPerson(person);
                    } else if (widget.has("type") && widget.getString("type").equals("news") ) {
                        News news = new News(widget);
                        printNews(news);
                    }
                }
            } catch (JSONException e) {
                printException(e);
            }
        }
    }

    public void printPerson(Person p){
        addWidget("textview", "Люди", 2);
        addWidget("imageview", p.getImgUrl(), 1);
        addWidget("textview", p.getImgDesc(), 0);
        addWidget("textview", p.getDesc(), 0);
    }

    public void printNews(News n){
        addWidget("textview", "Новости", 2);
        addWidget("imageview", n.getImgUrl(), 1);
        addWidget("textview", n.getDesc(), 1);
    }

    public void addWidget(String typeofwidget, String str, int i){
        mainLayout = (RelativeLayout) findViewById(R.id.lay_sec);
        scrollView = (ScrollView) mainLayout.findViewById(R.id.scrollView);
        linearLayout = (LinearLayout) scrollView.findViewById(R.id.info);
        if (typeofwidget.equals("textview")) {
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(str);
            if (i == 2) {
                mTextView.setBackgroundResource(R.color.color_type3);
            }
            else if (i == 0) {
                mTextView.setBackgroundResource(R.color.color_type1);

            } else {
                mTextView.setBackgroundResource(R.color.color_type2);
            }
            mTextView.setPadding(0, 0, 0, 0);
            mTextView.setTextColor(getResources().getColor(R.color.color_text));
            linearLayout.addView(mTextView);
        }
        if (typeofwidget.equals("imageview")) {
            try {
                ImageView myimage2 = new ImageView(getApplicationContext());
                new LoadImagefromUrl().execute(myimage2, str);
                linearLayout.addView(myimage2);
            } catch (Exception e) {
                printException(e);
            }
        }
    }

    private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap > {
        ImageView ivPreview = null;

        @Override
        protected Bitmap doInBackground( Object... params ) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            System.out.println(url);
            return loadBitmap( url );
        }

        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics metricsB = new DisplayMetrics();
            display.getMetrics(metricsB);
            try {
                int height = result.getHeight();
                int width = result.getWidth();
                height = (int)(height * (metricsB.widthPixels/(float)width));
                ivPreview.getLayoutParams().height = height;
                ivPreview.setImageBitmap(result);
            } catch (Exception e){

            }
        }
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (MalformedURLException e){

        } catch (IOException e){

        }
        return bitmap;
    }


}
