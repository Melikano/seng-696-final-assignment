import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChooseAppointmentUI extends UserInterface implements ActionListener {
    JLabel descriptionTextField = new JLabel("");
    JTextField avalNumTextField = new JTextField("          ");
    JPanel panel = new JPanel();
    JList<String> appointmentData;
    JButton goBackHome = new JButton("Home");
    JTable appointmentsTable;
    JButton submitButton = new JButton("submit");
    private static ChooseAppointmentUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<LocalDateTime> availabilities;

    private ChooseAppointmentUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(500, 500));
        descriptionTextField.setPreferredSize(new Dimension(400, 40));
        descriptionTextField.setText("Availability times for selected doctor");
        panel.setLayout(new FlowLayout());
        panel.add(descriptionTextField);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        goBackHome.addActionListener(this);
        submitButton.addActionListener(this);
        frame.add(panel);
    }

    public void show(String[] data) {
        frame.setVisible(true);

    }

    public static ChooseAppointmentUI createUI() {
        if (singleton == null) {
            singleton = new ChooseAppointmentUI("past");

        }
        return singleton;

    }

    public void tableHandler(ArrayList<LocalDateTime> availabilities) {

        this.availabilities = availabilities;
        // parse input to an array list of strings and show it in chooseAppUI
        String[][] avalabilityList = new String[availabilities.size()][2];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int avalabilityCounter = 0;
        for (int i = 0; i < availabilities.size(); i++) {
            String[] tempArray = new String[2];
            tempArray[0] = Integer.toString(i);
            String time = availabilities.get(i).format(formatter);
            tempArray[1] = time;
            avalabilityList[avalabilityCounter] = tempArray;
            avalabilityCounter += 1;
        }
        String[] columnNames = { "Number", "Date/Time" };
        DefaultTableModel model = new DefaultTableModel(avalabilityList, columnNames);
        this.appointmentsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        panel.add(scrollPane);
        panel.add(avalNumTextField);
        panel.add(submitButton);
        panel.add(goBackHome);
        frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.submitButton) {
            String numberString = avalNumTextField.getText();
            numberString = numberString.replaceAll("\\s+", "");
            int number = Integer.parseInt(numberString);
            //check the number user has entered
            if (number < 0 || number >= availabilities.size()) {
                showFailureNoOptions();
                return;
            }
            PortalUI.returnSingleton().requestCreateAppointment(availabilities.get(number));

        }

        if (e.getSource() == this.goBackHome) {
            this.disposeFrame();
            this.panel.removeAll();
            HomeUI homeUIInstance = HomeUI.createUI();
            homeUIInstance.show();

        }

    }

    //handling appointment failure
    public void showFailureNoOptions() {
        JOptionPane.showMessageDialog(null, "No options selected", "alert", JOptionPane.ERROR_MESSAGE);

    }

    public void showFailureMessage() {
        JOptionPane.showMessageDialog(null, "Creating appointment failed", "alert", JOptionPane.ERROR_MESSAGE);

    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(null, "Creating appointment success", "alert", JOptionPane.INFORMATION_MESSAGE);

    }

}