package com.spells.dentistryarticles;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;

    class ArticleViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView image;
        private TextView brief;

        ArticleViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            image = view.findViewById(R.id.display_image);
            brief = view.findViewById(R.id.brief);
        }
    }

    ArticleAdapter(List<Article> articlesList) {
        this.articleList = articlesList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_row, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReadArticle.class);

                // Get the title of the article
                /* start here */
                CardView article   = (CardView) ((ViewGroup) view).getChildAt(0);
                RelativeLayout row = (RelativeLayout) article.getChildAt(0);
                TextView title     = (TextView) row.getChildAt(1);
                intent.putExtra("ArticleTitle", title.getText());
                /* end here */

                view.getContext().startActivity(intent);
            }
        });

        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.title.setText(article.getTitle());
        holder.image.setImageBitmap(article.getImage());
        holder.brief.setText(article.getBrief());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
