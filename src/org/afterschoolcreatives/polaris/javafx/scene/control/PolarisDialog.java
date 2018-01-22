/**
 *
 * Polaris Java Library - Afterschool Creatives "Captivating Creativity"
 *
 * Copyright 2018 Jhon Melvin Perello
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.polaris.javafx.scene.control;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * NOTICE: To use the official JavaFX Dialogs you need JDK 8u40 or later.
 *
 * Simplified Dialog Creation for Java FX.
 *
 * @author Jhon Melvin
 */
public class PolarisDialog {

    /**
     * Types.
     */
    public enum Type {
        INFORMATION, ERROR, CONFIRMATION, WARNING
    }

    private Alert alert;

    private PolarisDialog() {
        this.alert = null;
    }

    //--------------------------------------------------------------------------
    // Content Methods.
    //--------------------------------------------------------------------------
    public static PolarisDialog create(Type type) {
        PolarisDialog polarisFx = new PolarisDialog();
        polarisFx.alert = new Alert(AlertType.valueOf(type.toString()));
        return polarisFx;
    }

    /**
     * special create for exception dialogs.
     *
     * @param exception
     * @return
     */
    public static PolarisDialog exceptionDialog(Exception exception) {
        PolarisDialog polarisFx = new PolarisDialog();
        polarisFx.alert = new Alert(AlertType.ERROR);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        String labelText = "Exception Details: ";
        if (exception.getMessage() != null) {
            labelText = exception.getMessage();
        }

        Label label = new Label(labelText);

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        polarisFx.alert.getDialogPane().setExpandableContent(expContent);

        // return
        return polarisFx;
    }

    public PolarisDialog setTitle(String title) {
        this.alert.setTitle(title);
        return this;
    }

    public PolarisDialog setHeaderText(String header) {
        this.alert.setHeaderText(header);
        return this;
    }

    public PolarisDialog setContentText(String message) {
        this.alert.setContentText(message);
        return this;
    }

    //--------------------------------------------------------------------------
    // Class Methods.
    //--------------------------------------------------------------------------
    /**
     * Sets the owner of this dialog.
     *
     * @param owner parent stage.
     * @return
     */
    public PolarisDialog setOwner(Stage owner) {
        this.alert.initOwner(owner);
        return this;
    }

    /**
     * Modify the style of the created dialog.
     *
     * @param style
     * @return
     */
    public PolarisDialog setStyle(StageStyle style) {
        this.alert.initStyle(style);
        return this;
    }

    /**
     * Set Custom buttons to this dialog.
     *
     * @param buttons
     * @return
     */
    public PolarisDialog setButtons(ButtonType... buttons) {
        this.alert.getButtonTypes().setAll(buttons);
        return this;
    }

    public PolarisDialog setGraphic(Node graphic) {
        this.alert.setGraphic(graphic);
        return this;
    }

    //--------------------------------------------------------------------------
    // Display Methods.
    //--------------------------------------------------------------------------
    public void show() {
        this.alert.show();
    }

    public Optional<ButtonType> showAndWait() {
        return this.alert.showAndWait();
    }

}
