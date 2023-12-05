import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TestUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("List of all tests:");
    JPanel panel = new JPanel();
    JList<String> appointmentData;
    JButton goBackHome = new JButton("Home");
    JTable testsTable;
    private static TestUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> tests;

    private TestUI(String frameTitle) {
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



    public void tableHandler(ArrayList<ArrayList<String>> tests) {
        this.tests = tests;
        // parse input to an array list of strings and show it in chooseMedUI
        String[][] testList = new String[tests.size()][2];
        int testCounter = 0;
        for (int i = 0; i < tests.size(); i++) {
            String[] tempArray = new String[3];
            String name = tests.get(i).get(0);
            tempArray[0] = name;
            String description = tests.get(i).get(1);
            tempArray[1] = description;

            testList[testCounter] = tempArray;
            testCounter += 1;
        }
        String[] columnNames = { "ID", "Description" };
        DefaultTableModel model = new DefaultTableModel(testList, columnNames);

        this.testsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(testsTable);
        panel.add(scrollPane);
        panel.add(goBackHome);
        frame.pack();
    }

    public static TestUI createUI() {
        if (singleton == null) {
            singleton = new TestUI("Hello");

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
        
        JOptionPane.showMessageDialog(null, "You have not taken any tests", "alert", JOptionPane.ERROR_MESSAGE);

    }


}
