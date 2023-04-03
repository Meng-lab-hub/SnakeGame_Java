import javax.swing.JFrame;

public class GameFrame extends JFrame {
    GameFrame() {
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        // so far we add a component to the JFrame
        this.pack();    // this pack method will add our JFrame to the frame
        this.setVisible(true);
        this.setLocationRelativeTo(null);   // if we want our window to appears on the middle of the screen
    }
}