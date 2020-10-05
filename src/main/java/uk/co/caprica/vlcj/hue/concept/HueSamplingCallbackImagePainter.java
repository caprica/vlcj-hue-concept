package uk.co.caprica.vlcj.hue.concept;

import uk.co.caprica.brue.core.domain.bridge.builder.LightStateBuilder;
import uk.co.caprica.brue.core.service.bridge.BridgeService;
import uk.co.caprica.brue.core.settings.bridge.BridgeSettings;
import uk.co.caprica.brue.okhttp.service.bridge.BridgeServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static uk.co.caprica.vlcj.hue.concept.ColourUtil.getRGBtoXY;

public class HueSamplingCallbackImagePainter extends SamplingCallbackImagePainter {

    private final BridgeService bridgeService = new BridgeServiceImpl(new TestBridgeSettings());

    private int[] samples;

    // Update the lights every 100ms, you should experiment with how often you want to (or can) update the lights
    private final Timer timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // FIXME adjust these to your own lights - discover them rather than hard-coding them etc
            // FIXME maybe optimise to set all lights in one go if the API supports it
//            setColour(7, samples[0]);
//            setColour(8, samples[1]);
//            setColour(9, samples[2]);
//            setColour(10, samples[3]);
        }
    });

    public HueSamplingCallbackImagePainter(int rows, int cols) {
        super(rows, cols);
        timer.start();
    }

    @Override
    protected void sample(int[] samples) {
        super.sample(samples);
        this.samples = samples;
    }

    private void setColour(int light, int rgb) {
        double[] xy = getRGBtoXY(new Color(rgb));
        bridgeService.light().state(light, LightStateBuilder.lightState().xy((float) xy[0], (float) xy[1]));
    }

    private static final class TestBridgeSettings implements BridgeSettings {

        @Override
        public String host() {
            return "192.168.0.xxx";
        } // FIXME

        @Override
        public String username() {
            return "yoursecrethere";
        } // FIXME
    }
}
