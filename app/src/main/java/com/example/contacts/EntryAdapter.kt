package com.example.contacts

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    private var entryList: ArrayList<Entry> = ArrayList()
    private var onClickItem: ((Entry) -> Unit)? = null
    private var onClickDeleteItem: ((Entry) -> Unit)? = null

    fun addItems(items: ArrayList<Entry>) {
        this.entryList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (Entry) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (Entry) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_entries, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ent = entryList[position]
        holder.bindView(ent)
        holder.itemView.setOnClickListener { onClickItem?.invoke(ent) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(ent) }
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<Button>(R.id.IdTv)
        private var name = view.findViewById<TextView>(R.id.NameTV)
        private var lastname = view.findViewById<TextView>(R.id.LastNameTV)
        private var telephone = view.findViewById<TextView>(R.id.TelephoneTV)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(entry: Entry) {
            id.text = entry.id.toString()
            name.text = entry.name
            lastname.text = entry.lastname
            telephone.text = entry.telephone
        }
    }
}