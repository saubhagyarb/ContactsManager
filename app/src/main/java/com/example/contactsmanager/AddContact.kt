package com.example.contactsmanager

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddContact : AppCompatActivity() {
    private var editTextName: EditText? = null
    private var editTextMobile: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextAddress: EditText? = null
    private var buttonSaveContact: Button? = null
    private var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        dbHelper = DatabaseHelper(this)
        editTextName = findViewById(R.id.editText_name)
        editTextMobile = findViewById(R.id.editText_mobile)
        editTextEmail = findViewById(R.id.editText_email)
        editTextAddress = findViewById(R.id.editText_address)
        buttonSaveContact = findViewById(R.id.button_save_contact)

        buttonSaveContact?.setOnClickListener(View.OnClickListener { v: View? ->
            val name = editTextName?.text.toString()
            val mobile = editTextMobile?.text.toString()
            val email = editTextEmail?.text.toString()
            val address = editTextAddress?.text.toString()
            dbHelper!!.addContact(name, mobile, email, address)
            setResult(RESULT_OK)
            finish()
        })
    }
}
