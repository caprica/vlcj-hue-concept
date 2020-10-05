package uk.co.caprica.vlcj.hue.concept;

import javax.swing.*;
import java.awt.*;

public class ColourGrid extends JPanel {

    private final JPanel[] panels;

    public ColourGrid(int rows, int cols) {
        this.panels = new JPanel[rows * cols];

        setBackground(Color.black);
        setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows * cols; i++) {
            this.panels[i] = new JPanel();
            this.panels[i].setBackground(Color.black);
            add(this.panels[i]);
        }
    }

    public void set(int[] rgbs) {
        for (int i = 0; i < rgbs.length; i++) {
            panels[i].setBackground(new Color(rgbs[i]));
        }
    }
}
