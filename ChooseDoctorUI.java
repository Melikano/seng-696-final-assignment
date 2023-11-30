import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChooseDoctorUI extends UserInterface implements ActionListener  {
    JLabel descriptionTextField = new JLabel("Available times for the selected therapist:");
    JTextField therapistNumTextField = new JTextField("         ");
    JPanel panel = new JPanel();
    JList<String> appointmentData;
    JButton goBackHome = new JButton("Home");
    JTable appointmentsTable;
    JButton submitButton = new JButton("submit");
    private static ChooseDoctorUI singleton = null;
    JCheckBox[][] checkboxes;
    ArrayList<ArrayList<String>> doctors;

    private ChooseDoctorUI(String frameTitle) {
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

    public static ChooseDoctorUI createUI()
    {
        if (singleton == null)
        {
            singleton = new ChooseDoctorUI("Therapist");

        }
        return singleton;

    }
    public void tableHandler(ArrayList<ArrayList<String>> doctors)
    {
        this.doctors = doctors;
        //parse input to an array list of strings and show it in chooseTherapistUI
        String[][] doctorsList = new String[doctors.size()][3];
        int therapistCounter = 0;
        for (int i=0; i< doctors.size();i++)
        {
            String[] tempArray = new String[4];
            tempArray[0] = Integer.toString(i);
            String name = doctors.get(i).get(0);
            tempArray[1] = name;
            String profession = doctors.get(i).get(1);
            tempArray[2] = profession;

            doctorsList[therapistCounter] = tempArray;
            therapistCounter += 1;
        }
        String[] columnNames = { "ID", "Therapist Name", "Therapist Profession"};
        DefaultTableModel model = new DefaultTableModel(doctorsList, columnNames);

        this.appointmentsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        panel.add(scrollPane);
        panel.add(therapistNumTextField);
        panel.add(submitButton);
        panel.add(goBackHome);
        frame.pack();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.submitButton)
        {
            String numberString = therapistNumTextField.getText();
            numberString = numberString.replaceAll("\\s+","");
            int number = Integer.parseInt(numberString);
            if (number < 0 || number >= doctors.size())
            {
                showFailureNoOptions();
                return;
            }
            PortalUI.returnSingleton().requestAvailability(doctors.get(number).get(2));


            }
        if (e.getSource() == this.goBackHome)
        {
            this.disposeFrame();
            HomeUI homeUIInstance = HomeUI.createUI();
            homeUIInstance.show();

        }

    }

    public void showFailureNoOptions()
    {
        JOptionPane.showMessageDialog(null, "Invalid option", "alert", JOptionPane.ERROR_MESSAGE);

    }


}
