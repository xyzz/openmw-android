/*
    Copyright (C) 2015, 2016 sandstranger
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