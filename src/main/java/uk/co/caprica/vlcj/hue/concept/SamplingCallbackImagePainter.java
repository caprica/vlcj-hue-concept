package uk.co.caprica.vlcj.hue.concept;

import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.component.callback.SampleAspectRatioCallbackImagePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

/**
 *
 * <p>
 * This example uses the vlcj {@link CallbackMediaPlayerComponent}, it could be ported to a JavaFX solution quite easily
 * instead.
 * <p>
 * This is a naive implementation, there are likely many optimisations that could be made.
 * <p>
 * The sampling is done during painting, which is likely not optimal - it may be better to periodically grab one frame
 * and sample that in a thread at a reduced rate rather than sampling every video frame.
 * <p>
 * Other avenues for optimisation would be buffer re-use when grabbing the RGB pixels, perhaps inlining the RGB colour
 * component operations, and probably many other non-optimal things.
 */
public class SamplingCallbackImagePainter extends SampleAspectRatioCallbackImagePainter {

    // This example uses 4 light regions - the 4 rectangular quadrants, TL, TR, BL, BR - the idea with this arrangement
    // is two lights in front and two lights behind (or to the sides)
    private final int rows;
    private final int cols;

    private final ColourFrame colourFrame;

    private final Rectangle[] rectangles;

    public int segmentWidth;
    public int segmentHeight;

    private int[] samples;

    public SamplingCallbackImagePainter(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.colourFrame = new ColourFrame(rows, cols);
        this.rectangles = new Rectangle[rows * cols];

        for (int i = 0; i < rows * cols; i++) {
            rectangles[i] = new Rectangle();
        }
        samples = new int[rows * cols];
        colourFrame.setVisible(true);
    }

    @Override
    public void newVideoBuffer(int width, int height) {
        segmentWidth = width / cols;
        segmentHeight = height / cols;
        for (int i = 0, r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rectangles[i++] = new Rectangle(c * segmentWidth, r * segmentHeight, segmentWidth, segmentHeight);
            }
        }
    }

    @Override
    public void paint(Graphics2D g2, JComponent component, BufferedImage image) {
        if (image != null) {
            for (int i = 0; i < rectangles.length; i++) {
                samples[i] = sample(image, rectangles[i]);
            }
            sample(samples);
        }
        super.paint(g2, component, image);
    }

    // FIXME using difference of squares is likely overkill here
    private static final boolean useSquares = true;

    private int sample(BufferedImage image, Rectangle rc) {
        // FIXME could reuse a buffer for param #5
        int[] pixels = image.getRGB(rc.x, rc.y, rc.width, rc.height, null, 0, rc.width);
        float r = 0;
        float g = 0;
        float b = 0;
        for (int i = 0; i < pixels.length; i++) {
            // FIXME maybe don't create an object here and inline the maths
            Color c = new Color(pixels[i]);
            if (useSquares) {
                r += c.getRed() * c.getRed();
                g += c.getGreen() * c.getGreen();
                b += c.getBlue() * c.getBlue();
            } else {
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        float sz = rc.width * rc.height;
        if (useSquares) {
            return new Color((int) floor(sqrt(r / sz)), (int) floor(sqrt(g / sz)), (int) floor(sqrt(b / sz))).getRGB();
        } else {
            return new Color((int) floor(r / sz), (int) floor(g / sz), (int) floor(b / sz)).getRGB();
        }
    }

    protected void sample(int[] samples) {
        colourFrame.set(samples);
    }
}
