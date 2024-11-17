package com.example.focus_time.Classes;

import java.util.List;

// Classe pour gérer les préférences des utilisateurs
class PréférencesUtilisateur {
    private Map<Application, Durée> limitesTemps; // Limites de temps par application
    private boolean notificationsPause; // État des notifications de pause
    private List<ObjectifProductivite> objectifsProductivite; // Liste des objectifs de productivité

    // Méthodes pour configurer les préférences
    public void définirLimiteTemps(Application app, Durée durée) { /* Définit une limite de temps */ }
    public void activerNotificationsPause(boolean activer) { /* Active ou désactive les notifications */ }
    public void définirObjectifProductivite(ObjectifProductivite objectif) { /* Définit un objectif */ }
    public void changerMotDePasse(String nouveauMotDePasse) { /* Change le mot de passe */ }
}
