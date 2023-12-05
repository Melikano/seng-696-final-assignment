import java.util.Hashtable;

public class MedicationUtils {
    public static Hashtable<String, Medication> generateMedication() {
        // a sample medication list is hard coded and returned in this func
        Hashtable<String, Medication> medications = new Hashtable<String, Medication>();

        Medication medic1 = new Medication("Sertraline",
                "an antidepressant of the selective serotonin reuptake inhibitor class",
                (float) 35.0,
                8);

        
        Medication medic2 = new Medication("Acetaminophen","pain reliever and fever reducer", (float) 10.00 , 10 );

        Medication medic3 = new Medication("Dupixent","help prevent asthma attacks", (float) 20.00 , 10 );

        Medication medic4 = new Medication("Ibuprofen","Reduces inflammation, pain, and fever", (float) 40.00 , 20 );

        Medication medic5 = new Medication("Aspirin","Reduces pain, inflammation, and fever", (float) 30.00 , 45 );

        Medication medic6 = new Medication("Lisinopril","Treats high blood pressure", (float) 22.00 , 10 );

        Medication medic7 = new Medication("Atorvastatin","Lowers cholesterol levels", (float) 10.50 , 15 );

        medications.put(medic1.getName(), medic1);
        medications.put(medic2.getName(), medic2);
        medications.put(medic3.getName(), medic3);
        medications.put(medic4.getName(), medic4);
        medications.put(medic5.getName(), medic5);
        medications.put(medic6.getName(), medic6);
        medications.put(medic7.getName(), medic7);



        return medications;
    }

}
