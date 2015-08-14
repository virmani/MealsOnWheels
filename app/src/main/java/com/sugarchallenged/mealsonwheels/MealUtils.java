package com.sugarchallenged.mealsonwheels;

import com.sugarchallenged.mealsonwheels.models.DayTime;

import java.util.Calendar;

public class MealUtils {
  public static String getMealToShow() {
    Calendar cal = Calendar.getInstance();
    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
    int currentMinute = cal.get(Calendar.MINUTE);

    String meal;
    if(currentHour < 10 || (currentHour == 10 && currentMinute < 20)) {
      meal = DayTime.BREAKFAST;
    } else if((currentHour == 10 && currentMinute >= 20) || (currentHour > 10 && currentHour < 15)) {
      meal = DayTime.LUNCH;
    } else {
      meal = DayTime.DINNER;
    }

    return meal;
  }
}
