package edu.ecu.csc412.televeyes.tv;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ecu.csc412.televeyes.ApplicationSingleton;
import edu.ecu.csc412.televeyes.VolleySingleton;
import edu.ecu.csc412.televeyes.json.thetvdb.Image;
import edu.ecu.csc412.televeyes.json.thetvdb.ImageContainer;
import edu.ecu.csc412.televeyes.json.thetvdb.ImageSummary;
import edu.ecu.csc412.televeyes.json.tvmaze.ShowContainer;

/**
 * Created by bi00dsh0t on 12/10/16.
 */

public class TheTVDB {

    private static TheTVDB sInstance;

    private final String root = "https://api.thetvdb.com/";
    private final String login = root + "login";
    private final String imageSummary = root + "series/{id}/images";
    private final String images = root + "series/{id}/images/query?keyType=poster";
    private final String imageRoot = "http://thetvdb.com/banners/";

    private final String key = "4B1BDA51ADFD371E";
    private final String auth = "{\"apikey\": \"" + key + "\"}";

    public static String token;

    private Gson gson;

    public static TheTVDB getInstance(@Nullable TokenListener listener){
        if(sInstance == null){
            sInstance = new TheTVDB(listener);
        } else if(listener != null){
            listener.OnTokenReceived(token);
        }
        return sInstance;
    }

    private TheTVDB(TokenListener listener){
        gson = new Gson();
        getToken(listener);
    }

    private void getToken(@Nullable final TokenListener listener){
        JSONObject obj = null;
        try {
            obj = new JSONObject(auth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, login, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    token = response.getString("token");
                    if(listener != null) listener.OnTokenReceived(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApplicationSingleton.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                getToken(listener);
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    public void getImageSummary(String id, final ImageSummaryListener listener, Response.ErrorListener errorListener){
        String url = imageSummary.replace("{id}", id);
        TVDBRequest request = new TVDBRequest(url, token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ImageSummary summary = gson.fromJson(response, ImageSummary.class);
                listener.OnImageSummary(summary);
            }
        }, errorListener);
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    public void getBanners(String id, final BannerListener listener, Response.ErrorListener errorListener) {
        String url = images.replace("{id}", id);
        TVDBRequest request = new TVDBRequest(url, token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Image> images = gson.fromJson(response, ImageContainer.class).data;
                List<String> urls = new ArrayList<>(images.size());

                for (int i = 0; i < images.size(); i++) {
                    urls.add(getImageUrl(images.get(i).fileName));
                }
                listener.OnBanner(urls);
            }
        }, errorListener);
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    public String getImageUrl(String url) {
        return imageRoot + url;
    }

    public interface ImageSummaryListener {
        void OnImageSummary(ImageSummary summary);
    }

    public interface BannerListener {
        void OnBanner(List<String> images);
    }

    public interface TokenListener {
        void OnTokenReceived(String token);
    }
}
