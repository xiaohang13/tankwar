import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 定义一个坦克类，该类包含属于自己的成员变量及实现方法，采用面向对象的设计思想
 */
public class Tank {
    private int x ,y;
    public TankClient tc;

    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean good;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    // 添加炮筒属性，初始化时给予一个方向，类型为Direction
    public Direction ptDir = Direction.U;

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

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, TankClient tc) {
        this(x,y,good);
        this.tc = tc;
    }

    // 坦克类中自定义画笔
    public void draw(Graphics g) {
        if (!live) return;
        Color c = g.getColor();
        if (good) g.setColor(Color.GREEN);
        else g.setColor(Color.BLUE);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        // 根据炮筒的位置，画出炮筒
        switch (ptDir) {
            case L:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x, y + this.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x, y);
                break;
            case U:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x + this.WIDTH/2, y);
                break;
            case RU:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x + this.WIDTH, y);
                break;
            case R:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x + this.WIDTH, y + this.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x + this.WIDTH, y + this.HEIGHT);
                break;
            case D:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x + this.WIDTH/2, y + this.HEIGHT);
                break;
            case LD:
                g.drawLine(x + this.WIDTH/2, y + this.HEIGHT/2, x, y + this.HEIGHT);
                break;
        }

        // 每一次draw坦克类时，根据方向进行移动
        move();
    }

    // move方法表示坦克的移动及炮筒的方向
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

        if (this.dir != Direction.STOP) {
            this.ptDir = this.dir;
        }

        // 判断坦克是否出界
        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
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
            case KeyEvent.VK_CONTROL:
                tc.missiles.add(fire());
                break;
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

        locateDirection();
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

    // 该方法用于发射子弹
    public Missile fire() {
        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
        // 根据炮筒的方向发射子弹
        Missile m = new Missile(x, y, ptDir,this.tc);
        return m;
    }

    // 该函数用于返回包含坦克的矩形实例，用于与子弹进行碰撞检测
    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
