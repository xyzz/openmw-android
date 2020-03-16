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

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

import org.libsdl.app.SDLActivity

class ButtonTouchListener(private val keyCode: Int, private val needEmulateMouse: Boolean) : OnTouchListener {

    enum class Movement {
        KEY_DOWN,
        KEY_UP,
        MOUSE_DOWN,
        MOUSE_UP
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onTouchDown()
                return true
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                onTouchUp()
                return true
            }
        }
        return false
    }

    private fun onTouchDown() {
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_DOWN)
        } else {
            eventMovement(Movement.MOUSE_DOWN)
        }
    }

    private fun onTouchUp() {
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_UP)
        } else {
            eventMovement(Movement.MOUSE_UP)
        }
    }

    private fun eventMovement(event: Movement) {
        when (event) {
            Movement.KEY_DOWN -> SDLActivity.onNativeKeyDown(keyCode)
            Movement.KEY_UP -> SDLActivity.onNativeKeyUp(keyCode)
            Movement.MOUSE_DOWN -> SDLActivity.sendMouseButton(1, keyCode)
            Movement.MOUSE_UP -> SDLActivity.sendMouseButton(0, keyCode)
        }
    }
}
