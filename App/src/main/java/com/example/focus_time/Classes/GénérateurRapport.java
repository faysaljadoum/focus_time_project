package com.example.focus_time.Classes;


// Classe pour gérer la génération des rapports
class GénérateurRapport {
    // Méthodes pour générer différents types de rapports
    public Rapport générerRapportQuotidien() { /* Génère un rapport quotidien */ }

    public Rapport générerRapportHebdomadaire() { /* Génère un rapport hebdomadaire */ }

    public Rapport générerRapportMensuel() { /* Génère un rapport mensuel */ }

    // Méthode pour exporter un rapport dans un format spécifique
    public void exporterRapport(Rapport rapport, String format) { /* Exporte le rapport */ }
}
