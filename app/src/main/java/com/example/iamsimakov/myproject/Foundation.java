package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONObject;

/**
 * Created by alexey.simakov on 06.06.2015.
 */
public class Foundation extends Activity {

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

}
