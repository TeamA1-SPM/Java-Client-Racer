package main.menuhelper;

import javax.swing.*;
import java.awt.*;

public class LabelManager {

    public static void createLabel(JLabel label, Font font, Color color, Rectangle bounds) {
        label.setFont(font);
        label.setForeground(color);
        label.setBounds(bounds);
        label.setOpaque(true);
        label.setVisible(false);
    }
}
