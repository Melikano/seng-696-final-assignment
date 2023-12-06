
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.lang.acl.MessageTemplate;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.*;
import java.util.*;

public class PatientAgent extends Agent {

    ArrayList<Appointment> appointments;
    ArrayList<Medication> medications;

    private Hashtable<String, Patient> patients;

    protected void setup() {
        appointments = new ArrayList<Appointment>();
        medications = new ArrayList<Medication>();
        patients = new Hashtable<String, Patient>();

        patients = PatientUtils.generatePatients();

        // Register patient service so portal agent can search and find
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());

        sd.setType("patient");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new PatientAgent.userRegServer());
        addBehaviour(new PatientAgent.userLoginServer());
        addBehaviour(new PatientAgent.appointmentServer());
        addBehaviour(new PatientAgent.appointmentsListServer());
        addBehaviour(new PatientAgent.medicationServer());
        addBehaviour(new PatientAgent.medicationsListServer());

    }

    public void updateUsers(Patient patient) {
        patients.put(patient.getEmail(), patient);
    }

    public boolean loginUser(String username, String password) {

        Patient patient = patients.get(username);
        boolean isAuthenticated = false;

        if (patient != null)
            isAuthenticated = Cryptography.decrypt(patient.getPassword()).equals(Cryptography.decrypt(password));
        return isAuthenticated;
    }

    protected void takeDown() {
        System.out.println("Patient-agent " + getAID().getName() + " terminating.");
    }

    private class userRegServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.REGISTER_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                String info = msg.getContent();
                String[] newInfo = info.split(Messages.DELIMITER);
                ArrayList<Medication> medications = new ArrayList<Medication>();
                Patient newPatient = new Patient(newInfo[0], newInfo[2], newInfo[3], newInfo[1], medications);

                System.out.println("Patient: Received a register request for user: " + newPatient.getEmail());

                ACLMessage reply = msg.createReply();
                reply.setPerformative(Messages.REGISTER_RESPONSE);
                String content = "";

                if (patients.containsKey(newPatient.getEmail())) {
                    content = Messages.MESSAGE_FAILURE;
                } else {
                    updateUsers(newPatient);
                    content = Messages.MESSAGE_SUCCESS;
                }
                System.out.println("Patient: Sending registration confirmation back to portal");
                reply.setContent(content);
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }

    private class userLoginServer extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.LOGIN_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println("heelloo");
                String info = msg.getContent();
                String[] newInfo = info.split(Messages.DELIMITER);

                System.out.println("Patient: Received an login request for user: " + newInfo[0]);

                boolean flag = loginUser(newInfo[0], newInfo[1]);
                ACLMessage reply = msg.createReply();
                reply.setPerformative(Messages.LOGIN_RESPONSE);
                String content = "";
                if (flag) {
                    String name = patients.get(newInfo[0]).getName();
                    content = Messages.MESSAGE_SUCCESS;
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(name);
                } else
                    content = Messages.MESSAGE_FAILURE;

                System.out.println("Patient: Sending login confirmation back to portal");
                reply.setContent(content);
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }

    private class appointmentServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.CREATE_APPOINTMENT_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                String[] payloadLst = msg.getContent().split(Messages.DELIMITER);

                ACLMessage reply = msg.createReply();
                reply.setPerformative(Messages.CREATE_APPOINTMENT_RESPONSE);
                String content = "";

                String[] contentLst = msg.getContent().split(Messages.DELIMITER);
                String dateTimeStr = contentLst[0];
                String patientEmail = contentLst[1];
                String doctorEmail = contentLst[2];

                System.out.println("Patient: appointment creation received for " + patientEmail +
                        " on " + dateTimeStr);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

                boolean alreadyExists = false;
                for (Appointment appointment : appointments) {
                    if (appointment.getPatientEmail().equals(patientEmail) &&
                            appointment.getDoctorEmail().equals(doctorEmail)) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (alreadyExists) {
                    content = Messages.MESSAGE_FAILURE;
                } else {
                    Integer newAppointmentID = appointments.size();
                    Appointment newAppointment = new Appointment(newAppointmentID, patientEmail,
                            doctorEmail, dateTime, DoctorsUtils.HOURLY_WAGE, Boolean.FALSE);
                    appointments.add(newAppointment);
                    content = Messages.MESSAGE_SUCCESS;
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(newAppointmentID.toString());
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(DoctorsUtils.HOURLY_WAGE.toString());
                }

                reply.setContent(content);
                send(reply);

            }
        }

    }

    private class appointmentsListServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.APPOINTMENTS_LIST_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println("PATIENT: appointments' list request received");
                String content = "";
                // geting a message from portal and iterate over the doctors to get the three
                // fields
                ACLMessage replyAppointmentsList = msg.createReply();
                replyAppointmentsList.setPerformative(Messages.APPOINTMENTS_LIST_RESPONSE);
                System.out.println(appointments);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (Appointment appointment : appointments) {
                    content = content.concat(String.valueOf(appointment.getAppointmentID()));
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(appointment.getDoctorEmail());
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(appointment.getDateTime().format(formatter));
                    content = content.concat(Messages.DELIMITER);
                }
                System.out.println("PATIENT: Sending appointments' list back to portal");

                replyAppointmentsList.setContent(content);
                send(replyAppointmentsList);
            }
        }

    }

    private class medicationServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.ADD_MEDICATION_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                String[] payloadLst = msg.getContent().split(Messages.DELIMITER);

                ACLMessage reply = msg.createReply();
                reply.setPerformative(Messages.ADD_MEDICATION_RESPONSE);
                String content = "";

                String[] contentLst = msg.getContent().split(Messages.DELIMITER);
                String medicationName = contentLst[0];
                String medicationDesc = contentLst[1];

                Medication newMedication = new Medication(medicationName, medicationDesc, null, null);
                medications.add(newMedication);

                reply.setContent(Messages.MESSAGE_SUCCESS);
                send(reply);

            }
        }

    }

    private class medicationsListServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.PAST_MEDICATIONS_LIST_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                System.out.println("PATIENT: Past medications' list request received");
                String content = "";
                // geting a message from portal and iterate over the doctors to get the three
                // fields
                ACLMessage replyMedicationsList = msg.createReply();
                replyMedicationsList.setPerformative(Messages.PAST_MEDICATIONS_LIST_RESPONSE);
                System.out.println(medications);

                for (Medication medication : medications) {
                    content = content.concat(medication.getName());
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(medication.getDescription());
                    content = content.concat(Messages.DELIMITER);
                }
                System.out.println("PATIENT: Sending past medications' list back to portal");

                replyMedicationsList.setContent(content);
                send(replyMedicationsList);
            }
        }

    }
}
