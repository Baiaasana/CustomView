package com.example.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView


class CustomView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val textView: AutoCompleteTextView
    private val listView: ListView
    private val title: TextView
    private val addButton: AppCompatImageView

    private var selectedItems: MutableList<String> = ArrayList()
    private var allItems: MutableList<String> = ArrayList()


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_view, this, true)
        textView = view.findViewById(R.id.tvAddItem)
        listView = view.findViewById(R.id.listView)
        title = view.findViewById(R.id.tvTitle)
        addButton = view.findViewById(R.id.ivAdd)

        textView.threshold = 1

        addButton.setOnClickListener {
            val selectedString = textView.text.trim().toString()
            when {
                selectedString.isEmpty() -> Toast.makeText(context,
                    "enter text",
                    Toast.LENGTH_SHORT).show()
                selectedItems.contains(selectedString) -> Toast.makeText(context,
                    "already exists",
                    Toast.LENGTH_SHORT)
                    .show()
                else -> {
                    selectedItems.add(0, selectedString)
                    refreshData(true)
                }
            }
        }
    }

    fun setData(data: MutableList<String>) {
        allItems = data
        textView.setAdapter(ArrayAdapter(context, R.layout.custom_view_item, allItems))
    }

    fun setTitle(str: String) {
        textView.setText(str)
    }

    fun getSelectedData(): MutableList<String> {
        return selectedItems
    }

    private fun refreshData(clearData: Boolean) {
        listView.adapter = CustomViewAdapter(context, R.layout.custom_view_item, selectedItems)
        setListViewHeightBasedOnChildren(listView)
        if (clearData) {
            textView.setText("")
        }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = listView.paddingTop + listView.paddingBottom
        for (i in 0 until listAdapter.count) {
            // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.

            val listItem = listAdapter.getView(i, null, listView)
            (listView as? ViewGroup)?.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight

        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
//            listView.requestLayout()
    }

    inner class CustomViewAdapter(
        context: Context,
        var resource: Int,
        var objects: MutableList<String>,
    ) :
        ArrayAdapter<String>(context, resource, objects) {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return objects!!.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = inflater.inflate(resource, parent, false)
            val title = view.findViewById<TextView>(R.id.tvItemTitle)
            val remove = view.findViewById<AppCompatImageView>(R.id.ivRemove)
            title.text = objects[position]
            remove.setOnClickListener {
                selectedItems.removeAt(position)
                refreshData(false)
            }
            return view
        }
    }
}