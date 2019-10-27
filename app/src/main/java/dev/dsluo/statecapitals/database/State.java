package dev.dsluo.statecapitals.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "states")
public class State {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String capital;
    public String city2;
    public String city3;
    public int statehood;
    @ColumnInfo(name = "capital_since")
    public int capitalSince;
    @ColumnInfo(name = "size_rank")
    public int sizeRank;

    public State(
            String name,
            String capital,
            String city2,
            String city3,
            int statehood,
            int capitalSince,
            int sizeRank) {
        this.name = name;
        this.capital = capital;
        this.city2 = city2;
        this.city3 = city3;
        this.statehood = statehood;
        this.capitalSince = capitalSince;
        this.sizeRank = sizeRank;
    }
}
