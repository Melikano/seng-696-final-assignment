package entities;

import java.util.ArrayList;

public class Patient extends Person {

    private ArrayList<Medication> medications;

    public Patient(String name, String email, String phonenum,
            ArrayList<Medication> medications) {
        super(name, email, phonenum);
        this.medications = medications;
    }


    public ArrayList<Calendar> getMedications() {
        return this.medications;
    }

    public void setMedications(ArrayList<Medication> medications) {
        this.medications = medications;
    }

}
