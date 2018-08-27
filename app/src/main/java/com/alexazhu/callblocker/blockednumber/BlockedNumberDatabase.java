package com.alexazhu.callblocker.blockednumber;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {BlockedNumber.class}, version = 1)
@TypeConverters({BlockedNumber.class})
public abstract class BlockedNumberDatabase extends RoomDatabase {
    private static final String DB_NAME = "blockednumbers.db";

    public static volatile BlockedNumberDatabase INSTANCE;

    public abstract BlockedNumberDao blockedNumberDao();

    public static BlockedNumberDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BlockedNumberDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            BlockedNumberDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
