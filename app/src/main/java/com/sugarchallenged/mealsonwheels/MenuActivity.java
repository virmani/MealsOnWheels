package com.sugarchallenged.mealsonwheels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sugarchallenged.mealsonwheels.models.DayTime;
import com.sugarchallenged.mealsonwheels.models.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MenuActivity extends ActionBarActivity {
  public static String TAG = "MenuActivity";

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;

  private Map<String, Map<String, FoodItem[]>> daytimeMenuMap;
  private ArrayList<String> drawerList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    ((MealsApplication) getApplication()).getTracker(MealsApplication.TrackerName.APP_TRACKER);

    Intent intent = getIntent();
    Map<String, Map<String, FoodItem[]>> daytimeMenus =
        parseMenus(intent.getStringExtra(SplashScreen.MENU_JSON_INTENT_NAME));
    drawerList = new ArrayList<>(daytimeMenus.keySet());

    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerList));

    mDrawerToggle = new ActionBarDrawerToggle(
        this,
        mDrawerLayout,
        R.string.drawer_open,
        R.string.drawer_close) {

      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        getSupportActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        getSupportActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    mDrawerLayout.setDrawerListener(mDrawerToggle);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    scrollToCurrentMeal();
  }

  private void scrollToCurrentMeal() {
    if (drawerList.size() != 0) {
      int mealToShow = drawerList.indexOf(MealUtils.getMealToShow());
      selectItem(mealToShow != -1 ? mealToShow : 0);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //getMenuInflater().inflate(R.menu.menu_menu, menu);
    return true;
  }

  protected Map<String, Map<String, FoodItem[]>> parseMenus(String menuString) {
    daytimeMenuMap = new HashMap<String, Map<String, FoodItem[]>>();
    final ListView listview = (ListView) findViewById(android.R.id.list);

    try {
      JSONObject menuJson = new JSONObject(menuString);
      Map<String, FoodItem[]> breakfastMenu = getDayTimeMenu(menuJson, DayTime.BREAKFAST);
      Map<String, FoodItem[]> lunchMenu = getDayTimeMenu(menuJson, DayTime.LUNCH);
      Map<String, FoodItem[]> dinnerMenu = getDayTimeMenu(menuJson, DayTime.DINNER);

      addMealToDaytimeMap(DayTime.BREAKFAST, breakfastMenu);
      addMealToDaytimeMap(DayTime.LUNCH, lunchMenu);
      addMealToDaytimeMap(DayTime.DINNER, dinnerMenu);

    } catch (JSONException e) {
      showErrorDialog();
    }

    return daytimeMenuMap;
  }

  private void addMealToDaytimeMap(String daytime, Map<String, FoodItem[]> menu) {
    if (menu != null && menu.size() != 0) {
      daytimeMenuMap.put(daytime, menu);
    }
  }

  private Map<String, FoodItem[]> getDayTimeMenu(JSONObject menuJson, String daytime) throws JSONException {
    Map<String, FoodItem[]> daytimeMenu = new HashMap<String, FoodItem[]>(menuJson.length());

    JSONObject cafeJson = menuJson.optJSONObject(daytime);
    if (cafeJson != null) {
      Iterator<String> cafes = cafeJson.keys();

      while (cafes.hasNext()) {
        String cafeName = cafes.next();
        FoodItem[] items = getFoodItems(cafeJson.getJSONArray(cafeName));
        daytimeMenu.put(cafeName, items);
      }
    }

    return daytimeMenu;
  }

  private FoodItem[] getFoodItems(JSONArray jsonItems) throws JSONException {

    FoodItem[] items = new FoodItem[jsonItems.length()];
    for (int i = 0; i < jsonItems.length(); i++) {
      items[i] = new FoodItem(jsonItems.getJSONObject(i));
    }

    Comparator<FoodItem> stationComparator = new Comparator<FoodItem>() {
      @Override
      public int compare(FoodItem lhs, FoodItem rhs) {
        return lhs.stationName.compareTo(rhs.stationName);
      }
    };

    Arrays.sort(items, stationComparator);

    return items;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  protected void showErrorDialog() {
    new AlertDialog.Builder(this)
        .setTitle("There is some gibberish in the menu that I got")
        .setMessage("Do you want to try again?")
        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            // TODO: Exit the app?
          }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
      selectItem(position);
    }
  }

  /**
   * Swaps fragments in the main content view
   */
  private void selectItem(int position) {
    Fragment fragment = new DaytimeMenuFragment();

    Bundle args = ParcelingUtils.convertMapToBundle(daytimeMenuMap.get(drawerList.get(position)));
    fragment.setArguments(args);

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .commit();

    mDrawerList.setItemChecked(position, true);
    setTitle(drawerList.get(position));
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getSupportActionBar().setTitle(mTitle);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
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
