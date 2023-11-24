package main;

public class Main {

    public static void main(String[] args) {

        Window window = new Window();
        Game game = new Game(window);

        Thread t1 = new Thread(game);
        t1.run();

    }
}