import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeUI extends UserInterface implements ActionListener {
    JLabel welcomeTextLabel = new JLabel();
    JButton doctorsButton = new JButton("Doctors List");
    JButton appointmentsButton = new JButton("Past Appointments");
    JButton pharmacyButton = new JButton("Medications List");
    JButton laboratoryButton = new JButton("Tests List");
    JButton insuraceButton = new JButton("Insurance List");

    private static HomeUI singleton = null;
    JPanel panel = new JPanel();

    private HomeUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(250, 250));
        doctorsButton.addActionListener(this);
        appointmentsButton.addActionListener(this);
        pharmacyButton.addActionListener(this);
        laboratoryButton.addActionListener(this);
        insuraceButton.addActionListener(this);

        panel.setLayout(new FlowLayout());
        panel.add(welcomeTextLabel);
        panel.add(doctorsButton);
        panel.add(appointmentsButton);
        panel.add(pharmacyButton);
        panel.add(laboratoryButton);
        panel.add(insuraceButton);

        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.add(panel);
        frame.pack();
    }

    public static HomeUI createUI() {
        if (singleton == null) {
            singleton = new HomeUI("Home");

        }
        return singleton;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == doctorsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestDoctorsList();
        }
        if (e.getSource() == appointmentsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestAppointmentsList();
        }
        if (e.getSource() == pharmacyButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestMedicationsList();
        }
        if (e.getSource() == laboratoryButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestTestsList();
        }
        if (e.getSource() == insuraceButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestInsuranceList();
        }
    }

    public void setName(String name) {
        welcomeTextLabel.setText("Welcome user " + name);
    }
}
