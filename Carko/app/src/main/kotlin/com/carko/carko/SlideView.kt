package com.carko.carko

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout

class SlideView: FrameLayout{
    companion object {
        val POSITION_HINT = 0
        val POSITION_HALF = 1
        val POSITION_FULL = 2

        val TAG = "APYA - " + SlideView::class.java.getSimpleName()
    }

    private var hidden = false
    private var position = POSITION_HINT

    private var screenHeight: Int? = null

    private val mGestureDetector = GestureDetector(context, SlideOnGestureListener())

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        Log.i(TAG, "init")
        val a: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0)
        try {
            this.hidden = a.getBoolean(R.styleable.SlideView_hidden, false)
            this.position = a.getInteger(R.styleable.SlideView_position, POSITION_HINT)
        } finally {
            a.recycle()
        }
        val metrics = DisplayMetrics()
        (getContext() as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
        when (this.position) {
            POSITION_HINT -> this.translationY = screenHeight!!.toFloat()*0.85f
            POSITION_HALF -> this.translationY = screenHeight!!.toFloat()*0.5f
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

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent")
        mGestureDetector.onTouchEvent(ev)
        return true
    }

    private inner class SlideOnGestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            Log.i(TAG, "onScroll")
            val currY = this@SlideView.y
            this@SlideView.y = currY - distanceY
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            Log.i(TAG, "onDown")
            return super.onDown(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.i(TAG, "onDoubleTap")
            return super.onDoubleTap(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.i(TAG, "onFling")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.i(TAG, "onLongPress")
            super.onLongPress(e)
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i(TAG, "onSingleTapUp")
            return super.onSingleTapUp(e)
        }
    }
}