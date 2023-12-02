
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.*;
import java.util.*;
import java.util.ArrayList;

public class HealthCareProviderAgent extends Agent {
    Hashtable <String, Doctor> doctors;
    protected void setup() {

        // Register doctor service so portal agent can search and find
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("healthcareprovider");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // fill the doctors list with some hard-coded data
        doctors = DoctorsUtils.generateDoctors();

        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg;
                msg = myAgent.receive();
                if (msg != null) {
                    String content = "";
                    switch (msg.getPerformative()){
                        case Messages.DOCTORS_LISTS_REQUEST:
                            System.out.println("HEALTHCARE_PROVIDER: doctors' list request received");
                            //geting a message from portal and iterate over the doctors to get the three fields
                            ACLMessage replyDoctorsList = msg.createReply();
                            replyDoctorsList.setPerformative(Messages.DOCTORS_LISTS_RESPONSE);
                            System.out.println(doctors);
                            Set<String> setOfKeys = doctors.keySet();
                            System.out.println(setOfKeys);
                            for (String key : setOfKeys)
                            {
                                content = content.concat(doctors.get(key).getName());
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(doctors.get(key).getSpeciality());
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(doctors.get(key).getEmail());
                                content = content.concat(Messages.DELIMITER);
                            }
                            System.out.println("HEALTHCARE_PROVIDER: Sending doctors' list back to portal");

                            replyDoctorsList.setContent(content);
                            System.out.println(content);
                            send(replyDoctorsList);
                            break;
                        case Messages.AVAILABILITY_REQUEST:
                            System.out.println("HEALTHCARE_PROVIDER: chosen doctor's availability request received");
                            //get a message from the portal and parse the message and iterate over their times
                            String[] payloadLst = msg.getContent().split("-");
                            String selectedDoctorEmail = payloadLst[0];
                            Doctor selectedDoctor = doctors.get(selectedDoctorEmail);

                            ACLMessage replyAvailability = msg.createReply();
                            replyAvailability.setPerformative(Messages.AVAILABILITY_RESPONSE);
                            if (selectedDoctor != null) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                for (int i = 0; i < selectedDoctor.getAvailability().size(); i++){
                                    if (!selectedDoctor.getAvailability().get(i).getReserved()) {
                                        content = content.concat(selectedDoctor.getAvailability().get(i).getStartingDateTime().format(formatter));
                                        content = content.concat("-");
                                    }
                                }
                            }
                            else {
                                content = Messages.MESSAGE_FAILURE;
                            }

                            System.out.println("THERAPIST: Sending selected doctor's availability back to portal");

                            replyAvailability.setContent(content);
                            send(replyAvailability);
                            break;
                    }
                }
            }
        });

    }
}
