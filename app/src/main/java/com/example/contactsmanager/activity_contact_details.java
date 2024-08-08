package com.example.contactsmanager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class activity_contact_details extends AppCompatActivity {

    private EditText editTextName, editTextMobile, editTextEmail, editTextAddress;
    private Button buttonUpdateContact, buttonDelete, buttonCall, buttonEmail, buttonMessage;
    private DatabaseHelper dbHelper;
    private int contactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        dbHelper = new DatabaseHelper(this);

        editTextName = findViewById(R.id.editText_name);
        editTextMobile = findViewById(R.id.editText_mobile);
        editTextEmail = findViewById(R.id.editText_email);
        editTextAddress = findViewById(R.id.editText_address);
        buttonUpdateContact = findViewById(R.id.button_update_contact);
        buttonDelete = findViewById(R.id.button_delete);
        buttonCall = findViewById(R.id.button_call);
        buttonEmail = findViewById(R.id.button_email);
        buttonMessage = findViewById(R.id.button_message);

        contactId = getIntent().getIntExtra("CONTACT_ID", -1);
        Log.d("activity_contact_details", "Received CONTACT_ID: " + contactId);

        if (contactId != -1) {
            loadContactDetails(contactId);
        }

        buttonUpdateContact.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String mobile = editTextMobile.getText().toString();
            String email = editTextEmail.getText().toString();
            String address = editTextAddress.getText().toString();

            if (contactId == -1) {
                dbHelper.addContact(name, mobile, email, address);
            } else {
                dbHelper.updateContact(contactId, name, mobile, email, address);
            }

            setResult(RESULT_OK);
            finish();
        });

        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Contact")
                    .setMessage("Are you sure you want to delete this contact?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        dbHelper.deleteContact(contactId);
                        Toast.makeText(activity_contact_details.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        buttonCall.setOnClickListener(v -> {
            String mobile = editTextMobile.getText().toString();
            if (!mobile.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Mobile number is empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEmail.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            if (!email.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                startActivity(emailIntent);
            } else {
                Toast.makeText(this, "Email address is empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonMessage.setOnClickListener(v -> {
            String mobile = editTextMobile.getText().toString();
            if (!mobile.isEmpty()) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobile));
                startActivity(smsIntent);
            } else {
                Toast.makeText(this, "Mobile number is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadContactDetails(int id) {
        Cursor cursor = dbHelper.getContactById(id);
        if (cursor != null && cursor.moveToFirst()) {
            editTextName.setText(cursor.getString(cursor.getColumnIndex("name")));
            editTextMobile.setText(cursor.getString(cursor.getColumnIndex("mobile")));
            editTextEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
            editTextAddress.setText(cursor.getString(cursor.getColumnIndex("address")));
            cursor.close();
        } else {
            Log.e("activity_contact_details", "Failed to load contact details for ID: " + id);
        }
    }
}
