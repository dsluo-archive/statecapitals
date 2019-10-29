package dev.dsluo.statecapitals.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.Repository;
import dev.dsluo.statecapitals.database.entities.Quiz;
import dev.dsluo.statecapitals.database.entities.dumbwiths.QuizWithQuestions;

public class ResultsActivity extends AppCompatActivity {


    private ListView resultsListView;
    private List<Quiz> quizList;
    private List<Float> results;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsListView = (ListView) findViewById(R.id.results_list);
        resultsListView.setAdapter(adapter);

        new GetResultsTask();

    }

    private static class GetResultsTask extends AsyncTask<Void, Void, Pair<List<Quiz>, List<Float>>> {

        private WeakReference<ResultsActivity> activityReference;

        @Override
        protected Pair<List<Quiz>, List<Float>> doInBackground(Void... voids) {
            ResultsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return null;

            Repository repo = Repository.newInstance(activity);

            Pair<List<Quiz>, List<Float>> quizzesAndScores = repo.getAllQuizzesAndScores();

            return quizzesAndScores;
        }

        @Override
        protected void onPostExecute(Pair<List<Quiz>, List<Float>> quizzesAndScores) {
            super.onPostExecute(quizzesAndScores);
            ResultsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return;

            activity.quizList = quizzesAndScores.first;
            activity.results = quizzesAndScores.second;
            activity.adapter.notifyDataSetChanged();
        }
    }

}
