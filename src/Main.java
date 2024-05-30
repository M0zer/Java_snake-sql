import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        final GamePanel[] gamePanel = {new GamePanel()};

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Játék");
        JMenuItem newGameItem = new JMenuItem("Új Játék");
        JMenuItem highScoresItem = new JMenuItem("Top Eredmény");

        newGameItem.addActionListener(e -> {
            frame.remove(gamePanel[0]);
            gamePanel[0] = new GamePanel();
            frame.add(gamePanel[0]);
            frame.revalidate();
            frame.repaint();
            gamePanel[0].requestFocusInWindow();
        });

        highScoresItem.addActionListener(e -> {
            List<Score> topScores = gamePanel[0].getDatabase().getTopScores(10);
            StringBuilder message = new StringBuilder("Top 10 Eredmény:\n");
            for (Score score : topScores) {
                message.append(score.getName()).append(": ").append(score.getScore()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, message.toString(),"Top eredmények",JOptionPane.INFORMATION_MESSAGE);
        });

        gameMenu.add(newGameItem);
        gameMenu.add(highScoresItem);
        menuBar.add(gameMenu);

        frame.setJMenuBar(menuBar);
        frame.add(gamePanel[0]);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
