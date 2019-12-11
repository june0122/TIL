package kr.ourguide.sqllite_tutorial

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    data class VersionInfo(val versionName: String, val versionNumber: String)

    data class ContactInfo(val contactNum: Int, val contactName: String, val contactPhone: String, val over20: Boolean)

    private var items = arrayListOf(VersionInfo("1", "01012345678"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(parent)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model = items[position]

        with(holder.itemView) {
//            nameTextView.text = model.versionName
//            nameTextView.text = model.versionNumber
        }
    }

}