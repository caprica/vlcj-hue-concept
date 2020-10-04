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
    private final int rows = 2;
    private final int cols = 2;

    private final ColourFrame colourFrame = new ColourFrame(rows, cols);

    private final Rectangle[] rectangles = new Rectangle[rows * cols];

    public SamplingCallbackImagePainter() {
        for (int i = 0; i < rows * cols; i++) {
            rectangles[i] = new Rectangle();
        }
        colourFrame.setVisible(true);
    }

    @Override
    public void paint(Graphics2D g2, JComponent component, BufferedImage image) {
        if (image != null) {
            sample(image);
        }
        super.paint(g2, component, image);
    }

    private void sample(BufferedImage image) {
        int width = image.getWidth() / 2;
        int height = image.getHeight() / 2;
        rectangles[0].setBounds(0, 0, width, height);
        rectangles[1].setBounds( width, 0, width, height);
        rectangles[2].setBounds( 0, height, width, height);
        rectangles[3].setBounds( width, height, width, height);
        int rgb1 = sample(image, rectangles[0]);
        int rgb2 = sample(image, rectangles[1]);
        int rgb3 = sample(image, rectangles[2]);
        int rgb4 = sample(image, rectangles[3]);
        sample(rgb1, rgb2, rgb3, rgb4);
    }

    // FIXME using difference of squares is likely overkill here
    private static final boolean useSquares = true;

    private static int sample(BufferedImage image, Rectangle rc) {
        // FIXME could reuse a buffer for param #5
        int[] pixels = image.getRGB(rc.x, rc.y, rc.width, rc.height, null, 0, image.getWidth());
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

    protected void sample(int rgb1, int rgb2, int rgb3, int rgb4) {
        colourFrame.set(0, new Color(rgb1));
        colourFrame.set(1, new Color(rgb2));
        colourFrame.set(2, new Color(rgb3));
        colourFrame.set(3, new Color(rgb4));
    }
}
