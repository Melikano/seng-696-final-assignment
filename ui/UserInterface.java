package ui;


import javax.swing.*;
import java.awt.*;

public abstract class UserInterface{
    JFrame frame;
    UserInterface(String frameTitle)
    {
        frame = new JFrame(frameTitle);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));

    }

    public void disposeFrame()
    {
        this.frame.setVisible(false);
    }
    public void show(){this.frame.setVisible(true);}
}
