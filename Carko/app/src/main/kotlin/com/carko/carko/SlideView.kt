package com.carko.carko

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup

class SlideView(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {
    companion object {
        val POSITION_HINT = 0
        val POSITION_HALF = 1
        val POSITION_FULL = 2
    }

    private var hidden = false
    private var position = POSITION_HINT

    init {
        val a: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0)
        try {
            this.hidden = a.getBoolean(R.styleable.SlideView_hidden, false)
            this.position = a.getInteger(R.styleable.SlideView_position, POSITION_HINT)
        } finally {
            a.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount > 1) {
            throw SlideViewException()
        }
        val child = getChildAt(0)
        child.layout(left, top, right, bottom)
        TODO("not implemented")
    }

    fun hide() {
        hidden = true
        invalidate()
        requestLayout()
    }

    fun isVisible(): Boolean {
        return !hidden
    }

    fun slide(pos: Int) {
        // TODO: Do magic
        this.position = pos
    }
}