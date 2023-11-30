package agents;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcp.xml.dsig.internal.dom.Utils;

import entities.Appointment;
import jade.core.Agent;

                            if (alreadyExists){
                                content = Messages.MESSAGE_FAILURE;
                            }
                            else {
                                Integer newAppointmentID = appointments.size();
                                Appointment newAppointment = new Appointment(newAppointmentID, patientEmail,
                                        doctorEmail, dateTime, DoctorsUtils.HOURLY_WAGE, Boolean.FALSE);
                                appointments.add(newAppointment);
                                content =  Messages.MESSAGE_SUCCESS;
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(newAppointmentID.toString());
                                content = content.concat(Messages.DELIMITER);
                                content = content.concat(DoctorsUtils.HOURLY_WAGE.toString());
                            }

    
}


