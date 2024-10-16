package com.arasthel.spannedgridlayoutmanager.sample

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

object ViewUtils {
    fun doFlipAnimationHorizontal(oldView: View, newView: View, time: Long, reverse: Boolean = false) {
        var animator1 = ObjectAnimator.ofFloat(oldView, "rotationX", 0f, 90f)
        var animator2 = ObjectAnimator.ofFloat(newView, "rotationX", -90f, 0f)
        if (reverse) {
            animator1 = ObjectAnimator.ofFloat(oldView, "rotationX", 0f, -90f)
            animator2 = ObjectAnimator.ofFloat(newView, "rotationX", 90f, 0f)
        }
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                oldView.visibility = View.GONE;
                animator2.setDuration(time).start()
                newView.visibility = View.VISIBLE;
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animator1.setDuration(time).start();
    }

    fun doFlipAnimationVertical(oldView: View, newView: View, time: Long, reverse: Boolean = false) {
        var animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0f, 90f)
        var animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90f, 0f)
        if (reverse) {
            animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0f, -90f)
            animator2 = ObjectAnimator.ofFloat(newView, "rotationY", 90f, 0f)
        }
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }
            override fun onAnimationEnd(animation: Animator) {
                oldView.visibility = View.GONE;
                animator2.setDuration(time).start()
                newView.visibility = View.VISIBLE;
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animator1.setDuration(time).start();
    }

}