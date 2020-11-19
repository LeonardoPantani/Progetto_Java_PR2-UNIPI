package app;

import exception.EmptyFieldException;
import exception.PostNotFoundException;
import exception.TextLengthException;

import java.util.Map;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public interface SafeSocialNetworkInterface {
    /**
     * Segnala un post (parametro del metodo).
     * @param post il post da segnalare
     * @param reason il motivo di segnalazione del post
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     */
    void reportPost(Post post, ReportReason reason) throws PostNotFoundException;

    /**
     * Segnala un post con la segnalazione personalizzata (parametro del metodo).
     * @param post il post da segnalare
     * @param reason il motivo di segnalazione del post (deve essere per forza "custom" con questo metodo)
     * @param text il motivo personalizzato
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     */
    void reportPost(Post post, ReportReason reason, String text) throws PostNotFoundException, TextLengthException;

    /**
     * Restituisce il numero di report assegnati ad un post (parametro del metodo)
     * @param post il post di cui vedere i report
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     */
    int getReports(Post post) throws PostNotFoundException;

    /**
     * Restituisce la lista completa delle segnalazioni, dove:
     *  Post è il post segnalato
     *  Integer è il numero di segnalazioni di quel post
     */
    Map<Post, Integer> getMostReportedPosts() throws EmptyFieldException, PostNotFoundException;

    /**
     * Restituisce la lista completa delle segnalazioni, dove:
     *  Post è il post segnalato
     *  Integer è il numero di segnalazioni di quel post
     *  @param minReports numero di reports minimi per poter apparire nella lista restituita
     */
    Map<Post, Integer> getMostReportedPosts(int minReports) throws EmptyFieldException, PostNotFoundException;

    /**
     * Elimina tutti i post con almeno 1 segnalazione
     * @return il numero di post rimossi
     */
    int removeReportedPosts() throws PostNotFoundException;

    /**
     * Elimina tutti i post con almeno minReports segnalazioni
     * @param minReports il numero di segnalazioni minime perché il post sia cancellato
     * @return il numero di post rimossi
     */
    int removeReportedPosts(int minReports) throws PostNotFoundException;
}
