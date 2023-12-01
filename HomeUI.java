import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeUI extends UserInterface implements ActionListener  {
    JLabel welcomeTextLabel = new JLabel();
    JButton doctorsButton = new JButton("Doctors List");
    private static HomeUI singleton = null;
    JPanel panel = new JPanel();

    private HomeUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(250, 250));
        doctorsButton.addActionListener(this);

        panel.setLayout(new FlowLayout());
        panel.add(welcomeTextLabel);
        panel.add(doctorsButton);

        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.add(panel);
        frame.pack();
    }


    public static HomeUI createUI()
    {
        if (singleton == null)
        {
            singleton = new HomeUI("Home");

        }
        return singleton;

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void setName(String name)
    {
        welcomeTextLabel.setText("Welcome user " + name);
    }
}
