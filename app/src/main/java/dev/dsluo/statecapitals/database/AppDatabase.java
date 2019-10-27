package dev.dsluo.statecapitals.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.dsluo.statecapitals.R;

/**
 * @author David Luo
 */
@Database(entities = {State.class, Quiz.class, Question.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "capitals-db";

    private static volatile AppDatabase instance;

    /**
     * Get instance of the database.
     *
     * @param context The application context.
     * @return An instance of {@link AppDatabase}
     */
    public static synchronized AppDatabase getInstance(final Context context) {
//        File dbs = new File(context.getApplicationInfo().dataDir + "/databases");
//        File dbi = new File(dbs, DATABASE_NAME);
//        dbi.delete();

        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            initDatabase(context, instance);
        }
        return instance;
    }

    /**
     * Initialize the database from the state_capitals.csv raw resource.
     *
     * @param context  The application context.
     * @param instance An instance of {@link AppDatabase}
     */
    private static synchronized void initDatabase(
            final Context context,
            final AppDatabase instance) {

        // Skip if database file exists.
        File dbFile = context.getDatabasePath(AppDatabase.DATABASE_NAME);
        if (dbFile.exists())
            return;


        // Construct CSV reader
        InputStream stream = context.getResources().openRawResource(R.raw.state_capitals);
        CSVReaderHeaderAware csvReader;
        try {
            csvReader = new CSVReaderHeaderAware(new InputStreamReader(stream));
        } catch (IOException e) {
            throw new RuntimeException("state_capitals.csv could not be opened", e);
        }

        // Construct list of states from CSV lines.
        List<State> states = new ArrayList<>();
        Map<String, String> line;
        while (true) {
            try {
                if ((line = csvReader.readMap()) == null) break;
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException("state_captials.csv could not be parsed.", e);
            }

            //noinspection ConstantConditions
            states.add(new State(
                    line.get("State"),
                    line.get("Capital city"),
                    line.get("Second city"),
                    line.get("Third city"),
                    Integer.parseInt(line.get("Statehood")),
                    Integer.parseInt(line.get("Capital since")),
                    Integer.parseInt(line.get("Size rank"))
            ));
        }

        // Put states into database.
        instance.stateDao().insertAll(states.toArray(new State[0]));
    }

    public abstract StateDao stateDao();

    public abstract QuizDao quizDao();
}
