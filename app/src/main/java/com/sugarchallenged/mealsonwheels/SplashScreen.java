package com.sugarchallenged.mealsonwheels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONObject;

import bolts.Continuation;
import bolts.Task;

public class SplashScreen extends Activity {
  public static String MENU_JSON_INTENT_NAME = "menuJson";

  private final String TAG = "SplashScreen";
  private final String menuServiceHost = "twunch.herokuapp.com"; // "10.0.2.2:3000";
  private RequestQueue queue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    ((MealsApplication) getApplication()).getTracker(MealsApplication.TrackerName.APP_TRACKER);

    fetchJson(menuUrl(menuServiceHost)).onSuccess(new Continuation<JSONObject, Object>() {
      @Override
      public Object then(Task<JSONObject> task) throws Exception {
        JSONObject serviceResponse = task.getResult();
        Log.d(TAG, serviceResponse.toString());
        Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
        intent.putExtra(MENU_JSON_INTENT_NAME, serviceResponse.toString());
        startActivity(intent);
        return null;
      }
    });

  }

  protected Task<JSONObject> fetchJson(String url) {
    final Task<JSONObject>.TaskCompletionSource tcs = Task.create();

    queue = Volley.newRequestQueue(this);
    JsonObjectRequest request = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener() {
          @Override
          public void onResponse(Object response) {
            tcs.setResult((JSONObject) response);
          }
        },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                tcs.setError(error);
              }
            });
    queue.add(request);
    return tcs.getTask();
  }

  private String menuUrl(String host) {
    return "http://" + host + "/";
  }

  @Override
  protected void onStart() {
    super.onStart();
    GoogleAnalytics.getInstance(this).reportActivityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    GoogleAnalytics.getInstance(this).reportActivityStop(this);
  }
}
