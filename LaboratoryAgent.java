
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

public class LaboratoryAgent extends Agent {

    Hashtable<LabTest, String> tests;


    protected void setup() {
        // Register pharmacy service so portal agent can search and find
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("laboratory");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // fill the testlist list with some hard-coded data
        tests = TestUtils.generateTests();


        addBehaviour(new LaboratoryAgent.requestTest());


    }

    public ArrayList getUserTests(String userEmail) {
        ArrayList<LabTest> foundtests = new ArrayList<LabTest>();
      
        for(Map.Entry entry: tests.entrySet()){
            if(userEmail.equals(entry.getValue())){
                foundtests.add((LabTest) entry.getKey()); //no break, looping entire hashtable
            }
        }
        System.out.println("keys : " );

        return foundtests;
    }


    private class requestTest extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(Messages.TEST_LIST_REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                    
                    String info = msg.getContent();
                    String[] newInfo = info.split(Messages.DELIMITER);
                    String content = "";

                    System.out.println("LABORATORY AGENT: tests list request received");
                    //geting a message from portal and iterate over the doctors to get the three fields
                    ACLMessage replyTestList = msg.createReply();
                    replyTestList.setPerformative(Messages.TEST_LIST_RESPONSE);

                    ArrayList<LabTest> foundtests = new ArrayList<LabTest>();
                    System.out.println("INFOOOOO" + newInfo[0]);
                    foundtests = getUserTests(newInfo[0]);
                    
                    System.out.println(foundtests);
                    for (LabTest key : foundtests)
                    {
                        content = content.concat(key.gettestId().toString());
                        content = content.concat(Messages.DELIMITER);
                        content = content.concat(key.getDescription());
                        content = content.concat(Messages.DELIMITER);
                    }
                    System.out.println("LABORATORY: Sending test list back to portal");

                    replyTestList.setContent(content);
                    send(replyTestList);
            } else {
                block();
            }
        }
    }


}
