package com.spells.dentistryarticles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ReadArticle extends AppCompatActivity {

    private TextView articleTitleTxtView;
    private TextView articleBodyTxtView;
    private String articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_article);

        Intent intent = getIntent();
        articleTitle = intent.getStringExtra("ArticleTitle");

        articleTitleTxtView = (TextView) findViewById(R.id.article_title);
        articleTitleTxtView.setText(articleTitle);

        articleBodyTxtView  = (TextView) findViewById(R.id.article_body);

        new HTTPConnection(ReadArticle.this).execute();
    }

    private class HTTPConnection extends AsyncTask<Void, Void, JSONObject> {

        private URL nodeUrl;
        private HttpsURLConnection urlConnection;
        private StringBuilder stringBuilder = new StringBuilder();

        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;

        private Context context;

        private Bitmap[] image;

        HTTPConnection(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Loading...");
            alertDialogBuilder.setCancelable(false);

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            JSONObject article = null;

            try {
                nodeUrl = new URL("https://dentistry.herokuapp.com/mobile/single/article/" + articleTitle);
                urlConnection = (HttpsURLConnection) nodeUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);

                inputStreamReader.close();

                article = new JSONObject(stringBuilder.toString());

                /* JSONArray imagesURLs = article.getJSONArray("images_urls");

                for (int i = 0; i < imagesURLs.length(); i++) {
                    image[i] = BitmapFactory.decodeStream(new URL(imagesURLs.getString(i)).openStream());
                } */
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return article;
        }

        @Override
        protected void onPostExecute(JSONObject article) {
            try {
                articleBodyTxtView.setText(article.getString("article"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            alertDialog.dismiss();
        }
    }
}
