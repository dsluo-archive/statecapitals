package dev.dsluo.statecapitals.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface StateDao {

    @Query("SELECT * FROM states")
    List<State> getAll();

    // only 50 states, so whatever
    @Query("SELECT * FROM states ORDER BY RANDOM() LIMIT :count")
    List<State> getRandoms(int count);

    @Insert(entity = State.class)
    void insertAll(State... states);
}
