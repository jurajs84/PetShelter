package com.example.petshelter;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PetViewModel extends AndroidViewModel {

    LiveData<List<Pet>> allPetList;
    PetRepository petRepository;

    public PetViewModel(@NonNull Application application) {
        super(application);
        petRepository = new PetRepository(application);
        allPetList = petRepository.getPetList();
    }

    public void insert(Pet... pets){
        petRepository.insert(pets);
    }

    public void update(Pet pet){
        petRepository.update(pet);
    }

    public void delete(Pet... pets){
        petRepository.delete(pets);
    }

    public void deleteAll(){
        petRepository.deleteAllPet();
    }

    public LiveData<List<Pet>> getAllPetList() {
        return allPetList;
    }
}
