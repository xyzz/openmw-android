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

import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import org.libsdl.app.SDLActivity

class OskTouchListener(val btn: OskButton): View.OnTouchListener {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> btn.pressed()
            MotionEvent.ACTION_UP -> btn.released()
        }

        return true
    }

}

/**
 * Base class for keyboard buttons.
 *
 * @param text text displayed on the button
 * @param positionX X position of the button
 * @param positionY Y position of the button
 * @param sizeW width of the button
 * @param sizeH height of the button
 */
abstract class OskButton(
    val text: String,
    private val positionX: Int,
    private val positionY: Int,
    private val sizeW: Int,
    private val sizeH: Int
) {

    var view: Button? = null

    /**
     * Show the keyboard button.
     */
    fun show() {
        view?.visibility = View.VISIBLE
    }

    /**
     * Hide the keyboard button.
     */
    fun hide() {
        view?.visibility = View.GONE
    }

    /**
     * Place this button into a RelativeLayout.
     * This also allocates an underlying View object.
     */
    fun place(target: RelativeLayout) {
        val v = Button(target.context)
        v.transformationMethod = null
        v.text = text
        v.tag = this
        v.alpha = 0.5f
        v.visibility = View.GONE

        // TODO: this doesn't take soft keys into account
        val realScreenWidth = v.context.resources.displayMetrics.widthPixels
        val realScreenHeight = v.context.resources.displayMetrics.heightPixels
        val realX = positionX * realScreenWidth / VIRTUAL_SCREEN_WIDTH
        val realY = positionY * realScreenHeight / VIRTUAL_SCREEN_HEIGHT

        val realW = sizeW * realScreenWidth / VIRTUAL_SCREEN_WIDTH
        val realH = sizeH * realScreenHeight / VIRTUAL_SCREEN_HEIGHT
        val params = RelativeLayout.LayoutParams(realW, realH)

        params.leftMargin = realX
        params.topMargin = realY

        v.layoutParams = params

        v.setOnTouchListener(OskTouchListener(this))

        target.addView(v)
        view = v
    }

    /**
     * This is called when the button is pressed down.
     * Optionally, children can override this method.
     */
    open fun pressed() {
    }

    /**
     * This is called when the button is released.
     * Every child probably should override this method.
     */
    open fun released() {
    }
}

/**
 * A simple keyboard button which has two states: normal and shift pressed.
 * This uses nativeCommitText to send the text events.
 *
 * @param key key sent when in normal state
 * @param shiftKey key sent when shift is pressed
 */
class OskSimpleButton(val key: Char, val shiftKey: Char, positionX: Int, positionY: Int, sizeW: Int, sizeH: Int):
    OskButton(key.toString(), positionX, positionY, sizeW, sizeH) {

    private val keyStr = key.toString()
    private val shiftKeyStr = shiftKey.toString()
    private var curKeyStr = keyStr

    override fun released() {
        SDLActivity.nativeCommitText(curKeyStr, 0)
    }

    fun shift(on: Boolean) {
        curKeyStr = if (on) shiftKeyStr else keyStr
        view?.text = curKeyStr
    }
}

class OskRawButton(text: String, private val keyCode: Int, positionX: Int, positionY: Int, sizeW: Int, sizeH: Int):
    OskButton(text, positionX, positionY, sizeW, sizeH) {

    override fun pressed() {
        SDLActivity.onNativeKeyDown(keyCode)
    }

    override fun released() {
        SDLActivity.onNativeKeyUp(keyCode)
    }
}

class OskShift(val buttons: ArrayList<OskSimpleButton>, positionX: Int, positionY: Int, sizeW: Int, sizeH: Int):
    OskButton("Shift", positionX, positionY, sizeW, sizeH) {

    override fun pressed() {
        for (btn in buttons)
            btn.shift(true)
    }

    override fun released() {
        for (btn in buttons)
            btn.shift(false)
    }
}

class Osk {
    /* Every key is defined by two characters, first is input normally, second is input when shift is active
     * Note: This is only the "middle" part of the virtual keyboard
     */
    private val keyboardLayout = arrayListOf(
        "1!2@3#4$5%6^7&8*9(0)-_=+",
        "qQwWeErRtTyYuUiIoOpP[{]}\\|",
        "aAsSdDfFgGhHjJkKlL;:'\"",
        "zZxXcCvVbBnNmM,<.>/?"
    )

    private var elements = ArrayList<OskButton>()

    private var visible = false

    init {
        val buttonWidth = 60
        val buttonHeight = 100
        val buttonMarginX = 3
        val buttonMarginY = 1
        val offsetX = 30
        val offsetY = 110

        var curX: Int
        var curY = offsetY

        val lineOffset = arrayOf(
            (offsetX + buttonWidth * 1.0 + buttonMarginX).toInt(),
            (offsetX + buttonWidth * 0.5 + buttonMarginX).toInt(),
            (offsetX + buttonWidth * 1.0 + buttonMarginX).toInt(),
            (offsetX + buttonWidth * 1.5 + buttonMarginX).toInt()
        )

        val simpleButtons = ArrayList<OskSimpleButton>()
        keyboardLayout.forEachIndexed{ i, line ->
            curX = lineOffset[i]

            for (j in 0..(line.length - 1) step 2) {
                simpleButtons.add(OskSimpleButton(line[j], line[j + 1], curX, curY, buttonWidth, buttonHeight))
                curX += buttonWidth + buttonMarginX
            }
            curY += buttonHeight + buttonMarginY
        }
        elements.addAll(simpleButtons)

        // Shift
        elements.add(OskShift(simpleButtons, offsetX, offsetY + 3 * (buttonHeight + buttonMarginY), (buttonWidth * 1.5).toInt(), buttonHeight))

        // Backspace
        elements.add(OskRawButton(
            "Bksp",
            KeyEvent.KEYCODE_DEL,
            lineOffset[0] + (buttonWidth + buttonMarginX) * keyboardLayout[0].length / 2,
            offsetY,
            buttonWidth * 2,
            buttonHeight
        ))

        // Enter
        elements.add(OskRawButton(
            "Return",
            KeyEvent.KEYCODE_ENTER,
            lineOffset[2] + (buttonWidth + buttonMarginX) * keyboardLayout[2].length / 2,
            offsetY + (buttonHeight + buttonMarginY) * 2,
            buttonWidth * 3,
            buttonHeight
        ))

        // Spacebar
        elements.add(OskSimpleButton(' ', ' ', offsetX + buttonWidth * 3, curY, buttonWidth * 7, buttonHeight))

        // Arrows
        var arrowsCurX = lineOffset[3] + (buttonWidth + buttonMarginX) * keyboardLayout[3].length / 2 + buttonWidth
        var arrowsCurY = offsetY + (buttonHeight + buttonMarginY) * 3
        elements.add(OskRawButton("↑", KeyEvent.KEYCODE_DPAD_UP, arrowsCurX, arrowsCurY, buttonWidth, buttonHeight))
        arrowsCurX -= buttonWidth + buttonMarginX
        arrowsCurY += buttonHeight + buttonMarginY
        elements.add(OskRawButton("←", KeyEvent.KEYCODE_DPAD_LEFT, arrowsCurX, arrowsCurY, buttonWidth, buttonHeight))
        arrowsCurX += buttonWidth + buttonMarginX
        elements.add(OskRawButton("↓", KeyEvent.KEYCODE_DPAD_DOWN, arrowsCurX, arrowsCurY, buttonWidth, buttonHeight))
        arrowsCurX += buttonWidth + buttonMarginX
        elements.add(OskRawButton("→", KeyEvent.KEYCODE_DPAD_RIGHT, arrowsCurX, arrowsCurY, buttonWidth, buttonHeight))

        // Tilde. It's kinda crappy that it's separate just for console but oh well
        elements.add(OskRawButton("~", 68, offsetX, offsetY, buttonWidth, buttonHeight))
    }

    fun placeElements(target: RelativeLayout) {
        for (element in elements) {
            element.place(target)
        }
    }

    fun toggle() {
        visible = !visible
        for (element in elements) {
            if (visible)
                element.show()
            else
                element.hide()
        }
    }
}
