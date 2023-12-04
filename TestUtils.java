import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;



public class TestUtils {

    public static Hashtable<LabTest, String> generateTests() {

        Hashtable<LabTest, String> tests = new Hashtable<>();



        LabTest testObj1 = new LabTest("saina.d@gmail.com",1111,  "You are well");
        LabTest testObj2 = new LabTest("melika.n@gmail.com",1112, "You have to check your blood pressure");
        LabTest testObj3 = new LabTest("robin.c@gmail.com",1113, "Calcium is high");
        LabTest testObj4 = new LabTest("sanaz.d@gmail.com",1114, "You are not bad");
        LabTest testObj5 = new LabTest("saina.d@gmail.com",1115,  "You have cancer");


        tests.put(testObj1, testObj1.getEmail());
        tests.put(testObj2, testObj2.getEmail());
        tests.put(testObj3, testObj2.getEmail());
        tests.put(testObj4, testObj4.getEmail());

        return tests;
    }

}