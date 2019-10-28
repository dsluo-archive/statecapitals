package dev.dsluo.statecapitals.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Quiz model.
 *
 * @author David Luo
 */
@Entity()
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public Date completed;
}
