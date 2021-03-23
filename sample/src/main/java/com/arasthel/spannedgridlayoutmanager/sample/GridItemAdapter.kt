package com.arasthel.spannedgridlayoutmanager.sample

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import org.lucasr.twowayview.widget.SpannableGridLayoutManager
import java.util.*

/**
 * Created by Jorge Martín on 24/5/17.
 */
class GridItemAdapter: RecyclerView.Adapter<GridItemViewHolder>() {

    val clickedItems: MutableList<Boolean>
    var isVertical = false
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mItems: ArrayList<Int>? = null
    private var mCurrentItemId = 0
    init {
        clickedItems = MutableList(itemCount, { false })
        setHasStableIds(true)
        mItems = ArrayList<Int>(50)
        for (i in 0 until 50) {
            addItem(i)
        }
    }

    fun addItem(position: Int) {
        val id: Int = mCurrentItemId++
        mItems?.add(position, id)
        notifyItemInserted(position)
    }

     fun  setItemTouchHelper(mItemTouchHelper: ItemTouchHelper?){
        this.mItemTouchHelper  = mItemTouchHelper
    }

    override fun getItemId(position: Int): Long {
       return  mItems?.get(position)?.hashCode()?.toLong()?:0L
    }

    val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.MAGENTA, Color.YELLOW)
    private var animation:Runnable?  = null


    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        var itemView = holder.itemView
        (itemView?.findViewById<TextView>(R.id.title))?.text  = ("${mItems?.get(position)}")

        (itemView?.findViewById<ConstraintLayout>(R.id.linear))?.setOnClickListener {
            (itemView?.findViewById<ProgressBar>(R.id.progressbar))?.visibility = View.VISIBLE
            (itemView?.findViewById<ImageView>(R.id.iv_dev))?.visibility = View.GONE
//            holder.itemView?.requestLayout()
            animation = object :Runnable{
                override fun run() {
                    (itemView?.findViewById<ProgressBar>(R.id.progressbar))?.visibility = View.GONE
                    (itemView?.findViewById<ImageView>(R.id.iv_dev))?.visibility = View.VISIBLE
                }
            }
            itemView?.postDelayed(animation, 3000)
        }

        itemView?.setOnLongClickListener {
            mItemTouchHelper?.startDrag(holder)
            true
        }

//        holder.itemView.setBackgroundColor(
//                colors[position % colors.size]
//        )

        itemView.setOnClickListener {
            clickedItems[position] = !clickedItems[position]
            notifyItemChanged(position)
        }
        val lp = itemView.layoutParams
        if(lp is SpannableGridLayoutManager.LayoutParams){
            if(position % 3 == 0){
                SpanSize(1, 1)
            }else  if(position % 3 == 1){
                SpanSize(1, 2)
            }else{
                SpanSize(2, 1)
            }

            val span1 = if ((position % 3) == 2) 2 else 1
            val span2 = if ((position % 3) == 1) 2 else 1

            val colSpan = if (isVertical) span2 else span1
            val rowSpan = if (isVertical) span1 else span2

            if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
                lp.rowSpan = rowSpan
                lp.colSpan = colSpan
                itemView.setLayoutParams(lp)
            }
        }
    }


    fun  swap(fromPos:Int,toPos:Int)
    {
        Collections.swap(mItems,fromPos,toPos)
        notifyItemMoved(fromPos,toPos)
    }

    override fun getItemViewType(position: Int): Int {
        return position % 3
    }

    override fun getItemCount(): Int {
        return mItems?.size?:0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        var layout = when (viewType) {
            0 -> R.layout.grid_item_normal
            1 -> R.layout.grid_item_normal_1_2
            else -> R.layout.grid_item_normal_2_1
        }
        val gridItemView =  LayoutInflater.from(parent.context).inflate(layout, parent, false)
        var layoutParam = gridItemView.layoutParams
        gridItemView.layoutParams = layoutParam

        return GridItemViewHolder(gridItemView)
    }
}

class GridItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)