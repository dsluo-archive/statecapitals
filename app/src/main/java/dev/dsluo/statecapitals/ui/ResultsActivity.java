package dev.dsluo.statecapitals.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
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

/**
 * Displays and adds functionality to the results page.
 *
 * @author Darren Ing
 */
public class ResultsActivity extends AppCompatActivity {


    private ListView resultsListView;
    private ArrayAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();;

    /**
     * Populat the list-view with reviews using ArrayAdapter updated through background thread.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        resultsListView = (ListView) findViewById(R.id.results_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        resultsListView.setAdapter(adapter);

        new GetResultsTask(this).execute();
    }

    /**
     * Executes the main thread and modifies the global arrayList
     */
    private static class GetResultsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private WeakReference<ResultsActivity> activityReference;

        GetResultsTask(ResultsActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ResultsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return null;

            Repository repo = Repository.newInstance(activity);

            Pair<List<Quiz>, List<Integer>> quizzesAndScores = repo.getAllQuizzesAndScores();

            ArrayList<String> stringArrayList = new ArrayList<>();
            for(int i = 0; i < quizzesAndScores.first.size(); i++) {
                Quiz quiz = quizzesAndScores.first.get(i);
                Integer score = quizzesAndScores.second.get(i);
                String line = "Quiz " + quiz.id + " Score: " + score + " | Date: " + quiz.completed;
                stringArrayList.add(line);
            }
            return stringArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> stringArrayList) {
            super.onPostExecute(stringArrayList);
            ResultsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing())
                return;

            activity.arrayList.clear();
            for(String item : stringArrayList) {
                activity.arrayList.add(item);
            }

            activity.adapter.notifyDataSetChanged();
        }
    }

}
