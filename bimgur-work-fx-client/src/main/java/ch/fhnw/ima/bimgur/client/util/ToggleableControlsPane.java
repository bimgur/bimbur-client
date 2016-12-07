package ch.fhnw.ima.bimgur.client.util;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javaslang.collection.List;

/**
 * A pane which features a main node at its center, and multiple auxiliary control nodes. All control nodes are
 * presented in one split pane, which can be positioned at either the top, right, bottom, or left of the main node.
 * Control nodes can be toggled selectively. The control split pane is only visible if at least one control node is
 * toggled.
 *
 * @author Rahel Lüthy
 */
public class ToggleableControlsPane extends StackPane {

    public enum ControlPanePosition {
        TOP, RIGHT, BOTTOM, LEFT
    }

    private final Node centerNode;
    private final List<Node> controlNodes;
    private final ObservableList<Node> toggledControlNodes;
    private final SplitPane globalSplitPane;
    private final ControlPanePosition controlPanePosition;
    private final SplitPane controlPane;

    public ToggleableControlsPane(Node centerNode, ControlPanePosition controlPanePosition, Node... controlNodes) {
        this.centerNode = centerNode;
        this.controlPanePosition = controlPanePosition;
        this.controlNodes = List.of(controlNodes);

        globalSplitPane = new SplitPane();
        controlPane = new SplitPane();
        if (isControlPaneOnSide(controlPanePosition)) {
            globalSplitPane.setOrientation(Orientation.HORIZONTAL);
            controlPane.setOrientation(Orientation.VERTICAL);
        } else {
            globalSplitPane.setOrientation(Orientation.VERTICAL);
            controlPane.setOrientation(Orientation.HORIZONTAL);
        }
        globalSplitPane.getItems().setAll(centerNode);

        toggledControlNodes = FXCollections.observableArrayList();

        getChildren().add(globalSplitPane);
    }

    public ReadOnlyDoubleProperty controlPaneHeightProperty() {
        return controlPane.heightProperty();
    }

    public ReadOnlyDoubleProperty controlPaneWidthProperty() {
        return controlPane.widthProperty();
    }

    public void setGlobalDividerLocation(double dividerLocation) {
        globalSplitPane.setDividerPosition(0, dividerLocation);
    }

    public void toggleControlNode(Node node) {

        if (!controlNodes.contains(node)) {
            throw new IllegalArgumentException("Unknown node");
        }

        if (toggledControlNodes.contains(node)) {
            toggledControlNodes.remove(node);
        } else {
            toggledControlNodes.add(node);
        }

        if (toggledControlNodes.isEmpty()) {
            globalSplitPane.getItems().setAll(centerNode);
        } else {
            if (isCenterFirstInGlobalSplitPane(controlPanePosition)) {
                globalSplitPane.getItems().setAll(centerNode, controlPane);
            } else {
                globalSplitPane.getItems().setAll(controlPane, centerNode);
            }
        }

        // determine new split pane items while maintaining order
        List<? extends Node> items = controlNodes.filter(toggledControlNodes::contains);

        // nodes must be nested in a container before adding them to a JavaFX split pane
        List<BorderPane> nestedItems = items.map(BorderPane::new);

        controlPane.getItems().setAll(nestedItems.toJavaList());

        if (toggledControlNodes.size() > 0) {
            controlPane.setDividerPositions(evenDividerPositions(controlPane.getItems().size()));
        }
    }

    private static boolean isControlPaneOnSide(ControlPanePosition controlSplitPanePosition) {
        return ControlPanePosition.LEFT.equals(controlSplitPanePosition)
                || ControlPanePosition.RIGHT.equals(controlSplitPanePosition);
    }

    private static boolean isCenterFirstInGlobalSplitPane(ControlPanePosition controlSplitPanePosition) {
        return ControlPanePosition.BOTTOM.equals(controlSplitPanePosition)
                || ControlPanePosition.RIGHT.equals(controlSplitPanePosition);
    }

    private static double[] evenDividerPositions(int size) {
        double[] positions = new double[size - 1];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = (i + 1) * 1.0 / size;
        }
        return positions;
    }

}
