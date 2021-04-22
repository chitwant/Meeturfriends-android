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
import kotlin.collections.ArrayList

class CountryListAdapter(
    val cntryList: ArrayList<Result>,
    var callBack: AdapterCallback,
    val context: Context
) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>(), Filterable {

    var list = ArrayList<Result>()
    var filteredUserList = ArrayList<Result>()

    // private val userList: ArrayList<Result>? =ArrayList<com.meetfriend.app.responseclasses.challenge.Result>()


    init {
        cntryList.removeAt(0);
        filteredUserList = cntryList
    }

    interface AdapterCallback {
        fun itemCheckedAll(checked: Boolean)
        fun itemClick(result: Result)
    }

    public fun checkedAll() {
        for (obj in filteredUserList) {
            obj.checked = true
           // Log.e("set c-", "" + obj.checked)
        }
    }

    public fun unCheckedAll() {
        for (obj in filteredUserList) {
            obj.checked = false
        }
    }
    fun listisEmpty():Boolean{

        var result:Boolean=false
        for (obj in filteredUserList){
            if(obj.checked){
                result=true
                break
            }
        }
        return result
    }

    public fun getItem(): ArrayList<Result> {

        for (obj in filteredUserList) {
            if (obj.checked) {
                list.add(obj)
                Log.e("added:","inlist")
            }
        }
        return list
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
        return filteredUserList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(filteredUserList[position], cntryList, context)
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(
            dataX: Result,
            list: ArrayList<Result>,
            context: Context
        ) {
            //if (adapterPosition != 0) {
            val tvName = itemView.findViewById(R.id.txtName) as TextView
            val chkName = itemView.findViewById(R.id.checkBox) as CheckBox
            tvName.text = dataX.name
            chkName.isChecked = dataX.checked
            chkName.setOnCheckedChangeListener { buttonView, isChecked ->
                dataX.checked = isChecked
                Log.e("checke---dataX", "" + dataX.id)

                for (items in list.indices){
                if (list[items].id==dataX.id){
                    list[items].checked=isChecked
                    Log.e("checke---", "" + list[items].checked)

                }
            }
            }
            // }
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.e("char search:",""+ charSearch)
                if (charSearch.isEmpty()) {
                    filteredUserList = cntryList
                   /* for (items in cntryList.indices) {
                        if (cntryList[items].checked) {
                            Log.e("checke---li", "" + cntryList[items].id)
                        }
                    }*/
                } else {
                    val resultList = ArrayList<Result>()
                    for (obj in filteredUserList) {
                        if (obj.name.toLowerCase()
                                .contains(charSearch.toLowerCase())
                        ) {
                            resultList.add(obj)
                        }
                        else{
                            Log.e("name nt:","found")
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

    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    //countryFilterList = countryList
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            }

        }
    }*/
}