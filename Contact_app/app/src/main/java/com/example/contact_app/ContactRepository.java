package com.example.contact_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository extends SQLiteOpenHelper {

    private  Context context;

    private static  final  int VERSION = 2;
    private static  final  String DB_NAME = "contactdb.db";
    private static final String TABLE_NAME = "contact";

    private  static final String COLUMN_ID = "id";
    private  static final String COLUMN_FIRSTNAME = "firstname";
    private  static final String COLUMN_LASTNAME = "lastname";
    private  static final String COLUMN_PHONENUMBER = "phonenumber";
    private  static final String COLUMN_EMAILADDRESS = "emailaddress";
    private  static final String COLUMN_ADDRESS = "address";
    private  static final String COLUMN_BIRTHDAY = "birthday";
    private  static final String COLUMN_RELATIONSHIP = "relationship";



    public ContactRepository(@Nullable Context context) {
        super(context , DB_NAME, null, VERSION);
        this.context = context;
    }

    //Kontakt tábla létrehozása
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_LASTNAME + " TEXT, " +
                COLUMN_PHONENUMBER + " TEXT, " +
                COLUMN_EMAILADDRESS + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_BIRTHDAY + " TEXT, " +
                COLUMN_RELATIONSHIP + " TEXT);";

        sqLiteDatabase.execSQL(query);
    }

    //Kontakt tábla törlése majd létrehozása
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);

    }

    //Új kontakt hozzáadása
    public void addContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME , contact.getFirstName());
        values.put(COLUMN_LASTNAME , contact.getLastName());
        values.put(COLUMN_PHONENUMBER , contact.getPhoneNumber());
        values.put(COLUMN_EMAILADDRESS , contact.getEmailAddress());
        values.put(COLUMN_ADDRESS , contact.getAddress());
        values.put(COLUMN_BIRTHDAY , contact.getBirthDay());
        values.put(COLUMN_RELATIONSHIP , contact.getRelationship());

        long result = db.insert(TABLE_NAME,null,values);

        if(result == -1){
            Toast.makeText(context , "Nem sikerült hozzáadni a kontaktot." , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context , "Új kontakt hozzáadva." , Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    //Kontakt keresése id szerint
    public Contact findById(int id){

        Contact actualContact;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID,COLUMN_FIRSTNAME,COLUMN_LASTNAME,COLUMN_PHONENUMBER,COLUMN_EMAILADDRESS,COLUMN_ADDRESS,COLUMN_BIRTHDAY,COLUMN_RELATIONSHIP},
                COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)},
                null,null,null,null
        );

        if(cursor != null){
            cursor.moveToFirst();
            actualContact = new Contact(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            return actualContact;
        }
        return null;
    }

    //Összes kontakt lekérése
    public List<Contact> findAll(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Contact actualContact = new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                );
                contacts.add(actualContact);
            }
            while (cursor.moveToNext());
        }

        return contacts;
    }

    //Kontakt módosítása
    public void updateContact(Contact actualContact){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRSTNAME , actualContact.getFirstName());
        values.put(COLUMN_LASTNAME , actualContact.getLastName());
        values.put(COLUMN_PHONENUMBER , actualContact.getPhoneNumber());
        values.put(COLUMN_EMAILADDRESS , actualContact.getEmailAddress());
        values.put(COLUMN_ADDRESS , actualContact.getAddress());
        values.put(COLUMN_BIRTHDAY , actualContact.getBirthDay());
        values.put(COLUMN_RELATIONSHIP , actualContact.getRelationship());

        long result = db.update(
                TABLE_NAME,
                values,
                COLUMN_ID + "= ?",
                new String[]{String.valueOf(actualContact.getId())}
        );

        if(result == -1){
            Toast.makeText(context , "Nem sikerült módosítani a kontaktot." , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context , "Kontakt módosítva." , Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    //Kontakt törlése
    public void deleteContact(Contact actualContact){

        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(
                TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[]{String.valueOf(actualContact.getId())}
        );

        if(result == -1){
            Toast.makeText(context , "Nem sikerült törölni a kontaktot." , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context , "Kontakt törölve." , Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    //Kontaktok keresése vezetéknév vagy keresztnév szerint.
    public List<Contact> findByName(String name){
        Contact actualContact;
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + "(" + COLUMN_LASTNAME + " LIKE " + "'%" + name + "%'" + "OR " + COLUMN_FIRSTNAME + " LIKE " + "'%" + name + "%')";

        Cursor cursor = db.rawQuery(query , null);

        if(cursor.moveToFirst()){
            do {
                actualContact = new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                );
                contacts.add(actualContact);
            }while (cursor.moveToNext());
        }

        return contacts;
    }
}
