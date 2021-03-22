package com.arasthel.spannedgridlayoutmanager.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.sample.SpannedGridLayoutManager.SpanInfo
import org.lucasr.twowayview.widget.SpannableGridLayoutManager
import java.util.*

/**
 * Created by Jorge Martín on 24/5/17.
 */
class MainActivity2: AppCompatActivity() {

    val recyclerview: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private var mItemTouchHelper:ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        recyclerview.addItemDecoration(SpaceItemDecorator(left = 12, top = 12, right = 12, bottom = 12))
        val adapter = GridItemAdapter()
        adapter.setItemTouchHelper(mItemTouchHelper)
        recyclerview.adapter = adapter

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
//        mItemTouchHelper?.attachToRecyclerView(recyclerview)


    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBooleanArray("clicked", (recyclerview.adapter as GridItemAdapter).clickedItems.toBooleanArray())

    }

}