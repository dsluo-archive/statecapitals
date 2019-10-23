package dev.dsluo.statecapitals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dev.dsluo.statecapitals.database.QuizActivity;

//splash screen
public class MainActivity extends AppCompatActivity {
    Button splashStart;
    Button splashResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashStart = findViewById(R.id.splashStart);
        splashResults = findViewById(R.id.splashResults);

        splashStart.setOnClickListener((view) -> {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        });

    }
}
