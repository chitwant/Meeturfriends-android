package com.meetfriend.app.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.challenge.Result

class StateListAdapter(
    val cntryList: ArrayList<Result>,
    var callBack: AdapterCallback,
    val context: Context
) : RecyclerView.Adapter<StateListAdapter.ViewHolder>(),Filterable {
    var list = ArrayList<Result>()
    var filteredUserList = ArrayList<Result>()

    init {
        filteredUserList = cntryList
    }

    interface AdapterCallback {
         fun itemCheckedAll(checked: Boolean)
        fun itemClick(result: Result)
    }

    fun listisEmpty(): Boolean {
        var result: Boolean = false
        for (obj in filteredUserList) {
            if (obj.checked) {
                result = true
                break
            }
        }
        return result
    }

     public fun checkedAll() {
          for (obj in cntryList) {
              obj.checked = true
              Log.e("set c-", "" + obj.checked)
          }
      }

      public fun unCheckedAll() {
          for (obj in cntryList) {
              obj.checked = false
          }
      }
    public fun getItem(): ArrayList<Result> {
        Log.e("retun", "retun ")

        for (obj in filteredUserList) {
            if (obj.checked) {
                list.add(obj)
            }
        }
        return list
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return  position
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_country_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        Log.e("size:", "" + filteredUserList.size)
        //cntryList.removeAt(0)
        return filteredUserList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(filteredUserList[position], callBack, context)
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(
            dataX: Result,
            callBack: AdapterCallback,
            context: Context
        ) {
            val tvName = itemView.findViewById(R.id.txtName) as TextView
            val chkName = itemView.findViewById(R.id.checkBox) as CheckBox
            tvName.text = dataX.name
            Log.e("checke---", "" + dataX.checked)
            chkName.isChecked = dataX.checked
            chkName.setOnCheckedChangeListener { buttonView, isChecked ->
                dataX.checked = isChecked
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.e("char search:", "" + charSearch)
                if (charSearch.isEmpty()) {
                    filteredUserList = cntryList
                } else {
                    val resultList = ArrayList<Result>()
                    for (obj in filteredUserList) {
                        if (obj.name.toLowerCase()
                                .contains(charSearch.toLowerCase())
                        ) {
                            resultList.add(obj)
                        } else {
                            Log.e("name nt:", "found")
                        }
                    }
                    filteredUserList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filteredUserList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUserList = results?.values as ArrayList<Result>
                notifyDataSetChanged()
            }
        }
    }
}
