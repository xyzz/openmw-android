package ui.controls

import android.content.Context
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.libopenmw.openmw.R

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
        private val uniqueId: String,
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

        // TODO: this doesn't take soft keys into account
        val realScreenWidth = v.context.resources.displayMetrics.widthPixels
        val realScreenHeight = v.context.resources.displayMetrics.heightPixels
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
        private val imageSrc: Int,
        defaultX: Int,
        defaultY: Int,
        defaultSize: Int,
        private val keyCode: Int,
        private val needMouse: Boolean = false
) : OscElement(uniqueId, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = ImageView(ctx)
        v.setImageResource(imageSrc)
        v.setOnTouchListener(ButtonTouchListener(keyCode, needMouse))
        v.tag = this

        view = v
    }

}

class OscJoystick(
        uniqueId: String,
        defaultX: Int,
        defaultY: Int,
        defaultSize: Int,
        private val stick: Int
) : OscElement(uniqueId, defaultX, defaultY, defaultSize) {

    override fun makeView(ctx: Context) {
        val v = Joystick(ctx)
        v.setStick(stick)
        v.tag = this

        view = v
    }

}

class Osc {
    private val elements = arrayOf(
        OscImageButton("run", R.drawable.run, 65, 330, 50, 115),
        OscImageButton("inventory", R.drawable.inventory, 950, 95, 50, 2, true),
        OscImageButton("pause", R.drawable.pause, 950, 0, 50, KeyEvent.KEYCODE_ESCAPE),
        OscImageButton("console", R.drawable.ontarget, 140, 0, 50, 132),
        OscImageButton("changePerson", R.drawable.backup, 212, 0, 50, KeyEvent.KEYCODE_TAB),
        OscImageButton("wait", R.drawable.wait, 274, 0, 50, KeyEvent.KEYCODE_T),

        OscJoystick("joystickLeft", 75, 400, 250, 0),
        OscJoystick("joystickRight", 650, 400, 250, 1)
    )

    fun placeElements(target: RelativeLayout) {
        for (element in elements) {
            element.place(target)
            element.loadPrefs(target.context)
        }
    }

    fun placeConfigurableElements(target: RelativeLayout, listener: View.OnTouchListener) {
        for (element in elements) {
            element.placeConfigurable(target, listener)
            element.loadPrefs(target.context)
        }
    }

    fun resetElements(ctx: Context) {
        for (element in elements) {
            element.resetPrefs(ctx)
        }
    }

}
