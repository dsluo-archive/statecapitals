package dev.dsluo.statecapitals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.dsluo.statecapitals.database.AppDatabase;
import dev.dsluo.statecapitals.database.entities.City;
import dev.dsluo.statecapitals.database.entities.State;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private AppDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getCleanInstance(context);
    }

    @After
    public void tearDown() {
        if (db != null)
            db.close();
    }

    @Test
    public void testInitialization() {
        List<State> states = db.stateDao().getAll();
        assertEquals(50, states.size());

        List<City> cities = db.cityDao().getAll();
        assertEquals(150, cities.size());

        Set<Long> capitalIds = new HashSet<>();
        states.forEach(state -> capitalIds.add(state.capitalId));

        List<City> capitals = new ArrayList<>(cities);
        capitals.removeIf(city -> !capitalIds.contains(city.id));

        assertEquals(50, capitals.size());
    }
}
