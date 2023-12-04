
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

public class InsuranceAgent extends Agent {

    Hashtable<Insurance, String> insurances;


    protected void setup() {
        // Register pharmacy service so portal agent can search and find
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("insurance");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // fill the testlist list with some hard-coded data
        insurances = InsuranceUtils.generateInsurances();


        addBehaviour(new InsuranceAgent.requestInsuranceList());


    }

    public ArrayList getUserInsurances(String userEmail) {
        ArrayList<Insurance> foundinsurances = new ArrayList<Insurance>();
      
        for(Map.Entry entry: insurances.entrySet()){
            if(userEmail.equals(entry.getValue())){
                foundinsurances.add((Insurance) entry.getKey()); //no break, looping entire hashtable
            }
        }
        System.out.println("keys : " );

        return foundinsurances;
    }


    private class requestInsuranceList extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.INSURANCE_LIST_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                    
                    String info = msg.getContent();
                    String[] newInfo = info.split(Messages.DELIMITER);
                    String content = "";

                    System.out.println("INSURANCE AGENT: tests list request received");
                    //geting a message from portal and iterate over the doctors to get the three fields
                    ACLMessage replyTestList = msg.createReply();
                    replyTestList.setPerformative(Messages.INSURANCE_LIST_RESPONSE);

                    ArrayList<Insurance> foundinsurances = new ArrayList<Insurance>();
                    System.out.println("INFOOOOO" + newInfo[0]);
                    foundinsurances = getUserInsurances(newInfo[0]);
                    
                    System.out.println(foundinsurances);
                    for (Insurance key : foundinsurances)
                    {
                        content = content.concat(key.getprice().toString());
                        content = content.concat(Messages.DELIMITER);
                        content = content.concat(key.getcompanyName());
                        content = content.concat(Messages.DELIMITER);
                    }
                    System.out.println("INSURANCE: Sending test list back to portal");

                    replyTestList.setContent(content);
                    send(replyTestList);
            } else {
                block();
            }
        }
    }


}
