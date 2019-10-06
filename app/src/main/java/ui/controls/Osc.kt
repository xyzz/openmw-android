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
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.libopenmw.openmw.R
import org.jetbrains.anko.defaultSharedPreferences
import org.libsdl.app.SDLActivity
import ui.activity.GameActivity
import ui.activity.MouseMode

const val VIRTUAL_SCREEN_WIDTH = 1024
const val VIRTUAL_SCREEN_HEIGHT = 768
const val CONTROL_DEFAULT_SIZE = 70
const val JOYSTICK_SIZE = 230
const val JOYSTICK_OFFSET = 110
const val TOP_BAR_SPACING = 90

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
        var visibility: OscVisibility,
        val defaultX: Int,
        val defaultY: Int,
        private val defaultSize: Int = CONTROL_DEFAULT_SIZE,
        private val defaultOpacity: Float = 0.4f
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

        val realScreenWidth = (v.parent as View).width
        val realScreenHeight = (v.parent as View).height
        val realX = x * realScreenWidth / VIRTUAL_SCREEN_WIDTH
        val realY = y * realScreenHeight / VIRTUAL_SCREEN_HEIGHT

        val screenSize = (1.0 * size * realScreenWidth / VIRTUAL_SCREEN_WIDTH).toInt()
        val params = RelativeLayout.LayoutParams(screenSize, screenSize)

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
        defaultSize: Int = CONTROL_DEFAULT_SIZE
) : OscElement(uniqueId, visibility, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        // fix blurry icons on old android
        v.scaleType = ImageView.ScaleType.FIT_XY
        v.setOnTouchListener(ButtonTouchListener(keyCode, needMouse))
        v.tag = this

        view = v
    }

}

class OscCustomButton(
    uniqueId: String,
    visibility: OscVisibility,
    private val imageSrc: Int,
    defaultX: Int,
    defaultY: Int,
    private val handler: () -> Unit
) : OscElement(uniqueId, visibility, defaultX, defaultY) {

    @SuppressLint("ClickableViewAccessibility")
    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        v.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP)
                handler()
            true
        }
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
    // Widgets visible during gameplay
    NORMAL(2),
}

class Osc {
    private var osk = Osk()
    var keyboardVisible = false //< Mode where only keyboard is visible
    var mouseVisible = false //< Mode where only mouse-switch icon is visible
    private var topVisible = true //< The controls located at the top hidden behind the hamburger toggle
    private var visibilityState = 0
    private val btnMouse = OscCustomButton("mouse", OscVisibility.NULL,
        R.drawable.mouse, TOP_BAR_SPACING * 6, 0) { toggleMouse() }
    private val btnTopToggle = OscCustomButton("toggle", OscVisibility.NULL,
        R.drawable.toggle, 0, 0) { toggleTopControls() }

    private val joystickLeft = OscJoystickLeft("joystickLeft", OscVisibility.NORMAL,
        JOYSTICK_OFFSET, 400, JOYSTICK_SIZE, 0)
    private val joystickRight = OscJoystickRight("joystickRight", OscVisibility.ESSENTIAL,
        VIRTUAL_SCREEN_WIDTH - JOYSTICK_SIZE - JOYSTICK_OFFSET,
        400, JOYSTICK_SIZE, 1)

    private var elements = arrayListOf(
        joystickLeft,
        joystickRight,

        btnTopToggle,
        OscImageButton("inventory", OscVisibility.NULL,
            R.drawable.inventory, 940, 95, 3, true),
        OscImageButton("crouch", OscVisibility.NORMAL,
                R.drawable.sneak, 940 - TOP_BAR_SPACING, 0, 113),
        OscImageButton("pause", OscVisibility.ESSENTIAL,
            R.drawable.pause, 940, 0, KeyEvent.KEYCODE_ESCAPE),
        OscImageButton("magic", OscVisibility.NORMAL,
            R.drawable.toggle_magic, 940, 450, KeyEvent.KEYCODE_R),
        OscImageButton("weapon", OscVisibility.NORMAL,
            R.drawable.toggle_weapon, 940, 560, KeyEvent.KEYCODE_F),
        OscAttackButton("fire", OscVisibility.ESSENTIAL,
            R.drawable.attack, 800, 315, 1, 120),
        OscImageButton("use", OscVisibility.NORMAL,
            R.drawable.use, joystickLeft.defaultX + JOYSTICK_SIZE/2 + 105, 630,
            KeyEvent.KEYCODE_SPACE),
        OscImageButton("jump", OscVisibility.NORMAL, R.drawable.jump,
            joystickRight.defaultX + JOYSTICK_SIZE/2 - 105 - CONTROL_DEFAULT_SIZE,
            630, KeyEvent.KEYCODE_E)
    )

    private val topButtons: ArrayList<OscElement>
    private val fnButtons = arrayListOf<OscHiddenButton>()
    private val quickButtons = arrayListOf<OscHiddenButton>()
    private val fn: OscHiddenToggle
    private val qp: OscHiddenToggle

    init {
        val btnRowSpacing = 74
        val btnColumnSpacing = 65

        // Fn buttons: F1, F3, F4, F10, F11 are the only ones we care about
        arrayOf(1, 3, 4, 10, 11).forEachIndexed{ i, el ->
            val code = 130 + el
            val column = (i + 1) / 4 + 2
            val row = (i + 1) % 4 + 1
            fnButtons.add(OscHiddenButton("f$el", OscVisibility.NULL,
                btnColumnSpacing * column, btnRowSpacing * row, "F$el", code))
        }
        fn = OscHiddenToggle("fn", OscVisibility.NULL,
            2 * btnColumnSpacing, btnRowSpacing, "FN", fnButtons)

        // Quick buttons: 0 to 9
        for (i in 0..9) {
            val code = KeyEvent.KEYCODE_0 + i
            val column = (i + 1) / 9
            val row = (i + 1) % 9 + 1
            quickButtons.add(OscHiddenButton("qp$i", OscVisibility.NULL,
                btnColumnSpacing * column, btnRowSpacing * row, "$i", code))
        }
        qp = OscHiddenToggle("qp", OscVisibility.NULL,
            0, btnRowSpacing, "QP", quickButtons)

        topButtons = arrayListOf(
            OscImageButton("changePerson", OscVisibility.NORMAL,
                R.drawable.third_person, TOP_BAR_SPACING * 1, 0, KeyEvent.KEYCODE_TAB),
            OscImageButton("quickSave", OscVisibility.NORMAL,
                R.drawable.save, TOP_BAR_SPACING * 2, 0, 135),
            OscImageButton("diary", OscVisibility.ESSENTIAL,
                R.drawable.journal, TOP_BAR_SPACING * 3, 0, KeyEvent.KEYCODE_J),
            OscImageButton("wait", OscVisibility.NORMAL,
                R.drawable.wait, TOP_BAR_SPACING * 4, 0, KeyEvent.KEYCODE_T),
            OscCustomButton("keyboard", OscVisibility.NULL,
                R.drawable.keyboard, TOP_BAR_SPACING * 5, 0) { toggleKeyboard() },
            btnMouse
        )

        elements.addAll(fnButtons)
        elements.add(fn)
        elements.addAll(quickButtons)
        elements.add(qp)
        elements.addAll(topButtons)
    }

    fun placeElements(target: RelativeLayout) {
        val prefs = target.context.defaultSharedPreferences
        val showQp = prefs.getBoolean("pref_show_qp", false)
        val showFn = prefs.getBoolean("pref_show_fn", false)
        val alwaysShowTop = prefs.getBoolean("pref_always_show_top_bar", false)

        for (element in elements) {
            if (!showQp && (element == qp || quickButtons.contains(element)))
                continue
            if (!showFn && (element == fn || fnButtons.contains(element)))
                continue
            if (alwaysShowTop && element == btnTopToggle)
                continue
            // If we want to utilize top-bar, don't let mouse/keyboard icons control it
            if (!alwaysShowTop && topButtons.contains(element))
                element.visibility = OscVisibility.NULL

            element.place(target)
            element.loadPrefs(target.context)
        }
        osk.placeElements(target)

        target.addOnLayoutChangeListener { v, l, t, r, b, ol, ot, or, ob -> relayout(l, t, r, b, ol, ot, or, ob) }

        showBasedOnState()

        // Mouse button is only needed in hybrid mode
        if (GameActivity.mouseMode != MouseMode.Hybrid)
            btnMouse.view?.visibility = View.GONE

        // Prepare initial top-button state
        if (!alwaysShowTop)
            toggleTopControls()
    }

    fun toggleKeyboard() {
        osk.toggle()

        keyboardVisible = !keyboardVisible
        showBasedOnState()
    }

    private fun toggleTopControls() {
        topVisible = !topVisible
        // Note that this is done separate from the showBasedOnState mode
        // Perhaps some refactoring is due
        topButtons.forEach {
            it.view?.visibility = if (topVisible) View.VISIBLE else View.GONE
        }
    }

    /**
     * Displays different controls depending on current state
     * - keyboard visibility
     * - mouse-mode visibility
     * - actual mouse cursor visibility
     */
    fun showBasedOnState() {
        // If keyboard or mouse-mode or both, then hide everything
        if (keyboardVisible || mouseVisible) {
            setVisibility(OscVisibility.NULL.v)
        } else {
            if (SDLActivity.isMouseShown() == 0)
                setVisibility(OscVisibility.ESSENTIAL.v or OscVisibility.NORMAL.v)
            else
                setVisibility(OscVisibility.ESSENTIAL.v)
        }
    }

    fun toggleMouse() {
        mouseVisible = !mouseVisible
        showBasedOnState()
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

    private fun relayout(l: Int, t: Int, r: Int, b: Int, ol: Int, ot: Int, or: Int, ob: Int) {
        // don't do anything if layout didn't change
        if (l == ol && t == ot && r == or && b == ob)
            return
        for (element in elements) {
            element.updateView()
        }
    }

}
