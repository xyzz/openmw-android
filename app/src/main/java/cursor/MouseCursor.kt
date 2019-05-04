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

package cursor

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Choreographer
import android.view.View
import android.widget.RelativeLayout

import com.libopenmw.openmw.R

import org.libsdl.app.SDLActivity

import ui.activity.GameActivity
import ui.activity.MainActivity
import ui.controls.Osc

/**
 * An image view which doesn't downsize itself when moved to the border of a RelativeLayout
 * (not sure if original behavior is intended)
 */
internal class FixedSizeImageView : androidx.appcompat.widget.AppCompatImageView {

    // Just something so that it's visible
    private var fixedWidth = 10
    private var fixedHeight = 10

    // Default constructors for studio UI editor
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int):
        super(context, attrs, defStyle)

    /**
     * @param w Fixed width of this view
     * @param h Fixed height of this view
     */
    constructor(context: Context, w: Int, h: Int): super(context) {
        fixedWidth = w
        fixedHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(fixedWidth, fixedHeight)
    }
}

class MouseCursor(activity: GameActivity, private val osc: Osc?) : Choreographer.FrameCallback {

    private val choreographer: Choreographer
    private val cursor: FixedSizeImageView
    private var prevMouseShown = -1

    init {
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f,
            activity.resources.displayMetrics).toInt()
        val width = Math.round(height / 1.5).toInt()

        cursor = FixedSizeImageView(activity, width, height)
        cursor.setImageResource(R.drawable.pointer_arrow)

        cursor.layoutParams = RelativeLayout.LayoutParams(width, height)

        activity.layout.addView(cursor)

        choreographer = Choreographer.getInstance()
        choreographer.postFrameCallback(this)
    }

    override fun doFrame(frameTimeNanos: Long) {
        // Check if we need to switch osc widgets visibility
        val mouseShown = SDLActivity.isMouseShown()
        if (osc != null && mouseShown != prevMouseShown) {
            if (osc.defaultMouse) {
                // If the player has default mouse-mode enabled, trigger it here
                osc.mouseVisible = mouseShown != 0
            }
            osc.showBasedOnState()
        }

        if (mouseShown == 0 || (osc != null && osc.keyboardVisible)) {
            cursor.visibility = View.GONE
        } else {
            cursor.visibility = View.VISIBLE

            val surface = SDLActivity.getSurface()

            var translateX = 1.0f
            var translateY = 1.0f

            if (MainActivity.resolutionX > 0) {
                translateX = 1.0f * surface.width / MainActivity.resolutionX
                translateY = 1.0f * surface.height / MainActivity.resolutionY
            }

            val mouseX = SDLActivity.getMouseX()
            val mouseY = SDLActivity.getMouseY()

            // calling setX/setY here results in a bug cropping part of the mouse cursor
            // changing LayoutParams works as expected...
            val params = cursor.layoutParams as RelativeLayout.LayoutParams
            params.leftMargin = (mouseX * translateX + surface.left).toInt()
            params.topMargin = (mouseY * translateY + surface.top).toInt()
            cursor.layoutParams = params
        }

        prevMouseShown = mouseShown
        choreographer.postFrameCallback(this)
    }
}
