package com.example.iamsimakov.myproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
    ArrayList<ImageView> mylist = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foundation);   //ставим представление для этого класса
        new ParseTask().execute();  //создаем asynktask class  и запускаем его

        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);  //связываем прогресс бар с переменной такого же типа
        myProgressBar.setVisibility(View.VISIBLE); //отображаем прогресс бар, изначально он невидим,
                                                    //можно было бы сразу сделать видимым в виз. редакторе
    }

    private class ParseTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJson = "";

            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://api.pandem.pro/healthcheck/w/");

                urlConnection = (HttpURLConnection) url.openConnection(); //открываем соединение
                urlConnection.setRequestMethod("GET");
                urlConnection.connect(); //подключаемся

                InputStream inputStream = urlConnection.getInputStream();//поток ввода
                StringBuffer buffer = new StringBuffer(); //в эту переменную будем читать

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {  //читаем строку из потока
                    buffer.append(line); // и пока она не пустая дописываем в буфер
                }

                resultJson = buffer.toString(); //буфер - это объект, переводим в строку

            } catch (Exception e) {

            }
            return resultJson; //возвращаем строку и передаем ее в onPostExecute
        }


        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            JSONObject dataJsonObj = null;

            myProgressBar.setProgress(100);  //прогресс бар ставим 100%
            myProgressBar.setVisibility(View.INVISIBLE); //убираем его с глаз

            try {  //оборачиваем все в try ... catch на случай ошибок
                dataJsonObj = new JSONObject(strJson);  //на основе принятой строки создаем объект типа JSONObject
                JSONArray widgets = dataJsonObj.getJSONArray("widgets"); //объявляем массив объектов типа JSON
                //пишем туда найденный массив из принятой JSON структуры с именем widgets

                for (int i = 0; i < widgets.length(); i++) { // в цикле будем перебирать весь массив widgets
                    JSONObject widget = widgets.getJSONObject(i); //каждый элемент массива будем писать в объект widget

                    String type = null;  //в каждом объекте есть поле type
                    if (widget.has("type")) type = widget.getString("type"); //находим его значение и пишем в строку type

                    //далее будем выбирать 2 типа либо person либо news
                    if (type.equals("person")){
                        JSONObject content = new JSONObject();
                        if (widget.has("content")) content = widget.getJSONObject("content");
                        //читаем значение поля content  и пишем в JSONObject

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

                        //зная все значения будем создавать в linearlayout либо textview либо imageview
                        addWidget("textview", "Люди", 2);//передаем тип в виде строки, строку и стиль в виде цифры
                        addWidget("imageview", img_url, i+1);//здесь тип, url, стиль
                        addWidget("textview", img_desc, 0);
                        addWidget("textview", desc, 0);

                    }
                    if (type.equals("news")){
                        JSONObject img = new JSONObject();
                        if (widget.has("img")) img = widget.getJSONObject("img");

                            String img_url = null;
                            if (img.has("url")) img_url = img.getString("url");

                        String desc = null;
                        if (widget.has("desc")) desc = widget.getString("desc");
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

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.lay_sec);  //связываем родиельский слой с переменной
        ScrollView scrollView = (ScrollView) mainLayout.findViewById(R.id.scrollView); //полосу прокрутки с переменной
        LinearLayout linearLayout = (LinearLayout) scrollView.findViewById(R.id.info); //слой с переменной
        if (typeofwidget.equals("textview")) { //если на входе пришел textview

            TextView mTextView = new TextView(getApplicationContext()); //создаем textview
            mTextView.setText(str); //пишем в него строку со входа процедуры
            if (i == 2) { //если на входе приняли 2, то это будет стиль заголовка
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle3); //этот слиль в разделе res/value/styles
                mTextView.setBackgroundResource(R.color.color_type3); // это цвет в res/value/color
                mTextView.setPadding(0, 0, 0, 0); //отступы
            }
            else if (i == 0) {
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle1);
                mTextView.setBackgroundResource(R.color.color_type1);
                mTextView.setPadding(0, 0, 0, 0);
            } else {
                mTextView.setTextAppearance(getApplicationContext(), R.style.mystyle2);
                mTextView.setBackgroundResource(R.color.color_type2);
                mTextView.setPadding(0, 0, 0, 0);
            }
            linearLayout.addView(mTextView); // в linearlayout создаем текстовое поле

        }

        if (typeofwidget.equals("imageview")) { //если на входе нам передали картинку
            try {
                final ImageView myimage2 = new ImageView(getApplicationContext()); //создаем переменную imageview-типа
                new LoadImagefromUrl().execute(myimage2, str); // AsyncTask по загрузке картинки
                myimage2.setScaleType(ImageView.ScaleType.CENTER_CROP); //ставим заполнение полученной картинки
                linearLayout.addView(myimage2); //создаем Imageview
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

        } catch ( IOException e ) {

        }
        return bitmap;
    }


}
