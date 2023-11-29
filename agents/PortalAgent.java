import FIPA.DateTime;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PortalAgent extends Agent{
    AID patientAgent;

      protected void setup() {

        System.out.println("Hello world! This is portal agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        // a one shot behavior to get all services' AIDs
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                PatientAgent = Utils.searchForService(myAgent, "patient");
                HealthCareProviderAgent = Utils.searchForService(myAgent, "healthcareprovider");
                InsuranceAgent = Utils.searchForService(myAgent, "insurace");
                LaboratoryAgent = Utils.searchForService(myAgent, "laboratory");
                PharmacyAgent = Utils.searchForService(myAgent, "pharmacy");
                
            }
        });

  


    }
}