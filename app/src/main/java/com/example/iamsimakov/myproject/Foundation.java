package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by alexey.simakov on 06.06.2015.
 */
public class Foundation extends Activity {

    public static final String PARSE_URL = "http://search.twitter.com/trends.json";

    ProgressBar myProgressBar;
    int myProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
       super.onCreate(savedInstanceState);
       setContentView(R.layout.foundation);

        myProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        new Thread(myThread).start();
        myProgressBar.setVisibility(View.VISIBLE);

        try {
            load();
        }
        catch (Throwable t) {
            TextView textView = (TextView) findViewById(R.id.textout);
            textView.setText(t.toString());
        }

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

        String jsonText = "{\"name\":\"Мурзик\",\"color\":-16777216,\"age\":9}";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Cat murzik = gson.fromJson(jsonText, Cat.class);
        TextView textView = (TextView) findViewById(R.id.textout);
        textView.setText("GSON "+ "Имя: " + murzik.name + "\nВозраст: " + murzik.age);

        /*
        StringBuffer buffer = null;
        try {
            URL url = new URL(PARSE_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            buffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }
            in.close();
        } catch (Throwable e) {

        }

        String outstr = "";
        TextView textView = (TextView) findViewById(R.id.textout);


        String str = "{\"errors\":[{\"message\":\"The Twitter REST API v1 is no longer active. Please migrate to API v1.1. https://dev.twitter.com/docs/api/1.1/overview.\",\"code\":64}]}";

        TwitterTrends trends = new Gson().fromJson(str, TwitterTrends.class);
        for (int counter = 0; counter < trends.getTrends().length; counter++) {
            outstr += trends.getTrends(counter);
        }
        textView.setText(str);

        */


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

}
