package ui.controls;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by sylar on 06.07.15.
 */
public class AlphaView {
    public static void setAlphaForView(View v, float alpha) {
        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }
}
