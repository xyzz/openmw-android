package cursor;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.View;
import android.widget.RelativeLayout;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;

import ui.activity.GameActivity;
import ui.activity.MainActivity;
import ui.controls.Osc;

/**
 * An image view which doesn't downsize itself when moved to the border of a RelativeLayout
 * (not sure if original behavior is intended)
 */
class FixedSizeImageView extends android.support.v7.widget.AppCompatImageView {

    private int measuredWidth;
    private int measuredHeight;

    public FixedSizeImageView(Context context, int measuredWidth, int measuredHeight) {
        super(context);
        this.measuredWidth = measuredWidth;
        this.measuredHeight = measuredHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}

public class MouseCursor implements Choreographer.FrameCallback {

    private Choreographer choreographer;
    private FixedSizeImageView cursor;
    private RelativeLayout layout;
    private Osc osc;
    private int prevMouseShown = -1;

    public MouseCursor(GameActivity activity, Osc osc) {
        this.osc = osc;

        Resources r = activity.getResources();

        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
        int width = (int) Math.round(height / 1.5);

        cursor = new FixedSizeImageView(activity, width, height);
        cursor.setImageResource(R.drawable.pointer_arrow);

        cursor.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

        layout = activity.getLayout();
        layout.addView(cursor);

        choreographer = Choreographer.getInstance();
        choreographer.postFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        // Check if we need to switch osc widgets visibility
        int mouseShown = SDLActivity.isMouseShown();
        if (this.osc != null && mouseShown != prevMouseShown) {
            if (mouseShown == 0)
                this.osc.showNonEssential();
            else
                this.osc.hideNonEssential();
        }

        if (mouseShown == 0) {
            cursor.setVisibility(View.GONE);
        } else {
            cursor.setVisibility(View.VISIBLE);

            View surface = SDLActivity.getSurface();

            float translateX = 1.0f * surface.getWidth() / MainActivity.resolutionX;
            float translateY = 1.0f * surface.getHeight() / MainActivity.resolutionY;

            int mouseX = SDLActivity.getMouseX();
            int mouseY = SDLActivity.getMouseY();

            // calling setX/setY here results in a bug cropping part of the mouse cursor
            // changing LayoutParams works as expected...
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
            params.leftMargin = (int) (mouseX * translateX + surface.getLeft());
            params.topMargin = (int) (mouseY * translateY + surface.getTop());
            cursor.setLayoutParams(params);
        }

        choreographer.postFrameCallback(this);
        prevMouseShown = mouseShown;
    }
}
