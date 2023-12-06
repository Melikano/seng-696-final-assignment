import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChooseMedicationUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("List of all medications:");
    JTextField medicationtNumTextField = new JTextField("         ");
    JPanel panel = new JPanel();
    JButton goBackHome = new JButton("Home");
    JTable medicationsTable;
    JButton submitButton = new JButton("submit");
    private static ChooseMedicationUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> medications;

    private ChooseMedicationUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(500, 500));
        descriptionTextField.setPreferredSize(new Dimension(400, 40));
        panel.setLayout(new FlowLayout());
        panel.add(descriptionTextField);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        goBackHome.addActionListener(this);
        submitButton.addActionListener(this);
        frame.add(panel);
    }

    public static ChooseMedicationUI createUI() {
        if (singleton == null) {
            singleton = new ChooseMedicationUI("Pharmacy");

        }
        return singleton;

    }

    public void tableHandler(ArrayList<ArrayList<String>> medications) {
        this.medications = medications;
        // parse input to an array list of strings and show it in chooseMedUI
        String[][] medicationList = new String[medications.size()][4];
        int medicationtCounter = 0;
        for (int i = 0; i < medications.size(); i++) {
            String[] tempArray = new String[4];
            tempArray[0] = Integer.toString(i);
            String name = medications.get(i).get(0);
            tempArray[1] = name;
            String description = medications.get(i).get(1);
            tempArray[2] = description;
            String price = medications.get(i).get(2);
            tempArray[3] = price;

            medicationList[medicationtCounter] = tempArray;
            medicationtCounter += 1;
        }
        String[] columnNames = { "ID", "Name", "Description", "Price" };
        DefaultTableModel model = new DefaultTableModel(medicationList, columnNames);

        this.medicationsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(medicationsTable);
        panel.add(scrollPane);
        panel.add(medicationtNumTextField);
        panel.add(submitButton);
        panel.add(goBackHome);
        frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.submitButton) {
            String numberString = medicationtNumTextField.getText();
            numberString = numberString.replaceAll("\\s+", "");
            int number = Integer.parseInt(numberString);
            if (number < 0 || number >= medications.size()) {
                showFailureNoOptions();
                return;
            }
            Medication newMed = new Medication(medications.get(number).get(0), medications.get(number).get(1), null, null);
            System.out.println("selected: " + number);
            PortalUI.returnSingleton().requestAddMedication(newMed);
            JOptionPane.showMessageDialog(null, "Order for the selected medication was confirmed by the pharmacy!", "alert", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == this.goBackHome) {
            this.disposeFrame();
            this.panel.removeAll();
            HomeUI homeUIInstance = HomeUI.createUI();
            homeUIInstance.show();

        }

    }

    public void showFailureNoOptions() {
        JOptionPane.showMessageDialog(null, "Invalid option", "alert", JOptionPane.ERROR_MESSAGE);

    }

}
