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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import dev.dsluo.statecapitals.R;
import dev.dsluo.statecapitals.database.converters.DateConverter;
import dev.dsluo.statecapitals.database.daos.AnswerDao;
import dev.dsluo.statecapitals.database.daos.CityDao;
import dev.dsluo.statecapitals.database.daos.QuestionDao;
import dev.dsluo.statecapitals.database.daos.QuizDao;
import dev.dsluo.statecapitals.database.daos.StateDao;
import dev.dsluo.statecapitals.database.entities.Answer;
import dev.dsluo.statecapitals.database.entities.City;
import dev.dsluo.statecapitals.database.entities.Question;
import dev.dsluo.statecapitals.database.entities.Quiz;
import dev.dsluo.statecapitals.database.entities.State;

/**
 * An implementation of {@link RoomDatabase}.
 *
 * @author David Luo
 */
@Database(
        entities = {
                State.class,
                City.class,
                Quiz.class,
                Question.class,
                Answer.class,
        },
        version = 1
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "capitals-db";

    private static volatile AppDatabase instance;

    /**
     * Helper method to delete the database if it exists.
     *
     * @param context The application context.
     */
    private static synchronized void deleteDatabase(final Context context) {
        if (instance != null) {
            instance.close();
            instance = null;
        }
        File dbs = new File(context.getApplicationInfo().dataDir + "/databases");
        File dbi = new File(dbs, DATABASE_NAME);
        //noinspection ResultOfMethodCallIgnored
        dbi.delete();
    }

    /**
     * Deletes the database if it exists and initializes a new one.
     *
     * @param context The application context.
     * @return A clean instance of {@link AppDatabase}
     */
    public static synchronized AppDatabase getCleanInstance(final Context context) {
        deleteDatabase(context);
        return getInstance(context);
    }

    /**
     * Get instance of the database.
     *
     * @param context The application context.
     * @return An instance of {@link AppDatabase}
     */
    public static synchronized AppDatabase getInstance(final Context context) {

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

        instance.runInTransaction(() -> {
            // Construct list of states from CSV lines.
            Map<String, String> line;
            while (true) {
                // Read the CSV line, if available.
                try {
                    if ((line = csvReader.readMap()) == null) break;
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException("state_captials.csv could not be parsed.", e);
                }

                // Insert cities
                City capital = new City(line.get("Capital city"));
                List<City> cities = Arrays.asList(
                        capital,
                        new City(line.get("Second city")),
                        new City(line.get("Third city"))
                );
                List<Long> cityIds = instance.cityDao().insertAll(cities);
                for (int i = 0; i < cities.size(); i++) {
                    long id = cityIds.get(i);
                    cities.get(i).id = (int) id;
                }

                //noinspection ConstantConditions
                State state = new State(
                        line.get("State"),
                        capital.id,
                        Integer.parseInt(line.get("Statehood")),
                        Integer.parseInt(line.get("Capital since")),
                        Integer.parseInt(line.get("Size rank"))
                );

                long id = instance.stateDao().insert(state);

                for (City city : cities) {
                    city.stateId = (int) id;
                    instance.cityDao().update(city);
                }
            }
        });

    }

    // The DAOs

    public abstract StateDao stateDao();

    public abstract CityDao cityDao();

    public abstract QuizDao quizDao();

    public abstract QuestionDao questionDao();

    public abstract AnswerDao answerDao();
}
