package com.example.eric.newtraveler.ui.attraction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionInfo
import kotlinx.android.synthetic.main.component_county_card.view.*

class AttractionListAdapter(
        private val onClickItemListener: (AttractionInfo) -> Unit
) : RecyclerView.Adapter<AttractionListAdapter.ViewHolder>() {

    private var items: List<AttractionInfo> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(items[position], onClickItemListener)

    fun updateData(
            items: List<AttractionInfo>
    ) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.component_county_card, parent, false)
    ) {

        fun bind(item: AttractionInfo, onClickItemListener: (AttractionInfo) -> Unit) {
            with(itemView) {
                text_title.text = item.name
                card_view.setOnClickListener {
                    onClickItemListener(item)
                }
            }
        }
    }
}