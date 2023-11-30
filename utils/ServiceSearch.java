package utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class ServiceSearch {
    public static AID searchForService(Agent myAgent, String serviceName) {
        AID targetService = new AID();
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceName);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            System.out.println("Found the following agent(s):");
            for (DFAgentDescription dfAgentDescription : result) {
                targetService = dfAgentDescription.getName();
                System.out.println(targetService.getName());
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return targetService;
    }
}
