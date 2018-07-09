package com.code;

import javax.swing.*;

public class Rain extends JFrame {
    private RainCanvas canvas = null;

    public Rain(OptionEnum optionEnum) {
        super("Rain");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        canvas = new RainCanvas(this.getWidth(), this.getHeight(), optionEnum);

        getContentPane().add(canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
//        args = new String[]{OptionEnum.UPPER_CHAR.name()};
        process(args);
    }

    private static void process(String[] args) {
        OptionEnum optionEnum = OptionEnum.NUMBER_01;

        if (args == null || args.length == 0) {
            optionEnum = OptionEnum.NUMBER_01;
        } else {
            String option = args[0];
            if (option == null || option.trim().length() == 0) {
                option = OptionEnum.NUMBER_01.name();
            }
            for (OptionEnum temp : OptionEnum.values()) {
                if (temp.name().equalsIgnoreCase(option)) {
                    optionEnum = temp;
                    break;
                }
            }
        }
        if (optionEnum == null) {
            optionEnum = OptionEnum.NUMBER_01;
        }
        Rain rain = new Rain(optionEnum);
        rain.canvas.startRain();
    }

}