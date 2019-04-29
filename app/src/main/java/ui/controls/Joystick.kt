/*
    Copyright (C) 2018, 2019 Ilya Zhuravlev

    This file is part of OpenMW-Android.

    OpenMW-Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenMW-Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenMW-Android.  If not, see <https://www.gnu.org/licenses/>.
*/

package ui.controls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

open class Joystick : View {

    // Initial touch position
    protected var initialX = 0f
    protected var initialY = 0f

    // Current touch position
    protected var currentX = -1f
    protected var currentY = -1f

    // Whether the finger is down
    protected var down = false

    // left or right stick
    protected var stickId = 0

    // width of a stroke to draw stick circles
    private var strokeWidth = 0

    private val paint = Paint()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, context.resources.displayMetrics).toInt()
    }

    fun setStick(id: Int) {
        stickId = id
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = Color.GRAY

        if (down) {
            // Draw initial touch
            canvas.drawCircle(initialX, initialY, width / 10f, paint)

            // Draw current stick position
            canvas.drawCircle(currentX, currentY, width / 5f, paint)
        } else {
            // Draw the outline
            canvas.drawCircle(width / 2f, height / 2f,
                width / 2f - strokeWidth, paint)
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.x
                initialY = event.y
                currentX = initialX
                currentY = initialY
                down = true
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = event.x
                currentY = event.y
            }
            MotionEvent.ACTION_UP -> {
                down = false
                // make sure it's hidden
                currentY = -1f
                currentX = -1f
            }
        }

        updateStick()
        invalidate()
        return true
    }

    protected open fun updateStick() {}
}
