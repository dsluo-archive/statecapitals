package dev.dsluo.statecapitals.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {State.class, Quiz.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract StateDao stateDao();

    public abstract QuizDao quizDao();
}
