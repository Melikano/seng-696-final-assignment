public class LabTest {

    private String personEmail;
    private Integer testId;
    private String description;


    public LabTest(String personEmail,Integer testId, String description) {
        this.personEmail = personEmail;
        this.testId = testId;
        this.description = description;
    }


    public String getEmail() {
        return this.personEmail;
    }


    public Integer gettestId() {
        return this.testId;
    }


    public String getDescription() {
        return this.description;
    }


}
