package edu.ecu.csc412.televeyes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import edu.ecu.csc412.televeyes.adapter.ExpandableListAdapter;
import edu.ecu.csc412.televeyes.model.Episode;
import edu.ecu.csc412.televeyes.model.Season;
import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.tv.TVMaze;

public class SynActivity extends AppCompatActivity {
    private NetworkImageView mBoxArt;
    private TextView mTitle;
    private TextView mNetwork;
    private TextView mAirTime;
    private TextView mGenres;
    private TextView mScore;
    private TextView mSynop;
    private ExpandableListView mSeasonView;
    private ExpandableListAdapter adapter;

    private List<Season> seasons;
    private List<Episode> episodes;

    public static void ShowSynop(Context context, int id) {
        Intent intent = new Intent(context, SynActivity.class);
        intent.setAction("edu.ecu.csc412.televeyes.SHOW");
        intent.putExtra("SHOW_ID", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_syn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBoxArt = (NetworkImageView) findViewById(R.id.box_art);
        mTitle = (TextView) findViewById(R.id.show_title);
        mNetwork = (TextView) findViewById(R.id.show_network);
        mAirTime = (TextView) findViewById(R.id.air_time);
        mGenres = (TextView) findViewById(R.id.show_genres);
        mScore = (TextView) findViewById(R.id.show_score);
        mSynop = (TextView) findViewById(R.id.show_synop);
        mSeasonView = (ExpandableListView) findViewById(R.id.season_view);

        handleIntent();
    }

    public void handleIntent() {
        String action = getIntent().getAction();

        if (action.compareToIgnoreCase("edu.ecu.csc412.televeyes.SHOW") == 0) {
            int id = getIntent().getIntExtra("SHOW_ID", 0);

            TVMaze.getInstance().getShowFromId(id, new TVMaze.OnShowLookupListener() {
                @Override
                public void onResult(final Show show) {
                    getSupportActionBar().setTitle(show.getName());
                    mBoxArt.setImageUrl(show.getLargeImage() != null ? show.getLargeImage() : show.getImage(), VolleySingleton.getInstance().getImageLoader());
                    mTitle.setText(show.getName());
                    mNetwork.setText(show.getNetwork());

                    String time = "";
                    List<String> days = show.getSchedule().getDays();

                    for (String day : days) {
                        time += " " + day.substring(0, 2);
                    }
                    time = time.replaceFirst(" ", "");
                    time = time.replaceAll(" ", ",");
                    time += " @ " + show.getSchedule().getShowTime();

                    mAirTime.setText(time);

                    List<String> genres = show.getGenres();
                    String gen = "";

                    for (String genre : genres) {
                        gen += " " + genre;
                    }

                    gen = gen.replaceFirst(" ", "");
                    gen = gen.replaceAll(" ", ",");

                    mGenres.setText(gen);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mSynop.setText(Html.fromHtml(show.getSummary(), Html.FROM_HTML_MODE_COMPACT).toString());
                    } else {
                        mSynop.setText(Html.fromHtml(show.getSummary()).toString());
                    }

                    TVMaze.getInstance().getSeasonsFromId(show.getId(), new TVMaze.OnSeasonLookupListener() {
                        @Override
                        public void onResults(List<Season> seasonsList) {
                            seasons = seasonsList;

                            TVMaze.getInstance().getEpisodesFromId(show.getId(), new TVMaze.OnEpisodeLookupListener() {
                                @Override
                                public void onResults(List<Episode> episodesList) {
                                    episodes = episodesList;
                                    adapter = new ExpandableListAdapter(seasons, episodes, getApplicationContext());
                                    mSeasonView.setAdapter(adapter);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
