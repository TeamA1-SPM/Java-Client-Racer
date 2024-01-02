package main.helper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputListener implements KeyListener {

    private final boolean[] keyPressed = new boolean[256];

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) { keyPressed[e.getKeyCode()] = false; }

    public boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }
    public void keyRelease(int keyCode) {
        keyPressed[keyCode] = false;
    }

}
