import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Lens {
    protected double ho; // Height of object
    protected double disO; // Distance of object
    protected double f; // Focal length
    
    public Lens(double ho, double disO, double f) {
        this.ho = ho;
        this.disO = disO;
        this.f = f;
    }
    
    /**
     * Draws the lens lines on the lens view
     * @param gc The GraphicsContext to draws the lens on
     */
    public final void drawLens(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);

        for (int x=0; x<=Main.CANVAS_WIDTH; x+=50) {
            gc.strokeLine(x, getYl(), x+25, getYl());
        }
        for (int y=0; y<=Main.CANVAS_HEIGHT; y+=50) {
            gc.strokeLine(getXl(), y, getXl(), y+25);
        }
    }
    
    
    /**
     * Calculates where the light rays will be and draws them on the lens view
     * @param gc The GraphicsContext to draws the light refractions on
     */
    public final void drawLight(GraphicsContext gc){          
        drawPRay(gc);
        drawCRay(gc);
        drawFRay(gc);
    }
    
    protected abstract void drawPRay(GraphicsContext gc);
    protected abstract void drawFRay(GraphicsContext gc);
    
    /**
     * Calculates the locations of the C ray and then draws them to the lens view
     * @param gc The GraphicsContext to draws the light refractions on
     */
    private void drawCRay(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        
        // Calulate the angle the light ray will travel to the center
        double rayAngle = Math.atan(disO / ho);
        
        // Create triangle to find x and y sizes
        double relativeX = Main.CANVAS_WIDTH; // A large x value to draw the line to
        double relativeY = relativeX / Math.tan(rayAngle);
        
        // Move x and y posistions to start at the object
        double exactX = relativeX + getXl() - disO;
        double exactY = relativeY + getYl() - ho;
        
        gc.strokeLine(getXl() - disO, getYl() - ho, exactX, exactY);
        
        // Draw reflected ray for virtual image
        if (f > disO || this instanceof DivergingLens) {
            gc.setStroke(Color.RED);
            
            // Create imaginary triangle to find x and y values
            double xRelative = Main.CANVAS_WIDTH; // large number to ensure ray is long enough
            // Find the complementary angle (the angle that the triangle will use) by doing 90 - the ray angle
            double reflectedAngle = Math.toRadians(90 - Math.toDegrees(rayAngle));
            double yRelative = Math.tan(reflectedAngle) * xRelative;
            
            // Move the points to the correct posistion 
            double xExact = getXl() - disO - xRelative;
            double yExact = getYl() - ho - yRelative;
            
            gc.strokeLine(getXl() - disO, getYl() - ho, xExact, yExact);
        }
    }
    
    /**
     * Prints the values of the image to the lens view
     * @param gc The GraphicsContext to draws the light refractions on
     */
    public abstract void outputValues (GraphicsContext gc);
    
    /**
     * @return The center x position of the lense
     */
    protected final double getXl() {
        return Main.CANVAS_WIDTH / 2;
    }
    
    /**
     * @return The center y position of the lense
     */
    protected final double getYl() {
        return Main.CANVAS_HEIGHT / 2;
    }
}
