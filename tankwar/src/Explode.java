import java.awt.*;

/**
 * 该类是一个爆炸类，用于表示坦克被炸毁
 */
public class Explode {
    int x,y;

    private static boolean initial = false;

    // toolkit类用于获取当前平台提供的工具包对象，这里可以通过工具包对象拿到硬盘上的图片
    public static Toolkit tk = Toolkit.getDefaultToolkit();

    // 定义一个数组，用于存储爆炸圆的直径
    //int[] diemote = {4, 7, 11, 19, 29, 32, 40, 30, 17, 6};
    /**
     * 这里定义一个图片数组，通过反射机制来获取硬盘上存取的图片
     * 这里定义为静态的数组，原因是不需要每次加载该类时都去读取创建图片
     */
    private static Image image[] = {
        tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
    };

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

        if (false == initial) {
            for (int i=0; i<image.length; i++) {
                g.drawImage(image[i], -200, -200, null);
            }
            initial = true;
        }

        if (!live) {
            tc.explodes.remove(this);
            return;
        }

        if (step == image.length) {
            this.live = false;
            step = 0;
            return;
        }

        g.drawImage(image[step], x, y, null);

        //Color c = g.getColor();
        //g.setColor(Color.ORANGE);
        //g.fillOval(x, y, diemote[step], diemote[step]);
        //g.setColor(c);

        step++;
    }
}
