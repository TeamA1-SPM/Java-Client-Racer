package main;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {

        Window window = new Window();
        Thread thread1 = new Thread(window);
        thread1.start();

    }
}
    