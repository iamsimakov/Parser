package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * Created by alexey.simakov on 06.06.2015.
 */
public class Foundation extends Activity {

    public static String LOG_TAG = "my_log";
    public static final String PARSE_URL = "http://ya.ru";

    ProgressBar myProgressBar;
    int myProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
       super.onCreate(savedInstanceState);
       setContentView(R.layout.foundation);
        new ParseTask().execute();

        myProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        new Thread(myThread).start();
        myProgressBar.setVisibility(View.VISIBLE);
/*
        try {
            load();
        }
        catch (Throwable t) {
            TextView textView = (TextView) findViewById(R.id.textout);
            textView.setText(t.toString());
        }
*/
    }

    private Runnable myThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (myProgress < 100) {
                try {
                    myHandle.sendMessage(myHandle.obtainMessage());
                    Thread.sleep(20);
                } catch (Throwable t) {
                }
            }
            //JSONObject jsonObject = onNewIntent();
        }

        Handler myHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                myProgress++;
                myProgressBar.setProgress(myProgress);
                if (myProgress==100){myProgressBar.setVisibility(View.INVISIBLE);}
            }
        };
    };

    public void load(){
        /*
        String jsonText = "{\"name\":\"Мурзик\",\"color\":-16777216,\"age\":9}";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Cat murzik = gson.fromJson(jsonText, Cat.class);
        TextView textView = (TextView) findViewById(R.id.textout);
        textView.setText("GSON "+ "Имя: " + murzik.name + "\nВозраст: " + murzik.age);
        */
/*
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        try {
            URL url = new URL("http://ya.ru");

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

            TextView textView = (TextView) findViewById(R.id.textout);

            resultJson = buffer.toString();
            textView.setText(buffer.toString() + " 6" + " " + isNetworkOnline(this));
        } catch (Exception e) {
            TextView textView = (TextView) findViewById(R.id.textout);
            textView.setText(e.toString() + " " + isNetworkOnline(this));
        }
*/
        //String outstr = buffer.toString();

        //String str = "";
        /*
        TwitterTrends trends = new Gson().fromJson(str, TwitterTrends.class);
        for (int counter = 0; counter < trends.getTrends().length; counter++) {
            outstr += trends.getTrends(counter);
        }
        */
        //textView.setText(resultJson+" 6");
    }

    public boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public class Cat {

        public String name; // имя
        public int age; // возраст
        public int color; // цвет

        // Конструктор
        public Cat(){

        }
    }

    static class TwitterTrends {
        private String as_of;
        private Trends[] trends;

        public String getAs_of() {
            return as_of;
        }

        public void setAs_of(String as_of) {
            this.as_of = as_of;
        }

        public Trends[] getTrends() {
            return trends;
        }

        public Trends getTrends(int counter) {
            return trends[counter];
        }

        public void setTrends(Trends[] trends) {
            this.trends = trends;
        }

        public String toString() {
            return "Trends at " + as_of + ". Count: " + trends.length;
        }

        static class Trends {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String toString() {
                return "name: " + name + "; url: " + url;
            }
        }
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

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
            // выводим целиком полученную json-строку
            //Log.d(LOG_TAG, strJson);


            String strres = "";
            //strres += strJson;

            //TextView textView = (TextView) findViewById(R.id.textout);
            //textView.setText(strres);

            JSONObject dataJsonObj = null;
            //String firstname = "";

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray widgets = dataJsonObj.getJSONArray("widgets");

                // 1. достаем инфо о втором друге - индекс 1
                //String firstobj = widgets.getJSONObject(0).getString("nid");
                //firstname = firstobj.toString();
                //Log.d(LOG_TAG, "Второе имя: " + firstname);
                //strres += "nid: " + firstobj;
                // 2. перебираем и выводим контакты каждого друга

                for (int i = 0; i < widgets.length(); i++) {
                    JSONObject widget = widgets.getJSONObject(i);

                    String nid = null;
                    if (widget.has("nid")) nid = " nid: " + widget.getString("nid");


                    Date time = new Date();
                    if (widget.has("time")) time.setTime(widget.getLong("time"));
                    String strtime =  time.toString();

                    String title = null;
                    if (widget.has("title")) title = " title: " + widget.getString("title");


                    //String sport_id = widget.getString("sport_id");
                    //String type = widget.getString("type");
                    //long id = Long.parseLong(widget.getString("id"));
                    //int event_id = Integer.parseInt(widget.getString("event_id"));
                    //String desc = widget.getString("desc");

                    //strres += "Элемент " + i + " nid: " + nid + " time: " + mydate.getTime() + " title: " + title + " sport_id: " + sport_id + " type: " + type + " id: " + id + " event_id: " + event_id + " desc: " + desc;
                    strres += " Элемент " + i + nid + strtime + title;

                    /*
                    String phone = contacts.getString("mobile");

                    String email = contacts.getString("email");
                    String skype = contacts.getString("skype");

                    Log.d(LOG_TAG, "phone: " + phone);
                    strres += "phone: " + phone;
                    Log.d(LOG_TAG, "email: " + email);
                    strres += "email: " + email;
                    Log.d(LOG_TAG, "skype: " + skype);
                    strres += "skype: " + skype;
                    */
                    TextView textView = (TextView) findViewById(R.id.textout);
                    textView.setText(strres);
                }


            } catch (JSONException e) {
                TextView textView = (TextView) findViewById(R.id.textout);
                textView.setText(e.toString());
            }

        }
    }

}
