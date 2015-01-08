package com.sugarchallenged.mealsonwheels;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jpardogo.android.flabbylistview.lib.FlabbyLayout;
import com.sugarchallenged.mealsonwheels.models.FoodItem;

import java.util.Random;

public class FoodItemListAdapter extends ArrayAdapter<FoodItem> {
  private final Random random;

  private final Context context;
  private final FoodItem[] values;

  private static int ItemLayout = R.layout.menu_list_item;

  public FoodItemListAdapter(Context context, FoodItem[] values) {
    super(context, ItemLayout, values);
    this.random = new Random();
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
      holder.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
      holder.position = position;

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.foodNameView.setText(values[position].name);
    holder.foodDescriptionView.setText(values[position].description);
    ((FlabbyLayout) convertView).setFlabbyColor(holder.color);


    return convertView;
  }

  static class ViewHolder {
    TextView foodNameView;
    TextView foodDescriptionView;
    int color;
    int position;
  }

}
