package ch.fhnw.ima.bimgur.util.fx.notification;

public interface NotificationController {

    void notifyError(String message);

    void notifyError(String message, Throwable t);

    void notifyWarn(String message);

    void notifyInfo(String message);

    void notifySilent(String message);

}
