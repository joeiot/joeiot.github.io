package com.arasthel.spannedgridlayoutmanager.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager.Orientation.*
import kotlin.random.Random

/**
 * Created by Jorge Martín on 24/5/17.
 */
class MainActivity: AppCompatActivity() {

    val recyclerview: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val spannedGridLayoutManager = SpannedGridLayoutManager(orientation = HORIZONTAL, spans = 2)
        spannedGridLayoutManager.itemOrderIsStable = true

        recyclerview.layoutManager = spannedGridLayoutManager

        recyclerview.addItemDecoration(SpaceItemDecorator(left = 10, top = 10, right = 10, bottom = 10))

        val adapter = GridItemAdapter()

        if (savedInstanceState != null && savedInstanceState.containsKey("clicked")) {
            val clicked = savedInstanceState.getBooleanArray("clicked")!!
            adapter.clickedItems.clear()
            adapter.clickedItems.addAll(clicked.toList())
        }

        spannedGridLayoutManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
//            if (adapter.clickedItems[position]) {
//                SpanSize(Random.nextInt(adapter.itemCount)%3 + 1, Random.nextInt(adapter.itemCount)%3 + 1)
//            } else {
//                SpanSize(1, 1)
//            }
             if(position % 3 == 0){
                 SpanSize(1, 2)
             }else  if(position % 3 == 1){
                 SpanSize(2, 1)
             }else  if(position % 3 == 2){
                 SpanSize(2, 2)
             }else{
                 SpanSize(1, 1)
             }
        }

        recyclerview.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBooleanArray("clicked", (recyclerview.adapter as GridItemAdapter).clickedItems.toBooleanArray())

    }

}