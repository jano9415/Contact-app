package com.example.contact_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddContactActivity extends AppCompatActivity {

    private ContactRepository contactRepository;
    private Context context;

    private Button addRelationshipButton, addContactButton;
    private EditText lastNameEditText, firstNameEditText, phoneNumberEditText, emailAddressEditText,
            addressEditText, birthdayEditText;

    private FloatingActionButton backToMain2;

    private String relationShip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().hide();

        context = this;
        contactRepository = new ContactRepository(context);

        addRelationshipButton = findViewById(R.id.addRelationshipButton);
        addContactButton = findViewById(R.id.addContactButton);

        lastNameEditText = findViewById(R.id.lastNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        addressEditText = findViewById(R.id.addressEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);

        backToMain2 = findViewById(R.id.backToMain2);

        //Visszatérés a MainActivity-re
        backToMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context , MainActivity.class));
            }
        });

        //Kapcsolat hozzáadása
        //Alertdialog megjelenítése három kiválasztható lehetőséggel: családtag, munkatárs, egyéb.
        addRelationshipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Kapcsolat kiválasztása alert dialog
                CharSequence charSequence[] = {"családtag" , "munkatárs" , "egyéb"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Kapcsolat kiválasztása")
                        .setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    relationShip = "családtag";
                                }
                                else if(i == 1){
                                    relationShip = "munkatárs";
                                }
                                else if(i == 2){
                                    relationShip = "egyéb";
                                }
                            }
                        })
                        .show();
            }
        });

        //Új kontakt hozzáadása
        //Visszatérés a Main activity-re
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lastName = lastNameEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String emailAddress = emailAddressEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String birthday = birthdayEditText.getText().toString().trim();

                Contact actualContact = new Contact(firstName, lastName, phoneNumber, emailAddress, address, birthday, relationShip);

                if(validateDatas(firstName,lastName,phoneNumber)){
                    contactRepository.addContact(actualContact);
                    startActivity(new Intent(context , MainActivity.class));
                }

            }
        });
    }

    //Megadott adatok validációja
    //A vezetéknév, keresztnév és telefonszám mezőt kötelező kitölteni.
    public boolean validateDatas(String firstName , String lastName , String phonenumber){

        if(lastName.isEmpty()){
            lastNameEditText.setError("Add meg a vezetéknevet!");
            lastNameEditText.requestFocus();
            return false;
        }
        if(firstName.isEmpty()){
            firstNameEditText.setError("Add meg a keresztnevet!");
            firstNameEditText.requestFocus();
            return false;
        }
        if(phonenumber.isEmpty()){
            phoneNumberEditText.setError("Add meg a telefonszámot!");
            phoneNumberEditText.requestFocus();
            return false;
        }
        return true;

    }
}