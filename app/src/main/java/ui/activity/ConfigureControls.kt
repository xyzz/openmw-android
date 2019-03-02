package ui.activity

import com.libopenmw.openmw.R

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

import ui.controls.Osc
import ui.controls.OscElement
import ui.controls.VIRTUAL_SCREEN_HEIGHT
import ui.controls.VIRTUAL_SCREEN_WIDTH
import utils.Utils.hideAndroidControls

class ConfigureCallback(activity: Activity) : View.OnTouchListener {

    var currentView: View? = null
    private var layout: RelativeLayout = activity.findViewById(R.id.controlsContainer)
    private var origX: Float = 0.0f
    private var origY: Float = 0.0f
    private var startX: Float = 0.0f
    private var startY: Float = 0.0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentView?.setBackgroundColor(Color.TRANSPARENT)
                currentView = v
                v.setBackgroundColor(Color.RED)
                origX = v.x
                origY = v.y
                startX = event.rawX
                startY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> if (currentView != null) {
                val view = currentView!!
                val x = ((event.rawX - startX) + origX).toInt()
                val y = ((event.rawY - startY) + origY).toInt()

                val el = view.tag as OscElement
                el.changePosition(x * VIRTUAL_SCREEN_WIDTH / layout.width, y * VIRTUAL_SCREEN_HEIGHT / layout.height)
                el.updateView()
            }
        }

        return true
    }

}

class ConfigureControls : Activity() {

    private var callback: ConfigureCallback? = null
    private var osc = Osc()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.configure_controls)

        val cb = ConfigureCallback(this)
        callback = cb

        val container: RelativeLayout = findViewById(R.id.controlsContainer)
        osc.placeConfigurableElements(container, cb)
        window.decorView.setOnSystemUiVisibilityChangeListener {
            osc.relayout()
        }
        container.post { osc.relayout() }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            hideAndroidControls(this)
        }
    }

    private fun changeOpacity(delta: Float) {
        val view = callback?.currentView ?: return
        val el =  view.tag as OscElement
        el.changeOpacity(delta)
        el.updateView()
    }

    private fun changeSize(delta: Int) {
        val view = callback?.currentView ?: return
        val el =  view.tag as OscElement
        el.changeSize(delta)
        el.updateView()
    }

    fun clickOpacityPlus(v: View) {
        changeOpacity(0.1f)
    }

    fun clickOpacityMinus(v: View) {
        changeOpacity(-0.1f)
    }

    fun clickSizePlus(v: View) {
        changeSize(5)
    }

    fun clickSizeMinus(v: View) {
        changeSize(-5)
    }

    fun clickResetControls(v: View) {
        osc.resetElements(applicationContext)
    }

    fun clickBack(v: View) {
        finish()
    }

}
