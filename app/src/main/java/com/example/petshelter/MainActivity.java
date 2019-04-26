package com.example.petshelter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_PET_REQUEST = 1;
    public static final int EDIT_PET_REQUEST = 2;

    private PetViewModel petViewModel;
    private PetRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PetRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        petViewModel = ViewModelProviders.of(this).get(PetViewModel.class);
        petViewModel.getAllPetList().observe(this, allPetList ->{
            //update UI
            adapter.setAllPetList(allPetList);//sending allList to adapter
        });

        FloatingActionButton addButton = findViewById(R.id.add_fab_button);
        addButton.setOnClickListener(view ->{
            Intent intent = new Intent(this, AddEditPet.class);
            startActivityForResult(intent, ADD_PET_REQUEST);
        });

        //continuing creating clickable Pets
        //4. implementing setOnItemClickListener from our adapter
        adapter.setOnItemClickListener(pet -> {
            //because we are in anonymous class we cannot call only this
            Intent intent = new Intent(MainActivity.this, AddEditPet.class);
            //putting data to AddEditPet.class
            intent.putExtra(AddEditPet.EXTRA_NAME, pet.getName());
            intent.putExtra(AddEditPet.EXTRA_BREED, pet.getBreed());
            intent.putExtra(AddEditPet.EXTRA_GENDER, pet.getGender());
            intent.putExtra(AddEditPet.EXTRA_WEIGHT, pet.getWeight());
            //add pet id to distinguish in AddEdit activity if it is editing or adding
            intent.putExtra(AddEditPet.EXTRA_ID, pet.getId());
            //pay attention to select ADD_EDIT_REQUEST
            startActivityForResult(intent, EDIT_PET_REQUEST);
        });

        adapter.setOnLongClickListener(() -> {
            //force onCreateOptionsMenu when checkboxes are visible to change menu icon
            //change menu icon back to three dots or trash icon
            invalidateOptionsMenu();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PET_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddEditPet.EXTRA_NAME);
            String breed = data.getStringExtra(AddEditPet.EXTRA_BREED);
            int gender = data.getIntExtra(AddEditPet.EXTRA_GENDER, 0);
            int weight = data .getIntExtra(AddEditPet.EXTRA_WEIGHT, 0);

            Pet pet = new Pet(name, breed, gender, weight);
            petViewModel.insert(pet);
            Toast.makeText(this, "Pet saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PET_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditPet.EXTRA_NAME);
            String breed = data.getStringExtra(AddEditPet.EXTRA_BREED);
            int gender = data.getIntExtra(AddEditPet.EXTRA_GENDER, 0);
            int weight = data.getIntExtra(AddEditPet.EXTRA_WEIGHT, 0);
            int id = data.getIntExtra(AddEditPet.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Pet cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }


            Pet pet = new Pet(name, breed, gender, weight);
            pet.setId(id);
            petViewModel.update(pet);
            Toast.makeText(this, "Pet updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pet not saved", Toast.LENGTH_SHORT).show();
        } 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (adapter.isCheckBoxesVisible()) {
            //trash icon
            menuInflater.inflate(R.menu.main_activity_menu_delete_only, menu);
            return true;
        } else {
            //three dots icon
            menuInflater.inflate(R.menu.main_activity_menu, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_all:
                petViewModel.deleteAll();
                return true;
            case R.id.add_dummy_pets:
                Pet[] dummyPets = {new Pet("pet1", "breed1", 1, 11),
                                   new Pet("pet2", "breed2", 1, 12),
                                   new Pet("pet3", "breed3", 1, 13),
                                   new Pet("pet4", "breed2", 1, 12),
                                   new Pet("pet5", "breed2", 1, 12),
                                   new Pet("pet6", "breed2", 1, 12),
                                   new Pet("pet7", "breed2", 1, 12),
                                   new Pet("pet8", "breed2", 1, 12),
                                   new Pet("pet9", "breed2", 1, 12),
                                   new Pet("pet10", "breed2", 1, 12),
                                   new Pet("pet11", "breed2", 1, 12),
                                   new Pet("pet12", "breed2", 1, 12),};
                petViewModel.insert(dummyPets);
                return true;
            case R.id.delete_selected_pets://trash icon
                petViewModel.delete(adapter.getDeletePetsArray());
                /*
                after deleting pets it will hide checkboxes and set false to checkBoxState and
                we need change menu icon back to three dots
                */
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when checkboxes are visible, back button will hide them
    @Override
    public void onBackPressed() {
        if (adapter.isCheckBoxesVisible()) {
            adapter.hideCheckBoxes();
            //change menu icon back to three dots
            invalidateOptionsMenu();
        } else {
            finish();
        }
    }
}
