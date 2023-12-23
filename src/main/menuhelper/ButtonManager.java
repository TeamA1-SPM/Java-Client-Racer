package main.menuhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ButtonManager {

    public static JButton createButton(String text, int x, int y, int width, int height, ActionListener listener) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Calibri", Font.BOLD, 40));
        button.setText(text);
        button.addActionListener(listener);
        return button;
    }

    public static void addButtonsToLabel(JLabel label, List<JButton> buttons) {
        for (JButton button : buttons) {
            label.add(button);
        }
    }
}
