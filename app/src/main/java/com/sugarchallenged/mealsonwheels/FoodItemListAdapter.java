package com.sugarchallenged.mealsonwheels;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sugarchallenged.mealsonwheels.models.FoodCategory;
import com.sugarchallenged.mealsonwheels.models.FoodItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;

public class FoodItemListAdapter extends ArrayAdapter<FoodItem> {
  private static int[] COLORS = new int[]{
      Color.parseColor("#55ACEE"),
      Color.parseColor("#F58888"),
      Color.parseColor("#F5F8FA"),
      Color.parseColor("#F2B50F"),
      Color.parseColor("#63D290"),
      Color.parseColor("#DD4B39"),
  };

  private final Context context;
  private final FoodItem[] values;

  private static int ItemLayout = R.layout.menu_list_item;

  public FoodItemListAdapter(Context context, FoodItem[] values) {
    super(context, ItemLayout, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(ItemLayout, parent, false);

      holder = new ViewHolder();
      holder.foodDescriptionView = (TextView) convertView.findViewById(R.id.foodDescription);
      holder.foodNameView = (TextView) convertView.findViewById(R.id.foodName);
      holder.foodStationView = (TextView) convertView.findViewById(R.id.foodStation);
      holder.foodTypeIconListView = (HListView) convertView.findViewById(R.id.foodtype_icon_list);

      holder.position = position;

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.foodNameView.setText(values[position].name);
    holder.foodDescriptionView.setText(values[position].description);
    holder.foodStationView.setText(values[position].stationName);
    holder.foodTypeIconListView.setAdapter(new FoodTypeIconsAdapter(context, values[position].getCategoryImages()));
    convertView.setBackgroundColor(getRowColor(values[position].category));

    return convertView;
  }

  private int getRowColor(FoodCategory[] foodCategories) {
    int color = FoodCategory.Other.getColor();

    if(foodCategories != null && foodCategories.length > 0) {
      List<FoodCategory> categories = Arrays.asList(foodCategories);
      if(categories.contains(FoodCategory.Vegetarian)) {
        color = FoodCategory.Vegetarian.getColor();
      } else if(categories.contains(FoodCategory.Vegan)) {
        color = FoodCategory.Vegan.getColor();
      } else if(categories.contains(FoodCategory.SeafoodWatch)) {
        color = FoodCategory.SeafoodWatch.getColor();
      } else if(categories.contains(FoodCategory.GlutenFree)) {
        color = FoodCategory.GlutenFree.getColor();
      } else {
        color = foodCategories[0].getColor();
      }
    }

    return color;
  }

  private static class ViewHolder {
    TextView foodNameView;
    TextView foodDescriptionView;
    TextView foodStationView;
    HListView foodTypeIconListView;
    int position;
  }
}
