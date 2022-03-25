package aniyogibear;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author ani
 */

public class Level {
    private long levelbegin;
    private final int basketX = 60;
    private final int basketY = 70;
    private final int treeX = 100;
    private final int treeY = 90;
    private final int rangerX = 40;
    private final int rangerY = 70;

    private ArrayList<Character> rangers;
    private ArrayList<Character> trees;
    private ArrayList<Character> baskets;

    public Level(String lvl) throws IOException {
        readFile(lvl);
    }

    public void readFile(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        rangers = new ArrayList<>();
        trees = new ArrayList<>();
        baskets = new ArrayList<>();

        int  RangerNum= sc.nextInt();
        for (int i = 0; i < RangerNum; i++) {
            Image img1 = new ImageIcon("parkranger.png").getImage();
            rangers.add(new Character(sc.nextInt(), sc.nextInt(), sc.nextInt(), rangerX, rangerY, img1));
        }

        int TreesRockNum = sc.nextInt();
        Image tree = new ImageIcon("tree.png").getImage();
        Image rock = new ImageIcon("rock.png").getImage();
        for (int i = 0; i < TreesRockNum; i++) {
            if (i % 2 == 0) {
                // Trees
                trees.add(new Character(sc.nextInt(), sc.nextInt(), 0, treeX, treeY, tree));
            } else {
                // Rocks
                trees.add(new Character(sc.nextInt(), sc.nextInt(), 0, treeX, treeY, rock));
            }
        }

  
        int BasketNum = sc.nextInt();
        Image img3 = new ImageIcon("basket.png").getImage();
        for (int i = 0; i < BasketNum; i++) {
            baskets.add(new Character(sc.nextInt(), sc.nextInt(), 0, basketX, basketY, img3));
        }

        levelbegin = System.currentTimeMillis();
    }



    public void settingUI(Graphics view) {
        trees.forEach(tree -> {
            tree.settingUI(view);
        });

        baskets.forEach(basket -> {
            basket.settingUI(view);
        });

        for (int i = 0; i < rangers.size(); i++) {
            if (i % 2 == 0) {
                rangers.get(i).pieceMoving(rangers.get(i).getSpeed(), 0);
                rangers.get(i).settingUI(view);
            } else {
                rangers.get(i).pieceMoving(0, rangers.get(i).getSpeed());
                rangers.get(i).settingUI(view);
            }
        }
    }

    public void rangerMove() {
        rangers.forEach(ranger -> {
            ranger.playerMove(600, 800);
        });
    }
    //my player is considered as bear
    public boolean intersectsOther(Character myPlayer, ArrayList<Character> objects) {
        Character intersectionWith = null;
        for (Character chrctr: objects) {
            if (myPlayer.intersects(chrctr)) {
                intersectionWith = chrctr;
                break;
            }
        }
        if (intersectionWith != null) {
            baskets.remove(intersectionWith);
            return true;
        } else {
            return false;
        }
    }

    public boolean newLVL() {
        return baskets.isEmpty();
    }

    public ArrayList<Character> getRangers() {
        return rangers;
    }

    public ArrayList<Character> getTrees() {
        return trees;
    }

    public ArrayList<Character> getBaskets() {
        return baskets;
    }

    public long getLevelTime() {
        return (System.currentTimeMillis() - levelbegin) / 1000;
    }
}