package pkg24h;

import java.util.regex.Pattern;

/**
 * Classe de définition des cartes
 *
 * @author Adrien Jussak
 */
public class Carte {

    private TypeCarte type;
    private String nom;
    private int valeur;

    /**
     * Constructeur d'une carte simple
     *
     * @param type Type de la carte
     */
    public Carte(TypeCarte type) {
        this.type = type;
    }

    /**
     * Constructeur d'une carte complète (Raisin)
     *
     * @param type   Type de la carte
     * @param nom    Nom de la carte
     * @param valeur Valeur de la carte
     */
    public Carte(TypeCarte type, String nom, int valeur) {
        this.type = type;
        this.nom = nom;
        this.valeur = valeur;
    }

    /**
     * Obtenir le type de la carte
     *
     * @return Type de la carte
     */
    public TypeCarte getType() {
        return type;
    }

    /**
     * Obtenir le nom de la carte
     *
     * @return Nom de la carte
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obetenir la valeur de la carte
     *
     * @return Valeur de la carte
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Générer une liste de carte à partir d'un message du serveur
     *
     * @param message Message du serveur
     * @return List des cartes générées.
     */
    public static GroupeCarte parseCartes(String message) {
        String[] messageData = message.split(Pattern.quote("|"));
        GroupeCarte groupeCarte = new GroupeCarte();
        for (int i = 0; i < messageData.length - 1; i++) {
            String[] carteData = messageData[i + 1].split(Pattern.quote(";"));

            TypeCarte type = TypeCarte.valueOf(carteData[0]);

            if (carteData.length > 1) {
                String nom = carteData[1];
                int valeur = Integer.parseInt(carteData[2]);
                groupeCarte.add(new Carte(type, nom, valeur));
            } else
                groupeCarte.add(new Carte(type));
        }
        return groupeCarte;
    }
}
