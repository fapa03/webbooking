package mx.com.paquetexpress.dto;

import java.io.Serializable;

public class EmailNotificationDTO implements Serializable {

    public EmailNotificationDTO() {
    }

    private String emailDestination;
    private String emailSubject;
    private String emailMensaje;
    private String emailDestinationCC;

    public String getEmailDestination() {
        return emailDestination;
    }

    public void setEmailDestination(String emailDestination) {
        this.emailDestination = emailDestination;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailMensaje() {
        return emailMensaje;
    }

    public void setEmailMensaje(String emailMensaje) {
        this.emailMensaje = emailMensaje;
    }

    public String getEmailDestinationCC() {
        return emailDestinationCC;
    }

    public void setEmailDestinationCC(String emailDestinationCC) {
        this.emailDestinationCC = emailDestinationCC;
    }

    @Override
    public String toString() {
        return "EmailNotificationDTO{ emailDestination=" + emailDestination + ", emailSubject=" + emailSubject + ", emailMensaje=" + emailMensaje + ", emailDestinationCC=" + emailDestinationCC + '}';
    }

}
