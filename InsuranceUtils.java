import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;


//hardcoded insurance data
public class InsuranceUtils {

    public static Hashtable<Insurance, String> generateInsurances() {

        Hashtable<Insurance, String> Insurances = new Hashtable<>();


        Insurance InsuranceObj1 = new Insurance("saina.d@gmail.com",100,  "Insurance1");
        Insurance InsuranceObj2 = new Insurance("melika.n@gmail.com",200, "Insurance2");
        Insurance InsuranceObj3 = new Insurance("robin.c@gmail.com",300, "Insurance2");
        Insurance InsuranceObj4 = new Insurance("sanaz.d@gmail.com",400, "Insurance3");
        Insurance InsuranceObj5 = new Insurance("saina.d@gmail.com",500,  "Insurance3");


        Insurances.put(InsuranceObj1, InsuranceObj1.getEmail());
        Insurances.put(InsuranceObj2, InsuranceObj2.getEmail());
        Insurances.put(InsuranceObj3, InsuranceObj3.getEmail());
        Insurances.put(InsuranceObj4, InsuranceObj4.getEmail());
        Insurances.put(InsuranceObj5, InsuranceObj5.getEmail());


        return Insurances;
    }

}