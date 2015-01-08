package com.sugarchallenged.mealsonwheels.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class FoodItem implements Parcelable {
  public static final Parcelable.Creator CREATOR = new FoodItemCreator();

  public String id;
  public String name;
  public String description;
  public String cafeName;
  public String stationName;
  public FoodCategory[] category;

  public FoodItem(Parcel parcel) {
    id = parcel.readString();
    name = parcel.readString();
    description = parcel.readString();
    cafeName = parcel.readString();
    stationName = parcel.readString();
    String[] categoryStrings = new String[parcel.readInt()];
    parcel.readStringArray(categoryStrings);

    category = new FoodCategory[categoryStrings.length];
    for (int i = 0; i < categoryStrings.length; i++) {
      category[i] = FoodCategory.valueOf(FoodCategory.class, categoryStrings[i]);
    }
  }

  public FoodItem(JSONObject jsonObject) throws JSONException {
    id = jsonObject.getString("id");
    name = jsonObject.getString("label");
    description = jsonObject.getString("description");
    cafeName = jsonObject.getString("floor");
    stationName = jsonObject.getString("station");

    JSONObject jsonCors = jsonObject.getJSONObject("cor_icon");
    category = new FoodCategory[jsonCors.length()];

    Iterator<String> keys = jsonCors.keys();

    for (int i = 0; keys.hasNext(); i++) {
      category[i] = corToCategory(jsonCors.getString(keys.next()));
    }
  }

  private FoodCategory corToCategory(String cor) {
    FoodCategory category = null;
    switch (cor) {
      case "vegetarian":
        category = FoodCategory.Vegetarian;
        break;
      case "vegan":
        category = FoodCategory.Vegan;
        break;
      case "seafood watch":
        category = FoodCategory.SeafoodWatch;
        break;
      case "farm to fork":
        category = FoodCategory.FarmToFork;
        break;
      case "Made without Gluten-Containing Ingredients":
        category = FoodCategory.GlutenFree;
        break;
      case "humane":
        category = FoodCategory.Humane;
        break;
    }

    return category;
  }

  @Override
  public String toString() {
    String categories = "";
    for (int i = 0; i < category.length; i++) {
      categories += category[i] + "|";
    }
    return String.format("id: %s, name: %s, description: %s, cafeName: %s, station: %s, categories: %s",
        id, name, description, cafeName, stationName, categories);
  }

  @Override
  public int describeContents() {
    return hashCode();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(cafeName);
    dest.writeString(stationName);
    dest.writeInt(category.length);

    String[] categories = new String[category.length];
    for (int i = 0; i < category.length; i++) {
      categories[i] = category[i].toString();
    }

    dest.writeStringArray(categories);
  }
}