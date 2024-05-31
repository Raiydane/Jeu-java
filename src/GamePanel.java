import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private Image marioImage, background, otherWorldImage;
    private Rectangle mario, cube, teleportZone;
    private boolean isInOtherWorld = false;
    private String dialogue = "";
    private final int SPEED = 10;
    private final int NEW_WIDTH = 50;
    private final int NEW_HEIGHT = 50;
    private final int CUBE_SIZE = 40;

    public GamePanel() { //
        ImageIcon marioIcon = new ImageIcon("res/mario.png");
        marioImage = marioIcon.getImage().getScaledInstance(NEW_WIDTH, NEW_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon("res/plateau.png");
        background = backgroundIcon.getImage();
        ImageIcon otherWorldIcon = new ImageIcon("res/autre_plateau.jpeg"); // Assurez-vous que cette image est disponible
        otherWorldImage = otherWorldIcon.getImage();

        mario = new Rectangle(100, 100, NEW_WIDTH, NEW_HEIGHT);
        cube = new Rectangle(300, 300, CUBE_SIZE, CUBE_SIZE);
        teleportZone = new Rectangle(200, 200, 60, 60); // Zone de téléportation

        Timer timer = new Timer(100, e -> repaint());
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                moveMario(e);
            }
        });
    }

    private void moveMario(KeyEvent e) {
        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: dy -= SPEED; break;
            case KeyEvent.VK_DOWN: dy += SPEED; break;
            case KeyEvent.VK_LEFT: dx -= SPEED; break;
            case KeyEvent.VK_RIGHT: dx += SPEED; break;
        }
        
        mario.x += dx;
        mario.y += dy;

        // Déplacement du cube par Mario
        if (mario.intersects(cube)) {
            cube.x += dx;
            cube.y += dy;
            dialogue = "J'ai réussi !!!";
        }

        // Vérification si le cube est dans la zone de téléportation
        if (cube.intersects(teleportZone) && !isInOtherWorld) {
            isInOtherWorld = true;
            dialogue = "Téléportation vers un autre monde!";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image currentBackground = isInOtherWorld ? otherWorldImage : background;
        g.drawImage(currentBackground, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(marioImage, mario.x, mario.y, this);
        g.setColor(Color.MAGENTA);
        g.fillRect(cube.x, cube.y, cube.width, cube.height);
        g.setColor(Color.ORANGE);
        g.fillRect(teleportZone.x, teleportZone.y, teleportZone.width, teleportZone.height);

        if (!dialogue.isEmpty()) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(dialogue, mario.x, mario.y - 20);
        }
    }
}



