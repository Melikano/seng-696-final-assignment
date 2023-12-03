import java.util.Hashtable;

public class MedicationUtils {
    public static Hashtable<String, Medication> generateMedication() {
        // a sample medication list is hard coded and returned in this func
        Hashtable<String, Medication> medications = new Hashtable<String, Medication>();

        Medication medic1 = new Medication("Sertraline",
                "an antidepressant of the selective serotonin reuptake inhibitor class",
                (float) 35.0,
                8);

        medications.put(medic1.getName(), medic1);

        return medications;
    }

}
