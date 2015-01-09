package com.sugarchallenged.mealsonwheels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sugarchallenged.mealsonwheels.models.FoodItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DaytimeMenuFragment extends Fragment {

  CafePagerAdapter pagerAdapter;
  ViewPager mViewPager;
  Map<String, FoodItem[]> cafeItemsMap;

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.daytime_menu, container, false);
    mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

    cafeItemsMap = ParcelingUtils.convertBundleToMap(getArguments());
    pagerAdapter = new CafePagerAdapter(cafeItemsMap, getActivity(), getChildFragmentManager());

    mViewPager.setAdapter(pagerAdapter);
    return rootView;
  }

  public static class CafePagerAdapter extends FragmentPagerAdapter {

    private List<String> fragmentTitles = new ArrayList<>();
    private List<FoodItem[]> fragmentItems = new ArrayList<>();
    private Context context;

    public CafePagerAdapter(Map<String, FoodItem[]> cafeItemsMap, Context context, FragmentManager fm) {
      super(fm);

      Iterator<Map.Entry<String, FoodItem[]>> entries = cafeItemsMap.entrySet().iterator();
      while(entries.hasNext()) {
        Map.Entry<String, FoodItem[]> entry = entries.next();
        fragmentTitles.add(entry.getKey());
        fragmentItems.add(entry.getValue());
      }
      this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = new CafeMenuFragment();
      Bundle args = new Bundle();
      args.putParcelableArray(context.getString(R.string.cafe_food_items),
          fragmentItems.get(position));
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public int getCount() {
      return fragmentTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return fragmentTitles.get(position);
    }
  }

  public static class CafeMenuFragment extends Fragment {
    private FoodItem[] foodItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      foodItems = ParcelingUtils.convertToFoodItems(getArguments().getParcelableArray(
          getActivity().getString(R.string.cafe_food_items)));

      View rootView = inflater.inflate(R.layout.cafe_menu, container, false);
      final ListView listview = (ListView) rootView.findViewById(android.R.id.list);
      final ArrayAdapter adapter = new FoodItemListAdapter(this.getActivity(), foodItems);
      listview.setAdapter(adapter);

      return rootView;
    }
  }
}
