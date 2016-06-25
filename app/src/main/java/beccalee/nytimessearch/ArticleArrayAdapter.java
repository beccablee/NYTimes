package beccalee.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import beccalee.nytimessearch.activities.ArticleActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by beccalee on 6/20/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder>{

    // Store a member variable for the contacts
    public List<Article> articles;
    private Context context;


    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.tvTitle) TextView title;
        @BindView(R.id.pBar) ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //setup listener
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Article article = articles.get(position);
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("article", article);
            context.startActivity(intent);
        }
    }

    public ArticleArrayAdapter(Context context, List<Article> allArticles) {
        articles = allArticles;
    }


    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.title;
        textView.setText(article.getHeadline());

        ImageView imageView = viewHolder.imageView;
        String thumbnail = article.getThumbnail();

        final ProgressBar progress = viewHolder.progressBar;

        if (!TextUtils.isEmpty(thumbnail)) {
            //Picasso.with(imageView.getContext()).load(thumbnail)
            //        .placeholder(R.drawable.ic_placeholder).into(viewHolder.imageView);

            // Show progress bar
            if (thumbnail != "") {
                progress.setVisibility(View.VISIBLE);
            }
            // Hide progress bar on successful load
            Picasso.with(imageView.getContext()).load(thumbnail)
                    .into(viewHolder.imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

    }


}
