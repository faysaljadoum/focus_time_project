package com.example.focus_time.Classes;

// Classe pour gérer les applications et leurs états
class GestionnaireApplication {
    private List<Application> applications; // Liste des applications
    private List<Application> applicationsBloquées; // Liste des applications bloquées

    // Méthodes pour gérer les applications
    public void ajouterApplication(Application app) { /* Ajoute une application */ }
    public void supprimerApplication(Application app) { /* Supprime une application */ }
    public void lancerApplication(Application app) { /* Lance une application */ }
    public void arreterApplication(Application app) { /* Arrête une application */ }
    public void bloquerApplication(Application app) { /* Bloque une application */ }
    public void débloquerApplication(Application app) { /* Débloque une application */ }
    public boolean estBloquée(Application app) { /* Vérifie si une application est bloquée */ }
}
