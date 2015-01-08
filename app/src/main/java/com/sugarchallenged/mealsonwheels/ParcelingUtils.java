package com.sugarchallenged.mealsonwheels;

import android.os.Bundle;
import android.os.Parcelable;

import com.sugarchallenged.mealsonwheels.models.FoodItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParcelingUtils {
  public static Bundle convertMapToBundle(Map<String, FoodItem[]> inputMap) {
    final Bundle bundle = new Bundle();
    final Iterator<Map.Entry<String, FoodItem[]>> iter = inputMap.entrySet().iterator();
    while (iter.hasNext()) {
      final Map.Entry<String, FoodItem[]> entry = iter.next();
      bundle.putParcelableArray(entry.getKey(), entry.getValue());
    }
    return bundle;
  }

  public static Map<String, FoodItem[]> convertBundleToMap(Bundle bundle) {
    Iterator<String> keys = bundle.keySet().iterator();
    Map<String, FoodItem[]> outputMap = new HashMap<String, FoodItem[]>(bundle.size());
    while (keys.hasNext()) {
      String key = keys.next();
      FoodItem[] value = convertToFoodItems(bundle.getParcelableArray(key));
      outputMap.put(key, value);
    }
    return outputMap;
  }

  public static FoodItem[] convertToFoodItems(Parcelable[] parcelables) {
    FoodItem[] items = new FoodItem[parcelables.length];
    for (int i = 0; i < parcelables.length; i++) {
      items[i] = (FoodItem) parcelables[i];
    }
    return items;
  }
}
