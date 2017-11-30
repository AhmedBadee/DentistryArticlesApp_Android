package com.spells.dentistryarticles;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    private RecyclerView.Adapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_articles);

        RecyclerView articlesRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        articleAdapter = new ArticleAdapter(articleList);
        articlesRecyclerView.setAdapter(articleAdapter);
        articlesRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        articlesRecyclerView.setLayoutManager(layoutManager);
        articlesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        new HTTPConnection(AllArticles.this).execute();
    }

    private class HTTPConnection extends AsyncTask<Void, Void, JSONArray> {

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
        protected JSONArray doInBackground(Void... voids) {

            JSONArray articles = null;

            try {
                nodeUrl = new URL("https://dentistry.herokuapp.com/mobile/all/articles/");
                urlConnection = (HttpsURLConnection) nodeUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);

                inputStreamReader.close();

                articles = new JSONArray(stringBuilder.toString());

                image = new Bitmap[articles.length()];

                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article;
                    article = articles.getJSONObject(i);

                    JSONArray imagesURLs = article.getJSONArray("images");
                    image[i] = BitmapFactory.decodeStream(new URL(imagesURLs.getString(0)).openStream());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return articles;
        }

        @Override
        protected void onPostExecute(JSONArray articles) {
            int articlesCount = articles.length();

            try {
                for (int i = 0; i < articlesCount; i++) {
                    Article newArticle = new Article();
                    JSONObject article;
                    article = articles.getJSONObject(i);

                    newArticle.setTitle(article.getString("title"));
                    newArticle.setImage(image[i]);
                    newArticle.setBrief(article.getString("brief"));

                    articleList.add(newArticle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            articleAdapter.notifyDataSetChanged();
            alertDialog.dismiss();
        }
    }
}
