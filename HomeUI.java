import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeUI extends UserInterface implements ActionListener {
    JLabel welcomeTextLabel = new JLabel();
    JButton doctorsButton = new JButton("Book Appointment");
    JButton appointmentsButton = new JButton("Past Appointments");
    JButton medicationsButton = new JButton("Past Medications");
    JButton pharmacyButton = new JButton("Pharmacy");
    JButton laboratoryButton = new JButton("Laboratory");
    JButton insuraceButton = new JButton("Insurance");

    JButton signoutButton = new JButton("Sign Out");


    private static HomeUI singleton = null;
    JPanel panel = new JPanel();

    private HomeUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(250, 250));
        doctorsButton.addActionListener(this);
        appointmentsButton.addActionListener(this);
        medicationsButton.addActionListener(this);
        pharmacyButton.addActionListener(this);
        laboratoryButton.addActionListener(this);
        insuraceButton.addActionListener(this);
        signoutButton.addActionListener(this);


        panel.setLayout(new FlowLayout());
        panel.add(welcomeTextLabel);
        panel.add(doctorsButton);
        panel.add(appointmentsButton);
        panel.add(medicationsButton);
        panel.add(pharmacyButton);
        panel.add(laboratoryButton);
        panel.add(insuraceButton);
        panel.add(signoutButton);


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
        if (e.getSource() == signoutButton) {
            this.disposeFrame();
            PatientUI patientUIInstance = PatientUI.createUI();
            patientUIInstance.show();

        }
        if (e.getSource() == appointmentsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestAppointmentsList();
        }
        if (e.getSource() == medicationsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            portal.requestPasMedicationsList();
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
        
        if (e.getSource() == appointmentsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            // portal.requestAppointmentsList();
        }
        
        if (e.getSource() == medicationsButton) {
            this.disposeFrame();
            PortalUI portal = PortalUI.returnSingleton();
            // portal.requestMedicationList();
        }
    }

    public void setName(String name) {
        welcomeTextLabel.setText("Welcome user " + name);
    }
}
