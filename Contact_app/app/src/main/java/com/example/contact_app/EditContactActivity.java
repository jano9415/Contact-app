package com.example.contact_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditContactActivity extends AppCompatActivity {

    private Context context;
    private ContactRepository contactRepository;
    private int id;
    private Contact actualContact;

    EditText editLastNameEditText, editFirstNameEditText, editPhoneNumberEditText, editEmailAddressEditText,
            editAddressEditText, editBirthdayEditText;
    Button editRelationshipButton, editContactButton;
    TextView backToDisplayContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        getSupportActionBar().hide();

        context = this;
        contactRepository = new ContactRepository(context);

        //Kontakt keresése id szerint a DisplayActivity-től megkapott id-val.
        id = getIntent().getIntExtra("id" , 0);
        actualContact = contactRepository.findById(id);

        backToDisplayContact = findViewById(R.id.backToDisplayContact);

        editLastNameEditText = findViewById(R.id.editLastNameEditText);
        editFirstNameEditText = findViewById(R.id.editFirstNameEditText);
        editPhoneNumberEditText = findViewById(R.id.editPhoneNumberNameEditText);
        editEmailAddressEditText = findViewById(R.id.editEmailAddressEditText);
        editAddressEditText = findViewById(R.id.editAddressEditText);
        editBirthdayEditText = findViewById(R.id.editBirthdayEditText);

        editRelationshipButton = findViewById(R.id.editRelationshipButton);
        editContactButton = findViewById(R.id.editContactButton);

        //Kontakt adatainak megjelenítése
        editLastNameEditText.setText(actualContact.getLastName());
        editFirstNameEditText.setText(actualContact.getFirstName());
        editPhoneNumberEditText.setText(actualContact.getPhoneNumber());
        editEmailAddressEditText.setText(actualContact.getEmailAddress());
        editAddressEditText.setText(actualContact.getAddress());
        editBirthdayEditText.setText(actualContact.getBirthDay());

        //Visszatérés a DisplayActivity-re.
        backToDisplayContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context , DisplayContactActivity.class).putExtra("id" , id));
            }
        });

        //Kapcsolat kiválasztása gomb megnyomása
        editRelationshipButton.setOnClickListener(new View.OnClickListener() {
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
                                    actualContact.setRelationship(charSequence[0].toString());
                                }
                                else if(i == 1){
                                    actualContact.setRelationship(charSequence[1].toString());
                                }
                                else if(i == 2){
                                    actualContact.setRelationship(charSequence[2].toString());
                                }
                            }
                        })
                        .show();

            }
        });

        //Kontakt szerkesztése gomb megnyomása
        editContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualContact.setLastName(editLastNameEditText.getText().toString());
                actualContact.setFirstName(editFirstNameEditText.getText().toString());
                actualContact.setPhoneNumber(editPhoneNumberEditText.getText().toString());
                actualContact.setEmailAddress(editEmailAddressEditText.getText().toString());
                actualContact.setAddress(editAddressEditText.getText().toString());
                actualContact.setBirthDay(editBirthdayEditText.getText().toString());

                contactRepository.updateContact(actualContact);

                startActivity(new Intent(context , DisplayContactActivity.class).putExtra("id" , id));

            }
        });
    }
}