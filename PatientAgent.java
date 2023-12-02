
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
import java.util.ArrayList;

public class PatientAgent extends Agent {

    ArrayList<Appointment> appointments;
    private Hashtable<String,Patient> patients;


    protected void setup() {
        appointments = new ArrayList<Appointment>();
        patients = new Hashtable<String,Patient>();

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
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        
       addBehaviour(new PatientAgent.userRegServer());

        addBehaviour(new PatientAgent.userLoginServer());   

        //addBehaviour(new PatientAgent.appointmentServer());    
 

    }


    public void updateUsers(Patient patient){
        patients.put(patient.getEmail(), patient);
    }

    public boolean loginUser(String username, String password){

        Patient patient = patients.get(username);
        boolean isAuthenticated = false;

        if (patient != null)
            isAuthenticated = Cryptography.decrypt(patient.getPassword()).equals(Cryptography.decrypt(password));
        return isAuthenticated;
    }

    protected void takeDown() {
        System.out.println("Patient-agent "+getAID().getName()+" terminating.");
    }

    private class userRegServer extends CyclicBehaviour{
        public void action(){
            MessageTemplate mt=MessageTemplate.MatchPerformative(Messages.REGISTER_REQUEST);
            ACLMessage msg=myAgent.receive(mt);
            System.out.println("Patient: Received a register request for user: ");

            if (msg!=null){
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
                }
                else {
                    updateUsers(newPatient);
                    content = Messages.MESSAGE_SUCCESS;
                }
                System.out.println("Patient: Sending registration confirmation back to portal");
                reply.setContent(content);
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }
    private class userLoginServer extends CyclicBehaviour{
        @Override
        public void action() {
            System.out.println("Patient: Received a login request for user: ");
            MessageTemplate mt=MessageTemplate.MatchPerformative(Messages.LOGIN_REQUEST);
            ACLMessage msg=myAgent.receive(mt);
            if (msg!=null){
                String info = msg.getContent();
                String[] newInfo = info.split(Messages.DELIMITER);

                System.out.println("Patient: Received an login request for user: " + newInfo[0]);

                boolean flag = loginUser(newInfo[0], newInfo[1]);
                ACLMessage reply = msg.createReply();
                reply.setPerformative(Messages.LOGIN_RESPONSE);
                String content = "";
                if (flag){
                    String name = patients.get(newInfo[0]).getName();
                    content = Messages.MESSAGE_SUCCESS;
                    content = content.concat(Messages.DELIMITER);
                    content = content.concat(name);
                }
                else
                    content = Messages.MESSAGE_FAILURE;

                System.out.println("Patient: Sending login confirmation back to portal");
                reply.setContent(content);
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }


    private class appointmentServer extends CyclicBehaviour{
        @Override
        public void action() {
            ACLMessage msg;
            msg = myAgent.receive();

            if (msg != null) {
                String[] payloadLst = msg.getContent().split(Messages.DELIMITER);
                switch (msg.getPerformative()){
                    case Messages.CREATE_APPOINTMENT_REQUEST:
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(Messages.CREATE_APPOINTMENT_RESPONSE);
                        String content = "";

                        String[] contentLst = msg.getContent().split(Messages.DELIMITER);
                        String dateTimeStr = contentLst[0];
                        String patientEmail = contentLst[1];
                        String doctorEmail = contentLst[2];

                        System.out.println("PATIENT: appointment creation received for " + patientEmail +
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

                        if (alreadyExists){
                            content = Messages.MESSAGE_FAILURE;
                        }
                        else {
                            Integer newAppointmentID = appointments.size();
                            Appointment newAppointment = new Appointment(newAppointmentID, patientEmail,
                                    doctorEmail, dateTime, DoctorsUtils.HOURLY_WAGE, Boolean.FALSE);
                            appointments.add(newAppointment);
                            content =  Messages.MESSAGE_SUCCESS;
                            content = content.concat(Messages.DELIMITER);
                            content = content.concat(newAppointmentID.toString());
                            content = content.concat(Messages.DELIMITER);
                            content = content.concat(DoctorsUtils.HOURLY_WAGE.toString());
                        }

                        reply.setContent(content);
                        send(reply);
                        break;

                    default:
                        break;
                }
            }
        }
     }
}
