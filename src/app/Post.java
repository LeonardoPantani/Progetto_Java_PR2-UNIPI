package app;

import exception.EmptyFieldException;
import exception.TextLengthException;

public final class Post implements PostInterface {
    /*
     *  OVERVIEW:
     *      Rappresentazione di un post che ogni utente può inviare con una limitazione
     *      sui caratteri che possono essere inviati (massimo 140). Questa classe
     *      include, oltre che hai metodi classici quali costruttore getter & setter, anche
     *      un metodo toString per convertire il post in formato visualizzabile e un metodo clone.
     *      L'oggetto Post è immutabile (non modificabile dall'esterno)
     *
     *  ABSTRACTION FUNCTION:
     *      <id, author, text, timestamp> dove:
     *          id (int) -> è l'indentificatore univoco del post
     *          author (string) -> è il nome dell'autore del post
     *          text (string) -> è il testo del post (max. 140 caratteri)
     *          timestamp (timestamp) -> è la data e ora di invio del post
     *
     *  REPRESENTATION INVARIANT:
     *      id > 0 &&
     *      author != "" &&
     *      text != "" && len(text) < 140 &&
     *      int(timestamp) && timestamp > 0 && timestamp < 2^31
     */
    private static int ids = 0;

    private final int id;
    private final String author;
    private final String text;
    private final long timestamp;

    // lunghezza massima del testo dei Post
    public final int MAX_TEXT_LENGTH = 140;

    /**
     * Costruttore della classe Post.
     * @param author autore del post
     * @param text contenuto testo del post
     * @throws EmptyFieldException se l'autore o contenuto sono stringhe vuote
     * @throws NullPointerException se l'autore o contenuto sono nulli
     * @throws TextLengthException se la lunghezza del testo text supera i 140 caratteri (costante MAX_TEXT_LENGTH)
     */
    public Post(String author, String text) throws NullPointerException, EmptyFieldException, TextLengthException {
        if(author == null || text == null)
            throw new NullPointerException();

        if(author.isBlank()|| text.isBlank()) {
            throw new EmptyFieldException();
        }

        if(text.length() > MAX_TEXT_LENGTH) {
            throw new TextLengthException(MAX_TEXT_LENGTH);
        }

        this.id = ids;
        ids++;
        this.author = author;
        this.text = text;
        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    /**
     * Costruttore di copia della classe Post.
     * @param post oggetto post da copiare
     */
    public Post(Post post) {
        if(post == null) {
            throw new NullPointerException();
        }

        this.id = post.id;
        this.author = post.author;
        this.text = post.text;
        this.timestamp = post.timestamp;
    }


    /**
     * Getter dell'id
     * @return id del post
     */
    public int getId() {
        return id;
    }

    /**
     * Getter dell'autore
     * @return autore del post
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter del testo
     * @return testo del post
     */
    public String getText() {
        return text;
    }

    /**
     * Getter del timestamp
     * @return timestamp del post
     */
    public long getTimestamp() {
        return timestamp;
    }


    /**
     * Restituisce una stringa per mostrare il post
     * @return post formato visualizzabile
     */
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Restituisce una copia del post attuale
     * @return copia del post
     */
    public Post clone() {
        return new Post(this);
    }
}
