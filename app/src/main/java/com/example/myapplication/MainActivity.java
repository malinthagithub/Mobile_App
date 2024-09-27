package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recipeRecyclerView);
        homeIcon = findViewById(R.id.homeIcon);
        categoryIcon = findViewById(R.id.categoryIcon);
        profileIcon = findViewById(R.id.profileIcon);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new CustomRecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);

        // Initialize Firebase Realtime Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("recipes");

        // Load all recipe images
        loadAllRecipeImages();

        homeIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        categoryIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, category.class)));
        profileIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, profile.class)));
        recyclerView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Recapidetails.class)));

    }

    private void loadAllRecipeImages() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear(); // Clear the existing list to avoid duplicates
                if (dataSnapshot.exists()) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        String id = recipeSnapshot.child("id").getValue(String.class);
                        String userId = recipeSnapshot.child("userId").getValue(String.class);
                        String name = recipeSnapshot.child("name").getValue(String.class); // Get recipe name
                        String imageUrl = recipeSnapshot.child("imageUrl").getValue(String.class); // Get image URL

                        if (id != null && userId != null && name != null && imageUrl != null) {
                            Recipe recipe = new Recipe(id, userId, name, imageUrl); // Create a Recipe object
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
}
