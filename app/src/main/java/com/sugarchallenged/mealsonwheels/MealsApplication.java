package com.sugarchallenged.mealsonwheels;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.logging.Logger;

public class MealsApplication extends Application {
  private final String TAG = "MealsApplication";

  public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
  }

  HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

  synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      analytics.setDryRun(false);
      analytics.getLogger().setLogLevel(com.google.android.gms.analytics.Logger.LogLevel.INFO);
      analytics.setLocalDispatchPeriod(30);
      Tracker t = analytics.newTracker(R.xml.app_tracker);
      mTrackers.put(trackerId, t);
    }
    return mTrackers.get(trackerId);
  }
}
