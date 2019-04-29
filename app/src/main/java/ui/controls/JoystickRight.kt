package ui.controls

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

import org.libsdl.app.SDLActivity

class JoystickRight : Joystick {

    private var curX: Float = 0.toFloat()
    private var curY: Float = 0.toFloat()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                curX = event.x
                curY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = event.x
                val newY = event.y

                val mouseScalingFactor = 2.0f // TODO: make configurable

                val movementX = (newX - curX) * mouseScalingFactor
                val movementY = (newY - curY) * mouseScalingFactor

                SDLActivity.sendRelativeMouseMotion(Math.round(movementX), Math.round(movementY))

                curX = newX
                curY = newY
            }
        }

        return super.onTouchEvent(event)
    }
}
