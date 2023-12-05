
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PortalUI {
    private static PortalUI singleton = null;
    PortalAgent portalAgentInstance;
    String patientEmail;
    String doctorEmail;
    String patientName;
    Integer appID;

    private PortalUI(PortalAgent portalAgentInstance) {
        this.portalAgentInstance = portalAgentInstance;
    }

    public static PortalUI createUI(PortalAgent portalAgentInstance) {
        if (singleton == null) {
            singleton = new PortalUI(portalAgentInstance);

        }
        return singleton;

    }

    public static PortalUI returnSingleton() {
        return singleton;
    }

    public void startUI() {
        PatientUI patientUIInstance = PatientUI.createUI();
        patientUIInstance.show();

    }

    public void showHome() {
        HomeUI home = HomeUI.createUI();
        home.setName(this.patientName);
        home.show();

    }

    public void showDoctorList(ArrayList<ArrayList<String>> doctors) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        ChooseDoctorUI doctorUI = ChooseDoctorUI.createUI();
        doctorUI.tableHandler(doctors);
        doctorUI.show();
    }

    public void showMedicationList(ArrayList<ArrayList<String>> medications) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        ChooseMedicationUI medicationUI = ChooseMedicationUI.createUI();
        medicationUI.tableHandler(medications);
        medicationUI.show();
    }

    public void showTestList(Boolean exist, ArrayList<ArrayList<String>> tests) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();

        if(exist == true){
        TestUI testUI = TestUI.createUI();
        testUI.tableHandler(tests);
        testUI.show();
        }
        else{
            TestUI testUI = TestUI.createUI();
            home.show();
            testUI.showFailureMessage();
        }
    }

    
    public void showInsuranceList(Boolean exist, ArrayList<ArrayList<String>> tests) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();

        if(exist == true){
        InsuranceUI insuranceUI = InsuranceUI.createUI();
        insuranceUI.tableHandler(tests);
        insuranceUI.show();
        }
        else{
            InsuranceUI insuranceUI = InsuranceUI.createUI();
            home.show();
            insuranceUI.showFailureMessage();
        }
    }
    


    public void requestRegister(String name, String email, String phone, String password) {
        portalAgentInstance.registerRequest(name, email, phone, password);
    }

    public void requestLoginUser(String email, String password) {
        this.patientEmail = email;
        portalAgentInstance.loginRequest(email, password);
    }

    public void loginConfirm(boolean loginConfirm, String name) {
        this.patientName = name;
        if (loginConfirm) {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showSuccessLogin();
            loginUIInstance.disposeFrame();
            showHome();
        } else {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showFailureMessage();
        }
    }

    public void registerConfirm(boolean confirm) {
        if (confirm) {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showSuccessRegister();
            registerUIInstance.disposeFrame();
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.show();

        } else {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showFailureMessage();
        }
    }

    public void requestDoctorsList() {
        portalAgentInstance.doctorsListRequest();
    }

    public void requestMedicationsList(){
        portalAgentInstance.medicationListRequest();
    }

    public void requestTestsList(){
        portalAgentInstance.testsListRequest(this.patientEmail);
    }

    public void requestInsuranceList(){
        portalAgentInstance.insuranceListRequest(this.patientEmail);
    }

    public void requestCreateAppointment(LocalDateTime appDateTime) {
        portalAgentInstance.createAppointmentRequest(appDateTime, this.patientEmail, this.doctorEmail);
    }

    public void requestAvailability(String doctorEmail) {
        this.doctorEmail = doctorEmail;
        portalAgentInstance.availabilityRequest(doctorEmail);
    }

    public void showAvailability(ArrayList<LocalDateTime> availabilityTimes) {
        ChooseDoctorUI doctorUI = ChooseDoctorUI.createUI();
        doctorUI.disposeFrame();
        // parse input to an array list of strings and show it in chooseDoctorUI
        ChooseAppointmentUI appUI = ChooseAppointmentUI.createUI();
        appUI.tableHandler(availabilityTimes);
        appUI.show();
    }

    public void appointmentConfirm(boolean appConfirm, Integer appID, float amount) {
        this.appID = appID;
        if (appConfirm) {
            ChooseAppointmentUI selectAPPUI = ChooseAppointmentUI.createUI();
            selectAPPUI.showSuccessMessage();
        } else {
            ChooseAppointmentUI selectAPPUI = ChooseAppointmentUI.createUI();
            selectAPPUI.showFailureMessage();
        }

    }

}