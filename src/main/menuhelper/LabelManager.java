package main.menuhelper;

import javax.swing.*;
import java.awt.*;

public class LabelManager {

    // Creates a label with the corresponding attributes
    public static void createLabel(JLabel label, Boolean visible, Font font, Color color, Rectangle bounds) {
        label.setFont(font);
        label.setForeground(color);
        label.setBounds(bounds);
        label.setOpaque(true);
        label.setVisible(visible);
    }
}
