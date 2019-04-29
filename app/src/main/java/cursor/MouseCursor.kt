package cursor

import android.content.Context
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
internal class FixedSizeImageView(context: Context, private val fixedWidth: Int, private val fixedHeight: Int) : androidx.appcompat.widget.AppCompatImageView(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(fixedWidth, fixedHeight)
    }
}

class MouseCursor(activity: GameActivity, private val osc: Osc?) : Choreographer.FrameCallback {

    private val choreographer: Choreographer
    private val cursor: FixedSizeImageView
    private val layout: RelativeLayout
    private var prevMouseShown = -1

    init {

        val r = activity.resources

        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, r.displayMetrics).toInt()
        val width = Math.round(height / 1.5).toInt()

        cursor = FixedSizeImageView(activity, width, height)
        cursor.setImageResource(R.drawable.pointer_arrow)

        cursor.layoutParams = RelativeLayout.LayoutParams(width, height)

        layout = activity.layout
        layout.addView(cursor)

        choreographer = Choreographer.getInstance()
        choreographer.postFrameCallback(this)
    }

    override fun doFrame(frameTimeNanos: Long) {
        // Check if we need to switch osc widgets visibility
        val mouseShown = SDLActivity.isMouseShown()
        if (this.osc != null && mouseShown != prevMouseShown) {
            if (mouseShown == 0)
                this.osc.showNonEssential()
            else
                this.osc.hideNonEssential()
        }

        if (mouseShown == 0) {
            cursor.visibility = View.GONE
        } else {
            cursor.visibility = View.VISIBLE

            val surface = SDLActivity.getSurface()

            val translateX = 1.0f * surface.width / MainActivity.resolutionX
            val translateY = 1.0f * surface.height / MainActivity.resolutionY

            val mouseX = SDLActivity.getMouseX()
            val mouseY = SDLActivity.getMouseY()

            // calling setX/setY here results in a bug cropping part of the mouse cursor
            // changing LayoutParams works as expected...
            val params = cursor.layoutParams as RelativeLayout.LayoutParams
            params.leftMargin = (mouseX * translateX + surface.left).toInt()
            params.topMargin = (mouseY * translateY + surface.top).toInt()
            cursor.layoutParams = params
        }

        choreographer.postFrameCallback(this)
        prevMouseShown = mouseShown
    }
}
