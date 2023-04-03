import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/* those line above includes all the below library that is needed
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
*/


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNIT]; // this array will hold the x coordinate of body of the snake
    final int y[] = new int[GAME_UNIT]; // this array will hold the y coordinate of body of the snake
    int bodyParts = 6;      // this will represent how many part of body does the snake have, begin with 6
    int applesEaten = 0;    // number of apple that has been eaten
    int appleX;         // x coordinate of apple
    int appleY;         // y coordinate of apple
    char direction = 'R';   // R: right, L: left, U: up, D: down
    boolean running = false;
    Timer timer;
    Random random;  // instance of the random class
    
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame() {
        newApple();                             // create an apple when the game start
        running = true;                         // indicate that the game is running
        timer = new Timer(DELAY, this);         // finish creating timer, we add "this" becasue we're using action listener interface
        timer.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        
        if (running) {
            // this loop below draw a vertical line(first drawLine()) and horizontal line (second drawLine())
            // as a result, we will have a grid on the screen
            // (0,0) is at top left corner
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            
            
            // draw an apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            
            
            // draw the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {   // in this case, we are dealing with the head of the snake
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(54, 180, 0));  // pick a new Color
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
            // draw a score on the screen
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(g.getFont());    // used for lining up text in center of the screen
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
        
    }
    
    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;     // we multiply by UNIT_SIZE because we want the apple to beplace evenly on the spot
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }
    
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];      // shifting the x-coordinate of the snake as it is moving
            y[i] = y[i-1];      // shifting the y-coordinate of the snake as it is moving
        }
        
        
        // switching the moving direction of the snake
        // (0,0) is at top left corner
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // move up
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; // move down
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; // move left
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; // move right
                break;
        }
    }
    
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    public void checkCollisions() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        
        // check if head touches left boarder 
        if (x[0] < 0) {
            running = false;
        }
        // check if head touches right boarder
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head touches top boarder
        if (y[0] < 0) {
            running = false;
        }
        // check if head touches bottom boarder
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }
    
    public void gameOver(Graphics g) {
        // draw a score on the screen
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics_01 = getFontMetrics(g.getFont());    // used for lining up text in center of the screen
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics_01.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics_02 = getFontMetrics(g.getFont());    // used for lining up text in center of the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics_02.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK.LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK.RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK.UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK.DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}