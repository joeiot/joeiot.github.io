package com.arasthel.spannedgridlayoutmanager.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.sample.SpannedGridLayoutManager.SpanInfo

/**
 * Created by Jorge Martín on 24/5/17.
 */
class MainActivity1: AppCompatActivity() {

    val recyclerview: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_1)

//        val spannedGridLayoutManager = SpannedGridLayoutManager(orientation = HORIZONTAL, spans = 2, ratio = 0.8f)
//        spannedGridLayoutManager.itemOrderIsStable = true
//
//        recyclerview.layoutManager = spannedGridLayoutManager

        recyclerview.addItemDecoration(SpaceItemDecorator(left = 12, top = 12, right = 12, bottom = 12))


        recyclerview.layoutManager = SpannedGridLayoutManager(
                { position ->
                    if(position % 3 == 0){
                        SpanInfo(1, 1)
                    }else  if(position % 3 == 1){
                        SpanInfo(1, 2)
                    }else{
                        SpanInfo(2, 1)
                    }
                },
                2 /* Three columns */,
                0.8f /* We want our items to be 1:1 ratio */)

        val adapter = GridItemAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey("clicked")) {
            val clicked = savedInstanceState.getBooleanArray("clicked")!!
            adapter.clickedItems.clear()
            adapter.clickedItems.addAll(clicked.toList())
        }
        recyclerview.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBooleanArray("clicked", (recyclerview.adapter as GridItemAdapter).clickedItems.toBooleanArray())

    }

}