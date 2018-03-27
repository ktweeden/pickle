package com.example.katepreston.food_tracker.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.katepreston.food_tracker.Database.Helpers.FoodDbHelper;
import com.example.katepreston.food_tracker.Database.Helpers.FoodGroupDbHelper;
import com.example.katepreston.food_tracker.Database.Helpers.MealDbHelper;
import com.example.katepreston.food_tracker.Models.Food;
import com.example.katepreston.food_tracker.Models.FoodGroup;
import com.example.katepreston.food_tracker.Models.Meal;
import com.example.katepreston.food_tracker.Models.Utils;
import com.example.katepreston.food_tracker.R;

import java.util.ArrayList;
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Intent intent = getIntent();
        Meal meal = (Meal) intent.getSerializableExtra("meal");

        FoodGroupDbHelper foodGroupHelper = new FoodGroupDbHelper(this);


        ArrayList<String> groupNames = new ArrayList<>();
        for(FoodGroup group : foodGroupHelper.findAll()) {
            groupNames.add(group.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, groupNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.food_group_selection);
        spinner.setAdapter(adapter);

        Button submit = findViewById(R.id.submit_new_food_button);
        submit.setTag(meal.getId());

    }

    public void onSubmitNewFoodClick(View submitNewFood) {
        FoodGroupDbHelper groupDbHelper = new FoodGroupDbHelper(this);
        FoodDbHelper foodDbHelper = new FoodDbHelper(this);
        MealDbHelper mealDbHelper = new MealDbHelper(this);

        Long mealId = (Long) submitNewFood.getTag();

        EditText foodName = findViewById(R.id.food_name_input);
        String name = foodName.getText().toString();

        Spinner spinner = findViewById(R.id.food_group_selection);
        FoodGroup group = groupDbHelper.findByName(spinner.getSelectedItem().toString()).get(0);

        Food food = new Food(name, group.getId(), mealId);
        foodDbHelper.save(food);

        Meal meal = mealDbHelper.findById(mealId).get(0);

        Intent intent = new Intent(this, SingleMealActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }
}
