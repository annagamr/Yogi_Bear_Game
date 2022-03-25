package aniyogibear;

/**
 *
 * @author ani
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Character {
    protected int x,y;
    protected int width, height, speed;
    protected Image image;

    public Character(int x, int y, int speed, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.speed = speed;
    }
    
    //drawing everything
//using drawImage from Graphics class
    public void settingUI(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

        //uses intersects from Rectangle class
    public boolean intersects(Character diffChar) {
        Rectangle char1 = new Rectangle(x, y, width - 20, height - 20);
        Rectangle char2 = new Rectangle(diffChar.x, diffChar.y, diffChar.width, diffChar.height);
        return char1.intersects(char2);
    }
    
    public void playerMove(int width, int height) {
        if (x >= width) {
            x = 0;
        }
        if (x < 0) {
            x = width;
        }
        if (y >= height) {
            y = 0;
        }
        if (y < 0) {
            y = height;
        }
    }

    public void pieceMoving(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void movesWhere(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public int getSpeed()
    {
        return this.speed;
    }
}