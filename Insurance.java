public class Insurance {

    private String personEmail;
    private Integer price;
    private String companyName;


    public Insurance(String personEmail,Integer price, String companyName) {
        this.personEmail = personEmail;
        this.price = price;
        this.companyName = companyName;
    }


    public String getEmail() {
        return this.personEmail;
    }


    public Integer getprice() {
        return this.price;
    }


    public String getcompanyName() {
        return this.companyName;
    }


}
