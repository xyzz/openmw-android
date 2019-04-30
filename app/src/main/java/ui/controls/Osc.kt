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
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.libopenmw.openmw.R
import org.libsdl.app.SDLActivity

const val VIRTUAL_SCREEN_WIDTH = 1024
const val VIRTUAL_SCREEN_HEIGHT = 768

/**
 * Class to hold on-screen control elements such as buttons or joysticks.
 * The position, opacity and size can be customized by the user.
 *
 * @param uniqueId a string identifying the element, used to store customized positions
 * @param defaultX: default X position of the element
 * @param defaultY: default Y position of the element
 * @param defaultSize: default size (both width and height) of the element
 * @param defaultOpacity: default opacity of the element
 */
open class OscElement(
        val uniqueId: String,
        val visibility: OscVisibility,
        private val defaultX: Int,
        private val defaultY: Int,
        private val defaultSize: Int = 50,
        private val defaultOpacity: Float = 0.5f
) {

    private var opacity = defaultOpacity
    var size = defaultSize
    var x = defaultX
    var y = defaultY

    var view: View? = null

    /**
     * Creates a View object for this element.
     * The object should have a custom OnTouchListener which performs the desired action.
     *
     * @param ctx Android Context, comes from layout.
     */
    open fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setBackgroundColor(Color.RED)
        v.tag = this
        view = v
    }

    /**
     * Creates and places this element into a RelativeLayout.
     *
     * @param target RelativeLayout to put the new element into.
     */
    fun place(target: RelativeLayout) {
        makeView(target.context)
        val v = view ?: return

        target.addView(v)
        updateView()
    }

    fun placeConfigurable(target: RelativeLayout, listener: View.OnTouchListener) {
        place(target)
        view?.setOnTouchListener(listener)
        view?.visibility = View.VISIBLE
    }

    fun changeOpacity(delta: Float) {
        opacity = Math.max(0f, Math.min(opacity + delta, 1.0f))
        savePrefs()
    }

    fun changeSize(delta: Int) {
        size = Math.max(0, size + delta)
        savePrefs()
    }

    fun changePosition(virtualX: Int, virtualY: Int) {
        x = virtualX
        y = virtualY
        savePrefs()
    }

    fun updateView() {
        val v = view ?: return

        // Convert display pixels units into real pixels
        val px: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), v.context.resources.displayMetrics)
        val params = RelativeLayout.LayoutParams(px.toInt(), px.toInt())

        val realScreenWidth = (v.parent as View).width
        val realScreenHeight = (v.parent as View).height
        val realX = x * realScreenWidth / VIRTUAL_SCREEN_WIDTH
        val realY = y * realScreenHeight / VIRTUAL_SCREEN_HEIGHT

        params.leftMargin = realX
        params.topMargin = realY

        v.layoutParams = params

        v.alpha = opacity
    }

    private fun savePrefs() {
        val v = view ?: return
        val prefs = PreferenceManager.getDefaultSharedPreferences(v.context)
        with (prefs.edit()) {
            putFloat("osc:$uniqueId:opacity", opacity)
            putInt("osc:$uniqueId:size", size)
            putInt("osc:$uniqueId:x", x)
            putInt("osc:$uniqueId:y", y)

            commit()
        }
    }

    fun loadPrefs(ctx: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

        opacity = prefs.getFloat("osc:$uniqueId:opacity", defaultOpacity)
        size = prefs.getInt("osc:$uniqueId:size", defaultSize)
        x = prefs.getInt("osc:$uniqueId:x", defaultX)
        y = prefs.getInt("osc:$uniqueId:y", defaultY)

        updateView()
    }

    fun resetPrefs(ctx: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

        with (prefs.edit()) {
            remove("osc:$uniqueId:opacity")
            remove("osc:$uniqueId:size")
            remove("osc:$uniqueId:x")
            remove("osc:$uniqueId:y")

            commit()
        }

        loadPrefs(ctx)
    }
}

class OscImageButton(
        uniqueId: String,
        visibility: OscVisibility,
        private val imageSrc: Int,
        defaultX: Int,
        defaultY: Int,
        private val keyCode: Int,
        private val needMouse: Boolean = false,
        defaultSize: Int = 50
) : OscElement(uniqueId, visibility, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        v.setOnTouchListener(ButtonTouchListener(keyCode, needMouse))
        v.tag = this

        view = v
    }

}

class OscKeyboardButton(
    uniqueId: String,
    visibility: OscVisibility,
    private val imageSrc: Int,
    defaultX: Int,
    defaultY: Int,
    private val osc: Osc
) : OscElement(uniqueId, visibility, defaultX, defaultY) {

    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        v.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                osc.toggleKeyboard()
            }
            return@OnTouchListener true
        })
        v.tag = this

        view = v
    }

}

class OscJoystickLeft(
    uniqueId: String,
    visibility: OscVisibility,
    defaultX: Int,
    defaultY: Int,
    defaultSize: Int,
    private val stick: Int
) : OscElement(uniqueId, visibility, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = JoystickLeft(ctx)
        v.setStick(stick)
        v.tag = this

        view = v
    }

}

class OscJoystickRight(
    uniqueId: String,
    visibility: OscVisibility,
    defaultX: Int,
    defaultY: Int,
    defaultSize: Int,
    private val stick: Int
) : OscElement(uniqueId, visibility, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = JoystickRight(ctx)
        v.setStick(stick)
        v.tag = this

        view = v
    }

}

open class OscHiddenButton(
    uniqueId: String,
    visibility: OscVisibility,
    defaultX: Int,
    defaultY: Int,
    private val title: String,
    private val keyCode: Int
) : OscElement(uniqueId, visibility, defaultX, defaultY) {

    override fun makeView(ctx: Context) {
        val v = Button(ctx)
        v.tag = this
        v.setOnTouchListener(ButtonTouchListener(keyCode, false))
        v.text = title
        v.visibility = View.GONE

        view = v
    }

}


class ToggleTouchListener(private val buttons: ArrayList<OscHiddenButton>): View.OnTouchListener {

    private var shown = false

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_UP)
            return false

        for (button in buttons)
            button.view?.visibility = if (shown) View.GONE else View.VISIBLE

        shown = !shown

        return true
    }

}

class OscHiddenToggle(
    uniqueId: String,
    visibility: OscVisibility,
    defaultX: Int,
    defaultY: Int,
    title: String,
    private val buttons: ArrayList<OscHiddenButton>
) : OscHiddenButton(uniqueId, visibility, defaultX, defaultY, title, 0) {

    override fun makeView(ctx: Context) {
        super.makeView(ctx)
        view?.setOnTouchListener(ToggleTouchListener(buttons))
        view?.visibility = View.VISIBLE
    }

}

// Visibility is used as a bitmask
enum class OscVisibility(val v: Int) {
    // Mark as should not be touched by osc-visibility handling
    NULL(0),
    // Widgets that must be visible when menu is open
    ESSENTIAL(1),
    // Keyboard button and keys are visible
    KEYBOARD(2),
    ESSENTIAL_KEYBOARD(ESSENTIAL.v or KEYBOARD.v),
    // Widgets visible during gameplay
    NORMAL(4)
}

class Osc {
    private var osk = Osk()
    public var keyboardVisible = false
    private var keyboardButton = OscKeyboardButton("keyboard", OscVisibility.ESSENTIAL_KEYBOARD,
        R.drawable.keyboard, 586, 0, this)
    private var visibilityState = 0

    private var elements = arrayListOf(
        OscImageButton("run", OscVisibility.NORMAL,
            R.drawable.run, 65, 330, 115),
        OscImageButton("inventory", OscVisibility.ESSENTIAL,
            R.drawable.inventory, 950, 95, 3, true),
        OscImageButton("changePerson", OscVisibility.NORMAL,
            R.drawable.third_person, 212, 0, KeyEvent.KEYCODE_TAB),
        OscImageButton("wait", OscVisibility.NORMAL,
            R.drawable.wait, 274, 0, KeyEvent.KEYCODE_T),
        OscImageButton("pause", OscVisibility.ESSENTIAL,
            R.drawable.pause, 950, 0, KeyEvent.KEYCODE_ESCAPE),
        // TODO: replace load/save icons with more intuitive
        OscImageButton("quickLoad", OscVisibility.NORMAL,
            R.drawable.load, 860, 0, 139),
        OscImageButton("quickSave", OscVisibility.NORMAL,
            R.drawable.save, 780, 0, 135),
        OscImageButton("weapon", OscVisibility.NORMAL,
            R.drawable.toggle_weapon, 880, 95, KeyEvent.KEYCODE_F),
        OscImageButton("jump", OscVisibility.NORMAL,
            R.drawable.jump, 920, 195, KeyEvent.KEYCODE_E),
        OscImageButton("fire", OscVisibility.ESSENTIAL,
            R.drawable.attack, 720, 300, 1, true, 90),
        OscImageButton("magic", OscVisibility.NORMAL,
            R.drawable.toggle_magic, 940, 480, KeyEvent.KEYCODE_R),
        OscImageButton("crouch", OscVisibility.NORMAL,
            R.drawable.sneak, 940, 670, 113),
        OscImageButton("diary", OscVisibility.NORMAL,
            R.drawable.journal, 414, 0, KeyEvent.KEYCODE_J),
        keyboardButton,
        OscImageButton("use", OscVisibility.ESSENTIAL,
            R.drawable.use, 940, 368, KeyEvent.KEYCODE_SPACE),

        OscJoystickLeft("joystickLeft", OscVisibility.NORMAL,
            75, 400, 170, 0),
        OscJoystickRight("joystickRight", OscVisibility.ESSENTIAL,
            650, 400, 170, 1)
    )

    init {
        val fnButtons = ArrayList<OscHiddenButton>()

        // Fn buttons: F1, F3, F4, F10, F11 are the only ones we care about
        arrayOf(1, 3, 4, 10, 11).forEachIndexed{ i, el ->
            val code = 130 + el
            fnButtons.add(OscHiddenButton("f$el", OscVisibility.NULL,
                70, 70 * (i + 1), "F$el", code))
        }
        val fn = OscHiddenToggle("fn", OscVisibility.NULL,
            70, 0, "FN", fnButtons)

        // Quick buttons: 0 to 9
        val quickButtons = ArrayList<OscHiddenButton>()
        for (i in 0..9) {
            val code = KeyEvent.KEYCODE_0 + i
            quickButtons.add(OscHiddenButton("qp$i", OscVisibility.NULL,
                0, 70 * (i + 1), "$i", code))
        }
        val qp = OscHiddenToggle("qp", OscVisibility.NULL,
            0, 0, "QP", quickButtons)

        elements.addAll(fnButtons)
        elements.add(fn)
        elements.addAll(quickButtons)
        elements.add(qp)
    }

    fun placeElements(target: RelativeLayout) {
        for (element in elements) {
            element.place(target)
            element.loadPrefs(target.context)
        }
        osk.placeElements(target)

        target.addOnLayoutChangeListener { v, l, t, r, b, ol, ot, or, ob -> relayout(l, t, r, b, ol, ot, or, ob) }

        showNonEssential()
    }

    fun toggleKeyboard() {
        osk.toggle()

        if (!keyboardVisible) {
            keyboardVisible = true
            setVisibility(OscVisibility.KEYBOARD.v)
        } else {
            keyboardVisible = false
            if (SDLActivity.isMouseShown() == 0)
                showNonEssential()
            else
                hideNonEssential()
        }
    }

    fun placeConfigurableElements(target: RelativeLayout, listener: View.OnTouchListener) {
        for (element in elements) {
            element.placeConfigurable(target, listener)
            element.loadPrefs(target.context)
        }

        target.addOnLayoutChangeListener { v, l, t, r, b, ol, ot, or, ob -> relayout(l, t, r, b, ol, ot, or, ob) }
    }

    fun resetElements(ctx: Context) {
        for (element in elements) {
            element.resetPrefs(ctx)
        }
    }

    /**
     * Hide/show stuff based on visibility state
     */
    private fun setVisibility(newState: Int) {
        if (visibilityState == newState)
            return

        for (element in elements) {
            // don't touch elements with NULL visibility as these are managed externally
            if (element.visibility == OscVisibility.NULL)
                continue
            if (newState and element.visibility.v == 0) {
                element.view?.visibility = View.GONE
            } else {
                element.view?.visibility = View.VISIBLE
            }
        }

        visibilityState = newState
    }

    /**
     * Hides everything except the widgets that should be visible in inventory screen
     */
    fun hideNonEssential() {
        if (!keyboardVisible)
            setVisibility(OscVisibility.ESSENTIAL.v)
    }

    /**
     * Shows all widgets again
     */
    fun showNonEssential() {
        if (!keyboardVisible)
            setVisibility(OscVisibility.ESSENTIAL.v or OscVisibility.NORMAL.v)
    }

    private fun relayout(l: Int, t: Int, r: Int, b: Int, ol: Int, ot: Int, or: Int, ob: Int) {
        // don't do anything if layout didn't change
        if (l == ol && t == ot && r == or && b == ob)
            return
        for (element in elements) {
            element.updateView()
        }
    }

}
