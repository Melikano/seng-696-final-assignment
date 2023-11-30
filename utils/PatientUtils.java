package utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;

import entities.Cryptography;
import entities.Medication;
import entities.Patient;
import entities.Medication;


public class PatientUtils {

    public static Hashtable<String, User> generatePatients() {

        Hashtable<String, User> patients = new Hashtable<>();

        Medication medicationObj1 = new Medication("Acetaminophen","Acetaminophen is used to treat mild to moderate pain, 
        moderate to severe pain in conjunction with opiates, or to reduce fever.", 10 , 10 );

        Medication medicationObj2 = new Medication("Dupixent","Dupixent is used in adults and children 6 months and older 
        to treat moderate-to-severe eczema (atopic dermatitis)", 10 , 10 );

        ArrayList<Medication> medications1 = new ArrayList<Medication>();
        ArrayList<Medication> medications2 = new ArrayList<Medication>();
        ArrayList<Medication> medications3 = new ArrayList<Medication>();


        medications1.add(medicationObj1);
        medications1.add(medicationObj2);

        medications2.add(medicationObj2);

        medications3.add(medicationObj1);


        User userObj1 = new User("Saina","saina.d@gmail.com","5875789886", Cryptographer.encrypt("saina"), medications1);
        User userObj2 = new User("Melika","melika.n@gmail.com","5875786666", Cryptographer.encrypt("melika"),medications2);
        User userObj3 = new User("Robin","robin.c@gmail.com","5875788888", Cryptographer.encrypt("robin"),medications1);
        User userObj4 = new User("Sanaz","sanaz.d@gmail.com","5875789656", Cryptographer.encrypt("sanaz"), medications3);

        patients.put(userObj1.getEmail(),userObj1);
        patients.put(userObj2.getEmail(),userObj2);
        patients.put(userObj3.getEmail(),userObj3);
        patients.put(userObj4.getEmail(),userObj4);

        return patients;
    }

}