package Main;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;
    public static void main(String[] args) {
        System.out.println("Hello world!");
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("WOLF VS WORLD");
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        gamePanel.config.loadConfig();

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}

