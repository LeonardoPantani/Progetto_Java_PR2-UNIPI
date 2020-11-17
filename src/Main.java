import app.Post;
import app.SafeSocialNetwork;
import exception.EmptyFieldException;
import exception.TestException;
import exception.TextLengthException;

import java.lang.invoke.WrongMethodTypeException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("__________________________");
        System.out.println("_______ MICRO BLOG _______");
        System.out.println("__________________________");

        SafeSocialNetwork sn = new SafeSocialNetwork(); // inizializzazione social network (con metodi di segnalazione)
        try {
            executeTest(sn);
            System.out.println("[☑] Test completati con successo!");
        } catch(TestException e) {
            System.out.println("[!] Errore durante il test: " + e.getMessage());
            System.exit(-2);
        }
    }

    private static void executeTest(SafeSocialNetwork sn) throws Exception  {
        // creo gli utenti...
        Set<String> fp = new HashSet<>(); fp.add("leo"); fp.add("federico"); fp.add("lorenzo");
        sn.initializeUser("gemma", fp); // gemma segue: leo, federico e lorenzo

        sn.initializeUser("leo", Collections.singleton("gemma")); // leopantani segue: gemma
        sn.initializeUser("federico", Collections.singleton("gemma")); // federico segue: gemma
        sn.initializeUser("lorenzo", Collections.singleton("gemma")); // lorenzo segue: gemma
        sn.initializeUser("andrea", null); // andrea NON SEGUE NESSUNO
        sn.initializeUser("marco", null); // marco NON SEGUE NESSUNO

        /*
            l'utente "gemma" è seguita da 2 persone (leopantani e federico)
            l'utente "leo" è seguito da 1 persona (gemma)
            l'utente "federico" è seguito da 1 persona (gemma)
            l'utente "lorenzo" è seguito da 1 persona (gemma)
            l'utente "andrea" è seguito da 0 persone
            l'utente "marco" è seguito da 0 persone
         */


        System.out.println("===== INIZIO FASE DI TEST =====");
        System.out.println("\n### TEST DEI POST (1/3) ###");
        // --------------------------
        // TEST DEI POST
        // --------------------------
        try {
            Post post1 = new Post("gemma", null);
        } catch(NullPointerException e) {
            System.out.println("TEST CREAZIONE POST TEXT NULL> Eccezione catturata ☑");
        }

        try {
            Post post1 = new Post("gemma", "");
        } catch(EmptyFieldException e) {
            System.out.println("TEST CREAZIONE POST TEXT BLANK> Eccezione catturata ☑");
        }

        try {
            Post post1 = new Post("gemma", "CiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiaoCiao");
        } catch(TextLengthException e) {
            System.out.println("TEST CREAZIONE POST TEXT LONG> Eccezione catturata ☑");
        }

        Post post1 = new Post("gemma", "Ehilà @leo, come stai?"); // post creato | menzione a leo

        try {
            Post post2 = new Post(null);
        } catch(NullPointerException e) {
            System.out.println("TEST CREAZIONE POST NULL> Eccezione catturata ☑");
        }

        Post post2 = new Post("leo", "Bene @gemma!"); // post creato | 1 menzione a gemma
        Post post3 = new Post("federico", "Oibò"); // post creato | 0 menzioni


        System.out.println("\n### TEST SOCIAL NETWORK (2/3) ###");
        // --------------------------
        // TEST SOCIAL NETWORK
        // --------------------------
        sn.addPost(post1);
        sn.addPost(post2);
        sn.addPost(post3);

        List<Post> lista_post = new ArrayList<>();
        lista_post.add(post1);
        lista_post.add(post2);
        lista_post.add(post3);

        System.out.println("TEST GUESSFOLLOWERS>");
        for(Map.Entry<String, Set<String>> entry : sn.guessFollowers(lista_post).entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }

        System.out.println("TEST INFLUENCERS>");
        for(String entry : sn.influencers()) {
            System.out.println(entry);
        }

        System.out.println("TEST GETMENTIONEDUSERS 1>");
        for(String entry : sn.getMentionedUsers()) {
            System.out.println(entry);
        }

        lista_post.remove(post2);
        System.out.println("TEST GETMENTIONEDUSERS 2>");
        for(String entry : sn.getMentionedUsers(lista_post)) {
            System.out.println(entry);
        }
        lista_post.add(post2);

        Post post4 = new Post("leo", "Che bel tempo che c'è oggi!!");
        sn.addPost(post4);
        lista_post.add(post4);
        System.out.println("TEST WRITTEBY 1>");
        for(Post entry : sn.writtenBy("leo")) {
            System.out.println(entry.toString());
        }
        lista_post.remove(post4);


        System.out.println("TEST WRITTEBY 2>");
        for(Post entry : sn.writtenBy(lista_post, "leo")) {
            System.out.println(entry.toString());
        }

        List<String> lista_termini = new ArrayList<>();
        lista_termini.add("come");
        lista_termini.add("Oibò");
        System.out.println("TEST CONTAINING>");
        for(Post entry : sn.containing(lista_termini)) {
            System.out.println(entry.toString());
        }


        System.out.println("\n### TEST SAFE SOCIAL NETWORK (3/3) ###");
        // --------------------------
        // TEST SAFE SOCIAL NETWORK
        // --------------------------
        Post post5 = new Post("andrea", "ECCO UN MODO PER FARE SOLDI FACILI: https://bit.ly/36EGvFI");
        sn.addPost(post5);

        try {
            sn.reportPost(null, "spam_misleading_content");
        } catch(NullPointerException e) {
            System.out.println("TEST REPORTPOST NORMAL NULL> Eccezione catturata ☑");
        }

        try {
            sn.reportPost(post5, "custom");
        } catch(WrongMethodTypeException e) {
            System.out.println("TEST REPORTPOST NORMAL CUSTOM ERRATO> Eccezione catturata ☑");
        }

        sn.reportPost(post5, "spam_misleading_content"); // REPORT 1
        System.out.println("TEST REPORTPOST NORMAL> ☑");

        try {
            sn.reportPost(null, "custom", "Mi ha fregato!");
        } catch(NullPointerException e) {
            System.out.println("TEST REPORTPOST NORMAL NULL> Eccezione catturata ☑");
        }

        try {
            sn.reportPost(post5, "spam_misleading_content", "Mi ha fregato!");
        } catch(WrongMethodTypeException e) {
            System.out.println("TEST REPORTPOST WRONG CUSTOM ERRATO> Eccezione catturata ☑");
        }

        sn.reportPost(post5, "custom", "Mi ha fregato!"); // REPORT 2

        if(sn.getReports(post5) == 2) {
            System.out.println("TEST GETREPORTS> ☑");
        } else {
            throw new TestException("TEST GETREPORTS> ERRORE: "+ sn.getReports(post5) + " invece di 5!");
        }

        System.out.println("TEST MOSTREPORTEDPOSTS 1>");
        for(Map.Entry<Post, Integer> entry: sn.getMostReportedPosts().entrySet()) {
            System.out.println(entry.getKey()+ " (" + entry.getValue() + " segnalazioni/e)");
        }

        sn.reportPost(post2, "hateful_content"); // REPORT 3
        System.out.println("TEST MOSTREPORTEDPOSTS 2>");
        for(Map.Entry<Post, Integer> entry: sn.getMostReportedPosts(2).entrySet()) {
            System.out.println(entry.getKey()+ " (" + entry.getValue() + " segnalazioni/e)");
        }

        int post_rimossi = sn.removeReportedPosts(2);
        if(post_rimossi == 1) {
            System.out.println("TEST REMOVEREPORTEDPOSTS 2> ☑");
        } else {
            throw new TestException("TEST REMOVEREPORTEDPOSTS 2> ERRORE: " + post_rimossi + " invece di 1!");
        }

        sn.reportPost(post3, "violent_content"); // REPORT 4
        post_rimossi = sn.removeReportedPosts();
        if(post_rimossi == 2) {
            System.out.println("TEST REMOVEREPORTEDPOSTS 1> ☑");
        } else {
            throw new TestException("TEST REMOVEREPORTEDPOSTS 1> ERRORE: " + post_rimossi + " invece di 2!");
        }
        System.out.println("===== FINE FASE DI TEST =====");
    }
}