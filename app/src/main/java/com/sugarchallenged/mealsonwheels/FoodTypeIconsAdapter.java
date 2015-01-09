package com.sugarchallenged.mealsonwheels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class FoodTypeIconsAdapter extends ArrayAdapter<Integer> {
  private static int ItemLayoutResource = R.layout.foodtype_icon_listitem;

  private final Context context;
  private final Integer[] values;

  public FoodTypeIconsAdapter(Context context, Integer[] objects) {
    super(context, ItemLayoutResource, objects);
    this.context = context;
    this.values = objects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageViewHolder holder;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(ItemLayoutResource, parent, false);

      holder = new ImageViewHolder();
      holder.imageView = (ImageView) convertView.findViewById(R.id.foodtype_icon);
      holder.position = position;

      convertView.setTag(holder);
    } else {
      holder = (ImageViewHolder) convertView.getTag();
    }

    holder.imageView.setImageResource(values[position]);
    return convertView;
  }

  private static class ImageViewHolder {
    ImageView imageView;
    int position;
  }
}
