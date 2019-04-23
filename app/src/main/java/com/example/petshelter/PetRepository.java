package com.example.petshelter;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PetRepository {

    private LiveData<List<Pet>> petList;
    private PetDao petDao;

    public PetRepository(Application application) {
        PetRoomDatabase db = PetRoomDatabase.getDatabase(application);
        this.petDao = db.petDao();
        this.petList = petDao.getAllPet();
    }

    public void insert(Pet... pets){
        new InsertAsyncTask(petDao).execute(pets);
    }

    public void update(Pet pet){
        new UpdateAsyncTask(petDao).execute(pet);
    }

    public void delete(Pet... pets){
        new DeleteAsyncTask(petDao).execute(pets);
    }

    public void deleteAllPet(){
        new DeleteAllAsyncTask(petDao).execute();
    }

    public LiveData<List<Pet>> getPetList() {
        return petList;
    }

    private static class InsertAsyncTask extends AsyncTask<Pet, Void, Void>{
        private PetDao petDao;

        public InsertAsyncTask(PetDao petDao) {
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            for (int i = 0; i <pets.length; i++) {
                petDao.insert(pets[i]);
            }
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Pet, Void, Void>{
        PetDao petDao;

        public UpdateAsyncTask(PetDao petDao) {
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            petDao.update(pets[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Pet, Void, Void>{
        PetDao petDao;

        public DeleteAsyncTask(PetDao petDao) {
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            for (int i = 0; i < pets.length; i++) {
                petDao.delete(pets[i]);
            }
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        PetDao petDao;

        public DeleteAllAsyncTask(PetDao petDao) {
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            petDao.deleteAll();
            return null;
        }
    }
}
