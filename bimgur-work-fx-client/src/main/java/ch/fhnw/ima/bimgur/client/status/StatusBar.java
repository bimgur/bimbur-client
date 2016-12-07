package ch.fhnw.ima.bimgur.client.status;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StatusBar extends org.controlsfx.control.StatusBar {

    private static final Image ICON_LIST = new Image(StatusBar.class.getResourceAsStream("/img/list.png"));
    private static final Image ICON_REFRESH = new Image(StatusBar.class.getResourceAsStream("/img/refresh.png"));
    private static final Insets BUTTON_PADDING = new Insets(0, 2, 0, 2);

    public StatusBar(StatusBarPresentationModel model, StatusBarController controller) {
        setText("Server: " + model.getStatusBarText());

        ImageView listIcon = new ImageView(ICON_LIST);
        ToggleButton notificationsButton = new ToggleButton("", listIcon);
        notificationsButton.setPadding(BUTTON_PADDING);
        notificationsButton.setTooltip(new Tooltip("Toggle Notification Log"));
        notificationsButton.setOnAction(e -> controller.toggleNotifications());

        ImageView refreshIcon = new ImageView(ICON_REFRESH);
        Button refreshButton = new Button("", refreshIcon);
        refreshButton.setPadding(BUTTON_PADDING);
        refreshButton.setTooltip(new Tooltip("Refresh"));
        refreshButton.setOnAction(e -> controller.refresh());

        getRightItems().addAll(notificationsButton, refreshButton);
    }

}