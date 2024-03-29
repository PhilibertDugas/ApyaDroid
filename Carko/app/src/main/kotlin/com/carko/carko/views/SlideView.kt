package com.carko.carko.views

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.util.TypedValue
import com.carko.carko.R

class SlideView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs){
    companion object {
        val TAG = "APYA - " + SlideView::class.java.getSimpleName()
    }

    enum class Position { HINT, HALF, FULL }
    enum class Direction { UP, DOWN }

    private var position = Position.HINT

    private val screenHeight: Int
    private var minY: Int
    private val maxY: Int

    private var downY = 0.0f
    private var lastY = 0.0f
    private lateinit var lastDirection: Direction

    init {
        Log.i(TAG, "init")
        val a: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0)
        try {
            val posInt = a.getInteger(R.styleable.SlideView_position, Position.HINT.ordinal)
            this.position = Position.values()[posInt]
        } finally {
            a.recycle()
        }

        // Get screen height
        val metrics = DisplayMetrics()
        (getContext() as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        maxY = (0.85f * screenHeight.toFloat()).toInt()

        // Set initial position
        when (this.position) {
            Position.HINT -> this.translationY = screenHeight.toFloat() * 0.85f
            Position.HALF -> this.translationY = screenHeight.toFloat() * 0.5f
            Position.FULL -> this.translationY = 0.0f
        }

        // Set maximum slide y value to be under the Actionbar
        val tv = TypedValue()
        (getContext() as Activity).theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
        minY = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onInterceptTouchEvent")
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "DOWN y: " + event.y)
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                lastDirection = if (event.rawY < lastY) Direction.UP else Direction.DOWN
                Log.i(TAG, "MOVE y: " + event.rawY + " " + lastDirection)
                moveBy(event.y)
                lastY = event.rawY
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "UP y: " + event.y)
                onUp()
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.i(TAG, "Cancel")
            }
        }
        return true
    }

    private fun onUp() {
        val half = 0.5f*maxY
        val bottomHalf = this.y > half
        val destination: Float
        Log.i(TAG, bottomHalf.toString() + " " + lastDirection.toString())
        if (bottomHalf) {
            if (lastDirection == Direction.UP) {
                destination = half
            } else {
                destination = maxY.toFloat()
            }
        } else {
            if (lastDirection == Direction.UP) {
                destination = minY.toFloat()
            } else {
                destination = half
            }
        }
        if (destination != this.y) {
            moveTo(destination)
        }
    }

    private fun moveBy(dy: Float) {
        val currY = this.y
        var distanceY = dy - downY
        if (currY + distanceY < minY)  {
            // Translation would cause the view to overflow
            distanceY = minY - currY
        } else if (currY + distanceY >= maxY) {
            // Translation would cause the view to underflow
            distanceY = maxY - currY
        }
        this.animate()
                .yBy(distanceY)
                .setDuration(0)
                .start()
    }

    private fun moveTo(y: Float, duration: Long = 200) {
        this.animate()
                .y(y)
                .setDuration(duration)
                .start()
    }

}