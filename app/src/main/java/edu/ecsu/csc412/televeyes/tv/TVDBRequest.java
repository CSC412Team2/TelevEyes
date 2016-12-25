package edu.ecsu.csc412.televeyes.tv;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bi00dsh0t on 12/11/16.
 */

public class TVDBRequest extends StringRequest {

    private String token;

    public TVDBRequest(String url, String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + token);
        map.put("Accept", "application/json");
        map.put("method", "GET");
        map.putAll(super.getHeaders());
        return map;
    }
}
