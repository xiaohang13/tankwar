import java.awt.*;

public class Missile {
    int x ,y;
    private static final int XSPEED = 8;
    private static final int YSPEED = 8;
    Tank.Direction dir;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    // 该变量用于表示子弹是否消亡，默认情况下为存在，因为子弹发射时，就已经创建了改对象
    private boolean Live = true;
    public TankClient tc;

    public void setLive(boolean live) {
        Live = live;
    }

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
        this(x,y,dir);
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!Live) {
            tc.missiles.remove(this);
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        move();
    }

    private void move() {
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
        }

        if (x < 0 || y < 0 || x + Missile.WIDTH > TankClient.GAME_WIDTH || y + Missile.HEIGHT > TankClient.GAME_HEIGHT) {
            Live = false;
        }
    }

    // 该函数用于返回包含子弹的矩形实例，用于与坦克进行碰撞检测
    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    // 子弹打击坦克
    public boolean hitTank(Tank t) {
        if (this.getRect().intersects(t.getRect()) && t.isLive()) {
            // 打中坦克后，产生爆炸效果
            Explode e = new Explode(x, y, tc);
            tc.explodes.add(e);

            t.setLive(false);
            this.Live = false;
            return true;
        }
        return false;
    }
}
