
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginUI extends UserInterface implements ActionListener {
    
    JTextField emailTextField = new JTextField(20);
    JTextField passwordTextField = new JTextField(20);
    JButton button = new JButton("Submit");
    JButton goBackHome = new JButton("Back");
    JPanel panel = new JPanel();
    private static LoginUI singleton = null;



    private LoginUI(String frameTitle) {
        super(frameTitle);
        panel.setPreferredSize(new Dimension(250, 250));
        emailTextField.setPreferredSize(new Dimension(250, 40));
        emailTextField.setText("Email");
        passwordTextField.setText("Password");
        passwordTextField.setPreferredSize(new Dimension(250, 40));
        button.addActionListener(this);
        goBackHome.addActionListener(this);
        panel.add(emailTextField);
        panel.add(passwordTextField);
        panel.setLayout(new FlowLayout());
        panel.add(button);
        panel.add(goBackHome);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.add(panel);
        frame.pack();

    }

    public static LoginUI createUI()
    {
        if (singleton == null)
        {
            singleton = new LoginUI("login");

        }
        return singleton;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //to go back to the patient UI
        if (e.getSource() == this.goBackHome) {
            this.disposeFrame();
            emailTextField.setText("Email");
            passwordTextField.setText("Password");
            PatientUI patientUIInstance = PatientUI.createUI();
            patientUIInstance.show();

        }

        if (e.getSource() == button)
        {
            String email = emailTextField.getText();
            String password = passwordTextField.getText();

//            System.out.println(passwordTextField.getText());
            // regex from https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
            Pattern pattern = Pattern.compile("^.+@.+\\..+$");
            Matcher matcher = pattern.matcher(email);
            boolean doesItMatch = matcher.matches();
            if (!doesItMatch)
            {
                this.showFailureEmail();
            }
            //check if the password is valid
            if (password.length() < 3 || password.length() > 10)
            {
                this.showFailurePassword();
            }
            String encryptedPassword = Cryptography.encrypt(password);
            //call requestLogin
            PortalUI.returnSingleton().requestLoginUser(email, encryptedPassword);
        }

    }

    public void showFailureMessage()
    {
        JOptionPane.showMessageDialog(null, "Login failed", "alert", JOptionPane.ERROR_MESSAGE);

    }

    public void showFailureEmail()
    {
        JOptionPane.showMessageDialog(null, "Login failed because email is invalid", "alert", JOptionPane.ERROR_MESSAGE);

    }
    public void showFailurePassword()
    {
        JOptionPane.showMessageDialog(null, "Login failed because password is too short (<3) or too long(>8)", "alert", JOptionPane.ERROR_MESSAGE);

    }
    public void showSuccessLogin()
    {
        JOptionPane.showMessageDialog(null, "Login successful! welcome!", "Success!", JOptionPane.INFORMATION_MESSAGE);

    }
}
