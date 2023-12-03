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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public int getAvailability() {
        return availability;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

}
