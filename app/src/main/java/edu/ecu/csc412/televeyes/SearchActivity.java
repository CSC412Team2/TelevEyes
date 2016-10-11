package edu.ecu.csc412.televeyes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import edu.ecu.csc412.televeyes.adapter.SearchAdapter;
import edu.ecu.csc412.televeyes.adapter.ShowRecyclerViewAdapter;
import edu.ecu.csc412.televeyes.json.ShowContainer;
import edu.ecu.csc412.televeyes.tv.TVMaze;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            TVMaze.getInstance().showSearch(query, 25, new TVMaze.OnShowSearchListener() {
                @Override
                public void onResults(List<ShowContainer> shows) {
                    view = (RecyclerView) findViewById(R.id.search_resuts);
                    if (view != null) {
                        view.setAdapter(new SearchAdapter(shows));
                        view.invalidate();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }


            });
        }
    }
}
