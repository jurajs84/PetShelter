package com.example.petshelter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PetRecyclerViewAdapter extends RecyclerView.Adapter<PetRecyclerViewAdapter.MyViewHolder> {

    private List<Pet> allPetList = new ArrayList<>();
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private Boolean checkBoxState = false;
    private OnItemClickListener clickListener;
    private OnLongClickListener longClickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }
    /*
        onBindViewHolder is called after deleting items
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pet currentPet = allPetList.get(position);
        holder.nameTextView.setText(currentPet.getName());
        holder.breedTextView.setText(currentPet.getBreed());
        holder.genderTextView.setText(currentPet.getGenderAsString());
        holder.weightTextView.setText(currentPet.getWeightAsString() + " kg");

        //set selected checkboxes back to "checked" after scrolling
        holder.bindCheckBox(position);

        //decision to show or hide all checkboxes induced by long click on LinearLayout
        //checkBoxState holds boolean if show or hide checkboxes
        if (checkBoxState) {
            holder.itemCheckBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.itemCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return allPetList.size();
    }

    public void setAllPetList(List<Pet> allPetList) {
        this.allPetList = allPetList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView breedTextView;
        TextView genderTextView;
        TextView weightTextView;
        LinearLayout linearLayout;
        CheckBox itemCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_textView);
            breedTextView = itemView.findViewById(R.id.breed_textView);
            genderTextView = itemView.findViewById(R.id.gender_textView);
            weightTextView = itemView.findViewById(R.id.weight_textView);
            linearLayout = itemView.findViewById(R.id.item_LinearLayout);
            //set listeners here in MyViewHolder NO in onBindViewHolder
            //showing and hiding checkboxes with long click
            itemView.setOnLongClickListener(v -> {
                longClickListener.onLongClick();//need it for calling it from mainActivity
                if (checkBoxState) {
                    checkBoxState = false;
                    //will call onBindViewHolder and then call decision to show or hide
                    notifyDataSetChanged();
                }
                else {
                    checkBoxState = true;
                    notifyDataSetChanged();
                }
                return true;
            });

            itemCheckBox = itemView.findViewById(R.id.item_checkbox);
            itemCheckBox.setVisibility(View.GONE);
            //set listeners here in MyViewHolder NO in onBindViewHolder
            //saving checked checkboxes to SparseBooleanArray
            itemCheckBox.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                //used SparseBooleanArray to hold selected item
                if (!sparseBooleanArray.get(adapterPosition, false)){
                    sparseBooleanArray.put(adapterPosition, true);
                }
                else {
                    sparseBooleanArray.put(adapterPosition, false);
                }
            });

            //3. creating click clickListener in our holder
            //next step in MainActivity onCreate method
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION){//crash avoiding
                    clickListener.onItemClick(allPetList.get(position));
                }
            });

        }

        // use the sparse boolean array to check desired checkboxes because of
        // unchecking during scrolling
        //it is call in onBindViewHolder
        private void bindCheckBox(int position){
            if (!sparseBooleanArray.get(position, false)) {
                itemCheckBox.setChecked(false);}
            else {
                itemCheckBox.setChecked(true);
            }
        }

    }

    //this method returns array of Pets to delete them from database
    //it iterates sparseBooleanArray and saves positions with true to Integer Array
    //than iterates this Integer Array with saved Pets positions of allPetList
    //and add Pet with demanded position to toDeleteList
    //returns array of Pets because List of Pets cannot use as varargs
    public Pet[] getDeletePetsArray() {
        List<Pet> toDeletePets = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            boolean state = sparseBooleanArray.valueAt(i);
            if (state) {
                integerList.add(sparseBooleanArray.keyAt(i));
            }
        }
        for (Integer integer : integerList) {
            toDeletePets.add(allPetList.get(integer));
        }
        checkBoxState = false;
        sparseBooleanArray.clear(); // to unselected items after deleting
        return toDeletePets.toArray(new Pet[toDeletePets.size()]);
    }

    // 1. interface for adding clickable notes
    public interface OnItemClickListener{
        void onItemClick(Pet pet);
    }

    public interface OnLongClickListener{
        void onLongClick();
    }

    //2. create method for clickable notes and chose OnItemClickListener from this package
    //do not forget to create variable clickListener in this class
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    //helper method to get state if checkboxes are visible
    public Boolean isCheckBoxesVisible(){
        return checkBoxState;
    }
    //helper method to hide checkboxes by back button
    public void hideCheckBoxes(){
        checkBoxState = false;
        notifyDataSetChanged();
    }
}
