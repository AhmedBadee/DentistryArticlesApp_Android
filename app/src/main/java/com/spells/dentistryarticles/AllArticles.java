package com.spells.dentistryarticles;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AllArticles extends AppCompatActivity {

    private List<Article> articleList = new ArrayList<>();
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_articles);

        RecyclerView articlesRecyclerView;

        articlesRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        articleAdapter = new ArticleAdapter(articleList);
        articlesRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        articlesRecyclerView.setLayoutManager(layoutManager);

        articlesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        articlesRecyclerView.setAdapter(articleAdapter);

        new HTTPConnection(AllArticles.this).execute();
    }

    private class HTTPConnection extends AsyncTask<Void, Void, Void> {

        private URL nodeUrl;
        private HttpsURLConnection urlConnection;
        private StringBuilder stringBuilder = new StringBuilder();

        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;

        private Context context;

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
        protected Void doInBackground(Void... voids) {

            try {
                nodeUrl = new URL("https://dentistry.herokuapp.com/");
                urlConnection = (HttpsURLConnection) nodeUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        stringBuilder.append(line);

                    inputStreamReader.close();

                    JSONArray articles = new JSONArray(stringBuilder.toString());

                    /* JSONObject firstArticle = jsonArray.getJSONObject(0);
                    Log.e("DATA", firstArticle.getString("title"));
                    JSONObject secondArticle = jsonArray.getJSONObject(1);
                    JSONArray secondImages = secondArticle.getJSONArray("images");
                    Log.e("DATA", secondImages.getString(0)); */

                    int articlesCount = articles.length();

                    for (int i = 0; i < articlesCount; i++) {
                        Article newArticle = new Article();
                        JSONObject article;
                        article = articles.getJSONObject(i);
                        newArticle.setTitle(article.getString("title"));
                        Log.e("DATA", newArticle.getTitle());
                        newArticle.setImage(null);
                        newArticle.setBrief(article.getString("brief"));
                        articleList.add(newArticle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // articleAdapter.notifyDataSetChanged();
            alertDialog.dismiss();
        }
    }
}
