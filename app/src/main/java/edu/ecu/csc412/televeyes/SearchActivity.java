package edu.ecu.csc412.televeyes;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.ecu.csc412.televeyes.adapter.ShowRecyclerViewAdapter;
import edu.ecu.csc412.televeyes.json.Series;
import edu.ecu.csc412.televeyes.json.Show;
import edu.ecu.csc412.televeyes.json.ShowContainer;

import static edu.ecu.csc412.televeyes.tv.TVMaze.multiSearch;
import static edu.ecu.csc412.televeyes.tv.TVMaze.schedule;

public class SearchActivity extends AppCompatActivity {

    private List<ShowContainer> shows;
    private Gson gson;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gson = new Gson();
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
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

            StringRequest request = new StringRequest(multiSearch + query, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Type collectionType = new TypeToken<List<ShowContainer>>() {
                    }.getType();

                    //Parse response from database into a list of shows
                    shows = gson.fromJson(response, collectionType);

                    //Write code to display search results down here

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //Display an error message
                }
            });
            requestQueue.add(request);
        }
    }
}
