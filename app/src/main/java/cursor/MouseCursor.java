package cursor;

import android.app.Activity;
import android.view.Choreographer;
import android.view.View;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;

public class MouseCursor implements Choreographer.FrameCallback {

    private Choreographer choreographer;
    private View cursor;

    public MouseCursor(Activity activity) {
        cursor = activity.findViewById(R.id.mouseCursor);

        choreographer = Choreographer.getInstance();
        choreographer.postFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (SDLActivity.isMouseShown() == 0) {
            cursor.setVisibility(View.GONE);
        } else {
            cursor.setVisibility(View.VISIBLE);

            int mouseX = SDLActivity.getMouseX();
            int mouseY = SDLActivity.getMouseY();

            cursor.setX(mouseX);
            cursor.setY(mouseY);
        }

        choreographer.postFrameCallback(this);
    }
}
