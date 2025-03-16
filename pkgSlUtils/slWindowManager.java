package pkgSlUtils;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

public class slWindowManager {
    private static slWindowManager instance = null;
    private long windowHandle;

    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST];

    private slWindowManager() { }

    public static slWindowManager get() {
        if (instance == null) {
            instance = new slWindowManager();
        }
        return instance;
    }

    public void initGLFWWindow(int width, int height, String title) {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        windowHandle = glfwCreateWindow(width, height, title, 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowHandle, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        glfwMakeContextCurrent(windowHandle);
        glfwShowWindow(windowHandle);

        // Set up key callback for handling key events
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key < 0 || key >= keyPressed.length) {
                return;
            }
            if (action == GLFW_PRESS) {
                keyPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                keyPressed[key] = false;
            }
        });
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    // Check if a specific key is pressed
    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keyPressed.length) {
            return keyPressed[keyCode];
        } else {
            return false;
        }
    }

    // Reset the key press event to prevent continuous firing
    public void resetKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keyPressed.length) {
            keyPressed[keyCode] = false;
        }
    }

    // Get the window handle (needed for setting window should close)
    public long getWindowHandle() {
        return windowHandle;
    }
}
