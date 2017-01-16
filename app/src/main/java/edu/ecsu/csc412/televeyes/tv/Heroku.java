package edu.ecsu.csc412.televeyes.tv;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.ecsu.csc412.televeyes.VolleySingleton;
import edu.ecsu.csc412.televeyes.json.tvmaze.Series;
import edu.ecsu.csc412.televeyes.json.tvmaze.ShowContainer;
import edu.ecsu.csc412.televeyes.model.Episode;
import edu.ecsu.csc412.televeyes.model.Season;
import edu.ecsu.csc412.televeyes.model.Show;

/**
 * Created by joshu on 10/2/2016.
 */

public class Heroku {
    private static final String tvmaze = "http://api.tvmaze.com";
    private static final String heroku = "https://televeyes.herokuapp.com/";
    private static final String singleSearch = tvmaze + "/singlesearch/shows?q=";
    private static final String multiSearch = heroku + "/search/";
    private static final String schedule = tvmaze + "/schedule?country=us";
    private static final String lookup = heroku + "/shows/";
    private static final String seasons = "/seasons";
    private static final String episodes = "/episodes";

    private static Heroku sInstance = null;

    private Gson gson;
    private RequestQueue requestQueue;

    private Heroku() {
        gson = new Gson();
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    public static Heroku getInstance() {
        if (sInstance == null) {
            sInstance = new Heroku();
        }
        return sInstance;
    }

    //TODO Add filtering options

    /**
     * @param maxResults           number of results to return
     * @param onShowSearchListener code to execute once the result is retrived
     * @param errorListener        what to do if the network request fails
     */
    public void getSchedule(final int maxResults, final OnShowSearchListener onShowSearchListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(schedule, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<Series>>() {
                }.getType();
                List<Series> parsed = gson.fromJson(response, collectionType);

                List<Show> shows = new ArrayList<>(parsed.size());

                for (int i = 0; i < parsed.size(); i++) {
                    shows.add(new Show(parsed.get(i)));
                }

                //Truncate the results
                truncateResults(shows, maxResults);

                //results are sent to the Listener
                onShowSearchListener.onResults(shows);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    /**
     * @param query                Search query
     * @param maxResults           maximum number of results to return
     * @param onShowSearchListener what to do once the results are returned
     * @param errorListener        what to do if the network request fails
     */
    public void showSearch(String query, final int maxResults, final OnShowSearchListener onShowSearchListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(multiSearch + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<ShowContainer>>() {
                }.getType();
                List<ShowContainer> parsed = gson.fromJson(response, collectionType);

                List<Show> shows = new ArrayList<>(parsed.size());

                for (int i = 0; i < parsed.size(); i++) {
                    shows.add(new Show(parsed.get(i)));
                }

                //Truncate the results
                truncateResults(shows, maxResults);

                //results are sent to the listener
                onShowSearchListener.onResults(shows);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    public void getShowFromId(int Id, final OnShowLookupListener onShowLookupListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(lookup + Id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edu.ecsu.csc412.televeyes.json.tvmaze.Show show = gson.fromJson(response, edu.ecsu.csc412.televeyes.json.tvmaze.Show.class);
                //results are sent to the listener
                onShowLookupListener.onResult(new Show(show));
            }
        }, errorListener);
        requestQueue.add(request);
    }

    public void getSeasonsFromId(int id, final OnSeasonLookupListener onSeasonLookupListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(lookup + id + seasons, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<edu.ecsu.csc412.televeyes.json.tvmaze.Season>>() {
                }.getType();

                List<edu.ecsu.csc412.televeyes.json.tvmaze.Season> parsed = gson.fromJson(response, collectionType);
                List<Season> seasons = new ArrayList<>(parsed.size());

                for (int i = 0; i < parsed.size(); i++) {
                    seasons.add(new Season(parsed.get(i)));
                }

                onSeasonLookupListener.onResults(seasons);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    public void getEpisodesFromId(int id, final OnEpisodeLookupListener episodeLookupListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(lookup + id + episodes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<edu.ecsu.csc412.televeyes.json.tvmaze.Episode>>() {
                }.getType();

                List<edu.ecsu.csc412.televeyes.json.tvmaze.Episode> parsed = gson.fromJson(response, collectionType);

                List<Episode> episodes = new ArrayList<>(parsed.size());

                for (int i = 0; i < parsed.size(); i++) {
                    episodes.add(new Episode(parsed.get(i)));
                }

                episodeLookupListener.onResults(episodes);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    public void getFullInfoFromId(int id, final OnFullInfoListener onFullInfoListener, Response.ErrorListener errorListener) {

    }


    private void truncateResults(List<?> list, int num) {
        if (num == -1) return;
        while (list.size() > num) list.remove(list.size() - 1);
    }

    public interface OnShowLookupListener {
        void onResult(Show show);
    }

    public interface OnShowSearchListener {

        /**
         * @param shows a list of objects containing the results
         */
        void onResults(List<Show> shows);
    }

    public interface OnSeasonLookupListener {
        void onResults(List<Season> seasons);
    }

    public interface OnEpisodeLookupListener {
        void onResults(List<Episode> episodes);
    }

    public interface OnFullInfoListener {
        void onResults(List<Season> seasons, List<Episode> episodes);
    }
}
