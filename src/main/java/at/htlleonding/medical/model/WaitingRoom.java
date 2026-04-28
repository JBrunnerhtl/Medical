package at.htlleonding.medical.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class WaitingRoom implements Serializable {
    private PriorityQueue<Patient> patients = new PriorityQueue<>();
    private transient List<ChangeObserver<WaitingRoom>> observers = new ArrayList<>();
    private Patient treatment = null;



    public int getPatientCount() {
        return this.patients.size();
    }
    public Patient getPatientInPreparation() {
        return this.patients.peek();
    }

    public Patient getPatientUndergoingTreatment() {
        return treatment;
    }

    public void addObserver(ChangeObserver<WaitingRoom> observer) {
        this.observers.add(observer);
    }

    public void removeObserver(ChangeObserver<WaitingRoom> observer) {
        this.observers.remove(observer);
    }

    public void addPatient(String name, LocalDateTime appointment, boolean isEmergency) {
        this.patients.add(new Patient(name, appointment, isEmergency));
        List<Patient> newList = new ArrayList<>(this.patients);
        Collections.sort(newList);
        this.patients.clear();
        this.patients = new PriorityQueue<>(newList);
        this.notifyAllObservers();
    }

    public void treatNextPatient() {
        this.treatment = this.patients.poll();
        this.notifyAllObservers();
    }

    public void notifyAllObservers() {
        this.observers.forEach(o -> o.update(this));
    }

    public void initTransientFields() {
        this.observers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.patients.stream().map(Patient::toString).collect(Collectors.joining("\n"));
    }
}
