package com.example.focus_time.Classes;

import java.util.List;

// Classe pour définir des objectifs de productivité
class ObjectifProductivite {
    private String nomObjectif; // Nom de l'objectif
    private String description; // Description de l'objectif
    private DateTime dateDebut; // Date de début de l'objectif
    private DateTime dateFin; // Date de fin de l'objectif
    private Application appAssociee; // Application associée à l'objectif
    private List<Tache> taches; // Liste des tâches associées

    // Méthode pour vérifier l'état de l'objectif
    public boolean verifierObjectif() { /* Vérifie si l'objectif est atteint */ }
}