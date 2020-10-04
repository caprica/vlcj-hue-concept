package uk.co.caprica.vlcj.hue.concept;

import javax.swing.*;
import java.awt.*;

public class ColourFrame extends JFrame {

    private final ColourGrid colourGrid;

    public ColourFrame(int rows, int cols) {
        this.colourGrid = new ColourGrid(rows, cols);

        setBackground(Color.black);
        setContentPane(this.colourGrid);
        setLocation(1000, 100);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void set(int index, Color colour) {
        colourGrid.set(index, colour);
    }
}
