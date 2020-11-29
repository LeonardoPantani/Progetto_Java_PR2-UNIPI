package app;

import exception.EmptyFieldException;
import exception.TextLengthException;

import java.lang.invoke.WrongMethodTypeException;
import java.util.HashMap;
import java.util.Map;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public class Report implements ReportInterface {
    /*
     *  OVERVIEW:
     *      Rappresentazione di un report (segnalazione) che un utente può inviare su un post. Ogni singola
     *      segnalazione può riguardare un post su più aspetti, per esempio se questo contiene testo offensivo o spam.
     *      Nell'assenza di un motivo specifico si può inviare una segnalazione con un testo personalizzato, che non può
     *      tuttavia eccedere i 70 caratteri. Questa classe include, oltre che ai metodi classici quali costruttore
     *      getter & setter, anche un metodo toString per convertire il post in formato visualizzabile e un metodo clone.
     *      Sono presenti anche altri metodi per facilitare le operazioni da svolgere su questo oggetto.
     *
     *  ABSTRACTION FUNCTION:
     *     <post_id, reports, custom_report> dove:
     *          post_id (int) -> l'id del post segnalato
     *          reports (Map<String, Integer>) -> map che contiene l'insieme di categorie della segnalazione
     *          custom_report (String) -> testo che contiene un motivo personalizzato della segnalazione
     *
     *  REPRESENTATION INVARIANT:
     *      post_id > 0 &&
     *      reports != null &&
     *      if reports.Entry<"custom", 1> == true then
     *              custom_report != null && custom_report.length() <= MAX_CUSTOM_TEXT_LENGTH &&
     */

    private final int post_id;
    private Map<ReportReason, Integer> reports;
    private String custom_report;

    // lunghezza massima del report custom
    public final int MAX_CUSTOM_TEXT_LENGTH = 70;

    /**
     * Costruttore della classe Report
     * @param post_id l'id del post segnalato
     */
    public Report(int post_id) {
        this.post_id = post_id;
        this.reports = getDefaultReports();
        custom_report = null;
    }

    /**
     * Costruttore della classe Report
     * @param report copia un report
     */
    public Report(Report report) {
        if(report == null) {
            throw new NullPointerException();
        }
        this.post_id = report.post_id;
        this.reports = report.reports;
        this.custom_report = report.custom_report;
    }


    /**
     * Crea una mappa con tutti i motivi di default della segnalazione impostati a 0
     * @return map con tutti i motivi impostati
     */
    private static Map<ReportReason, Integer> getDefaultReports() {
        Map<ReportReason, Integer> mappa = new HashMap<>();

        mappa.put(ReportReason.violent_content, 0);
        mappa.put(ReportReason.hateful_content, 0);
        mappa.put(ReportReason.sexual_content, 0);
        mappa.put(ReportReason.scam_attempt, 0);
        mappa.put(ReportReason.promotes_terrorism, 0);
        mappa.put(ReportReason.spam_misleading_content, 0);
        mappa.put(ReportReason.copyright_issue, 0);
        mappa.put(ReportReason.custom, 0); // se viene impostato a 1 legge il valore della stringa custom_report

        return mappa;
    }

    /**
     * Imposta il motivo della segnalazione specificato ad 1
     * @param type il tipo di segnalazione che si vuole impostare
     * @modifies this.reports
     * @effects report(this.reports) = pre(this.reports[type] = 1)
     */
    public void setReport(ReportReason type) {
        if(type == ReportReason.custom) {
            throw new WrongMethodTypeException("Usare il metodo setCustomReport per mandare una segnalazione custom!");
        } else {
            reports.put(type, 1);
        }
    }

    /**
     * Imposta un motivo della segnalazione custom
     * @param text il motivo personalizzato
     * @throws TextLengthException se la lunghezza del motivo text supera i 70 caratteri (MAX_CUSTOM_TEXT_LENGTH)
     * @modifies this.reports
     * @effects report(this.reports[custom]) = pre(this.reports[custom] = text)
     */
    public void setCustomReport(String text) throws TextLengthException {
        if(text.length() > MAX_CUSTOM_TEXT_LENGTH) {
            throw new TextLengthException(MAX_CUSTOM_TEXT_LENGTH);
        } else {
            reports.put(ReportReason.custom, 1);
            this.custom_report = text;
        }
    }

    /**
     * Restituisce l'id del post della segnalazione
     * @return l'id del post della segnalazione
     */
    public int getPostID() {
        return post_id;
    }

    /**
     * Restituisce tutti i motivi della segnalazione (quelli impostati a 1 sono utilizzati)
     * @return restituisce tutti i motivi della segnalazione
     */
    public Map<ReportReason, Integer> getReports() {
        return reports;
    }

    /**
     * Restituisce il numero di motivi della segnalazione del post
     * @return restituisce il numero di motivi della segnalazione del post
     */
    public int getReportNumber() {
        int somma = 0;

        for(Map.Entry<ReportReason, Integer> entry : reports.entrySet()) {
            somma += entry.getValue();
        }

        return somma;
    }

    /**
     * Imposta manualmente i motivi della segnalazione del post
     * @param reports mappa contenente i motivi della segnalazione
     * @modifies this.reports
     * @effects report(this.reports) = reports
     */
    public void setReports(Map<ReportReason, Integer> reports) {
        this.reports = reports;
    }

    /**
     * Restituisce il motivo personalizzato della segnalazione (se presente)
     * @return il motivo personalizzato della segnalazione
     * @throws EmptyFieldException se il campo custom non è impostato
     */
    public String getCustomReport() throws EmptyFieldException {
        if(custom_report == null) {
            throw new EmptyFieldException();
        }
        return custom_report;
    }

    /**
     * Restituisce il motivo personalizzato della segnalazione (se presente)
     * @return il motivo personalizzato della segnalazione
     */
    public String toString() {
        return "Report{" +
            "post_id=" + post_id +
            ", reports=" + reports +
            ", custom_report='" + custom_report + '\'' +
            '}';
    }
}
