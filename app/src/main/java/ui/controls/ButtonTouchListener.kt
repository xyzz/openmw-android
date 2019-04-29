package ui.controls

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

import org.libsdl.app.SDLActivity

class ButtonTouchListener(private val keyCode: Int, needEmulateMouse: Boolean) : OnTouchListener {
    internal var needEmulateMouse = false

    enum class Movement {
        KEY_DOWN,
        KEY_UP,
        MOUSE_DOWN,
        MOUSE_UP
    }

    init {
        this.needEmulateMouse = needEmulateMouse
        SDLActivity.mSeparateMouseAndTouch = needEmulateMouse
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onTouchDown(v)
                return true
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp(v)
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                onTouchUp(v)
                return true
            }
        }
        return false
    }

    private fun onTouchDown(v: View) {
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_DOWN)
        } else {
            eventMovement(Movement.MOUSE_DOWN)
        }
    }

    private fun onTouchUp(v: View) {
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_UP)
        } else {
            eventMovement(Movement.MOUSE_UP)
        }
    }

    protected fun eventMovement(event: Movement) {
        when (event) {
            ButtonTouchListener.Movement.KEY_DOWN -> SDLActivity.onNativeKeyDown(keyCode)
            ButtonTouchListener.Movement.KEY_UP -> SDLActivity.onNativeKeyUp(keyCode)
            ButtonTouchListener.Movement.MOUSE_DOWN -> SDLActivity.sendMouseButton(1, keyCode)
            ButtonTouchListener.Movement.MOUSE_UP -> SDLActivity.sendMouseButton(0, keyCode)
        }
    }
}