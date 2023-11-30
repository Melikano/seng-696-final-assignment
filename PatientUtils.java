import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;



public class PatientUtils {

    public static Hashtable<String, Patient> generatePatients() {

        Hashtable<String, Patient> patients = new Hashtable<>();

        Medication medicationObj1 = new Medication("Acetaminophen","Acetaminophen", (float) 10.00 , 10 );

        Medication medicationObj2 = new Medication("Dupixent","Dupixent", (float) 10.00 , 10 );

        ArrayList<Medication> medications1 = new ArrayList<Medication>();
        ArrayList<Medication> medications2 = new ArrayList<Medication>();
        ArrayList<Medication> medications3 = new ArrayList<Medication>();


        medications1.add(medicationObj1);
        medications1.add(medicationObj2);

        medications2.add(medicationObj2);

        medications3.add(medicationObj1);


        Patient userObj1 = new Patient("Saina","saina.d@gmail.com","5875789886", Cryptography.encrypt("saina"), medications1);
        Patient userObj2 = new Patient("Melika","melika.n@gmail.com","5875786666", Cryptography.encrypt("melika"),medications2);
        Patient userObj3 = new Patient("Robin","robin.c@gmail.com","5875788888", Cryptography.encrypt("robin"),medications1);
        Patient userObj4 = new Patient("Sanaz","sanaz.d@gmail.com","5875789656", Cryptography.encrypt("sanaz"), medications3);

        patients.put(userObj1.getEmail(),userObj1);
        patients.put(userObj2.getEmail(),userObj2);
        patients.put(userObj3.getEmail(),userObj3);
        patients.put(userObj4.getEmail(),userObj4);

        return patients;
    }

}