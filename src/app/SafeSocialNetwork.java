package app;

import exception.EmptyFieldException;
import exception.PostNotFoundException;
import exception.TextLengthException;

import java.lang.invoke.WrongMethodTypeException;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public class SafeSocialNetwork extends SocialNetwork implements SafeSocialNetworkInterface {
    /*
     *  OVERVIEW:
     *      Estensione della classe SocialNetwork che implementa un sistema di report.
     *      Segnalando un post, quest'ultimo viene aggiunto ad una lista di post segnalati,
     *      i quali possono essere visualizzati singolarmente e vedere quante segnalazioni
     *      hanno ricevuto. Si può vedere facilmente quante segnalazioni ha ricevuto un post.
     *      E' implementato un metodo che mostra i post con più segnalazioni e uno che mostra solo quelli
     *      con almeno tot segnalazioni ricevute.
     *      Si può specificare di cancellare automaticamente tutti i post
     *      che hanno una determinata quantità di segnalazioni, oppure rimuovere tutti quelli
     *      che ne possiedono almeno una.
     *
     *  ABSTRACTION FUNCTION:
     *      Set<Report> rp dove:
     *          Report -> è un oggetto di tipo "Report"
     *
     *  REPRESENTATION INVARIANT:
     *      rp != null &&
     *      for each(report) in rp : report != null && unique
     */

    private final Set<Report> rp;

    public SafeSocialNetwork() {
        super();
        rp = new HashSet<>();
    }

    /**
     * Segnala un post (parametro del metodo).
     * @param post il post da segnalare
     * @param reason il motivo di segnalazione del post
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     * @modifies this.rp
     * @effects if !not_found({post}, safesocialnetwork(this.rp)) then
     *      safesocialnetwork(this.rp) = pre(safesocialnetwork(this.rp)) U {post}
     *  else
     *      safesocialnetwork(this.rp[{post}]) = pre(safesocialnetwork(this.rp[{post}].add(reason)))
     */
    public void reportPost(Post post, ReportReason reason) throws PostNotFoundException {
        if(post == null) {
            throw new NullPointerException();
        } else {
            if(checkPost(post)) {
                Report segnalazione = new Report(post.getId());
                if(reason == ReportReason.custom) {
                    throw new WrongMethodTypeException("Usare il metodo reportPost specificando anche il testo della segnalazione custom!");
                } else {
                    segnalazione.setReport(reason);
                    rp.add(segnalazione);
                }
            } else {
                throw new PostNotFoundException();
            }
        }
    }

    /**
     * Segnala un post con la segnalazione personalizzata (parametro del metodo).
     * @param post il post da segnalare
     * @param reason il motivo di segnalazione del post (deve essere per forza "custom" con questo metodo
     * @param text il motivo personalizzato
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     * @modifies this.rp
     * @effects if !not_found({post}, safesocialnetwork(this.rp)) then
     *      safesocialnetwork(this.rp) = pre(safesocialnetwork(this.rp)) U {post}
     *  else
     *      safesocialnetwork(this.rp[{post}]) = pre(safesocialnetwork(this.rp[{post}].add(reason)))
     */
    public void reportPost(Post post, ReportReason reason, String text) throws PostNotFoundException, TextLengthException {
        if(post == null) {
            throw new NullPointerException();
        } else {
            if (checkPost(post)) {
                Report segnalazione = new Report(post.getId());
                if (reason == ReportReason.custom) {
                    segnalazione.setCustomReport(text);
                    rp.add(segnalazione);
                } else {
                    throw new WrongMethodTypeException("Usare il metodo reportPost per creare una segnalazione custom!");
                }
            } else {
                throw new PostNotFoundException();
            }
        }
    }

    /**
     * Restituisce il numero di report assegnati ad un post (parametro del metodo)
     * @param post il post di cui vedere i report
     * @throws PostNotFoundException se il post specificato non è nella lista dei post nel social network
     */
    public int getReports(Post post) throws PostNotFoundException {
        int somma = 0;

        if(checkPost(post)) {
            for(Report report : rp) {
                if(report.getPostID() == post.getId()) {
                    somma += report.getReportNumber();
                }
            }
            return somma;
        } else {
            throw new PostNotFoundException();
        }
    }

    /**
     * Restituisce la lista completa delle segnalazioni, dove:
     *  Post è il post segnalato
     *  Integer è il numero di segnalazioni di quel post
     */
    public Map<Post, Integer> getMostReportedPosts() throws EmptyFieldException, PostNotFoundException {
        if(rp.isEmpty()) {
            throw new EmptyFieldException();
        } else {
            Map<Post, Integer> mappa = new LinkedHashMap<>();

            for(Post p : ps) {
                mappa.put(p, getReports(p));
            }

            return mappa.entrySet()
                    .stream()
                    .sorted(comparingByValue(Comparator.reverseOrder()))
                    .collect(toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> null, // or throw an exception
                            LinkedHashMap::new));
        }
    }

    /**
     * Restituisce la lista completa delle segnalazioni, dove:
     *  Post è il post segnalato
     *  Integer è il numero di segnalazioni di quel post
     * @param minReports numero di reports minimi per poter apparire nella lista restituita
     */
    public Map<Post, Integer> getMostReportedPosts(int minReports) throws EmptyFieldException, PostNotFoundException {
        if(rp.isEmpty()) {
            throw new EmptyFieldException();
        } else {
            int reports;
            Map<Post, Integer> mappa = new LinkedHashMap<>();

            for(Post p : ps) {
                reports = getReports(p);
                if(reports >= minReports)
                    mappa.put(p, reports);
            }

            return mappa.entrySet()
                    .stream()
                    .sorted(comparingByValue(Comparator.reverseOrder()))
                    .collect(toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> null, // or throw an exception
                            LinkedHashMap::new));
        }
    }

    /**
     * Elimina tutti i post con almeno 1 segnalazione
     * @return il numero di post rimossi
     * @modifies this.rp
     * @effects if this.rp[i].getReports() > 1 then
     *      this.rp = pre(this.rp) \ {this.rp[i]}
     */
    public int removeReportedPosts() throws PostNotFoundException {
        int removeCount = 0;

        for(Post p : new ArrayList<>(ps)) {
            if(getReports(p) > 0) {
                ps.remove(p);
                if(deleteReport(p.getId()) == 0) { // errore imprevisto
                    throw new NoSuchFieldError();
                }
                removeCount++;
            }
        }

        return removeCount;
    }

    /**
     * Elimina tutti i post con almeno minReports segnalazioni
     * @param minReports il numero di segnalazioni minime perché il post sia cancellato
     * @return il numero di post rimossi
     * @modifies this.rp
     * @effects if this.rp[i].getReports() > minReports then
     *      this.rp = pre(this.rp) \ {this.rp[i]}
     */
    public int removeReportedPosts(int minReports) throws PostNotFoundException {
        int removeCount = 0;

        for(Post p : new ArrayList<>(ps)) {
            if(getReports(p) >= minReports) {
                ps.remove(p);
                if(deleteReport(p.getId()) == 0) { // errore imprevisto
                    throw new NoSuchFieldError();
                }
                removeCount++;
            }
        }

        return removeCount;
    }

    /**
     * Elimina i report assegnati al post con id post_id dalla lista
     * @param post_id l'id del post da cui cancellare i report
     * @return la quantità di elementi rimossi
     * @modifies this.rp
     * @effects if this.rp.getID() == post_id then
     *      this.rp = pre(this.rp) \ this.rp[post_id]
     */
    public int deleteReport(int post_id) {
        int removeCount = 0;

        for(Report r : new ArrayList<>(rp)) {
            if(r.getPostID() == post_id) {
                rp.remove(r);
                removeCount++;
            }
        }

        return removeCount;
    }
}
