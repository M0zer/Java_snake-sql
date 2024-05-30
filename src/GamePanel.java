import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 25;
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;
    private static final int DELAY = 100;
    private Snake snake;
    private Food food;
    private List<Rock> rocks;
    private boolean isRunning;
    private Timer timer;
    private int score;
    private Database database;

    public GamePanel() {
        database = new Database();
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyHandler());
        initializeGame();
    }

    public void initializeGame() {
        snake = new Snake(new Position(WIDTH / 2, HEIGHT / 2));
        rocks = new ArrayList<>();
        generateRocks(5);
        food = new Food(generateRandomPosition());
        isRunning = true;
        score = 0;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private Position generateRandomPosition() {
        Random random = new Random();
        Position position;
        do {
            position = new Position(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        } while (isPositionOccupied(position));
        return position;
    }

    private void generateRocks(int numberOfRocks) {
        for (int i = 0; i < numberOfRocks; i++) {
            rocks.add(new Rock(generateRandomPosition()));
        }
    }

    private boolean isPositionOccupied(Position position) {
        if (snake.getBody().contains(position)) return true;
        if (food != null && food.getPosition().equals(position)) return true;
        for (Rock rock : rocks) {
            if (rock.getPosition().equals(position)) return true;
        }
        return false;
    }

    private void updateDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                if(snake.getDirection().getX()==0 && snake.getDirection().getY()==1){break;}
                snake.setDirection(new Position(0, -1));
                break;
            case KeyEvent.VK_A:
                if(snake.getDirection().getX()==1 && snake.getDirection().getY()==0){break;}
                snake.setDirection(new Position(-1, 0));
                break;
            case KeyEvent.VK_S:
                if(snake.getDirection().getX()==0 && snake.getDirection().getY()==-1){break;}
                snake.setDirection(new Position(0, 1));
                break;
            case KeyEvent.VK_D:
                if(snake.getDirection().getX()==-1 && snake.getDirection().getY()==0){break;}
                snake.setDirection(new Position(1, 0));
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            snake.move();
            checkCollisions();
            checkFood();
        }
        repaint();
    }

    private void checkCollisions() {
        Position head = snake.getBody().getFirst();
        if (head.getX() < 0 || head.getX() >= WIDTH || head.getY() < 0 || head.getY() >= HEIGHT) {
            gameOver();
        }
        for (Rock rock : rocks) {
            if (head.equals(rock.getPosition())) {
                gameOver();
            }
        }
        if (snake.checkCollisionWithSelf()) {
            gameOver();
        }
    }

    private void checkFood() {
        if (snake.getBody().getFirst().equals(food.getPosition())) {
            snake.grow();
            score++;
            food.setPosition(generateRandomPosition());
        }
    }

    private void gameOver() {
        isRunning = false;
        timer.stop();
        String name = JOptionPane.showInputDialog(this, "A játéknak vége! Az eredményed: " + score + "\nÍrd be a nevedet:","Score",JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            database.saveScore(name, score);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isRunning) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawGame(Graphics g) {
        g.setColor(new Color(194, 178, 128));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.fillRect(food.getPosition().getX() * TILE_SIZE, food.getPosition().getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.GRAY);
        for (Rock rock : rocks) {
            g.fillRect(rock.getPosition().getX() * TILE_SIZE, rock.getPosition().getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(new Color(54,103,53));
        for (Position position : snake.getBody()) {
            g.fillRect(position.getX() * TILE_SIZE, position.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over";
        g.setColor(Color.RED);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(message, (getWidth() - metrics.stringWidth(message)) / 2, getHeight() / 2);
    }

    public Database getDatabase() {
        return database;
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            updateDirection(e.getKeyCode());
        }
    }
}
