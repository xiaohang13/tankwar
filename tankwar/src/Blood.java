import java.awt.*;

public class Blood {
    int x, y, w, h;
    public TankClient tc;

    private int[][] pos = {
            {300,350},{302,351},{303,354},{301,355},{300,352},{300,351}
    };

    private int step = 0;
    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Blood() {
        this.x = pos[0][0];
        this.y = pos[0][1];
        this.w = 10;
        this.h = 10;
    }

    public void draw(Graphics g) {
        if (!this.live) return;
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);

        move();
    }

    private void move() {
        step++;
        if (this.step == pos.length) {
            step = 0;
        }
        x = pos[step][0];
        y = pos[step][0];
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }
}
