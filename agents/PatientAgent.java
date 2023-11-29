package agents;
import jade.core.Agent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;

import utils.Messages;
import utils.PatientUtils;




public class PatientAgent extends Agent{

    private Hashtable<String,User> patientinfos;

    protected void setup(){

        patientinfos = new Hashtable<String,User>();

        patientinfos = Utils.generatePatients();

        // the welcome message
        System.out.println("Hi! Patient Agent " + getAID().getName() + " is started.");

        // Register Patient Agent so Portal Agent can have access to it
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
    }

        public void updateUsers(User u){
        patients.put(u.getEmail(), u);
    }

    public boolean confirmUser(String username, String password){

        User registeredUser = patients.get(username);
        boolean isAuthenticated = false;

        if (registeredUser != null)
            isAuthenticated = Cryptographer.decrypt(u.getPassword()).equals(Cryptographer.decrypt(password));
        return isAuthenticated;
    }

    protected void takeDown() {
        System.out.println("Patient Agent " + getAID().getName() + " terminating.");
    }

    private class userRegServer extends CyclicBehaviour{

        public void action(){
            MessageTemplate messagetemplate = MessageTemplate.MatchPerformative(Utils.REGISTER_REQUEST);
            ACLMessage msg = myAgent.receive(messagetemplate);
            if (msg!=null){
                String info = msg.getContent();
                String[] newInfo = info.split(Utils.DELIMITER);
                User newUser = new User(newInfo[0], newInfo[1], newInfo[2], newInfo[3]);

                System.out.println("ACCESS: Received a register request for user: " + newUser.getEmail());

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
                System.out.println("ACCESS: Sending registration confirmation back to portal");
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
            MessageTemplate mt=MessageTemplate.MatchPerformative(Utils.AUTH_REQUEST);
            ACLMessage msg=myAgent.receive(mt);
            if (msg!=null){
                String info = msg.getContent();
                String[] newInfo = info.split(Utils.DELIMITER);

                System.out.println("ACCESS: Received an authentication request for user: " + newInfo[0]);

                boolean flag = authUser(newInfo[0], newInfo[1]);
                ACLMessage reply = msg.createReply();
                reply.setPerformative(Utils.AUTH_RESPONSE);
                String content = "";
                if (flag){
                    String name = patients.get(newInfo[0]).getName();
                    content = Utils.MESSAGE_SUCCESS;
                    content = content.concat(Utils.DELIMITER);
                    content = content.concat(name);
                }
                else
                    content = Utils.MESSAGE_FAILURE;

                System.out.println("ACCESS: Sending authentication confirmation back to portal");
                reply.setContent(content);
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }

}


