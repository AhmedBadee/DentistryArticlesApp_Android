package com.spells.dentistryarticles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class UserType extends AppCompatActivity {

    private boolean community_professional;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_type);

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void userTypeBtnClick(View view) {
        Intent intent = new Intent(this, AllArticles.class);

        switch (view.getId()) {
            case R.id.community:
                community_professional = true;
                break;
            case R.id.professional:
                community_professional = false;
                break;
        }

        intent.putExtra("UserType", community_professional);
        startActivity(intent);
    }
}
