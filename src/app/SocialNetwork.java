package app;

import exception.EmptyFieldException;
import exception.UserNotFoundException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static java.util.Collections.reverseOrder;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public class SocialNetwork implements SocialNetworkInterface {
    /*
     *  OVERVIEW:
     *      Rappresentazione in piccola scala di un Social Network basico
     *      che implementa la struttura dati Post. Per tenere traccia degli utenti
     *      e dei loro interessi si fa uso di una Map, mentre per immagazzinare
     *      i post inviati da questi utenti si usa una semplice Lista. Si è scelto
     *      di implementare dei metodi non specificati dalla consegna per facilitare
     *      i test da effettuare per il corretto funzionamento del dato. Ogni utente,
     *      come specificato, è identificato univocamente dall'username.
     *
     *  ABSTRACTION FUNCTION:
     *      Map<String, Set<String>> dove:
     *          String -> username
     *          Set<String> -> contiene la lista di username che sono seguiti da username
     *
     *      List<Post> dove:
     *          Post -> struttura dati post
     *
     *  REPRESENTATION INVARIANT:
     *      followed != null &&
     *      ps != null &&
     *      String != ""
     */

    protected final Map<String, Set<String>> followed;
    protected final List<Post> ps;

    private final String PREFIX_CHECK_MENTION = "@";
    private final String REGEX_CHECK_MENTION = "\\B"+PREFIX_CHECK_MENTION+"(?!(?:[a-z0-9.]*_){2})(?!(?:[a-z0-9_]*\\.){2})[._a-z0-9]{3,24}\\b";



    /**
     * Costruttore della classe SocialNetwork.
     */
    public SocialNetwork() {
        followed = new HashMap<>();
        ps = new ArrayList<>();
    }


    /**
     * Restituisce la rete sociale derivata dalla lista di post (parametro del metodo)
     * @param ps lista dei post
     * @return rete utente-lista follower in base agli autori dei post
     */
    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws UserNotFoundException {
        Map<String, Set<String>> rs = new HashMap<>();

        for(Post p : ps) {
            rs.put(p.getAuthor(), getFollowers(p.getAuthor()));
        }

        return rs;
    }

    /**
     * Restituisce gli utenti più influenti delle rete sociale,
     * ovvero quelli che hanno un numero maggiore di “follower”
     * @return lista degli utenti con maggior numero di follower
     */
    public List<String> influencers() throws EmptyFieldException {
        List<String> lista = new ArrayList<>();
        Map<String, Integer> u_f = new LinkedHashMap<>();

        if(!ps.isEmpty()) {
            for(Map.Entry<String, Set<String>> entry : followed.entrySet()) {
                if(entry.getValue() != null) {
                    for(String s : entry.getValue()) {
                        if(!u_f.containsKey(s)) {
                            u_f.put(s, 1);
                        } else {
                            u_f.put(s, u_f.get(s)+1);
                        }
                    }
                }
            }

            u_f = u_f
                    .entrySet()
                    .stream()
                    .sorted(reverseOrder(comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            for(Map.Entry<String, Integer> entry : u_f.entrySet()) {
                lista.add(entry.getKey());
            }

            return lista;
        } else {
            throw new EmptyFieldException();
        }
    }

    /**
     * Restituisce l’insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
     * @return lista degli utenti menzionati
     */
    public Set<String> getMentionedUsers() {
        // minimo 3 caratteri - massimo 24 caratteri
        Pattern pattern = Pattern.compile(REGEX_CHECK_MENTION, Pattern.CASE_INSENSITIVE);
        Matcher matcher;

        Set<String> utenti = new HashSet<>();
        for(Post p : ps) {
            matcher = pattern.matcher(p.getText());
            while(matcher.find()) {
                utenti.add(matcher.group().replace(PREFIX_CHECK_MENTION, "") + " [nel post di " + p.getAuthor() + "]");
            }
        }

        return utenti;
    }

    /**
     * Restituisce l’insieme degli utenti menzionati (inclusi) nella lista di post
     * @param ps lista dei post
     * @return lista degli utenti menzionati nei post dentro la lista "ps"
     */
    public Set<String> getMentionedUsers(List<Post> ps) {
        // minimo 3 caratteri - massimo 24 caratteri
        Pattern pattern = Pattern.compile(REGEX_CHECK_MENTION, Pattern.CASE_INSENSITIVE);
        Matcher matcher;

        Set<String> utenti = new HashSet<>();
        for(Post p : this.ps) {
            matcher = pattern.matcher(p.getText());
            while(matcher.find()) {
                utenti.add(matcher.group().replace(PREFIX_CHECK_MENTION, "") + " [nel post di " + p.getAuthor() + "]");
            }
        }

        return utenti;
    }

    /**
     * Restituisce la lista dei post effettuati dall’utente nella rete sociale
     * il cui nome è dato dal parametro username
     * @param username nome utente
     * @return lista di tutti i post dell'utente "username"
     */
    public List<Post> writtenBy(String username) {
        List<Post> lista = new ArrayList<>();
        for(Post p : ps) {
            if(p.getAuthor().equals(username)) {
                lista.add(p);
            }
        }

        return lista;
    }

    /**
     * Restituisce la lista dei post effettuati dall’utente il cui nome è dato
     * dal parametro username presenti nella lista ps
     * @param ps lista dei post
     * @param username nome utente
     * @return lista dei post scritti da username dentro la lista "ps"
     */
    public List<Post> writtenBy(List<Post> ps, String username) {
        List<Post> lista = new ArrayList<>();
        for(Post p : ps) {
            if(p.getAuthor().equals(username)) {
                lista.add(p);
            }
        }

        return lista;
    }

    /**
     * Restituisce la lista dei post presenti nella rete sociale che includono almeno una
     * delle parole presenti nella lista delle parole argomento del metodo
     * @param words lista di stringhe (parole)
     * @return lista dei post che contengono almeno una delle parole in "words"
     */
    public List<Post> containing(List<String> words) {
        List<Post> lista = new ArrayList<>();
        for(Post p : ps) {
            for(String s : words) {
                if(p.getText().toLowerCase().contains(s.toLowerCase())) {
                    lista.add(p);
                    break;
                }
            }
        }

        return lista;
    }


    // ------------------------------
    // METODI PRIVATI/PROTETTI
    // ------------------------------

    /**
     * Verifica che l'username esista nella mappa degli utenti
     * @param username l'username da controllare
     * @return true se l'utente compare nella mappa, false altrimenti
     */
    protected boolean checkUser(String username) {
        return followed.containsKey(username);
    }

    /**
     * Verifica che il post esista nella lista dei post
     * @param post il post da controllare
     * @return true se il post compare nella lista, false altrimenti
     */
    protected boolean checkPost(Post post) {
        return ps.contains(post);
    }

    /**
     * Verifica che il post esista nella lista dei post
     * @param idpost l'id del post da controllare
     * @return true se il post compare nella lista, false altrimenti
     */
    protected boolean checkPost(int idpost) {
        for(Post p : ps) {
            if(p.getId() == idpost) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce i nomi dei follower di username
     * @param username l'username di cui vedere i follower
     * @return set di stringhe contenente i nomi dei follower di username
     * @throws UserNotFoundException se l'utente specificato non esiste
     */
    private Set<String> getFollowers(String username) throws UserNotFoundException {
        Set<String> followers = new HashSet<>();

        if(checkUser(username)) {
            for(Map.Entry<String, Set<String>> entry : followed.entrySet()) {
                if(entry.getValue() != null) {
                    for(String s : entry.getValue()) {
                        if(s.equals(username)) {
                            followers.add(entry.getKey());
                            break;
                        }
                    }
                }
            }
        } else {
            throw new UserNotFoundException();
        }

        return followers;
    }

    /**
     * Restituisce i nomi degli utenti seguiti da username
     * @param username l'utente di cui vedere i seguiti
     * @return set di stringhe contenente i nomi degli utenti seguiti da username
     * @throws UserNotFoundException se l'utente specificato non esiste
     */
    private Set<String> getFollowed(String username) throws UserNotFoundException {
        if(checkUser(username)) {
            return followed.get(username);
        } else {
            throw new UserNotFoundException();
        }
    }


    // ------------------------------
    // METODI PER LA BATTERIA DI TEST
    // ------------------------------

    /**
     * Aggiunge un utente che username segue alla lista
     * @param username l'utente a cui viene aggiunto l'username da seguire
     * @param follower l'utente seguito da username
     * @throws UserNotFoundException se il parametro username non esiste
     */
    public void addFollower(String username, String follower) throws UserNotFoundException {
        if(checkUser(username)) {
            if(followed.get(username) == null) {
                Set<String> set = new HashSet<>();
                set.add(follower);
                followed.put(username, set);
            } else {
                Set<String> followers = followed.get(username);
                followers.add(follower);
                followed.put(username, followers);
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Rimuove un utente che username segue alla lista
     * @param username l'utente a cui viene rimosso l'username seguito
     * @param follower l'utente seguito da username
     * @throws UserNotFoundException se il parametro username non esiste
     */
    public void removeFollower(String username, String follower) throws UserNotFoundException {
        if(checkUser(username)) {
            if(followed.get(username) != null) {
                Set<String> followers = followed.get(username);
                followers.remove(follower);
                followed.put(username, followers);
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Inizializza un utente
     * @param username il nome dell'utente da creare
     * @param set il set di persone che username segue
     */
    public void initializeUser(String username, Set<String> set) {
        followed.put(username, set);
    }

    /**
     * Aggiunge un post alla lista.
     * @param p il post da aggiungere
     */
    public void addPost(Post p) {
        ps.add(p);
    }

    /**
     * Elimina un post dalla lista.
     * @param p il post da rimuovere
     */
    public void deletePost(Post p) {
        ps.remove(p);
    }

    /**
     * Stampa a video tutti i post nella lista.
     */
    public void printAllPosts() {
        for(Post p : ps) {
            System.out.println(p.toString() + "\n");
        }
    }

    /**
     * Stampa a video tutti gli utenti nella rete sociale.
     */
    public void printAllUsers() {
        for(Map.Entry<String, Set<String>> entry : followed.entrySet()) {
            if(entry.getValue() != null)
                System.out.println(entry.getKey() + " ha " + entry.getValue().size() + " followers");
            else
                System.out.println(entry.getKey() + " ha 0 followers");
        }
    }
}
