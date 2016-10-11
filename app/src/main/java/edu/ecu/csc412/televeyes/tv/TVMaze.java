package edu.ecu.csc412.televeyes.tv;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.json.Series;
import edu.ecu.csc412.televeyes.json.ShowContainer;

/**
 * Created by joshu on 10/2/2016.
 */

public class TVMaze {
    public static final String tvmaze = "http://api.tvmaze.com";
    public static final String singleSearch = tvmaze + "/singlesearch/shows?q=";
    public static final String multiSearch = tvmaze + "/search/shows?q=";
    public static final String schedule = tvmaze + "/schedule?country=us";

    private static TVMaze sInstance = null;

    private Gson gson;
    private RequestQueue requestQueue;

    public static TVMaze getInstance(){
        if(sInstance == null){
            sInstance = new TVMaze();
        }
        return sInstance;
    }

    private TVMaze(){
        gson = new Gson();
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    //TODO Add filtering options
    /**
     * @param maxResults number of results to return
     * @param onScheduleListener code to execute once the result is retrived
     * @param errorListener what to do if the network request fails
     */
    public void getSchedule(final int maxResults, final OnScheduleListener onScheduleListener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(schedule, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<Series>>() {
                }.getType();
                List<Series> shows = gson.fromJson(response, collectionType);

                //Truncate the results
                truncateResults(shows, maxResults);

                //results are sent to the Listener
                onScheduleListener.onResults(shows);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    /**
     * @param query Search query
     * @param maxResults maximum number of results to return
     * @param onShowSearchListener what to do once the results are returned
     * @param errorListener what to do if the network request fails
     */
    public void showSearch(String query, final int maxResults, final OnShowSearchListener onShowSearchListener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(multiSearch + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type collectionType = new TypeToken<List<ShowContainer>>() {
                }.getType();
                List<ShowContainer> shows = gson.fromJson(response, collectionType);

                //Truncate the results
                truncateResults(shows, maxResults);

                //results are sent to the listener
                onShowSearchListener.onResults(shows);
            }
        }, errorListener);
        requestQueue.add(request);
    }

    private void truncateResults(List<?> list, int num){
        while(list.size() > num) list.remove(list.size() - 1);
    }

    public interface OnScheduleListener{
        /**
         * @param series a list of objects containing the results
         */
        void onResults(List<Series> series);
    }

    public interface OnShowSearchListener{

        /**
         * @param shows a list of objects containing the results
         */
        void onResults(List<ShowContainer> shows);
    }
}
