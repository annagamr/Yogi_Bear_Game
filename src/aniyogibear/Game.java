package aniyogibear;

/**
 *
 * @author ani
 */

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    private final JTextField timerString;
    private final int framespersecond = 120;
    private final int charactermovement = 30;
    private final int characterradius = 60;
    private Character myPlayer;
    protected Level lvl;
    protected int score = 0;
    protected int hearts = 3;
    protected int stage = 1;
    private final Timer frameTimer;
    
    public Game() {
        super();

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent key) {
                int k = key.getKeyCode();
                switch (k) {

                    case KeyEvent.VK_A -> myPlayer.pieceMoving(-1 * charactermovement, 0);
                    case KeyEvent.VK_D -> myPlayer.pieceMoving(charactermovement, 0);
                    case KeyEvent.VK_W -> myPlayer.pieceMoving(0, -1 * charactermovement);
                    case KeyEvent.VK_S -> myPlayer.pieceMoving(0, charactermovement);
                    default -> {
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent key) {}
            @Override
            public void keyTyped(KeyEvent key) {}
        });

        timerString = new JTextField(8);
        timerString.setEditable(false);
        this.add(timerString, BorderLayout.LINE_END);

        restartLevel();
        frameTimer = new Timer(1000 / framespersecond, new eachFrame());
        frameTimer.start();
    }



    public void restartLevel() {
        if (hearts <= 0) {
            stage = 1;
            hearts = 3;
            score = 0;
        }
        try {
            lvl = new Level(stage + ".txt");
        } catch (IOException ex) {
            System.out.println("Error with loading level from file.");
        }
        Image bearImage = new ImageIcon("yogibear.png").getImage();
        //starting point for yogi
        myPlayer = new Character(0, 680, 0, characterradius, characterradius, bearImage);
    }

    //from JComponent
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        lvl.settingUI(graphics);
        myPlayer.settingUI(graphics);
    }

    class eachFrame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (lvl.newLVL()) {
                stage = (stage + 1);
                restartLevel();
            }
            lvl.rangerMove();

            if (lvl.intersectsOther(myPlayer, lvl.getBaskets())) {
                score++;
                if (lvl.newLVL()) {
                    if (stage == 10) {
                        JOptionPane.showMessageDialog(null, "You Beat The Game! Congrats! Replay Until You Lose!");
                        stage = 1;
                        restartLevel();

                    } else {
                        JOptionPane.showMessageDialog(null, "Level " + stage + " closed , Upcoming Level: " + (stage + 1));
                    }
                }
            }

            if (lvl.intersectsOther(myPlayer, lvl.getTrees())) {
                myPlayer.movesWhere(myPlayer.getX()-3, myPlayer.getY()-2);
            } else {
                myPlayer.playerMove(600, 800);
            }

            if (lvl.intersectsOther(myPlayer, lvl.getRangers())) {
                hearts -= 1;
                if (hearts <= 0) {
                    String pname  = JOptionPane.showInputDialog(null, "INPUT NAME: ", ("GAME OVER. Total Score: " + score), JOptionPane.INFORMATION_MESSAGE);
                    if (pname  != null) {
                        DbManager.getDbManager().insertScore(pname , score);
                    }
                    restartLevel();
                } else {
                    JOptionPane.showMessageDialog(null, "You got caught! Hearts: " + (hearts)+"/3");
                    restartLevel();
                }
            }
            timerString.setText("Time: " + String.valueOf(lvl.getLevelTime()));
            repaint();
        }
    }
}