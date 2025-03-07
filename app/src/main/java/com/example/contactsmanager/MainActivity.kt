package com.example.contactsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ContactAdapter adapter;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_contacts);
        EditText searchBar = findViewById(R.id.search_bar);
        FloatingActionButton fabAddContact = findViewById(R.id.fab_add_contact);

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList, this::onContactClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadContacts();

        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_add_contact.class);
            startActivityForResult(intent, 1);
        });

        searchBar.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(String text) {
                filterContacts(text);
            }
        });
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = dbHelper.getAllContacts();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                contactList.add(new Contact(id, name, mobile, email, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void filterContacts(String query) {
        ArrayList<Contact> filteredList = new ArrayList<>();
        for (Contact contact : contactList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        adapter.updateList(filteredList);
    }

    private void onContactClicked(Contact contact) {
        Log.d("MainActivity", "Clicked contact ID: " + contact.getId());
        Intent intent = new Intent(MainActivity.this, activity_contact_details.class);
        intent.putExtra("CONTACT_ID", contact.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadContacts();
        }
    }
}
