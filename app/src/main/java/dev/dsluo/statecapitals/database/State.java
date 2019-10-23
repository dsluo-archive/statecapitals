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
}
