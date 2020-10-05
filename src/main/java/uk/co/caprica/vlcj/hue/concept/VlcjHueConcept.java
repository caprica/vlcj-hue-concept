package uk.co.caprica.vlcj.hue.concept;

import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VlcjHueConcept {

    private static boolean hardware = false; // Set to true to enable Hue API calls - but make sure you read the source code first

    private final CallbackMediaPlayerComponent mediaPlayerComponent;

    public VlcjHueConcept() {
        final JFrame frame = new JFrame("vlcj Hue Concept Demo");

        int rows = 3;
        int cols = 3;

        mediaPlayerComponent = new CallbackMediaPlayerComponent(
            null,
            null,
            null,
            true,
            hardware ? new HueSamplingCallbackImagePainter(rows, cols) : new SamplingCallbackImagePainter(rows, cols),
            null,
            null,
            null);

        mediaPlayerComponent.mediaPlayer().overlay().enable(true);

        frame.setBackground(Color.black);
        frame.setContentPane(mediaPlayerComponent);

        frame.setLocation(200, 200);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        args = new String[] {"https://www.youtube.com/watch?v=7rCzIwlX8Dg"};

        VlcjHueConcept app = new VlcjHueConcept();
        app.mediaPlayerComponent.mediaPlayer().media().play(args[0]);
    }
}
