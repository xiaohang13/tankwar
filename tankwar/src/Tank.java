import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 定义一个坦克类，该类包含属于自己的成员变量及实现方法，采用面向对象的设计思想
 */
public class Tank {
    private int x ,y;
    private static final int XSPEED = 5;
    private static final int YSPEED = 5;

    // 定义上下左右方向，用于键盘按下后，标示当前的方向
    private boolean BL=false, BU=false, BR=false, BD=false;



    // 定义一个枚举类型，用于设置方向
    enum Direction {
        L, LU, U, RU, R, RD, D, LD, STOP
    }
    // 坦克初始化的状态
    private Direction dir = Direction.STOP;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 坦克类中自定义画笔
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.red);
        g.fillOval(x, y, 30, 30);
        g.setColor(c);

        // 每一次draw坦克类时，根据方向进行移动
        move();
    }

    public void move() {
        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }
    }

    // 坦克类自定义按键移动
    public void KeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                BL = true;
                break;
            case KeyEvent.VK_UP:
                BU = true;
                break;
            case KeyEvent.VK_RIGHT:
                BR = true;
                break;
            case KeyEvent.VK_DOWN:
                BD = true;
                break;
        }

        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                BL = false;
                break;
            case KeyEvent.VK_UP:
                BU = false;
                break;
            case KeyEvent.VK_RIGHT:
                BR = false;
                break;
            case KeyEvent.VK_DOWN:
                BD = false;
                break;
        }
    }

    // 每一次按键后，对当前方向进行调整
    void locateDirection() {
        if (BL && !BU && !BR && !BD) dir = Direction.L;
        else if (BL && BU && !BR && !BD) dir = Direction.LU;
        else if (!BL && BU && !BR && !BD) dir = Direction.U;
        else if (!BL && BU && BR && !BD) dir = Direction.RU;
        else if (!BL && !BU && BR && !BD) dir = Direction.R;
        else if (!BL && !BU && BR && BD) dir = Direction.RD;
        else if (!BL && !BU && !BR && BD) dir = Direction.D;
        else if (BL && !BU && !BR && BD) dir = Direction.LD;
        else if (!BL && !BU && !BR && !BD) dir = Direction.STOP;
    }
}
