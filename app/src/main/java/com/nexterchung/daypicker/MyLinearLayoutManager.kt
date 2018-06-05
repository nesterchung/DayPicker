package com.nexterchung.daypicker

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView

class MyLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    init {
        orientation = HORIZONTAL
    }

    class CenterLinearSmoothScroller(val context: Context) : LinearSmoothScroller(context) {

        //all for debug
        val backgroundColor by lazy {
            Color.parseColor("#40FF0000")
        }
        var delta = 0
        val dxToVisibleForTesting by lazy {
            (context as AppCompatActivity).findViewById<TextView>(R.id.dxToVisble)
        }

        val snapPrefference by lazy {
            (context as AppCompatActivity).findViewById<TextView>(R.id.snapPrefference)
        }

        val dxdy by lazy {
            (context as AppCompatActivity).findViewById<TextView>(R.id.dxdy)
        }
        //

        override fun onStop() {
            Log.d("MainActivity", "onStop")
            super.onStop()
            targetView?.setBackgroundColor(backgroundColor)
            targetView = null
        }

        override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
            Log.d("MainActivity", "calculateDxToMakeVisible")
            val layoutManager = layoutManager
            if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
                return 0
            }
            val params = view.layoutParams as RecyclerView.LayoutParams
            val left = layoutManager.getDecoratedLeft(view) - params.leftMargin
            val right = layoutManager.getDecoratedRight(view) + params.rightMargin
            val start = layoutManager.paddingLeft
            val end = layoutManager.width - layoutManager.paddingRight

            Log.d("MainActivity", "calculateDxToMakeVisible Lwidth     ${layoutManager.width} \tstart: $start end: $end")
            Log.d("MainActivity", "calculateDxToMakeVisible viewLeft   ${layoutManager.getDecoratedLeft(view)} \tviewRight   = ${layoutManager.getDecoratedRight(view)}")
            Log.d("MainActivity", "calculateDxToMakeVisible marginLeft ${params.leftMargin} \tmarginRight = ${params.rightMargin}")


            val dx = calculateDtToFit(left, right, start, end, snapPreference)
//            Log.d("MainActivity", "calculateDxToMakeVisible dx:$dx ${(view as TextView).text} delta: ${delta}")
            Thread.sleep(1000)
            Log.d("MainActivity ", "calculateDxToMakeVisible dx $dx")
            delta = 0
            dxToVisibleForTesting.text = "$dx"
            snapPrefference.text = when (snapPreference) {
                SNAP_TO_ANY -> "Snap: SNAP_TO_ANY"
                SNAP_TO_START -> "Snap: SNAP_TO_START"
                SNAP_TO_END -> "Snap: SNAP_TO_END"
                else -> "Snap: "
            }
            return dx
        }

        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
            return center(super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference), viewEnd - viewStart)
        }

        private fun center(dx: Int, viewWidth: Int): Int {
            return if (dx < 0) {
                dx + (viewWidth / 2)
            } else {
                dx - (viewWidth / 2)
            }
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            //slow down
            return (25F / displayMetrics.densityDpi) * 20
        }

        override fun onSeekTargetStep(dx: Int, dy: Int, state: RecyclerView.State?, action: Action?) {
            delta += dx
            dxdy.text = "dx:$dx dy:$dy delta: $delta"
            Log.d("MainActivity", "onSeekTargetStep dx:$dx \t dy:$dy delta: $delta")
            super.onSeekTargetStep(dx, dy, state, action)
        }

        override fun updateActionForInterimTarget(action: Action?) {
            Log.d("MainActivity", "updateActionForInterimTarget")
            super.updateActionForInterimTarget(action)
        }

        var targetView: View? = null
        override fun onTargetFound(targetView: View?, state: RecyclerView.State?, action: Action?) {
            Log.d("MainActivity", "onTargetFound")
            super.onTargetFound(targetView, state, action)
            this.targetView = targetView
            targetView?.setBackgroundColor(Color.CYAN)
        }

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            val point = super.computeScrollVectorForPosition(targetPosition)
            Log.d("MainActivity", "vectorForPosition $point")
            return point
        }
    }


    private var centerX: Int = 0
    private var itemWidth: Int = 0
    private var centerItem: View? = null
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        Log.d("MainActivity", "onLayoutChildren")
        super.onLayoutChildren(recycler, state)
        val visibleItemPosition = findFirstVisibleItemPosition()

        if (centerX == 0) {
            centerX = width / 2
        }

        if (itemWidth == 0) {
            val view = findViewByPosition(visibleItemPosition)
            val (left, right) = point(view)
            itemWidth = right - left
        }

        scale()
    }

    private fun scale() {
        iterator().forEach {
            val point = point(it)
            if (centerX in point) {
                if (centerItem != it) {
                    centerItem = it
                    it.scaleX = 1.2F
                    it.scaleY = 1.2F
                }
            } else {
                it.scaleX = 1F
                it.scaleY = 1F
            }
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scroll = super.scrollHorizontallyBy(dx, recycler, state)
        scale()
        return scroll
    }

    private fun point(view: View): Pair<Int, Int> {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = getDecoratedLeft(view) - params.leftMargin
        val right = getDecoratedRight(view) + params.rightMargin
        return Pair(left, right)
    }


    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = CenterLinearSmoothScroller(recyclerView.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

}