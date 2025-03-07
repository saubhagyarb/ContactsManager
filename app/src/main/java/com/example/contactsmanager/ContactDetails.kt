package com.example.contactsmanager

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class ContactDetails : AppCompatActivity() {
    private var editTextName: EditText? = null
    private var editTextMobile: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextAddress: EditText? = null
    private var buttonUpdateContact: Button? = null
    private var buttonDelete: Button? = null
    private var buttonCall: Button? = null
    private var buttonEmail: Button? = null
    private var buttonMessage: Button? = null
    private var dbHelper: DatabaseHelper? = null
    private var contactId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        dbHelper = DatabaseHelper(this)

        editTextName = findViewById<EditText>(R.id.editText_name)
        editTextMobile = findViewById<EditText>(R.id.editText_mobile)
        editTextEmail = findViewById<EditText>(R.id.editText_email)
        editTextAddress = findViewById<EditText>(R.id.editText_address)
        buttonUpdateContact = findViewById<Button>(R.id.button_update_contact)
        buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonCall = findViewById<Button>(R.id.button_call)
        buttonEmail = findViewById<Button>(R.id.button_email)
        buttonMessage = findViewById<Button>(R.id.button_message)

        contactId = getIntent().getIntExtra("CONTACT_ID", -1)
        Log.d("activity_contact_details", "Received CONTACT_ID: " + contactId)

        if (contactId != -1) {
            loadContactDetails(contactId)
        }

        buttonUpdateContact!!.setOnClickListener(View.OnClickListener { v: View? ->
            val name = editTextName!!.getText().toString()
            val mobile = editTextMobile!!.getText().toString()
            val email = editTextEmail!!.getText().toString()
            val address = editTextAddress!!.getText().toString()

            if (contactId == -1) {
                dbHelper!!.addContact(name, mobile, email, address)
            } else {
                dbHelper!!.updateContact(contactId, name, mobile, email, address)
            }

            setResult(RESULT_OK)
            finish()
        })

        buttonDelete!!.setOnClickListener(View.OnClickListener { v: View? ->
            AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        dbHelper!!.deleteContact(contactId)
                        Toast.makeText(
                            this@ContactDetails,
                            "Contact deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(RESULT_OK)
                        finish()
                    })
                .setIcon(R.drawable.delete)
                .show()
        })

        buttonCall!!.setOnClickListener(View.OnClickListener { v: View? ->
            val mobile = editTextMobile!!.getText().toString()
            if (!mobile.isEmpty()) {
                val callIntent = Intent(Intent.ACTION_DIAL, ("tel:$mobile").toUri())
                startActivity(callIntent)
            } else {
                Toast.makeText(this, "Mobile number is empty", Toast.LENGTH_SHORT).show()
            }
        })

        buttonEmail!!.setOnClickListener(View.OnClickListener { v: View? ->
            val email = editTextEmail!!.getText().toString()
            if (!email.isEmpty()) {
                val emailIntent = Intent(Intent.ACTION_SENDTO, ("mailto:$email").toUri())
                startActivity(emailIntent)
            } else {
                Toast.makeText(this, "Email address is empty", Toast.LENGTH_SHORT).show()
            }
        })

        buttonMessage!!.setOnClickListener(View.OnClickListener { v: View? ->
            val mobile = editTextMobile!!.getText().toString()
            if (!mobile.isEmpty()) {
                val smsIntent = Intent(Intent.ACTION_SENDTO, ("smsto:$mobile").toUri())
                startActivity(smsIntent)
            } else {
                Toast.makeText(this, "Mobile number is empty", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadContactDetails(id: Int) {
        val cursor = dbHelper!!.getContactById(id)
        if (cursor != null && cursor.moveToFirst()) {
            editTextName!!.setText(cursor.getString(cursor.getColumnIndex("name")))
            editTextMobile!!.setText(cursor.getString(cursor.getColumnIndex("mobile")))
            editTextEmail!!.setText(cursor.getString(cursor.getColumnIndex("email")))
            editTextAddress!!.setText(cursor.getString(cursor.getColumnIndex("address")))
            cursor.close()
        } else {
            Log.e("activity_contact_details", "Failed to load contact details for ID: $id")
        }
    }
}
