package com.example.eissa.myasynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public String apiUrl ="http://mobileappdatabase.in/demo/smartnews/app_dashboard/jsonUrl/single-article.php?article-id=71";

    public String title, image, category;
    public   TextView titleTextView, categoryTextView;
    public ProgressDialog progressDialog;
    public Button displayData;
    public ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        displayData = (Button) findViewById(R.id.displayData);
        imageView = (ImageView) findViewById(R.id.imageView);
        // implement setOnClickListener event on displayData button
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTasks myAsyncTasks= new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }

    class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            URL url;
            HttpURLConnection urlConnection=null;
            try{
                url = new URL(apiUrl);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream= urlConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                int data=inputStreamReader.read();
                while (data !=-1)
                {
                    current +=(char)data;
                    data=inputStreamReader.read();
                    System.out.print(current);
                }

                // return the data to onPostExecute method
                return current;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            // JSON Parsing of data
            try {

                JSONArray jsonArray= new JSONArray(s);
                JSONObject oneObject = jsonArray.getJSONObject(0);
                // Pulling items from the array
                title=oneObject.getString("title");
                category=oneObject.getString("category");
                image = oneObject.getString("image");
                // display the data in UI
                titleTextView.setText("Title: "+title);
                categoryTextView.setText("Category :"+category);
                // Picasso library to display the image from URL
                Picasso.with(getApplicationContext())
                        .load(image)
                        .into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}

