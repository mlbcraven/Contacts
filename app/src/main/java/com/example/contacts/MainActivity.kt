package com.example.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var nameInput : EditText
    private lateinit var lastnameInput :EditText
    private lateinit var btnAdd : Button
    private lateinit var btnView : Button
    private lateinit var btnUpdate :Button
    private lateinit var telephoneInput : EditText
    private lateinit var sqLiteHelper: SQLiteHelper

    private lateinit var recyclerView: RecyclerView
    private var adapter: EntryAdapter? = null

    private var entry: Entry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener { AddEntry() }
        btnView.setOnClickListener { getEntries() }
        btnUpdate.setOnClickListener { updateEntry() }
        adapter?.setOnClickItem { Toast.makeText(this, it.name + " " + it.lastname,Toast.LENGTH_SHORT).show()
            nameInput.setText(it.name)
            lastnameInput.setText(it.lastname)
            entry = it
        }
        adapter?.setOnClickDeleteItem {
            deleteEntry(it.id)
        }

    }

    private fun deleteEntry(id: Int) {
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Θελετε να διαγραψετε την επαφη???")
        builder.setCancelable(true)
        builder.setPositiveButton("Ναι") {

            dialog, _ ->
            sqLiteHelper.deleteEntryById(id)
            getEntries()

            dialog.dismiss()
        }
        builder.setNegativeButton("Οχι") {
            dialog, _ -> dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun updateEntry() {
        val name = nameInput.text.toString()
        val lastname = lastnameInput.text.toString()
        val telephone = telephoneInput.text.toString()

        if (name == entry?.name && lastname == entry?.lastname && telephone == entry?.telephone) {
            Toast.makeText(this, "Δεν εχει γινει καποια ανανεωση",Toast.LENGTH_SHORT).show()
            return
        }
        if (entry == null) return
        val entry = Entry(id = entry!!.id, name = name, lastname = lastname, telephone = telephone)
        val status = sqLiteHelper.updateEntry(entry)

        if (status > -1) {
            clearText()
            getEntries()
        }else{
            Toast.makeText(this, "Αποτυχια ανανεωσης", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getEntries() {
        val entryList = sqLiteHelper.getEntry()
        adapter?.addItems(entryList)

    }

    private fun AddEntry() {
        val name = nameInput.text.toString()
        val lastname = lastnameInput.text.toString()
        val telephone = telephoneInput.text.toString()

        if (name.isEmpty() || lastname.isEmpty() || telephone.isEmpty()) {
            Toast.makeText(this, "Παρακαλω Καταχωρηστε ολα τα Πεδια ", Toast.LENGTH_SHORT).show()
        } else{
            val ent = Entry(name = name, lastname = lastname, telephone = telephone)
            val status = sqLiteHelper.addEntry(ent)

            if (status > -1) {
                Toast.makeText(this,"Η Καταχωρηση ηταν επιτυχης",Toast.LENGTH_LONG).show()
                clearText()
                getEntries()
            }else{
                Toast.makeText(this,"Δεν Εγινε Καταχωρηση - Παρακαλω Προσπαθηστε Ξανα",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearText() {
        nameInput.setText("")
        lastnameInput.setText("")
        telephoneInput.setText("")
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EntryAdapter()
        recyclerView.adapter = adapter
    }


    private fun initView() {
        nameInput = findViewById(R.id.nameInput)
        lastnameInput =findViewById(R.id.lastnameInput)
        telephoneInput = findViewById(R.id.telephoneInput)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}