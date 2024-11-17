package com.example.focus_time.Classes;

import java.util.Map;

// Classe pour gérer le suivi du temps d'utilisation des applications
class SuiviTemps {
    private Map<Application, Durée> utilisationApplications; // Temps d'utilisation par application
    // private DateTime heureDebut; // Heure de début du suivi
    // private DateTime heureFin; // Heure de fin du suivi

    // Méthodes pour gérer le suivi
    public void démarrerSuivi(Application app) { /* Commence à suivre une application */ }
    public void arrêterSuivi(Application app) { /* Arrête le suivi d'une application */ }
    public Durée calculerUtilisation(Application app) { /* Calcule le temps total d'utilisation */ }
}
