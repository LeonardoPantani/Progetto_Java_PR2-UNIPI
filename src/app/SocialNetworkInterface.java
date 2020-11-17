package app;

import exception.EmptyFieldException;
import exception.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public interface SocialNetworkInterface {
    /**
     * Restituisce la rete sociale derivata dalla lista di post (parametro del metodo)
     * @param ps lista dei post
     * @return rete utente-lista follower in base agli autori dei post
     */
    Map<String, Set<String>> guessFollowers(List<Post> ps) throws UserNotFoundException;

    /**
     * Restituisce gli utenti più influenti delle rete sociale,
     * ovvero quelli che hanno un numero maggiore di “follower”
     * @return lista degli utenti con maggior numero di follower
     */
    List<String> influencers() throws EmptyFieldException;

    /**
     * Restituisce l’insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
     * @return lista degli utenti menzionati
     */
    Set<String> getMentionedUsers();

    /**
     * Restituisce l’insieme degli utenti menzionati (inclusi) nella lista di post
     * @param ps lista dei post
     * @return lista degli utenti menzionati nei post dentro la lista "ps"
     */
    Set<String> getMentionedUsers(List<Post> ps);

    /**
     * Restituisce la lista dei post effettuati dall’utente nella rete sociale
     * il cui nome è dato dal parametro username
     * @param username nome utente
     * @return lista di tutti i post dell'utente "username"
     */
    List<Post> writtenBy(String username);

    /**
     * Restituisce la lista dei post effettuati dall’utente il cui nome è dato
     * dal parametro username presenti nella lista ps
     * @param ps lista dei post
     * @param username nome utente
     * @return lista dei post scritti da username dentro la lista "ps"
     */
    List<Post> writtenBy(List<Post> ps, String username);

    /**
     * Restituisce la lista dei post presenti nella rete sociale che includono almeno una
     * delle parole presenti nella lista delle parole argomento del metodo
     * @param words lista di stringhe (parole)
     * @return lista dei post che contengono almeno una delle parole in "words"
     */
    List<Post> containing(List<String> words);
}
