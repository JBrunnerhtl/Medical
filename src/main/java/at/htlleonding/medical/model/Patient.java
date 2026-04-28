package at.htlleonding.medical.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Patient implements Comparable<Patient>, Serializable {
    private static final String EMERGENCY_TAG = "EMERGENCY";
    private final String name;
    private final LocalDateTime appointment;
    private final boolean isEmergency;

    public Patient(String name, LocalDateTime appointment, boolean isEmergency) {
        this.name = name;
        this.appointment = appointment;
        this.isEmergency = isEmergency;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getAppointment() {
        return appointment;
    }

    public boolean getIsEmergency() {
        return isEmergency;
    }

    @Override
    public String toString() {
        String output = "";
        if(isEmergency) {
            output+=EMERGENCY_TAG;
        }else {
            var formatter = DateTimeFormatter.ofPattern("dd.M.yy H:mm");
            output+=formatter.format(this.getAppointment());
        }
        output+=" " + this.getName();
        return output;
    }

    @Override
    public int compareTo(Patient o) {
        if(this.getIsEmergency() && !o.getIsEmergency()) return -1;
        if(!this.getIsEmergency() && o.getIsEmergency()) return 1;
        return this.getAppointment().compareTo(o.getAppointment());
    }
}
