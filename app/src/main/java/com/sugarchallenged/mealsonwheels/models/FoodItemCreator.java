package com.sugarchallenged.mealsonwheels.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItemCreator implements Parcelable.Creator<FoodItem> {
  public FoodItem createFromParcel(Parcel source) {
    return new FoodItem(source);
  }
  public FoodItem[] newArray(int size) {
    return new FoodItem[size];
  }
}
