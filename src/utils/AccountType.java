package utils;

/**
 * Une énumération contenant les différents types de compte possibles
 */
public enum AccountType {
    /**
     * Type de compte : Utilisateur
     * Valeur numérique : {@code 1}
     */
    USER,

    /**
     * Type de compte : Laboratoire
     * Valeur numérique : {@code 2}
     */
    LAB,

    /**
     * Type de compte : IA
     * Valeur numérique : {@code 3}
     */
    AI;

    /**
     * Fonction à utiliser pour récupérer la valeur du type de compte
     * @return la valeur associée au type de compte
     */
    public int getValue() {
        return ordinal() + 1;
    }
}
