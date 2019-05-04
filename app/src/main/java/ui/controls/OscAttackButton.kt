/*
    Copyright (C) 2019 Ilya Zhuravlev

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
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import org.libsdl.app.SDLActivity

/**
 * The attack button is special in that it allows us to control the camera a little
 */
class OscAttackButton(
    uniqueId: String,
    visibility: OscVisibility,
    private val imageSrc: Int,
    defaultX: Int,
    defaultY: Int,
    private val keyCode: Int,
    defaultSize: Int = 50
) : OscElement(uniqueId, visibility, defaultX, defaultY, defaultSize) {

    private var curX = 0f
    private var curY = 0f
    private var downFrom = 0L

    @SuppressLint("ClickableViewAccessibility")
    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        // fix blurry icons on old android
        v.scaleType = ImageView.ScaleType.FIT_XY
        v.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            onMotionEvent(motionEvent)
            return@OnTouchListener true
        })
        v.tag = this

        view = v
    }

    /**
     * Process events, emulating mouse clicks on down/up and movement on move
     */
    private fun onMotionEvent(ev: MotionEvent) {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                SDLActivity.sendMouseButton(1, keyCode)
                curX = ev.x
                curY = ev.y
                downFrom = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = ev.x
                val newY = ev.y

                // Must hold button for specified ms to activate
                if (System.currentTimeMillis() - downFrom > 100) {
                    val mouseScalingFactor = 400f

                    val movementX = (newX - curX) * mouseScalingFactor / view!!.width
                    val movementY = (newY - curY) * mouseScalingFactor / view!!.height

                    SDLActivity.sendRelativeMouseMotion(Math.round(movementX), Math.round(movementY))
                }

                curX = newX
                curY = newY
            }
            MotionEvent.ACTION_UP -> SDLActivity.sendMouseButton(0, keyCode)
        }
    }

}