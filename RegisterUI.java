
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//registeration interface
public class RegisterUI extends UserInterface implements ActionListener {
    JTextField emailTextField = new JTextField(20);
    JTextField passwordTextField = new JTextField(20);
    JTextField nameTextField = new JTextField(20);
    JTextField phoneTextField = new JTextField(20);
    JButton button = new JButton("Submit");
    JButton goBackPortal = new JButton("Portal");

    JPanel panel = new JPanel();
    private static RegisterUI singleton = null;

    private RegisterUI(String frameTitle)
    {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(250, 250));
        emailTextField.setPreferredSize(new Dimension(250, 40));
        emailTextField.setText("Email");
        passwordTextField.setText("Password");
        nameTextField.setText("Name");
        phoneTextField.setText("phone");
        phoneTextField.setPreferredSize(new Dimension(250, 40));
        passwordTextField.setPreferredSize(new Dimension(250, 40));
        nameTextField.setPreferredSize(new Dimension(250, 40));
        button.addActionListener(this);
        goBackPortal.addActionListener(this);
        panel.add(nameTextField);
        panel.add(emailTextField);
        panel.add(passwordTextField);
        panel.add(phoneTextField);
        panel.setLayout(new FlowLayout());
        panel.add(button);
        panel.add(goBackPortal);

        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.add(panel);
        frame.pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.goBackPortal) {
            
            this.disposeFrame();
            emailTextField.setText("Email");
            passwordTextField.setText("Password");
            nameTextField.setText("Name");
            phoneTextField.setText("phone");
            PatientUI patientUIInstance = PatientUI.createUI();
            patientUIInstance.show();

        }

        if (e.getSource() == button)
        {

            String email = emailTextField.getText();
            String name = nameTextField.getText();
            String password = passwordTextField.getText();
            String phone = phoneTextField.getText();

            // regex from https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
            Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");
            Matcher emailMatcher = emailPattern.matcher(email);
            boolean emailDoesItMatch = emailMatcher.matches();

            Pattern phonePattern = Pattern.compile("^\\d{10}$");
            Matcher phoneMatcher = phonePattern.matcher(phone);
            boolean phoneDoesItMatch = phoneMatcher.matches();

            //checking the information to be valid
            if (name.length() < 3 || name.length() > 10)
            {
                this.showFailureName();
                return;
            }
            if (!emailDoesItMatch)
            {
                this.showFailureEmail();
                return;
            }
            if (password.length() < 3 || password.length() > 10)
            {
                this.showFailurePassword();
                return;
            }
            if (!phoneDoesItMatch)
            {
                this.showFailurePhone();
                return;
            }

            String encryptedPassword = Cryptography.encrypt(password);
            //call portalUI with the info provided
            PortalUI.returnSingleton().requestRegister(name,email,phone,encryptedPassword);

        }

    }
    public static RegisterUI createUI()
    {
        if (singleton == null)
        {
            singleton = new RegisterUI("Register");

        }
        return singleton;

    }


    public void showFailureEmail()
    {
        JOptionPane.showMessageDialog(null, "Register failed because email is invalid", "alert", JOptionPane.ERROR_MESSAGE);

    }
    public void showFailurePassword()
    {
        JOptionPane.showMessageDialog(null, "Register failed because password is too short (<3) or too long(>10)", "alert", JOptionPane.ERROR_MESSAGE);

    }
    public void showFailureMessage()
    {
        JOptionPane.showMessageDialog(null, "Register failed", "alert", JOptionPane.ERROR_MESSAGE);

    }

    public void showSuccessRegister()
    {
        JOptionPane.showMessageDialog(null, "Register successful! please login.", "Success!", JOptionPane.INFORMATION_MESSAGE);

    }
    public void showFailureName()
    {
        JOptionPane.showMessageDialog(null, "Register failed because name is too short (<3) or too long(>10)", "alert", JOptionPane.ERROR_MESSAGE);

    }
    public void showFailurePhone()
    {
        JOptionPane.showMessageDialog(null, "Phone number is invalid, should be 10 digits.", "alert", JOptionPane.ERROR_MESSAGE);

    }
}
