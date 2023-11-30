
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;


public class DoctorsUtils {
        public final static Integer HOURLY_WAGE = 60; // In CAD for example


        public static Hashtable<String, Doctor> generateDoctors() {
                // a sample doctor list is hard coded and returned in this func
                Hashtable<String, Doctor> doctors = new Hashtable<String, Doctor>();

                Calendar calendar1 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);
                Calendar calendar2 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 15, 11, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);
                Calendar calendar3 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 16, 14, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);
                Calendar calendar4 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 16, 16, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);
                Calendar calendar5 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 17, 18, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);
                Calendar calendar6 = new Calendar(LocalDateTime.of(2022, Month.DECEMBER, 17, 19, 0),
                                LocalDateTime.of(2022, Month.DECEMBER, 15, 10, 0), false);

                ArrayList<Calendar> timing = new ArrayList<Calendar>();

                timing.add(calendar1);
                timing.add(calendar2);
                timing.add(calendar3);
                timing.add(calendar4);
                timing.add(calendar5);
                timing.add(calendar6);

                Doctor doctor1 = new Doctor("Siamak Honardar", "siamak.honardar@gmail.com", "1234567", "Family Physician", timing);

                doctors.put(doctor1.getEmail(), doctor1);

                return doctors;
        }
}