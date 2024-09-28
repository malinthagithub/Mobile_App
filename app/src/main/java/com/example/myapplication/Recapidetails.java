package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recapidetails extends AppCompatActivity {

    private TextView recipeNameText;
    private TextView recipeCookingTimeText;
    private TextView recipeIngredientsText;
    private TextView recipeInstructionsText;
    private VideoView recipeVideoView;  // VideoView for displaying the video

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapidetails);

        // Initialize views
        recipeNameText = findViewById(R.id.recipeNameText);
        recipeCookingTimeText = findViewById(R.id.recipeCookingTimeText);
        recipeIngredientsText = findViewById(R.id.recipeIngredientsText);
        recipeInstructionsText = findViewById(R.id.recipeInstructionsText);
        recipeVideoView = findViewById(R.id.recipeVideoView); // Initialize the VideoView

        // Get the recipe ID from the intent
        String recipeId = getIntent().getStringExtra("recipeId");

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("recipes");

        // Fetch and display the recipe details
        if (recipeId != null) {
            fetchRecipeDetails(recipeId);
        }
    }

    private void fetchRecipeDetails(String recipeId) {
        mDatabase.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the recipe details
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String cookingTime = dataSnapshot.child("cookingTime").getValue(String.class);
                    ArrayList<String> ingredients = new ArrayList<>();
                    ArrayList<String> instructions = new ArrayList<>();
                    String videoUrl = dataSnapshot.child("videoUrl").getValue(String.class); // Get video URL

                    for (DataSnapshot ingredientSnapshot : dataSnapshot.child("ingredients").getChildren()) {
                        String ingredient = ingredientSnapshot.getValue(String.class);
                        if (ingredient != null) {
                            ingredients.add(ingredient);
                        }
                    }

                    for (DataSnapshot instructionSnapshot : dataSnapshot.child("instructions").getChildren()) {
                        String instruction = instructionSnapshot.getValue(String.class);
                        if (instruction != null) {
                            instructions.add(instruction);
                        }
                    }

                    // Set the recipe details to TextViews
                    recipeNameText.setText(name);
                    recipeCookingTimeText.setText(cookingTime);
                    recipeIngredientsText.setText(String.join(", ", ingredients));
                    recipeInstructionsText.setText(String.join("\n", instructions));

                    // Set video to VideoView
                    if (videoUrl != null) {
                        Uri videoUri = Uri.parse(videoUrl);
                        recipeVideoView.setVideoURI(videoUri);
                        recipeVideoView.start();  // Start video playback
                    }
                } else {
                    Toast.makeText(Recapidetails.this, "Recipe not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Recapidetails.this, "Failed to load recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
