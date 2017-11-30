package com.spells.dentistryarticles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ReadArticle extends AppCompatActivity {

    private TextView article;
    private String articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_article);

        Intent intent = getIntent();
        articleTitle = intent.getStringExtra("ArticleTitle");

        article = (TextView) findViewById(R.id.article_title);
        article.setText(articleTitle);
    }
}
