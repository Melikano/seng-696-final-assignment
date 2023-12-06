
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

public class PharmacyAgent extends Agent {
    Hashtable<String, Medication> medications;

    protected void setup() {
        // Register pharmacy service so portal agent can search and find
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("pharmacy");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // fill the medication list with some hard-coded data
        medications = MedicationUtils.generateMedication();

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg;
                msg = myAgent.receive();
                if (msg != null) {
                    String content = "";
                    switch (msg.getPerformative()) {
                        case Messages.MEDICATION_LIST_REQUEST:
                            System.out.println("PHARMACY: medications' list request received");
                            // geting a message from portal and iterate over the medications to get the
                            // three fields
                            ACLMessage replyMedicationsList = msg.createReply();
                            replyMedicationsList.setPerformative(Messages.MEDICATION_LIST_RESPONSE);
                            System.out.println(medications);
                            Set<String> setOfKeys = medications.keySet();
                            System.out.println(setOfKeys);
                            for (String key : setOfKeys) {
                                content = content.concat(medications.get(key).getName());
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(medications.get(key).getDescription());
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(String.valueOf(medications.get(key).getPrice()));
                                content = content.concat(Messages.DELIMITER);
                            }
                            System.out.println("PHARMACY: Sending medications' list back to portal");

                            replyMedicationsList.setContent(content);
                            send(replyMedicationsList);
                            break;
                        case Messages.ORDER_MEDICATION_REQUEST:
                            System.out.println("PHARMACY: order medication request received");
                            String[] payloadLst = msg.getContent().split(Messages.DELIMITER);
                            String medicationName = payloadLst[0];
                            String medicationDesc = payloadLst[1];
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(Messages.ORDER_MEDICATION_RESPONSE);
                            String status = medications.get(medicationName).getAvailability() > 0
                                    ? Messages.MESSAGE_SUCCESS
                                    : Messages.MESSAGE_FAILURE;
                            reply.setContent(status
                                    + Messages.DELIMITER + medicationName + Messages.DELIMITER + medicationDesc);
                            send(reply);

                    }
                }
            }
        });

    }
}
