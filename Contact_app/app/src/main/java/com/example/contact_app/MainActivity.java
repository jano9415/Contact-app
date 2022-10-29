package com.example.contact_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ContactRepository contactRepository;

    private FloatingActionButton addContactButton;
    private ListView contactsListView;
    private EditText findByNameEditText;

    private List<Contact> contacts = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        context = this;
        contactRepository = new ContactRepository(context);

        addContactButton = findViewById(R.id.addContactActivityButton);
        contactsListView = findViewById(R.id.contactsListView);

        findByNameEditText = findViewById(R.id.findByNameEditText);

        showContacts("");

        //Kontakt hozzáadása activity megnyitása
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this , AddContactActivity.class));
            }
        });

        //Kattintási esemény, ha rákattintunk a listView egyik elemére.
        //Kontakt adatainak megjelenítése új activity-n.
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact actualContact = contacts.get(i);

                startActivity(new Intent(context , DisplayContactActivity.class).putExtra("id" , actualContact.getId()));
            }
        });

        //Hosszú kattintási esemény, ha rákattintunk a listView egyik elemére.
        //Kontakt törlése
        contactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact actualContact = contacts.get(i);

                CharSequence charSequence[] = {"igen" , "nem"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Biztosan törlöd a kontaktot?")
                                .setItems(charSequence, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0){
                                            contactRepository.deleteContact(actualContact);
                                            showContacts("");
                                        }
                                    }
                                })
                                .show();
                return true;
            }
        });

        findByNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showContacts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    //Kontaktok lekérése az adatbázisból.
    //Adatpter létrehozása, ami tartalmazza a kontaktok neveit.
    //Kontaktok megjelenítése a listView-ban.
    public void showContacts(String name){
        contacts.clear();

        if(!name.isEmpty()){
            contacts = contactRepository.findByLastName(name);
        }
        else{
            contacts = contactRepository.findAll();
        }

        List<String> contactNames = new ArrayList<>();

        for(Contact contact : contacts){
            contactNames.add(contact.getLastName() + " " + contact.getFirstName());
        }

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,contactNames);
        contactsListView.setAdapter(adapter);


    }
}