package com.sugarchallenged.mealsonwheels.models;

import android.graphics.Color;

public enum FoodCategory {
  Vegetarian, SeafoodWatch, Vegan, FarmToFork, GlutenFree, Humane, Organic, Other;

  public int getColor() {
    String colorString = "#EEEEEE";
    switch (this) {
      case Vegan:
        colorString = "#99FF33";
        break;
      case Vegetarian:
        colorString = "#00CC66";
        break;
      case SeafoodWatch:
        colorString = "#85FFFF";
        break;
      case FarmToFork:
        colorString = "#CC6600";
        break;
      case GlutenFree:
        colorString = "#CCFFFF";
        break;
      case Humane:
        colorString = "#CCCC00";
        break;
      case Organic:
        colorString = "#CCCCFF";
        break;
      case Other:
        colorString = "#FFFFFF";
    }

    return Color.parseColor(colorString);
  }
}
