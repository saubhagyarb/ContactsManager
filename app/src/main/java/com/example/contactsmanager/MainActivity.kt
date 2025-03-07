package com.example.contactsmanager

import android.R.attr.editable
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var dbHelper: DatabaseHelper? = null
    private var adapter: ContactAdapter? = null
    private var contactList: ArrayList<Contact>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_contacts)
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val fabAddContact = findViewById<FloatingActionButton>(R.id.fab_add_contact)

        contactList = ArrayList<Contact>()
        adapter = ContactAdapter(contactList!!, { contact: Contact? ->
            this.onContactClicked(
                contact!!
            )
        })
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)

        loadContacts()

        fabAddContact.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, AddContact::class.java)
            startActivityForResult(intent, 1)
        })

        searchBar.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: String?) {
                editable?.let {
                    filterContacts(it.toString())
                }
            }
        })

    }

    private fun loadContacts() {
        contactList!!.clear()
        val cursor = dbHelper!!.allContacts
        if (cursor!!.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val mobile = cursor.getString(cursor.getColumnIndex("mobile"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val address = cursor.getString(cursor.getColumnIndex("address"))
                contactList!!.add(Contact(id, name, mobile, email, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        adapter!!.notifyDataSetChanged()
    }

    private fun filterContacts(query: String) {
        val filteredList = ArrayList<Contact?>()
        for (contact in contactList!!) {
            if (contact.name!!.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault()))
            ) {
                filteredList.add(contact)
            }
        }
        adapter!!.updateList(filteredList as ArrayList<Contact>)
    }

    private fun onContactClicked(contact: Contact) {
        Log.d("MainActivity", "Clicked contact ID: " + contact.id)
        val intent = Intent(this@MainActivity, ContactDetails::class.java)
        intent.putExtra("CONTACT_ID", contact.id)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            loadContacts()
        }
    }
}
