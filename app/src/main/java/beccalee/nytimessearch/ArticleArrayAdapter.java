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
    /*
    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageView;
        public TextView title;
        public ProgressBar progressBar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            context = itemView.getContext();

            imageView = (ImageView) itemView.findViewById(R.id.ivImage);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pBar);

            // Setup the click listener
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

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for position
        Article article = this.getItem(position);
        // check to see if existing view is being reused
        // not using a recycled view = inflate layout
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // find image view
        //ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        // clear out recycled image from convertView from last time
        viewHolder.title.setText(article.headline);
        viewHolder.imageView.setImageResource(0);
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //tvTitle.setText(article.getHeadline());
        // populate the thumbnail image
        // remote download the image in the background
        String thumbnail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail)
                    .placeholder(R.drawable.ic_placeholder).into(viewHolder.imageView);
        }
        return convertView;
    }*/


    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
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
