package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.security.PrivilegedAction;


/**
 * Created by alexey.simakov on 06.06.2015.
 */
public class Foundation extends Activity {

    ProgressBar myProgressBar;
    //int myProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foundation);
        new ParseTask().execute();

        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

    }

    private class ParseTask extends AsyncTask<Void, Integer, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://api.pandem.pro/healthcheck/w/");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
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

                    String type = null;
                    if (widget.has("type")) type = widget.getString("type");

                    if (type.equals("person")){
                        JSONObject content = new JSONObject();
                        if (widget.has("content")) content = widget.getJSONObject("content");

                            JSONArray person = new JSONArray();
                            if (content.has("person")) person = content.getJSONArray("person");

                                JSONObject img = new JSONObject();
                                if (person.getJSONObject(0).has("img")) img = person.getJSONObject(0).getJSONObject("img");

                                    String img_url = null;
                                    if (img.has("url")) img_url = img.getString("url");

                                    String img_desc = null;
                                    if (person.getJSONObject(0).has("text")) img_desc = person.getJSONObject(0).getString("text");

                                String desc = null;
                                if (content.has("text")) desc = content.getString("text");

                        addWidget("textview", "Люди", 2);
                        addWidget("imageview", img_url, 0);
                        addWidget("textview", img_desc, 0);
                        addWidget("textview", desc, 0);

                    }
                    if (type.equals("news")){
                        JSONObject img = new JSONObject();
                        if (widget.has("img")) img = widget.getJSONObject("img");

                            String img_url = null;
                            if (img.has("url")) img_url = img.getString("url");

                        String desc = null;
                        if (widget.has("desc")) desc = widget.getString("desc") + "\n";
                        addWidget("textview", "Новости", 2);
                        addWidget("imageview", img_url, 1);
                        addWidget("textview", desc, 1);

                    }
                }

            } catch (JSONException e) {
            }

        }
    }

    public void addWidget(String typeofwidget, String str, int i){

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.lay_sec);
        ScrollView scrollView = (ScrollView) mainLayout.findViewById(R.id.scrollView);
        LinearLayout linearLayout = (LinearLayout) scrollView.findViewById(R.id.info);
        if (typeofwidget.equals("textview")) {

            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(str);
            if (i == 2) {
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle3);
                mTextView.setBackgroundResource(R.color.color_type3);
                mTextView.setPadding(0, 0, 0, 0);
            }
            else if (i == 0) {
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle1);
                mTextView.setBackgroundResource(R.color.color_type1);
                mTextView.setPadding(0, 0, 0, 10);
            } else {
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle2);
                mTextView.setBackgroundResource(R.color.color_type2);
                mTextView.setPadding(0, 0, 0, 10);
            }
            linearLayout.addView(mTextView);

        }

        if (typeofwidget.equals("imageview")) {
            try {
                ImageView myimage2 = new ImageView(getApplicationContext());
                new LoadImagefromUrl().execute(myimage2, str);
                myimage2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myimage2.setPadding(0,0,0,0);
                myimage2.setAdjustViewBounds(true);
                linearLayout.addView(myimage2);
            } catch (Exception e) {
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
            ivPreview.setImageBitmap( result );
        }
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL( url );
            bitmap = BitmapFactory.decodeStream( newurl.openConnection( ).getInputStream( ) );
        } catch ( MalformedURLException e ) {
            e.printStackTrace( );
        } catch ( IOException e ) {

            e.printStackTrace( );
        }
        return bitmap;
    }


}
