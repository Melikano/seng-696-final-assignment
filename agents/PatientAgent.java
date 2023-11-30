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

    protected void setup() {
        appointments = new ArrayList<Appointment>();
        // Register appointment service so portal agent can search and find
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
        addBehaviour(new CyclicBehaviour() {
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
        });
    }
}