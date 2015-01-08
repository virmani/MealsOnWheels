package com.sugarchallenged.mealsonwheels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sugarchallenged.mealsonwheels.models.DayTime;
import com.sugarchallenged.mealsonwheels.models.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MenuActivity extends ActionBarActivity {
  public static String TAG = "MenuActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    Intent intent = getIntent();
    parseMenus(intent.getStringExtra(SplashScreen.MENU_JSON_INTENT_NAME));
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_menu, menu);
    return true;
  }

  protected void parseMenus(String menuString) {
    final ListView listview = (ListView) findViewById(android.R.id.list);

    try {
      JSONObject menuJson = new JSONObject(menuString);
      Map<String, FoodItem[]> breakfastMenu = getDayTimeMenu(menuJson, DayTime.BREAKFAST);
      Map<String, FoodItem[]> lunchMenu = getDayTimeMenu(menuJson, DayTime.LUNCH);
      Map<String, FoodItem[]> dinnerMenu = getDayTimeMenu(menuJson, DayTime.DINNER);

      Intent intent = new Intent(MenuActivity.this, DaytimeMenuActivity.class);
      intent.putExtras(ParcelingUtils.convertMapToBundle(lunchMenu));
      startActivity(intent);

    } catch (JSONException e) {
      showErrorDialog();
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
}
