package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomRecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private DatabaseReference mDatabase;
    private ImageView homeIcon;
    private ImageView categoryIcon;
    private ImageView profileIcon;
    private EditText searchBar; // EditText for search input
    private Button searchButton; // Button for search action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recipeRecyclerView);
        homeIcon = findViewById(R.id.homeIcon);
        categoryIcon = findViewById(R.id.categoryIcon);
        profileIcon = findViewById(R.id.profileIcon);
        searchBar = findViewById(R.id.searchBar); // Get the search bar reference
        searchButton = findViewById(R.id.searchButton); // Get the search button reference

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new CustomRecipeAdapter(recipeList, this);
        recyclerView.setAdapter(recipeAdapter);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("recipes");

        // Load all recipe images
        loadAllRecipeImages();

        // Set click listeners for icons
        homeIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        categoryIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, category.class)));
        profileIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, profile.class)));

        // Set click listener for the search button
        searchButton.setOnClickListener(v -> {
            String searchText = searchBar.getText().toString().trim();
            if (!searchText.isEmpty()) {
                searchRecipes(searchText);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a recipe name to search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllRecipeImages() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear(); // Clear the existing list
                if (dataSnapshot.exists()) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        String recipeId = recipeSnapshot.child("recipeId").getValue(String.class);
                        String userId = recipeSnapshot.child("userId").getValue(String.class);
                        String name = recipeSnapshot.child("name").getValue(String.class);
                        String imageUrl = recipeSnapshot.child("imageUrl").getValue(String.class);
                        String cookingTime = recipeSnapshot.child("cookingTime").getValue(String.class);

                        // Get ingredients and instructions
                        ArrayList<String> ingredients = new ArrayList<>();
                        ArrayList<String> instructions = new ArrayList<>();

                        for (DataSnapshot ingredientSnapshot : recipeSnapshot.child("ingredients").getChildren()) {
                            String ingredient = ingredientSnapshot.getValue(String.class);
                            if (ingredient != null) {
                                ingredients.add(ingredient);
                            }
                        }

                        for (DataSnapshot instructionSnapshot : recipeSnapshot.child("instructions").getChildren()) {
                            String instruction = instructionSnapshot.getValue(String.class);
                            if (instruction != null) {
                                instructions.add(instruction);
                            }
                        }

                        // Create a Recipe object and add it to the list
                        if (recipeId != null && userId != null && name != null && imageUrl != null && cookingTime != null) {
                            Recipe recipe = new Recipe(recipeId, userId, name, cookingTime, ingredients, instructions, imageUrl, null);
                            recipeList.add(recipe);
                        }
                    }
                    recipeAdapter.notifyDataSetChanged(); // Notify adapter about data changes
                } else {
                    Toast.makeText(MainActivity.this, "No recipes available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchRecipes(String searchText) {
        List<Recipe> filteredList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            if (recipe.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(recipe);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No recipes found", Toast.LENGTH_SHORT).show();
        } else {
            recipeAdapter.updateList(filteredList); // Update the adapter with the filtered list
        }
    }
}
