package com.example.contactsmanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsmanager.ContactAdapter.ContactViewHolder

class ContactAdapter(
    private var contactList: ArrayList<Contact>,
    private val onContactClickListener: OnContactClickListener
) : RecyclerView.Adapter<ContactViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList.get(position)
        holder.textViewName.text = contact.name
        holder.itemView.setOnClickListener(View.OnClickListener { v: View? ->
            onContactClickListener.onContactClick(
                contact
            )
        })
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: ArrayList<Contact>) {
        contactList = newList
        notifyDataSetChanged()
    }

    fun interface OnContactClickListener {
        fun onContactClick(contact: Contact?)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView = itemView.findViewById<TextView>(R.id.textView_name)
    }
}
