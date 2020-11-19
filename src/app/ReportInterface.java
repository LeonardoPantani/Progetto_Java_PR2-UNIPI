package app;

import exception.EmptyFieldException;
import exception.TextLengthException;

import java.util.Map;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public interface ReportInterface {
    /**
     * Imposta il motivo della segnalazione specificato ad 1
     * @param type il tipo di segnalazione che si vuole impostare
     */
    void setReport(ReportReason type);

    /**
     * Imposta un motivo della segnalazione custom
     * @param text il motivo personalizzato
     * @throws TextLengthException se la lunghezza del motivo text supera i 70 caratteri (costante MAX_CUSTOM_TEXT_LENGTH)
     */
    void setCustomReport(String text) throws TextLengthException;

    /**
     * Restituisce l'id del post della segnalazione
     * @return l'id del post della segnalazione
     */
    int getPostID();

    /**
     * Restituisce tutti i motivi della segnalazione (quelli impostati a 1 sono utilizzati)
     * @return restituisce tutti i motivi della segnalazione
     */
    Map<ReportReason, Integer> getReports();

    /**
     * Restituisce il numero di motivi della segnalazione del post
     * @return restituisce il numero di motivi della segnalazione del post
     */
    int getReportNumber();

    /**
     * Imposta manualmente i motivi della segnalazione del post
     * @param reports mappa contenente i motivi della segnalazione
     */
    void setReports(Map<ReportReason, Integer> reports);

    /**
     * Restituisce il motivo personalizzato della segnalazione (se presente)
     * @return il motivo personalizzato della segnalazione
     * @throws EmptyFieldException se il campo custom non è impostato
     */
    String getCustomReport() throws EmptyFieldException;

    /**
     * Restituisce una stringa per mostrare il report
     * @return report formato visualizzabile
     */
    String toString();
}
