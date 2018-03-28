package com.example.katepreston.food_tracker.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.katepreston.food_tracker.Adaptors.FoodAdaptor;
import com.example.katepreston.food_tracker.Adaptors.MealAdaptor;
import com.example.katepreston.food_tracker.Database.Helpers.FoodDbHelper;
import com.example.katepreston.food_tracker.Database.Helpers.MealDbHelper;
import com.example.katepreston.food_tracker.Database.Helpers.SeedDbHelper;
import com.example.katepreston.food_tracker.Models.Food;
import com.example.katepreston.food_tracker.Models.Meal;
import com.example.katepreston.food_tracker.Models.Utils;
import com.example.katepreston.food_tracker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Fragment {

    private ArrayList<Meal> meals;
    private HashMap<Meal, ArrayList<Food>> foods;
    private Context context;
    private MealAdaptor mealAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        SeedDbHelper.seed(context);
        prepareListData();

        View view = inflater.inflate(R.layout.activity_main, container, false);

        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.meal_list);
        mealAdaptor = new MealAdaptor(getActivity(), this.meals, this.foods);
        listView.setAdapter(mealAdaptor);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Food selectedFood = (Food) mealAdaptor.getChild(groupPosition, childPosition);
                Intent intent = new Intent(context, SingleFoodActivity.class);
                intent.putExtra("food", selectedFood);
                startActivity(intent);

                return false;
            }
        });

        return view;
    }


    public void onAddNewMealClick(View listFoods) {
        Intent intent = new Intent(context, AddMealActivity.class);
        startActivity(intent);
    }

    public void prepareListData() {
        MealDbHelper mealDbHelper = new MealDbHelper(context);
        FoodDbHelper foodDbHelper = new FoodDbHelper(context);
        this.meals = mealDbHelper.findAll();
        this.foods = new HashMap<>();

        for (Meal meal : this.meals) {
            ArrayList<Food>foodList = foodDbHelper.findByMealid(meal.getId());
            this.foods.put(meal, foodList);
        }
    }
    
}
