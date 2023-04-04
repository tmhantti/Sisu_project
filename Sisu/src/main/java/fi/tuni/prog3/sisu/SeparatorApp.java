package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

/**
 * Separator for UI to divide screen to two sections.
 * @author Sanna Nykänen
 */
public class SeparatorApp {

    /**
     * Creates vertical separator.
     * @return Separator
     */
    public Node drawSeparator() {
        final Separator sepVert2 = new Separator();
        sepVert2.setOrientation(Orientation.VERTICAL);
        sepVert2.setValignment(VPos.CENTER);
        GridPane.setRowSpan(sepVert2, 7);
        sepVert2.setPadding(new Insets(0, 30, 0, 30));
        sepVert2.setPrefHeight(620);
        return sepVert2;
    }
}
