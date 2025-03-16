package pkgSlRenderer;

import pkgSlUtils.slWindowManager;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;

public class slGameOfLifeRenderer extends slRenderEngine {
    private int numRows;
    private int numCols;
    private int frameDelay;

    private boolean[][] currentGrid;
    private boolean[][] nextGrid;

    private float cellWidth;
    private float cellHeight;

    private float cellPadding = 0.002f; // Amount of space between cells

    private slWindowManager windowManager;

    public slGameOfLifeRenderer(int numRows, int numCols, int frameDelay) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.frameDelay = frameDelay;

        currentGrid = new boolean[numRows][numCols];
        nextGrid = new boolean[numRows][numCols];

        initializeGrids(); // Initialize the grids with a random state
    }

    @Override
    public void initOpenGL(slWindowManager windowManager) {
        this.windowManager = windowManager;

        GL.createCapabilities();  // Initialize OpenGL context
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set background color to black

        // Calculate cell dimensions based on the window size and grid dimensions
        cellWidth = 2.0f / numCols; // OpenGL coordinate system ranges from -1 to 1
        cellHeight = 2.0f / numRows;
    }

    @Override
    public void render() {
        while (!windowManager.windowShouldClose()) {
            processInput(); // Handle user input

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  // Clear the screen

            renderGrid(); // Render the current state of the grid

            updateGrid(); // Update the grid based on the Game of Life rules

            windowManager.swapBuffers();  // Swap buffers to display the frame

            // Frame delay to control simulation speed
            if (frameDelay > 0) {
                try {
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Initialize the grids with a random state
    private void initializeGrids() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                currentGrid[row][col] = Math.random() < 0.2; // 20% chance the cell is alive
            }
        }
    }

    // Render the grid of cells
    private void renderGrid() {
        glBegin(GL_QUADS);
        glColor3f(0.0f, 1.0f, 0.0f); // Green color for live cells
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (currentGrid[row][col]) {
                    drawCell(row, col);
                }
            }
        }
        glEnd();
    }

    // Draw a single cell at the specified grid position with padding
    private void drawCell(int row, int col) {
        float x = -1.0f + col * cellWidth;
        float y = -1.0f + row * cellHeight;

        // Adjust cell dimensions to add padding
        float paddingX = cellPadding * cellWidth;
        float paddingY = cellPadding * cellHeight;

        float x0 = x + paddingX;
        float y0 = y + paddingY;
        float x1 = x + cellWidth - paddingX;
        float y1 = y + cellHeight - paddingY;

        glVertex2f(x0, y0);
        glVertex2f(x1, y0);
        glVertex2f(x1, y1);
        glVertex2f(x0, y1);
    }

    // Update the grid based on the Game of Life rules
    private void updateGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);
                if (currentGrid[row][col]) {
                    // Rule 1 & 3: Underpopulation or Overpopulation
                    nextGrid[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    // Rule 4: Reproduction
                    nextGrid[row][col] = liveNeighbors == 3;
                }
            }
        }
        // Swap the grids for the next iteration
        boolean[][] temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }

    // Count the number of live neighbors for a given cell
    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; // Skip the cell itself
                int r = row + dr;
                int c = col + dc;
                // Check if neighbor is within bounds and alive
                if (r >= 0 && r < numRows && c >= 0 && c < numCols && currentGrid[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Handle user input for key events
    private void processInput() {
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_R)) {
            initializeGrids();
            System.out.println("Game reset!");
            windowManager.resetKeyPressed(GLFW.GLFW_KEY_R);
        }

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_I)) {
            frameDelay += 100;
            System.out.println("Frame delay increased to " + frameDelay + " ms");
            windowManager.resetKeyPressed(GLFW.GLFW_KEY_I);
        }

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_D)) {
            frameDelay = Math.max(0, frameDelay - 100);
            System.out.println("Frame delay decreased to " + frameDelay + " ms");
            windowManager.resetKeyPressed(GLFW.GLFW_KEY_D);
        }

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            GLFW.glfwSetWindowShouldClose(windowManager.getWindowHandle(), true);
            windowManager.resetKeyPressed(GLFW.GLFW_KEY_ESCAPE);
        }
    }

    // These methods are not used but required by the abstract base class

}
