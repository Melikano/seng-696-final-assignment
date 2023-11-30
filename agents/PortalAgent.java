import FIPA.DateTime;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import utils.Messages;
import jade.core.AID;
import jade.core.behaviours.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcp.xml.dsig.internal.dom.Utils;

import java.time.LocalDateTime;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PortalAgent extends Agent {
    AID patientAgent;
    AID healthcareproviderAgent;

    PortalGUI PortalUIInstance = PortalGUI.createUI(this);

    protected void setup() {

        System.out.println("Hello world! This is portal agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        // a one shot behavior to get all services' AIDs
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                PatientAgent = Utils.searchForService(myAgent, "patient");
                healthCareProviderAgent = Utils.searchForService(myAgent, "healthcareprovider");
                InsuranceAgent = Utils.searchForService(myAgent, "insurace");
                LaboratoryAgent = Utils.searchForService(myAgent, "laboratory");
                PharmacyAgent = Utils.searchForService(myAgent, "pharmacy");

            }
        });

        PortalUIInstance.startGUI();
        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg;
                msg = myAgent.receive();

                if (msg != null) {
                    String[] payloadLst = msg.getContent().split(Messages.DELIMITER);
                    switch (msg.getPerformative()) {
                        case Messages.DOCTORS_LISTS_RESPONSE:
                            System.out.println("PORTAL: Received doctors list");
                            ArrayList<ArrayList<String>> doctorsLists = new ArrayList<>();
                            for (int i = 0; i < payloadLst.length; i++) {
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
                        + " to " + patientAgent.getLocalName());

                message.setContent(payload);
                message.addReceiver(patientAgent);
                send(message);
            }
        });
    }

}