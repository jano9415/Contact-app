package com.example.contact_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayContactActivity extends AppCompatActivity {

    private Context context;

    private int id;
    private Contact actualContact;
    private ContactRepository contactRepository;

    private TextView nameTextView, phoneNumberTextView, emailAddressTextView, addressTextView,
            birthdayTextView, relationshipTextView, editActivityButton;
    private Button callButton;
    private FloatingActionButton backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        getSupportActionBar().hide();

        context = this;
        contactRepository = new ContactRepository(context);

        //Kontakt keresése id szerint a MainActivity-től megkapott id-val.
        id = getIntent().getIntExtra("id" , 0);
        actualContact = contactRepository.findById(id);


        nameTextView = findViewById(R.id.nameTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        emailAddressTextView = findViewById(R.id.emailAddressTextView);
        addressTextView = findViewById(R.id.addressTextView);
        birthdayTextView = findViewById(R.id.birthdayTextView);
        relationshipTextView = findViewById(R.id.relationshipTextView);
        backToMain = findViewById(R.id.backToMain);

        editActivityButton = findViewById(R.id.editActivityButton);
        callButton = findViewById(R.id.callButton);

        //Kontakt adatainak megjelenítése
        nameTextView.setText(actualContact.getLastName() + " " + actualContact.getFirstName());
        phoneNumberTextView.setText(actualContact.getPhoneNumber());
        emailAddressTextView.setText(actualContact.getEmailAddress());
        addressTextView.setText(actualContact.getAddress());
        birthdayTextView.setText(actualContact.getBirthDay());
        relationshipTextView.setText(actualContact.getRelationship());

        //Visszatérés a MainActivity-re
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context , MainActivity.class));
            }
        });

        //Szerkesztés gomb megnyomása
        editActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(context , EditContactActivity.class).putExtra("id" , id));

            }
        });

        //Hívás gomb megnyomása
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + actualContact.getPhoneNumber()));
                startActivity(callIntent);

            }
        });

    }
}