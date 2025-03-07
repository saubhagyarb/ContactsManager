package com.example.contactsmanager

import android.text.Editable
import android.text.TextWatcher

abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No action needed
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No action needed
    }

    override fun afterTextChanged(s: Editable) {
        // Abstract method for subclasses to implement
        afterTextChanged(s.toString())
    }

    abstract fun afterTextChanged(text: String?)
}
