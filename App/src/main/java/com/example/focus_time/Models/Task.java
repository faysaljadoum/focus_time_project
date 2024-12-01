package com.example.focus_time.Models;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class Task {
    private int id;
    private StringProperty nom;
    private StringProperty description;
    private BooleanProperty statut;  // Use BooleanProperty for 'statut'
    private int objectifId;

    // Constructor
    public Task(int id, String nom, String description, boolean statut, int objectifId) {
        this.id = id;
        this.nom = new SimpleStringProperty(nom);
        this.description = new SimpleStringProperty(description);
        this.statut = new SimpleBooleanProperty(statut);
        this.objectifId = objectifId;
    }

    // Getter Methods
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public boolean isStatut() {
        return statut.get();
    }

    public BooleanProperty statutProperty() {
        return statut;
    }

    public int getObjectifId() {
        return objectifId;
    }

    // Setter Methods
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setStatut(boolean statut) {
        this.statut.set(statut);
    }

    public void setObjectifId(int objectifId) {
        this.objectifId = objectifId;
    }

}