import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
    int x ,y;
    private static final int XSPEED = 8;
    private static final int YSPEED = 8;
    Direction dir;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    // 该变量用于表示子弹是否消亡，默认情况下为存在，因为子弹发射时，就已经创建了改对象
    private boolean Live = true;
    public TankClient tc;

    // 标记子弹的好坏，同一方的人不能相互攻击
    private boolean good;
    
    private static Image[] missileImages = null;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Map<String, Image> missile = new HashMap<>();
    
    static {
    	missileImages = new Image[] {
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
            tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
        };
    	
    	missile.put("D", missileImages[0]);
    	missile.put("L", missileImages[1]);
    	missile.put("LD",missileImages[2]);
        missile.put("LU",missileImages[3]);
        missile.put("R", missileImages[4]);
        missile.put("RD",missileImages[5]);
        missile.put("RU",missileImages[6]);
        missile.put("U", missileImages[7]);
    }

    public void setLive(boolean live) {
        Live = live;
    }

    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x,y,dir);
        this.good =good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!Live) {
            tc.missiles.remove(this);
            return;
        }
        
        Color c = g.getColor();
        g.setColor(Color.GRAY);
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
        if (this.Live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
            // 如果击中的为我方坦克，则生命值减少，否则直接消灭
            if (t.isGood()) {
                t.setLife(t.getLife()-20);
                if (t.getLife() <= 0) {
                    t.setLive(false);
                }
            }else {
                t.setLive(false);
            }
            // 打中坦克后，产生爆炸效果
            Explode e = new Explode(x, y, tc);
            tc.explodes.add(e);
            this.Live = false;
            return true;
        }
        return false;
    }

    // 该函数用于判断打击一群坦克
    public boolean hitTanks(List<Tank> enmeyTank) {
        for (int i=0; i<enmeyTank.size(); i++) {
            if (hitTank(enmeyTank.get(i))){
                return true;
            }
        }
        return false;
    }

    // 子弹打击墙壁的判断函数
    public boolean hitWall(Wall w) {
        if (this.Live && this.getRect().intersects(w.getRect())) {
            this.Live = false;
            return true;
        }
        return false;
    }
}
