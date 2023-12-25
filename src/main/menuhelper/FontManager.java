package main.menuhelper;

import java.awt.*;

public class FontManager {

    private static final Font size30 = new Font("Calibri", Font.BOLD, 30);
    private static final Font size40 = new Font("Calibri", Font.BOLD, 40);
    private static final Font size50 = new Font("Calibri", Font.BOLD, 50);
    private static final Font size65 = new Font("Calibri", Font.BOLD, 65);

    public static Font getSize30() { return size30; }
    public static Font getSize40() { return size40; }
    public static Font getSize50() { return size50; }
    public static Font getSize65() { return size65; }
}
