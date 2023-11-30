package ui;

import java.time.LocalDateTime;
import java.util.ArrayList;

import agents.PortalAgent;

public class PortalUI {
    private static PortalUI singleton = null;
    PortalAgent portalAgentInstance;
    String patientEmail;
    String therapistEmail;
    String patientName;
    Integer appID;
    private PortalUI(PortalAgent portalAgentInstance)
    {
        this.portalAgentInstance = portalAgentInstance;
    }


    public static PortalUI createUI(PortalAgent portalAgentInstance)
    {
        if (singleton == null)
        {
            singleton = new PortalUI(portalAgentInstance);

        }
        return singleton;

    }
    public static PortalUI returnSingleton()
    {
        return singleton;
    }


    public void startUI()
    {
        PatientUI patientUIInstance = PatientUI.createUI();
        patientUIInstance.show();


    }

    public void showHome()
    {
        HomeUI home = HomeUI.createUI();
        home.setName(this.patientName);
        home.show();

    }

    public void showDoctorList(ArrayList<ArrayList<String>> therapists)
    {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        ChooseDoctorUI therapistUI = ChooseDoctorUI.createUI();
        therapistUI.tableHandler(therapists);
        therapistUI.show();
    }

    public void requestRegister(String name,String email,String phone,String password)
    {
        portalAgentInstance.registerRequest(name,email,phone,password);
    }
    
    public void requestLoginUser(String email, String password)
    {
        this.patientEmail = email;
        portalAgentInstance.loginRequest(email, password);
    }

       public void loginConfirm(boolean loginConfirm, String name)
    {
        this.patientName = name;
        if (loginConfirm)
        {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showSuccessLogin();
            loginUIInstance.disposeFrame();
            showHome();
        }
        else {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showFailureMessage();
        }
    }

        public void registerConfirm(boolean confirm)
    {
        if (confirm)
        {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showSuccessRegister();
            registerUIInstance.disposeFrame();
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.show();

        }
        else {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showFailureMessage();
        }
    }

    public void requestCreateAppointment(LocalDateTime appDateTime)
    {
        portalAgentInstance.createAppointmentRequest(appDateTime, this.patientEmail, this.therapistEmail);
    }

    public void requestAvailability(String therapistEmail)
    {
        portalAgentInstance.availabilityRequest(therapistEmail);
    }

    public void showAvailability(ArrayList<LocalDateTime> availabilityTimes)
    {
        ChooseDoctorUI doctorUI = ChooseDoctorUI.createUI();
        doctorUI.disposeFrame();
        //parse input to an array list of strings and show it in chooseTherapistUI
        ChooseAppointmentUI appUI = ChooseAppointmentUI.createUI();
        appUI.tableHandler(availabilityTimes);
        appUI.show();
    }

    public void appointmentConfirm(boolean appConfirm, Integer appID, float amount)
    {
        this.appID = appID;
        if (appConfirm)
        {
            ChooseAppointmentUI selectAPPUI = ChooseAppointmentUI.createUI();
            selectAPPUI.showSuccessMessage();
        }
        else {
            ChooseAppointmentUI selectAPPUI = ChooseAppointmentUI.createUI();
            selectAPPUI.showFailureMessage();
        }

    }




}