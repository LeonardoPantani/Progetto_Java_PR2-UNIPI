package app;

// Leonardo Pantani | Java project for PR2 course at UNIPI

/**
 * Enum ReportReason contiene tutti i motivi di una segnalazione. Quando si fa uso di "custom", va specificato
 * il motivo del report tramite l'apposito metodo setCustomReport che prende in input una stringa.
 */
public enum ReportReason {
    violent_content,
    hateful_content,
    sexual_content,
    scam_attempt,
    promotes_terrorism,
    spam_misleading_content,
    copyright_issue,
    custom
}
