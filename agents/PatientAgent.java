package agents;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcp.xml.dsig.internal.dom.Utils;

import entities.Appointment;
import jade.core.Agent;
import utils.DoctorsUtils;
import utils.Messages;

public class PatientAgent extends Agent {

    ArrayList<Appointment> appointments;
    private Hashtable<String,User> patients;


    protected void setup() {
        appointments = new ArrayList<Appointment>();
        patients = new Hashtable<String,User>();

        patients = Utils.generatePatients();

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

        addBehaviour(new PatientAgent.appointmentServer());    
 

    }


    public void updateUsers(User u){
        patients.put(u.getEmail(), u);
    }

    public boolean loginUser(String username, String password){

        User user = patients.get(username);
        boolean isAuthenticated = false;

        if (user != null)
            isAuthenticated = Cryptographer.decrypt(u.getPassword()).equals(Cryptographer.decrypt(password));
        return isAuthenticated;
    }

    protected void takeDown() {
        System.out.println("Patient-agent "+getAID().getName()+" terminating.");
    }

    private class userRegServer extends CyclicBehaviour{
        public void action(){
            MessageTemplate mt=MessageTemplate.MatchPerformative(Utils.REGISTER_REQUEST);
            ACLMessage msg=myAgent.receive(mt);
            if (msg!=null){
                String info = msg.getContent();
                String[] newInfo = info.split(Utils.DELIMITER);
                User newUser = new User(newInfo[0], newInfo[1], newInfo[2], newInfo[3]);

                System.out.println("Patient: Received a register request for user: " + newUser.getEmail());

                ACLMessage reply = msg.createReply();
                reply.setPerformative(Utils.REGISTER_RESPONSE);
                String content = "";

                if (patients.containsKey(newUser.getEmail())) {
                    content = Utils.MESSAGE_FAILURE;
                }
                else {
                    updateUsers(newUser);
                    content = Utils.MESSAGE_SUCCESS;
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
            MessageTemplate mt=MessageTemplate.MatchPerformative(Messages.LOGIN_REQUEST);
            ACLMessage msg=myAgent.receive(mt);
            if (msg!=null){
                String info = msg.getContent();
                String[] newInfo = info.split(Utils.DELIMITER);

                System.out.println("Patient: Received an login request for user: " + newInfo[0]);

                boolean flag = loginUser(newInfo[0], newInfo[1]);
                ACLMessage reply = msg.createReply();
                reply.setPerformative(Utils.LOGIN_RESPONSE);
                String content = "";
                if (flag){
                    String name = patients.get(newInfo[0]).getName();
                    content = Utils.MESSAGE_SUCCESS;
                    content = content.concat(Utils.DELIMITER);
                    content = content.concat(name);
                }
                else
                    content = Utils.MESSAGE_FAILURE;

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

