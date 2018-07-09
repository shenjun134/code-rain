package com.code;

import java.awt.*;
import java.util.Random;

class RainCanvas extends Canvas implements Runnable {
    private int width, height;
    private Image offScreen; // 缓冲图片
    private char[][] charset; // 随机字符集合
    private int[] pos; // 列的起始位置
    private Color[] colors = new Color[30]; // 列的渐变颜色

    private OptionEnum optionEnum = OptionEnum.NUMBER_01;

    public RainCanvas(int width, int height, OptionEnum optionEnum) {
        this.optionEnum = optionEnum;
        this.width = width;
        this.height = height;
// 生成ASCII可见字符集合
        Random rand = new Random();
        charset = new char[width / 10][height / 10];
        for (int i = 0; i < charset.length; i++) {
            for (int j = 0; j < charset[i].length; j++) {
                charset[i][j] = randChar();
            }
        }
// 随机化列起始位置
        pos = new int[charset.length];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = rand.nextInt(pos.length);
        }
// 生成从黑色到绿色的渐变颜色，最后一个保持为白色
        for (int i = 0; i < colors.length - 1; i++) {
            colors[i] = new Color(0, 255 / colors.length * (i + 1), 0);
        }
        colors[colors.length - 1] = new Color(255, 255, 255);
        setBackground(Color.BLACK);
        setSize(width, height);
        setVisible(true);
    }

    private char randChar() {
        switch (optionEnum) {
            case NUMBER_01:
                return randChar01();
            case NUMBER_ONLY:
                return randCharNumber();
            case NUMBER_CHAR:
                return randCharAny();
            case UPPER_CHAR:
                return randCharUppcase();
            case LOWER_CHAR:
                return randCharLowcase();
        }

        return randChar01();
    }

    /**
     * 48 ~ 122
     * <p>
     * 58 ~ 64 exclude
     * <p>
     * 91 ~ 96 exclude
     *
     * @return
     */
    private char randCharAny() {
        Random rand = new Random();
        int rnum = rand.nextInt(122);

        if (rnum < 48) {
            rnum = 48;
        }
        if (rnum >= 58 && rnum <= 64) {
            rnum = 49;
        }
        if (rnum >= 91 && rnum <= 96) {
            rnum = 49;
        }
        return (char) rnum;
    }

    /**
     * 0/1
     *
     * @return
     */
    private char randChar01() {
        Random rand = new Random();
        int rnum = rand.nextInt(90) % 2;
        int temp = rnum == 0 ? 48 : 49;
        return (char) temp;
    }

    /**
     * 0 ~ 9
     *
     * @return
     */
    private char randCharNumber() {
        Random rand = new Random();
        int rnum = rand.nextInt(57) % 10 + 48;
        return (char) rnum;
    }


    /**
     * A~Z
     *
     * @return
     */
    private char randCharUppcase() {
        Random rand = new Random();
        int rnum = rand.nextInt(90);

        if (rnum <= 64) {
            rnum = 65;
        }
        return (char) rnum;
    }

    /**
     * a~z
     *
     * @return
     */
    private char randCharLowcase() {
        Random rand = new Random();
        int rnum = rand.nextInt(122);

        if (rnum <= 96) {
            rnum = 97;
        }
        return (char) rnum;

    }

    public void startRain() {
        new Thread(this).start();
    }

    public void drawRain() {
        if (offScreen == null) {
            return;
        }
        Random rand = new Random();
        Graphics g = offScreen.getGraphics();
        g.clearRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
//
        for (int i = 0; i < charset.length; i++) {
            int speed = rand.nextInt(3);
            for (int j = 0; j < colors.length; j++) {
                int index = (pos[i] + j) % charset[i].length;
                g.setColor(colors[j]);
                g.drawChars(charset[i], index, 1, i * 10, index * 10);
            }
            pos[i] = (pos[i] + 1) % charset[i].length;
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void run() {
        while (true) {
            drawRain();
            repaint();
            try {
                Thread.sleep(50); // 可改变睡眠时间以调节速度
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
// 当组件显示时检测是否要创建缓冲图片，在组件还不可见时调用createImage将返回null
        if (offScreen == null) {
            offScreen = createImage(width, height);
        }
        g.drawImage(offScreen, 0, 0, this);
    }
}
