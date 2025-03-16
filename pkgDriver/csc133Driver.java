package pkgDriver;

import pkgSlRenderer.slGameOfLifeRenderer;
import static pkgDriver.slSpot.*;
import pkgSlUtils.slWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        int numRows = 100; // Number of rows in the grid
        int numCols = 100; // Number of columns in the grid
        int frameDelay = 0; // Initial frame delay in milliseconds

        slGameOfLifeRenderer my_re = new slGameOfLifeRenderer(numRows, numCols, frameDelay);
        slWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "csc133");
        my_re.initOpenGL(slWindowManager.get());

        my_re.render();  // Start the Game of Life simulation
    }
}
