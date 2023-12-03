import FIPA.DateTime;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.time.LocalDateTime;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PortalAgent extends Agent {
    AID patientAgent;
    AID healthcareproviderAgent;
    AID insuranceAgent;
    AID laboratoryAgent;
    AID pharmacyAgent;

    PortalUI PortalUIInstance = PortalUI.createUI(this);

    protected void setup() {

        System.out.println("Hello world! This is portal agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        // a one shot behavior to get all services' AIDs
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                patientAgent = ServiceSearch.searchForService(myAgent, "patient");
                healthcareproviderAgent = ServiceSearch.searchForService(myAgent, "healthcareprovider");
                insuranceAgent = ServiceSearch.searchForService(myAgent, "insurace");
                laboratoryAgent = ServiceSearch.searchForService(myAgent, "laboratory");
                pharmacyAgent = ServiceSearch.searchForService(myAgent, "pharmacy");

            }
        });

        PortalUIInstance.startUI();
        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg;
                msg = myAgent.receive();

                if (msg != null) {
                    String[] payloadLst = msg.getContent().split(Messages.DELIMITER);
                    switch (msg.getPerformative()) {
                        case Messages.REGISTER_RESPONSE:
                            System.out.println("PORTAL: Received register confirmation");
                            boolean registered = false;
                            if (payloadLst[0].equals(Messages.MESSAGE_SUCCESS)) {
                                registered = true;
                            } else if (payloadLst[0].equals(Messages.MESSAGE_FAILURE)) {
                                registered = false;
                            } else
                                assert true : "UNKNOWN REGISTRATION MESSAGE";
                            PortalUIInstance.registerConfirm(registered);
                            break;

                        case Messages.LOGIN_RESPONSE:
                            System.out.println("PORTAL: Received login confirmations");
                            String confirmation = payloadLst[0];
                            boolean isAuthenticated = false;
                            String name = "";
                            if (confirmation.equals(Messages.MESSAGE_SUCCESS)) {
                                isAuthenticated = true;
                                name = payloadLst[1];
                                System.out.println("PORTAL: Successfully logined user: " + name);
                            } else if (confirmation.equals(Messages.MESSAGE_FAILURE)) {
                                isAuthenticated = false;
                                System.out.println("PORTAL: Failed to login");
                            } else
                                assert true : "UNKNOWN LOGIN MESSAGE";
                            PortalUIInstance.loginConfirm(isAuthenticated, name);
                            break;

                        case Messages.DOCTORS_LISTS_RESPONSE:
                            System.out.println("PORTAL: Received doctors list");
                            System.out.println(payloadLst.length);

                            ArrayList<ArrayList<String>> doctorsLists = new ArrayList<>();
                            for (int i = 0; i <= payloadLst.length; i++) {
                                if (i % 3 == 0 && i != 0) {
                                    ArrayList<String> doctorInfo = new ArrayList<>();
                                    doctorInfo.add(payloadLst[i - 3]);
                                    doctorInfo.add(payloadLst[i - 2]);
                                    doctorInfo.add(payloadLst[i - 1]);
                                    doctorsLists.add(doctorInfo);
                                }
                            }

                            PortalUIInstance.showDoctorList(doctorsLists);
                            break;

                        case Messages.AVAILABILITY_RESPONSE:
                            System.out.println("PORTAL: Received doctor's availability");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            ArrayList<LocalDateTime> dateTimes = new ArrayList<>();

                            for (String dateTimeStr : payloadLst) {
                                dateTimes.add(LocalDateTime.parse(dateTimeStr, formatter));
                            }
                            PortalUIInstance.showAvailability(dateTimes);
                            break;

                        case Messages.CREATE_APPOINTMENT_RESPONSE:
                            System.out.println("PORTAL: Received appointment creation's confirmation");
                            boolean appointmentCreated = false;
                            int appointmentID = -1;
                            float amount = -1;
                            if (payloadLst[0].equals(Messages.MESSAGE_SUCCESS)) {
                                appointmentCreated = true;
                                appointmentID = Integer.parseInt(payloadLst[1]);
                                amount = Float.parseFloat(payloadLst[2]);
                            }
                            PortalUIInstance.appointmentConfirm(appointmentCreated, appointmentID, amount);
                            break;

                        case Messages.MEDICATION_LIST_RESPONSE:
                            System.out.println("PORTAL: Received medications list");
                            System.out.println(payloadLst.length);

                            ArrayList<ArrayList<String>> medicationList = new ArrayList<>();
                            for (int i = 0; i <= payloadLst.length; i++) {
                                if (i % 3 == 0 && i != 0) {
                                    ArrayList<String> medicationInfo = new ArrayList<>();
                                    medicationInfo.add(payloadLst[i - 3]);
                                    medicationInfo.add(payloadLst[i - 2]);
                                    medicationInfo.add(payloadLst[i - 1]);
                                    medicationList.add(medicationInfo);
                                }
                            }
                            PortalUIInstance.showMedicationList(medicationList);
                            break;
                    }
                }
            }
        });

    }

    public void availabilityRequest(String doctorEmail) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.AVAILABILITY_REQUEST);

                System.out.println("PORTAL: Requesting to get availability of doctors "
                        + doctorEmail + " from " + healthcareproviderAgent.getLocalName());

                message.setContent(doctorEmail);
                message.addReceiver(healthcareproviderAgent);
                send(message);
            }
        });
    }

    public void registerRequest(String name, String email, String phoneNumber, String password) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.REGISTER_REQUEST);
                List<String> payloadLst = Arrays.asList(name, password, email, phoneNumber);

                String payload = String.join(Messages.DELIMITER, payloadLst);

                System.out.println("PORTAL: Requesting to register " + email + " to " + patientAgent.getLocalName());

                message.setContent(payload);
                message.addReceiver(patientAgent);
                send(message);
            }
        });
    }

    public void loginRequest(String email, String passwordHash) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.LOGIN_REQUEST);
                List<String> payloadLst = Arrays.asList(email, passwordHash);

                String payload = String.join(Messages.DELIMITER, payloadLst);

                System.out.println("PORTAL: Requesting to login " + email + " to " + patientAgent.getLocalName());

                message.setContent(payload);
                message.addReceiver(patientAgent);
                send(message);
            }
        });
    }

    public void doctorsListRequest() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.DOCTORS_LISTS_REQUEST);

                System.out.println(
                        "PORTAL: Requesting to get doctors list from " + healthcareproviderAgent.getLocalName());

                message.addReceiver(healthcareproviderAgent);
                send(message);
            }
        });
    }

    public void createAppointmentRequest(LocalDateTime appDateTime, String patientEmail, String doctorEmail) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.CREATE_APPOINTMENT_REQUEST);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String dateTimeStr = appDateTime.format(formatter);
                List<String> payloadLst = Arrays.asList(
                        dateTimeStr,
                        patientEmail,
                        doctorEmail);

                String payload = String.join(Messages.DELIMITER, payloadLst);

                System.out.println("PORTAL: Requesting to make an appointment on  " + appDateTime.toString()
                        + "for DR. " + doctorEmail + "to " + patientAgent.getLocalName());

                message.setContent(payload);
                message.addReceiver(patientAgent);
                send(message);
                confirmAppointmentRequest(appDateTime, doctorEmail);
            }
        });
    }

    public void confirmAppointmentRequest(LocalDateTime appDateTime, String doctorEmail) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("DOCTOR EMAIL: " + doctorEmail);
                ACLMessage message = new ACLMessage(Messages.CONFIRM_APPOINTMENT_REQUEST);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String dateTimeStr = appDateTime.format(formatter);
                List<String> payloadLst = Arrays.asList(dateTimeStr, doctorEmail);

                String payload = String.join(Messages.DELIMITER, payloadLst);

                System.out.println("PATIENT: Requesting to confirm an appointment on  " + appDateTime.toString()
                        + " to " + healthcareproviderAgent.getLocalName());

                message.setContent(payload);
                message.addReceiver(healthcareproviderAgent);
                send(message);
            }
        });
    }

    public void medicationListRequest() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(Messages.MEDICATION_LIST_REQUEST);

                System.out.println(
                        "PORTAL: Requesting to get medication list from " + pharmacyAgent.getLocalName());

                message.addReceiver(pharmacyAgent);
                send(message);
            }
        });
    }

}