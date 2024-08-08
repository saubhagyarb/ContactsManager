package com.example.contactsmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<Contact> contactList;
    private OnContactClickListener onContactClickListener;

    public ContactAdapter(ArrayList<Contact> contactList, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textViewName.setText(contact.getName());
        holder.itemView.setOnClickListener(v -> onContactClickListener.onContactClick(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(ArrayList<Contact> newList) {
        contactList = newList;
        notifyDataSetChanged();
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name);
        }
    }
}
