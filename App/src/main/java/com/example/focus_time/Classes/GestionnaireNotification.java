package com.example.focus_time.Classes;

import java.util.List;

// Classe pour gérer les notifications
class GestionnaireNotification {
    private List<String> messagesNotifs; // Liste des messages de notification

    // Méthodes pour envoyer des notifications
    public void envoyerNotification(String msg) { /* Envoie une notification */ }
    public void programmerNotification(DateTime date, String msg) { /* Programme une notification */ }
    public void envoyerNotificationDebutObjectif(ObjectifProductivite objectif) {
        /* Envoie une notification pour le début d'un objectif */
    }
    public void envoyerNotificationFinObjectif(ObjectifProductivite objectif) {
        /* Envoie une notification pour la fin d'un objectif */
    }
}