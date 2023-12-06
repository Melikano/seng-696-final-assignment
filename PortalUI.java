
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

    //call doctorUI to show doctors list and passing doctors
    public void showDoctorList(ArrayList<ArrayList<String>> doctors) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        ChooseDoctorUI doctorUI = ChooseDoctorUI.createUI();
        doctorUI.tableHandler(doctors);
        doctorUI.show();
    }

    //call medicationUI to show medication list and passing medication that user can order
    public void showMedicationList(ArrayList<ArrayList<String>> medications) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        ChooseMedicationUI medicationUI = ChooseMedicationUI.createUI();
        medicationUI.tableHandler(medications);
        medicationUI.show();
    }

    //call medicationUI to show medication list and passing medication that user has ordered
    public void showPastMedicationList(ArrayList<ArrayList<String>> medications) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        MedicationsListUI medicationUI = MedicationsListUI.createUI();
        medicationUI.tableHandler(medications);
        medicationUI.show();
    }

    //call appointmentUI to show appointment list and passing appointments that user has created
    public void showAppointmentsList(ArrayList<ArrayList<String>> appointments) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();
        AppointmentsListUI appointmentsListUI = AppointmentsListUI.createUI();
        appointmentsListUI.tableHandler(appointments);
        appointmentsListUI.show();
    }

    //show the list of test that user has taken
    //exist indicate if he has taken any tests or not
    public void showTestList(Boolean exist, ArrayList<ArrayList<String>> tests) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();

        if (exist == true) {
            TestUI testUI = TestUI.createUI();
            testUI.tableHandler(tests);
            testUI.show();
        } else {
            TestUI testUI = TestUI.createUI();
            home.show();
            testUI.showFailureMessage();
        }
    }

    
    //show the list and amount of money each insurance covers the user
    public void showInsuranceList(Boolean exist, ArrayList<ArrayList<String>> tests) {
        HomeUI home = HomeUI.createUI();
        home.disposeFrame();

        if (exist == true) {
            InsuranceUI insuranceUI = InsuranceUI.createUI();
            insuranceUI.tableHandler(tests);
            insuranceUI.show();
        } else {
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

    //if login is confirmed it will show success otherwise failure
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

    //if register is confirmed it will show success otherwise failure
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

    public void requestAppointmentsList() {
        portalAgentInstance.appointmentsListRequest();
    }

    public void requestPastMedicationsList() {
        portalAgentInstance.pastMedicationsRequest();
    }

    public void requestMedicationsList() {
        portalAgentInstance.medicationListRequest();
    }

    public void requestPasMedicationsList() {
        portalAgentInstance.pastMedicationsRequest();
    }

    //passing email to find the tests related to that patient
    public void requestTestsList(){
        portalAgentInstance.testsListRequest(this.patientEmail);
    }

    //passing email to find the insurance related to that patient
    public void requestInsuranceList(){
        portalAgentInstance.insuranceListRequest(this.patientEmail);
    }

    public void requestCreateAppointment(LocalDateTime appDateTime) {
        portalAgentInstance.createAppointmentRequest(appDateTime, this.patientEmail, this.doctorEmail);
    }

    public void requestAddMedication(Medication medication) {
        portalAgentInstance.addMedicationRequest(medication);
    }

    //availability of the specific doctor that is passed
    public void requestOrderMedication(Medication medication) {
        portalAgentInstance.orderMedicationRequest(medication);
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

    //appID is the appointment Id and amount is the price of appointment
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

    public void orderMedicationConfirm(boolean confirmed, Medication newMed) {
        System.out.println(confirmed);
        if (confirmed) {
            PortalUI.returnSingleton().requestAddMedication(newMed);
            ChooseMedicationUI medUI = ChooseMedicationUI.createUI();
            medUI.showSuccess();
        } else {
            ChooseMedicationUI selectAPPUI = ChooseMedicationUI.createUI();
            selectAPPUI.showFailureNoOptions();
        }
    }

}