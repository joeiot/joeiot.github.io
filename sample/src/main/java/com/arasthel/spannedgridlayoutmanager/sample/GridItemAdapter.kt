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
import om.arasthel.spannedgridlayoutmanager.sample.R
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
        mItems = ArrayList<Int>(2)
        for (i in 0 until 51) {
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

    fun getItem(position: Int):Long{
        return mItems?.get(position)?.toLong()?:0L
    }

    val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.MAGENTA, Color.YELLOW)
    private var animation:Runnable?  = null

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        var itemView = holder.itemView
        (itemView?.findViewById<TextView>(R.id.title))?.text  = ("pos={$position} = ${mItems?.get(position)}")

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
    }


    fun  swap(fromPos:Int,toPos:Int)
    {
        Collections.swap(mItems,fromPos,toPos)
        notifyItemMoved(fromPos,toPos)
    }

    override fun getItemViewType(position: Int): Int {
//        return if(position == itemCount -1){
//            1
//        }else{
//            0
//        }
//        return position
        return getItem(position).toInt() % 4
    }

    override fun getItemCount(): Int {
        return mItems?.size?:0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
       var layout = when (viewType) {
           0 -> R.layout.grid_item_normal
           1 -> R.layout.grid_item_normal_1_2
           2 -> R.layout.grid_item_normal_2_1
           else -> R.layout.grid_item_normal_2_2
      }

//        var layout = when (viewType) {
//            0 -> R.layout.grid_item_normal
//            else -> R.layout.grid_item_normal_2_1
//        }
        val gridItemView =  LayoutInflater.from(parent.context).inflate(layout, parent, false)
        var layoutParam = gridItemView.layoutParams
        gridItemView.layoutParams = layoutParam

        return GridItemViewHolder(gridItemView)
    }
}

class GridItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)