package com.example.petshelter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEditPet extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.petshelter/EXTRA_NAME";
    public static final String EXTRA_BREED = "com.example.petshelter/EXTRA_BREED";
    public static final String EXTRA_GENDER = "com.example.petshelter/EXTRA_GENDER";
    public static final String EXTRA_WEIGHT = "com.example.petshelter/EXTRA_WEIGHT";
    public static final String EXTRA_ID = "com.example.petshelter/EXTRA_ID";

    private EditText petName;
    private EditText petBreed;
    private EditText petWeight;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pet);

        petName = findViewById(R.id.pet_name_editText);
        petBreed = findViewById(R.id.pet_breed_editText);
        petWeight = findViewById(R.id.pet_weight_editText);
         //Spinner settings
        genderSpinner = findViewById(R.id.gender_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the spinnerAdapter to the spinner
        genderSpinner.setAdapter(spinnerAdapter);

        //set proper title of up bar (difference btw FAB(new Note) and Note item click(edit Note)
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit pet");
            //setting edit texts to proper values
            petName.setText(intent.getStringExtra(EXTRA_NAME));
            petBreed.setText(intent.getStringExtra(EXTRA_BREED));
            petWeight.setText(String.valueOf(intent.getIntExtra(EXTRA_WEIGHT, 0)));
            genderSpinner.setSelection(intent.getIntExtra(EXTRA_GENDER, 0));
        }
        else {
            setTitle("Add pet");
        }
    }

    //menu in upper bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return true;
    }
    //menu listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_pet:
                savePet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    }

    private void savePet() {
        String name = petName.getText().toString().trim();
        String breed = petBreed.getText().toString().trim();
        String weightAsString = petWeight.getText().toString().trim();
        int weight = makeInt(weightAsString);
        //Gender: 0 - Unknown, 1 - Male, 2 - Female
        int gender = genderSpinner.getSelectedItemPosition();

        if (name.isEmpty()) {
            Toast.makeText(this, "Add name of pet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (breed.isEmpty()){
            breed = "unknown";
        }
        //sending data to MainActivity
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_BREED, breed);
        intent.putExtra(EXTRA_GENDER, gender);
        intent.putExtra(EXTRA_WEIGHT, weight);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    private int makeInt(String weightAsString) {
        if (weightAsString.isEmpty()){
            return 0;
        }
        return Integer.parseInt(weightAsString);
    }
}
