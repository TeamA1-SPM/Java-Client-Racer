package main.menuhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonManager {

    public static void createButton(JButton button, String text, Rectangle bounds, ActionListener listener) {
        button.setBounds(bounds);
        button.setFont(FontManager.getSize40());
        button.setText(text);
        button.addActionListener(listener);
    }
}
