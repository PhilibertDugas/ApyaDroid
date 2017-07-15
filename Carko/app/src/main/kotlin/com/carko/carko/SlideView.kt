package com.carko.carko

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.util.TypedValue
import android.view.GestureDetector

class SlideView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs){
    companion object {
        val POSITION_HINT = 0
        val POSITION_HALF = 1
        val POSITION_FULL = 2

        val TAG = "APYA - " + SlideView::class.java.getSimpleName()
    }

    private var hidden = false
    private var position = POSITION_HINT

    private val screenHeight: Int
    private var minY: Int = 0
    private val maxY: Int
    private var downY = 0.0f

    init {
        Log.i(TAG, "init")
        val a: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0)
        try {
            this.hidden = a.getBoolean(R.styleable.SlideView_hidden, false)
            this.position = a.getInteger(R.styleable.SlideView_position, POSITION_HINT)
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
            POSITION_HINT -> this.translationY = screenHeight.toFloat() * 0.85f
            POSITION_HALF -> this.translationY = screenHeight.toFloat() * 0.5f
        }

        // Set maximum slide y value to be under the Actionbar
        val tv = TypedValue()
        if ((getContext() as Activity).theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            minY = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
    }

    fun hide() {
        hidden = true
        invalidate()
        requestLayout()
    }

    fun isVisible(): Boolean {
        return !hidden
    }

    private val mGestureDetector = GestureDetector(getContext(),
            object: GestureDetector.SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    val half = 0.5f*maxY
                    val bottomHalf = this@SlideView.y < half
                    val goingUp = velocityY < 0
                    val destination: Float
                    if (bottomHalf) {
                        if (goingUp) {
                            destination = half
                        } else {
                            destination = minY.toFloat()
                        }
                    } else {
                        if (goingUp) {
                            destination = maxY.toFloat()
                        } else {
                            destination = half
                        }
                    }
                    val distance = Math.abs(destination-this@SlideView.y)
                    val duration = (distance / Math.abs(velocityY) * 1000).toLong()
                    moveTo(destination, duration)
                    return true
                }
    })

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: " + ev.action)
        if (!mGestureDetector.onTouchEvent(ev)) { // Listen to fling events first
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    Log.i(TAG, "DOWN y: " + ev.y)
                    downY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.i(TAG, "MOVE y: " + ev.y)
                    moveBy(ev.y)
                }
            }
        }
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onInterceptTouchEvent")
        super.onInterceptTouchEvent(ev)
        return true
    }

    private fun move(y: Float, dy: Float) {
        val currY = this@SlideView.y
        var distanceY = dy - y
        if (currY + distanceY < minY)  {
            // Translation would cause the view to overflow
            distanceY = minY - currY
        } else if (currY + distanceY >= maxY) {
            // Translation would cause the view to underflow
            distanceY = maxY - currY
        }
        this@SlideView.animate()
                .yBy(distanceY)
                .setDuration(0)
                .start()
    }

    private fun moveBy(dy: Float) {
        val currY = this@SlideView.y
        var distanceY = dy - downY
        if (currY + distanceY < minY)  {
            // Translation would cause the view to overflow
            distanceY = minY - currY
        } else if (currY + distanceY >= maxY) {
            // Translation would cause the view to underflow
            distanceY = maxY - currY
        }
        this@SlideView.animate()
                .yBy(distanceY)
                .setDuration(0)
                .start()
    }

    private fun moveTo(y: Float, duration: Long = 500) {
        this@SlideView.animate()
                .y(y)
                .setDuration(duration)
                .start()
    }
}