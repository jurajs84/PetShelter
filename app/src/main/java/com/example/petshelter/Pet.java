package com.example.petshelter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pet_table")
public class Pet {

    public Pet(String name, String breed, int gender, int weight) {
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
        //this.isChecked = false;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pet_id")
    private int id;

    @ColumnInfo(name = "pet_name")
    private String name;

    @ColumnInfo(name = "pet_breed")
    private String breed;

    @ColumnInfo(name = "pet_gender")
    private int gender;

    @ColumnInfo(name = "pet_weight")
    private int weight;

//    @Ignore
//    private Boolean isChecked; //for checkbox of each item
//
//    public Boolean getChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(Boolean checked) {
//        isChecked = checked;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }

    public String getGenderAsString(){
        switch (gender){
            case 1:
                return "male";
            case 2:
                return "female";
            default:
                return "unknown";
        }
    }

    public String getWeightAsString(){
        return Integer.toString(weight);
    }

}
