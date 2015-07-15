package ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import constants.Constants;

/**
 * Created by sylar on 15.07.15.
 */
public class GlExtensionsActivity extends Activity {

    private String GLExtensions = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGlExtencions();
    }

    private void getGlExtencions() {
        if (isDeviceSupportGles3()) {
            Constants.textureCompressionMode = "ETC2";
            startMainActivity();
        } else {
            GLSurfaceView surfaceView = new GLSurfaceView(this);
            surfaceView.setRenderer(renderer);

            setContentView(surfaceView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer() {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            GLExtensions = gl.glGetString(GL10.GL_EXTENSIONS);
            computeTextureCompressionMode();
            startMainActivity();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    };

    private void startMainActivity() {
        GlExtensionsActivity.this.startActivity(new Intent(GlExtensionsActivity.this, MainActivity.class));
        GlExtensionsActivity.this.finish();

    }

    public void computeTextureCompressionMode() {
        if (GLExtensions.contains("GL_IMG_texture_compression_pvrtc")) {
            Constants.textureCompressionMode = "PVR";
        } else if (GLExtensions.contains("GL_OES_texture_compression_S3TC") ||
                GLExtensions.contains("GL_EXT_texture_compression_s3tc")) {
            Constants.textureCompressionMode = "DXT";
        } else if (GLExtensions.contains("ETC1")) {
            Constants.textureCompressionMode = "ETC1";
        } else {
            Constants.textureCompressionMode = "UNKNOWN";
        }

    }


    protected boolean isDeviceSupportGles3() {
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x30000;

    }
}
