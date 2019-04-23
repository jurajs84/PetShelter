package com.example.petshelter;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Pet.class, version = 1)
public abstract class PetRoomDatabase extends RoomDatabase {

    public abstract PetDao petDao();

    private static volatile PetRoomDatabase INSTANCE;

    static PetRoomDatabase getDatabase (final Context context) {
        if (INSTANCE == null){
            synchronized (PetRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PetRoomDatabase.class, "pet_database")
                            .addCallback(roomCallback) // callback for testing to fill database
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        // onCreate is called only once with creating database
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // call async class here
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        PetDao petDao;
        public PopulateDbAsyncTask(PetRoomDatabase db) {
            this.petDao = db.petDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            petDao.insert(new Pet("Pet1", "breed1", 0, 12));
            petDao.insert(new Pet("Pet2", "breed2", 1, 13));
            petDao.insert(new Pet("Pet3", "breed3", 2, 14));
            return null;
        }
    }
}
