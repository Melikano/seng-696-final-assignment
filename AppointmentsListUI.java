import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppointmentsListUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("List of all appointments:");
    JPanel panel = new JPanel();
    JList<String> appointmentData;
    JButton goBackHome = new JButton("Home");
    JTable appointmentsTable;
    private static AppointmentsListUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> appointments;

    private AppointmentsListUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(500, 500));
        descriptionTextField.setPreferredSize(new Dimension(400, 40));
        panel.setLayout(new FlowLayout());
        panel.add(descriptionTextField);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        goBackHome.addActionListener(this);
        frame.add(panel);
    }

    public void tableHandler(ArrayList<ArrayList<String>> appointments) {
        this.appointments = appointments;

        String[][] appointmentsList = new String[appointments.size()][2];
        int testCounter = 0;
        for (int i = 0; i < appointments.size(); i++) {
            String[] tempArray = new String[3];
            String name = appointments.get(i).get(0);
            tempArray[0] = name;
            String description = appointments.get(i).get(1);
            tempArray[1] = description;

            appointmentsList[testCounter] = tempArray;
            testCounter += 1;
        }

        String[] columnNames = { "ID", "Doctor", "Date and Time" };
        DefaultTableModel model = new DefaultTableModel(appointmentsList, columnNames);

        this.appointmentsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);

        panel.add(scrollPane);
        if(appointments.size() == 0){
            JLabel label = new JLabel("You don't have any appointments yet");
            panel.add(label);
        }
        panel.add(goBackHome);
        frame.pack();
    }

    public static AppointmentsListUI createUI() {
        if (singleton == null) {
            singleton = new AppointmentsListUI("Hello");

        }
        return singleton;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.goBackHome) {
            this.disposeFrame();
            this.panel.removeAll();
            HomeUI homeUIInstance = HomeUI.createUI();
            homeUIInstance.show();

        }

    }

}
