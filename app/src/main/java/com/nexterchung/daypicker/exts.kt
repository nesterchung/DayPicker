package com.nexterchung.daypicker

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by nester on 2018/6/6.
 */
fun Pair<Int, Int>.mid(): Int {
    return (second - first) / 2
}

fun Pair<Int, Int>.distance(): Int {
    return (second - first)
}

operator fun Pair<Int, Int>.contains(e: Int): Boolean {
    return e in first..second
}

operator fun RecyclerView.LayoutManager.iterator() = object : Iterator<View> {
    var currentPos = 0
    override fun hasNext(): Boolean {
        val view = getChildAt(currentPos)
        return view != null
    }

    override fun next(): View {
        return getChildAt(currentPos++)
    }

}


fun Int.abs(): Int {
    return Math.abs(this)
}