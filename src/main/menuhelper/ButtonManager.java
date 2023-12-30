package main.menuhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static main.menuhelper.FontManager.*;

public class ButtonManager {

    public static void createButton(JButton button, String text, Rectangle bounds, ActionListener listener) {
        button.setBounds(bounds);
        button.setFont(FontSize40);
        button.setText(text);
        button.addActionListener(listener);
    }
}
