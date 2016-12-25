package edu.ecsu.csc412.televeyes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import edu.ecsu.csc412.televeyes.adapter.ExpandableListAdapter;
import edu.ecsu.csc412.televeyes.model.Episode;
import edu.ecsu.csc412.televeyes.model.Season;
import edu.ecsu.csc412.televeyes.model.Show;
import edu.ecsu.csc412.televeyes.tv.TVMaze;
import edu.ecsu.csc412.televeyes.tv.TheTVDB;
import edu.ecsu.csc412.televeyes.view.SquareNetworkImageView;

public class SynActivity extends AppCompatActivity {
    private SquareNetworkImageView mSeriesBanner;
    private TextView mTitle;
    private TextView mNetwork;
    private TextView mAirTime;
    private TextView mGenres;
    private TextView mScore;
    private TextView mSynop;
    private ExpandableListView mSeasonView;
    private ExpandableListAdapter adapter;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppbarLayout;

    private List<Season> seasons;
    private List<Episode> episodes;

    public static void ShowSynop(Context context, int id) {
        Intent intent = new Intent(context, SynActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("edu.ecsu.csc412.televeyes.SHOW");
        intent.putExtra("SHOW_ID", id);
        context.startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_syn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSeriesBanner = (SquareNetworkImageView) findViewById(R.id.series_banner);
        mTitle = (TextView) findViewById(R.id.show_title);
        mNetwork = (TextView) findViewById(R.id.show_network);
        mAirTime = (TextView) findViewById(R.id.air_time);
        mGenres = (TextView) findViewById(R.id.show_genres);
        mScore = (TextView) findViewById(R.id.show_score);
        mSynop = (TextView) findViewById(R.id.show_synop);
        mSeasonView = (ExpandableListView) findViewById(R.id.season_view);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppbarLayout = (AppBarLayout) findViewById(R.id.syn_appbar);

        handleIntent();
    }

    public void handleIntent() {
        String action = getIntent().getAction();

        if (action.compareToIgnoreCase("edu.ecsu.csc412.televeyes.SHOW") == 0) {
            int id = getIntent().getIntExtra("SHOW_ID", 0);

            getIntent().removeExtra("SHOW_ID");

            TVMaze.getInstance().getShowFromId(id, new TVMaze.OnShowLookupListener() {
                @Override
                public void onResult(final Show show) {
                    getSupportActionBar().setTitle(show.getName());
                    mTitle.setText(show.getName());
                    mNetwork.setText(show.getNetwork());

                    mCollapsingToolbar.setTitle(" ");

                    mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        boolean isShow = false;
                        int scrollRange = -1;

                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                            if (scrollRange == -1) {
                                scrollRange = appBarLayout.getTotalScrollRange();
                            }
                            if (scrollRange + verticalOffset == 0) {
                                mCollapsingToolbar.setTitle(show.getName());
                                isShow = true;
                            } else if (isShow) {
                                mCollapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                                isShow = false;
                            }
                        }
                    });

                    mSeriesBanner.addBitmapListener(new SquareNetworkImageView.OnBitmapSetListener() {
                        @Override
                        public void OnBitmapSet(Bitmap bm) {
                            Palette palette = Palette.from(bm).generate();
                            int def;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                def = getResources().getColor(R.color.colorPrimary, null);
                            } else {
                                def = getResources().getColor(R.color.colorPrimary);
                            }

                            Palette.Swatch lightSwatch;
                            Palette.Swatch darkSwatch;

                            if (palette.getLightVibrantSwatch() != null) {
                                lightSwatch = palette.getLightVibrantSwatch();
                            } else {
                                lightSwatch = palette.getDominantSwatch();
                            }

                            if (palette.getDarkVibrantSwatch() != null) {
                                darkSwatch = palette.getDarkVibrantSwatch();
                            } else if (palette.getDarkMutedSwatch() != null) {
                                darkSwatch = palette.getDarkMutedSwatch();
                            } else if (palette.getMutedSwatch() != null) {
                                darkSwatch = palette.getMutedSwatch();
                            } else {
                                darkSwatch = palette.getVibrantSwatch();
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(darkSwatch.getRgb());
                            }

                            mCollapsingToolbar.setExpandedTitleColor(lightSwatch.getTitleTextColor());
                            mCollapsingToolbar.setContentScrimColor(lightSwatch.getRgb());
                            mCollapsingToolbar.setStatusBarScrimColor(darkSwatch.getRgb());
                        }
                    });

                    TheTVDB.getInstance(new TheTVDB.TokenListener() {
                        @Override
                        public void OnTokenReceived(String token) {
                            TheTVDB.getInstance(null).getBanners(show.getThetvdb(), new TheTVDB.BannerListener() {
                                @Override
                                public void OnBanner(List<String> images) {
                                    mSeriesBanner.setImageUrl(images.get(0), VolleySingleton.getInstance().getImageLoader());
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SynActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
