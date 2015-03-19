/**
 * Created tankwar project
 * Created on 15-3-18.
 */

import java.awt.*;

/**
 * 该类是一个爆炸类，用于表示坦克被炸毁
 */
public class Explode {
    int x,y;
    // 定义一个数组，用于存储爆炸圆的直径
    int[] diemote = {4, 7, 11, 19, 29, 32, 40, 30, 17, 6};
    // 表示爆炸圆画到第几个直径圆
    int step = 0;

    TankClient tc;

    // 该变量表示爆炸类的生死
    private boolean live = true;

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.explodes.remove(this);
            return;
        }

        if (step == diemote.length) {
            this.live = false;
            step = 0;
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, diemote[step], diemote[step]);
        g.setColor(c);

        step++;
    }
}
