package dev.dsluo.statecapitals.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dev.dsluo.statecapitals.R;

/**
 * Main Activity
 *
 * @author David Luo
 * @author Darren Ing
 */
public class MainActivity extends AppCompatActivity {
    private Button splashStart;
    private Button splashResults;

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
        splashResults.setOnClickListener((view) -> {
            Intent intent = new Intent(this, ResultsActivity.class);
            startActivity(intent);
        });
    }
}
