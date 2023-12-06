import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MedicationsListUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("List of all medications:");
    JPanel panel = new JPanel();
    JButton goBackHome = new JButton("Home");
    JTable medicationsTable;
    private static MedicationsListUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> medications;

    private MedicationsListUI(String frameTitle) {
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

    //show the table of the medications
    public void tableHandler(ArrayList<ArrayList<String>> medications) {
        this.medications = medications;

        String[][] medicationsList = new String[medications.size()][2];
        int testCounter = 0;
        //iterate over the list received to get relevant data
        for (int i = 0; i < medications.size(); i++) {
            String[] tempArray = new String[2];
            String name = medications.get(i).get(0);
            tempArray[0] = name;
            String description = medications.get(i).get(1);
            tempArray[1] = description;

            medicationsList[testCounter] = tempArray;
            testCounter += 1;
        }

        String[] columnNames = { "Name", "Description" };
        DefaultTableModel model = new DefaultTableModel(medicationsList, columnNames);

        this.medicationsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(medicationsTable);

        panel.add(scrollPane);
        if (medications.size() == 0) {
            JLabel label = new JLabel("You don't have any medications yet");
            panel.add(label);
        }
        panel.add(goBackHome);
        frame.pack();
    }

    public static MedicationsListUI createUI() {
        if (singleton == null) {
            singleton = new MedicationsListUI("Hello");

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
