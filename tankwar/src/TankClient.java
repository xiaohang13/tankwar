
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
    //int x = 50, y = 50;

    Tank t = new Tank(50, 50, true, this);

    // 初始化一个敌方坦克
    Tank enmeyTank = new Tank(100, 100, false, this);

    // 用List容器存放多枚炮弹
    List<Missile> missiles = new ArrayList<>();
    // 用List存放爆炸对象
    List<Explode> explodes = new ArrayList<>();


    Image offScreenImage = null;

    public void launchFrame() {
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

        this.setBackground(Color.magenta);
        this.setResizable(false);

        this.addKeyListener(new KeyMonitor());

        new Thread(new TankThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("missiles count :" + missiles.size(), 10, 50);
        g.drawString("explodes count :" + explodes.size(), 10, 70);
        // 通过循环来画出多枚炮弹
        //for (Missile m : missiles) {
        //    m.draw(g);
        //}
        for (int i=0; i<missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTank(enmeyTank);
            m.draw(g);
        }

        for (int i=0; i<explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }

        t.draw(g);
        enmeyTank.draw(g);
    }

    // 重写update方法，使用双缓冲解决屏幕闪动问题
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics offScreenPaint = offScreenImage.getGraphics();
        Color c = offScreenPaint.getColor();
        offScreenPaint.setColor(Color.magenta);
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
