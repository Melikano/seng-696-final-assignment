import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InsuranceUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("List of all insurance:");
    JPanel panel = new JPanel();
    JList<String> appointmentData;
    JButton goBackHome = new JButton("Home");
    JTable insuranceTable;
    private static InsuranceUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> insurance;

    private InsuranceUI(String frameTitle) {
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


//showing the table of insurances that cover a person
    public void tableHandler(ArrayList<ArrayList<String>> insurance) {
        this.insurance = insurance;
        // parse input to an array list of strings and show it in chooseMedUI
        String[][] insuraceList = new String[insurance.size()][2];
        int insuranceCounter = 0;
        //iterate over the list received
        for (int i = 0; i < insurance.size(); i++) {
            String[] tempArray = new String[3];
            String name = insurance.get(i).get(0);
            tempArray[0] = name;
            String description = insurance.get(i).get(1);
            tempArray[1] = description;

            insuraceList[insuranceCounter] = tempArray;
            insuranceCounter += 1;
        }
        String[] columnNames = { "Amount (The Currency is Canadian Dollar)", "Covered By" };
        DefaultTableModel model = new DefaultTableModel(insuraceList, columnNames);

        this.insuranceTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(insuranceTable);
        panel.add(scrollPane);
        panel.add(goBackHome);
        frame.pack();
    }

    public static InsuranceUI createUI() {
        if (singleton == null) {
            singleton = new InsuranceUI("Hello");

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

    public void showFailureMessage() {
        
        JOptionPane.showMessageDialog(null, "You are not covered by any insurance", "alert", JOptionPane.ERROR_MESSAGE);

    }


}
