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
    protected var initialX: Float = 0.toFloat()
    protected var initialY: Float = 0.toFloat()
    // Current touch position
    protected var currentX = -1f
    protected var currentY = -1f
    // Whether the finger is down
    protected var down: Boolean? = false
    // left or right stick
    protected var stickId = 0

    // width of a stroke
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

    internal fun init() {
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

        if (down!!) {
            // Draw initial touch
            canvas.drawCircle(initialX, initialY, getWidth() / 10f, paint)

            // Draw current stick position
            canvas.drawCircle(currentX, currentY, getWidth() / 5f, paint)
        } else {
            // Draw the outline
            canvas.drawCircle(getWidth() / 2f, height / 2f,
                getWidth() / 2f - strokeWidth, paint)
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked

        when (action) {
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
                currentY = -1f
                currentX = currentY
            }
        }

        updateStick()
        invalidate()
        return true
    }

    protected open fun updateStick() {}
}
