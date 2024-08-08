package com.example.contactsmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class activity_add_contact extends AppCompatActivity {

    private EditText editTextName, editTextMobile, editTextEmail, editTextAddress;
    private Button buttonSaveContact;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        dbHelper = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editText_name);
        editTextMobile = findViewById(R.id.editText_mobile);
        editTextEmail = findViewById(R.id.editText_email);
        editTextAddress = findViewById(R.id.editText_address);
        buttonSaveContact = findViewById(R.id.button_save_contact);

        buttonSaveContact.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String mobile = editTextMobile.getText().toString();
            String email = editTextEmail.getText().toString();
            String address = editTextAddress.getText().toString();
            dbHelper.addContact(name, mobile, email, address);
            setResult(RESULT_OK);
            finish();
        });
    }
}
