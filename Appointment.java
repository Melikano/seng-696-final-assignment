import java.time.LocalDateTime;

public class Appointment {
    private Integer appointmentID;
    private String patientEmail;
    private String doctorEmail;
    private LocalDateTime dateTime;
    private Integer amount; // price

    public Appointment(Integer appointmentID, String patientEmail, String doctorEmail, LocalDateTime dateTime,
                       Integer amount, Boolean paid) {
        this.appointmentID = appointmentID;
        this.patientEmail = patientEmail;
        this.doctorEmail = doctorEmail;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setSoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
