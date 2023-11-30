package entities;

public class Medication {

    private String name;
    private String description;
    private Float price;
    private Integer availability;

    public Medication(String name, String description, Float price, Integer availability) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }


    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public void getAvailability() {
        return availability;
    }


    public void setPrice (Float price) {
        this.price = price;
    }

    public void getPrice() {
        return price;
    }

}
