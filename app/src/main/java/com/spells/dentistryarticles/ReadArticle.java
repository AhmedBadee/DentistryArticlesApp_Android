package com.spells.dentistryarticles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ReadArticle extends AppCompatActivity {

    private String articleTitle;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams textLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        articleTitle = intent.getStringExtra("ArticleTitle");

        setTitle(articleTitle);

        ScrollView scrollView = new ScrollView(this);
        ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT
        );

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textLayoutParams.setMargins(10, 10, 10, 10);

        TextView titleView = new TextView(this);
        titleView.setText(articleTitle);
        titleView.setLayoutParams(textLayoutParams);
        titleView.setTextSize(15);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        // linearLayout.addView(titleView);

        scrollView.addView(linearLayout, linearLayoutParams);

        setContentView(scrollView, layoutParams);

        new HTTPConnection(ReadArticle.this).execute();
    }

    private class HTTPConnection extends AsyncTask<Void, Void, JSONObject> {

        private URL nodeUrl;
        private HttpURLConnection urlConnection;
        private StringBuilder stringBuilder = new StringBuilder();

        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;

        private Context context;

        private Bitmap[] images;
        private String images_urls;
        private String[] images_urls_array;

        private String text;
        private String[] text_array;

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
                // nodeUrl = new URL("http://10.0.3.2:8080/DentistryArticlesMaven/REST/Article/Title/" + articleTitle);
                nodeUrl = new URL("https://dentistryarticles-190917.appspot.com/REST/Article/Title/" + articleTitle);
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
                images_urls = article.getString("images_urls");
                images_urls_array = images_urls.split(";");
                images = new Bitmap[images_urls_array.length];

                for (int i = 0; i < images.length; i++) {
                    images[i] = BitmapFactory.decodeStream(new URL(images_urls_array[i]).openStream());
                }

                text = article.getString("articleBody");
                text_array = text.split("\\+");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return article;
        }

        @Override
        protected void onPostExecute(JSONObject article) {

            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageLayoutParams.setMargins(10, 10, 10, 10);

            TextView[] textViews   = new TextView[text_array.length];
            ImageView[] imageViews = new ImageView[images.length];

            if (textViews.length > imageViews.length) {
                int no_of_images = images.length;
                for (int i = 0; i < textViews.length; i++) {
                    textViews[i] = new TextView(context);
                    textViews[i].setLayoutParams(textLayoutParams);
                    textViews[i].setText(text_array[i]);
                    linearLayout.addView(textViews[i]);

                    if (no_of_images > 0) {
                        imageViews[i] = new ImageView(context);
                        imageViews[i].setLayoutParams(imageLayoutParams);
                        imageViews[i].setImageBitmap(images[i]);
                        linearLayout.addView(imageViews[i]);
                        no_of_images--;
                    }
                }
            } else {
                int no_of_texts = text_array.length;
                for (int i = 0; i < imageViews.length; i++) {
                    textViews[i] = new TextView(context);
                    textViews[i].setLayoutParams(textLayoutParams);
                    textViews[i].setText(text_array[i]);
                    textViews[i].setTextSize(25);
                    linearLayout.addView(textViews[i]);

                    if (no_of_texts > 0) {
                        imageViews[i] = new ImageView(context);
                        imageViews[i].setLayoutParams(imageLayoutParams);
                        imageViews[i].setImageBitmap(images[i]);
                        linearLayout.addView(imageViews[i]);
                        no_of_texts--;
                    }
                }
            }

            alertDialog.dismiss();
        }
    }
}
