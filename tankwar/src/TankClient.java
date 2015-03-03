
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    //int x = 50, y = 50;

    Tank t = new Tank(50,50);

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

        this.setBackground(Color.green);
        this.setResizable(false);

        this.addKeyListener(new KeyMonitor());

        new Thread(new TankThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        t.draw(g);
    }

    // 重写update方法，使用双缓冲解决屏幕闪动问题
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics offScreenPaint = offScreenImage.getGraphics();
        Color c = offScreenPaint.getColor();
        offScreenPaint.setColor(Color.green);
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
                    Thread.sleep(20);
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
        new TankClient().launchFrame();
    }
}
