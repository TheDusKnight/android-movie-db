package com.example.trevormobilefilm;

import org.json.JSONArray;
import org.json.JSONException;

public interface VolleyCallback {
    void onSuccess(JSONArray result) throws JSONException;
}
