package beccalee.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import beccalee.nytimessearch.Article;
import beccalee.nytimessearch.ArticleArrayAdapter;
import beccalee.nytimessearch.EndlessRecyclerViewScrollListener;
import beccalee.nytimessearch.R;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity  {

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    RecyclerView rvArticles;

    int REQUEST_CODE = 5;
    Filters filters = new Filters();
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }


    public void setupViews(){
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        rvArticles.setAdapter(adapter);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);
        SpaceItemDecoration decoration = new SpaceItemDecoration(16);
        rvArticles.addItemDecoration(decoration);

        //rvArticles.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be appended to the list
                onArticleSearch(searchQuery, page);
                customLoadMoreDataFromApi(page);
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // SearchView
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(query, 0);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_filter) {
            Intent intent = new Intent(SearchActivity.this, SearchFilter.class);
            intent.putExtra("filters", filters);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            filters = (Filters) data.getExtras().getSerializable("filters");
            onArticleSearch(searchQuery, 0);
        }
    }

    public void onArticleSearch(String query, int page) {
        if (page == 0){
            articles.clear();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        searchQuery = query;

        ArrayList<String> newsDesks = new ArrayList<String>();
        getNewsDesks(newsDesks);

        RequestParams params = new RequestParams();
        params.put("api-key", "48b683c4ed744a8bb7515d2408e74cbe");
        params.put("page", page);
        params.put("q", query);

        if (filters.beginDate != null){ params.put("begin_date", filters.beginDate); }
        if (filters.sort != null) { params.put("sort", filters.sort); }
        if (newsDesks.size() != 0) {
            String newsDeskItemsStr =
                    android.text.TextUtils.join(" ", newsDesks);
            String newsDeskParamValue =
                    String.format("news_desk:(%s)", newsDeskItemsStr);
            params.put("fq", newsDeskParamValue);
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJSONResults =  null;
                try {
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJSONResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getNewsDesks(ArrayList<String> checked){
        if (filters.arts){checked.add("Arts");}
        if (filters.fashion){checked.add("Fashion&Style");}
        if (filters.health){checked.add("Health");}
        if (filters.sports){checked.add("Sports");}
    }
}
