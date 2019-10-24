package dev.dsluo.statecapitals.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import static dev.dsluo.statecapitals.Constants.DATABASE_NAME;

@Database(entities = {State.class, Quiz.class, Question.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract StateDao stateDao();

    public abstract QuizDao quizDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null)
                    instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return instance;
    }
}
