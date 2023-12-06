import java.util.ArrayList;

public class Patient extends Person {

    //each patient is a person with password and medications
    private String password;
    private ArrayList<Medication> medications;

    public Patient(String name, String email, String phonenum, String password,
            ArrayList<Medication> medications) {
        super(name, email, phonenum);
        this.password = password;
        this.medications = medications;
    }

    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    public void setMedications(ArrayList<Medication> medications) {
        this.medications = medications;
    }

    public String getPassword() {
        return password;
    }

}
