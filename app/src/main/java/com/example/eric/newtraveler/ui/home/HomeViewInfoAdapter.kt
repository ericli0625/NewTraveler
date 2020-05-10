package com.example.eric.newtraveler.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eric.newtraveler.R
import kotlinx.android.synthetic.main.component_county_card.view.*

class HomeViewInfoAdapter(
        private val onCountyClickListener: (String) -> Unit
) : RecyclerView.Adapter<HomeViewInfoAdapter.ViewHolder>() {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(items[position], onCountyClickListener)

    fun updateData(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.component_county_card, parent, false)
    ) {

        fun bind(item: String, onCountyClickListener: (String) -> Unit) {
            with(itemView) {
                county_name.text = item
                card_view.setOnClickListener {
                    onCountyClickListener(item)
                }
            }
        }
    }
}