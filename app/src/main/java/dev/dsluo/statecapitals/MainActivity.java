package dev.dsluo.statecapitals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dev.dsluo.statecapitals.database.Quiz;

//splash screen
public class MainActivity extends AppCompatActivity {
    Button splashStart;
    Button splashResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashStart = (Button) findViewById(R.id.splashStart);
        splashResults = (Button) findViewById(R.id.splashResults);

    }

    private class StartClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), QuizActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
