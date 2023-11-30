import java.util.ArrayList;

public class Patient extends Person {

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
