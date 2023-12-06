
import java.util.ArrayList;

//doctor is a oerson but has availability times and speciality
public class Doctor extends Person {

    private String speciality;
    private ArrayList<Calendar> availability;

    public Doctor(String name, String email, String phonenum, String speciality,
            ArrayList<Calendar> availability) {
        super(name, email, phonenum);
        this.speciality = speciality;
        this.availability = availability;
    }

    public String getSpeciality() {
        return this.speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public ArrayList<Calendar> getAvailability() {
        return this.availability;
    }

    public void setAvailability(ArrayList<Calendar> availability) {
        this.availability = availability;
    }

}
