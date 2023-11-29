package agents;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Set;

import org.jcp.xml.dsig.internal.dom.Utils;

import entities.Doctor;
import jade.core.Agent;
import utils.Messages;
import utils.DoctorsUtils;

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
                            Set<String> setOfKeys = doctors.keySet();
                            for (String key : setOfKeys)
                            {
                                content = content.concat(doctors.get(key).getName());
                                content = content.concat("-");
                                content = content.concat(doctors.get(key).getSpeciality());
                                content = content.concat("-");
                                content = content.concat(doctors.get(key).getEmail());
                                content = content.concat("-");
                            }
                            System.out.println("HEALTHCARE_PROVIDER: Sending doctors' list back to portal");

                            replyDoctorsList.setContent(content);
                            send(replyDoctorsList);
                            break;
                    }
                }
            }
        });

    }
}
