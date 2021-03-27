package com.arasthel.spannedgridlayoutmanager.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager

/**
 * Created by Jorge Martín on 24/5/17.
 */
class MainActivity: AppCompatActivity() {

    val recyclerview: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private var mItemTouchHelper:ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val spannedGridLayoutManager = SpannedGridLayoutManager(this, RecyclerView.HORIZONTAL, spans =2,ratio = 0.8f)
        spannedGridLayoutManager.itemOrderIsStable = true

        recyclerview.itemAnimator = null
        recyclerview.layoutManager = spannedGridLayoutManager

        recyclerview.addItemDecoration(SpaceItemDecorator(left = 12, top = 12, right = 12, bottom = 12))

        val adapter = GridItemAdapter()
        adapter.setHasStableIds(false)

        if (savedInstanceState != null && savedInstanceState.containsKey("clicked")) {
            val clicked = savedInstanceState.getBooleanArray("clicked")!!
            adapter.clickedItems.clear()
            adapter.clickedItems.addAll(clicked.toList())
        }

        spannedGridLayoutManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
//                if(position == adapter.itemCount -1){
//                    SpanSize(2, 2)
//                }else{
//                    SpanSize(1, 1)
//                }
//            SpanSize(1, 1)
              var d = adapter.getItem(position).toInt()
               if(d == 50){
                   Log.d("xxx","")
               }
              if(d%3 == 0){
                 SpanSize(1, 1)
             }else  if(d % 3 == 1){
                 SpanSize(1, 2)
             }else if(d % 3 == 2){
                 SpanSize(2, 1)
             }else{
                  SpanSize(2, 2)
              }
        }?.also {
            it.usesCache = true
        }
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        mItemTouchHelper =   ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(dragFlags, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                return if(fromPosition > -1 && toPosition > -1 && fromPosition < recyclerview.adapter?.itemCount ?: 0 && toPosition < recyclerview.adapter?.itemCount ?: 0){
                    adapter.swap(fromPosition,toPosition)
                    true
                }else{
                    false
                }
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        mItemTouchHelper?.attachToRecyclerView(recyclerview)
        adapter.setItemTouchHelper(mItemTouchHelper)
        recyclerview.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBooleanArray("clicked", (recyclerview.adapter as GridItemAdapter).clickedItems.toBooleanArray())

    }

}