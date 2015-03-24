
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;


public class TankClient extends Frame {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank t = new Tank(50, 50, true, Direction.STOP, this);

    // 初始化敌方坦克，用List容器填装
    //Tank enmeyTank = new Tank(100, 100, false, this);
    List<Tank> enmeyTanks = new ArrayList<>();

    // 用List容器存放多枚炮弹
    List<Missile> missiles = new ArrayList<>();
    // 用List存放爆炸对象
    List<Explode> explodes = new ArrayList<>();

    // 生成墙的对象
    Wall w1 = new Wall(110, 150, 20, 180);
    Wall w2 = new Wall(280, 150, 280, 20);

    // 生成血块
    Blood b = new Blood();

    // 加入双缓冲
    Image offScreenImage = null;

    public void launchFrame() {

        for (int i=0; i<10; i++) {
            // 初始化敌军坦克
            Tank tank = new Tank(100 + 40*(i+1), 50, false, Direction.D, this);
            this.enmeyTanks.add(tank);
        }

        this.setLocation(300, 200);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });

        //this.setBackground(Color.BLACK);
        this.setResizable(false);

        this.addKeyListener(new KeyMonitor());

        new Thread(new TankThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("missiles count :" + missiles.size(), 10, 50);
        g.drawString("explodes count :" + explodes.size(), 10, 70);
        g.drawString("enmeytanks count :" + enmeyTanks.size(), 10, 90);
        g.drawString("mytank life is  :" + t.getLife(), 10, 110);
        g.setColor(c);

        // 当敌军坦克全部消失后，自动生成新的坦克，以便游戏继续
        if (enmeyTanks.size() <= 0) {
            for (int i=0; i<5; i++) {
                Tank tank = new Tank(100 + 40*(i+1), 50, false, Direction.D, this);
                this.enmeyTanks.add(tank);
            }
        }

        // 通过循环来画出多枚炮弹
        //for (Missile m : missiles) {
        //    m.draw(g);
        //}
        for (int i=0; i<missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(enmeyTanks);
            m.hitTank(t);
            m.hitWall(w1);
            m.hitWall(w2);
            m.draw(g);
        }

        for (int i=0; i<explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }

        for (int i=0; i<enmeyTanks.size(); i++) {
            Tank tank = enmeyTanks.get(i);
            tank.collidesWithWall(w1);
            tank.collidesWithWall(w2);
            tank.collidesWithTank(enmeyTanks);
            tank.draw(g);
        }

        t.draw(g);
        w1.draw(g);
        w2.draw(g);
        b.draw(g);
        t.eatBloodBar(b);
    }

    // 重写update方法，使用双缓冲解决屏幕闪动问题
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics offScreenPaint = offScreenImage.getGraphics();
        Color c = offScreenPaint.getColor();
        offScreenPaint.setColor(Color.BLACK);
        offScreenPaint.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        offScreenPaint.setColor(c);
        paint(offScreenPaint);
        // 将缓冲至图片的内容添加到当前屏幕中
        g.drawImage(offScreenImage, 0, 0, null);
    }

    // 使用线程进行坦克的移动，这里采用内部类实现
    private class TankThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 定义一个内部类，用于键盘操作的监听
    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            t.KeyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            t.keyReleased(e);
        }
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}
