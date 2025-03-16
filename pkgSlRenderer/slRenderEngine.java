package pkgSlRenderer;

import pkgSlUtils.slWindowManager;

public abstract class slRenderEngine {
    // Abstract methods to be implemented by subclasses
    public abstract void initOpenGL(slWindowManager windowManager);
    public abstract void render();

}
