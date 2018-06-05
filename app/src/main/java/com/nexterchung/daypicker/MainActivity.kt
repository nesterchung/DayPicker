package com.nexterchung.daypicker

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dayAdapter = DayAdapter()
    private lateinit var myLayoutManager: MyLinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRv(findViewById<RecyclerView>(R.id.rv))

        initGuild()

        initButton()
    }

    private fun initButton() {
        val numberPicker = findViewById<NumberPicker>(R.id.numberPicker)
        numberPicker.displayedValues = (0..6).toList().map {
            "$it"
        }.toTypedArray()
        numberPicker.minValue = 0
        numberPicker.maxValue = 6

        findViewById<Button>(R.id.scrollTo).setOnClickListener {
            rv.smoothScrollToPosition(numberPicker.value)
        }

        findViewById<Button>(R.id.toggle).setOnClickListener {
            if (rv.visibility == View.VISIBLE) {
                rv.visibility = View.GONE
            } else {
                rv.visibility = View.VISIBLE
            }
        }
    }

    private fun initGuild() {
        val centerText = findViewById<TextView>(R.id.centerText)
        centerText.post { centerText.text = "center x = ${window.decorView.measuredWidth / 2}" }
    }

    private fun initRv(rv: RecyclerView) {
        val snapHelper = LinearSnapHelper()

        myLayoutManager = MyLinearLayoutManager(this)
        with(rv) {
            setHasFixedSize(true)
            adapter = dayAdapter
            layoutManager = myLayoutManager
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect?.right = 1
                    Log.d("ItemDecoration", "view: ${view.toString()}")
                }
            })
            snapHelper.attachToRecyclerView(this)
        }
        rv.post {
            Log.d("MainActivity", "RV - width - ${rv.width}")
            val rvPadding = rv.width / 2
            rv.setPadding(rvPadding, 0, rvPadding, 0)
        }
    }
}



