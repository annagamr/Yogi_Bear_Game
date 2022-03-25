package aniyogibear;

/**
 *
 * @author ani
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AniyogibearGui extends JFrame {
    private final JFrame frame;
    private final Game game;
    private JTable table;
    private final JFrame tableframe = new JFrame();
    String[][] points = new String[10][2];
    String[] sqltable = {
        "Player",
        "Points"
    };
    

    public AniyogibearGui() {
        JMenuBar main = new JMenuBar();
        JMenu menu = new JMenu("MENU");
        JMenuItem point = new JMenuItem("HIGH SCORES");
        JMenuItem restart = new JMenuItem("RESTART GAME");

        main.add(menu);
        menu.add(point);
        menu.add(restart);

        RestartGame restartGame = new RestartGame();
        restart.addActionListener(restartGame);

        Table highscores = new Table();
        point.addActionListener(highscores);

        game = new Game();
        game.setBackground(Color.YELLOW);
        game.setFocusable(true);
        game.requestFocusInWindow();

        frame = new JFrame("YOGI BEAR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(game);
        frame.setPreferredSize(new Dimension(600, 800));
        frame.setResizable(false);
        frame.pack();
        frame.setJMenuBar(main);
        frame.setVisible(true);
    }

    public class RestartGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent restart) {
            game.hearts=3;
            game.score=0;
            game.stage=1;
            game.restartLevel();

        }
    }

    public class Table implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent scores) {
            table = new JTable(points, sqltable);
            try {
                ArrayList<MaxPoints> highscore = DbManager.getDbManager().getHighScores();
                int i = 0;
                for (MaxPoints thisscore : highscore)
                {
                    points[i][0] = thisscore.getName();
                    points[i][1] = String.valueOf(thisscore.getScore());
                    i++;
                }
                table = new JTable(points, sqltable);
            } catch (SQLException ex) {
                System.out.println("database problem" + ex);
            }

            tableframe.setVisible(true);
            tableframe.setSize(400, 350);
            tableframe.add(new JScrollPane(table));
        }

    }

    public JFrame GetFrame() {
        return frame;
    }
}