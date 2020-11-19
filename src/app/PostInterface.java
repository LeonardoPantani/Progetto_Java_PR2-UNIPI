package app;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public interface PostInterface {
    /**
     * Getter dell'id
     * @return id del post
     */
    int getId();

    /**
     * Getter dell'autore
     * @return autore del post
     */
    String getAuthor();

    /**
     * Getter del testo
     * @return testo del post
     */
    String getText();

    /**
     * Getter del timestamp
     * @return timestamp del post
     */
    long getTimestamp();

    /**
     * Restituisce una stringa per mostrare il post
     * @return post formato visualizzabile
     */
    String toString();
}
