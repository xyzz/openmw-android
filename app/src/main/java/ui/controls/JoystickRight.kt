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
import android.util.AttributeSet
import android.view.MotionEvent

import org.libsdl.app.SDLActivity

class JoystickRight : Joystick {

    private var curX: Float = 0.toFloat()
    private var curY: Float = 0.toFloat()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
        : super(context, attrs, defStyle)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                curX = event.x
                curY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = event.x
                val newY = event.y

                // this isn't configurable here but configurable in openmw built-in settings
                val mouseScalingFactor = 900f

                val movementX = (newX - curX) * mouseScalingFactor / width
                val movementY = (newY - curY) * mouseScalingFactor / height

                SDLActivity.sendRelativeMouseMotion(Math.round(movementX), Math.round(movementY))

                curX = newX
                curY = newY
            }
        }

        return super.onTouchEvent(event)
    }
}
