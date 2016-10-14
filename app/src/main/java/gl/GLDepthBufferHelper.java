package gl;

/**
 * Created by sandstranger on 06.07.16.
 */
public class GLDepthBufferHelper {
    private static int GL_DEPTH_BUFFER_SIZE = 16;
    private static final String GL_DEPTH_KEY = "OES_depth24";

    public static int getGlDepthBufferSize() {
        return GL_DEPTH_BUFFER_SIZE;
    }

    public static void computeGlDepthBufferSize(String GLExtensions) {
        if (GLExtensions.contains(GL_DEPTH_KEY)) {
            GL_DEPTH_BUFFER_SIZE = 24;
        }
    }
}
