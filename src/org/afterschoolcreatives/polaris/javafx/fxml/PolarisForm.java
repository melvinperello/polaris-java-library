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
package org.afterschoolcreatives.polaris.javafx.fxml;

import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.afterschoolcreatives.polaris.java.exceptions.NoSceneException;
import org.afterschoolcreatives.polaris.java.exceptions.NoStageException;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;

/**
 *
 * @author Jhon Melvin
 */
public abstract class PolarisForm extends PolarisFxController {

    public PolarisForm() {
        this.dialogMessageTitle = "Polaris Message";
    }

    private String dialogMessageTitle;

    /**
     * Sets the message title for this controller.
     *
     * @param dialogMessageTitle
     */
    public final void setDialogMessageTitle(String dialogMessageTitle) {
        this.dialogMessageTitle = dialogMessageTitle;
    }

    private PolarisDialog createMessage(PolarisDialog.Type type, String header, String message) {
        Stage stage;

        try {
            stage = this.getStage();
        } catch (ClassCastException | NoSceneException | NoStageException ex) {
            stage = null;
        }

        return PolarisDialog.create(type)
                .setTitle(dialogMessageTitle)
                .setHeaderText(header)
                .setContentText(message)
                .setOwner(stage);
    }

    public PolarisDialog messageWarning(String header, String message) {
        if (header == null) {
            header = "Warning";
        }

        if (message == null) {
            message = "This is a Warning Message";
        }

        return this.createMessage(PolarisDialog.Type.WARNING, header, message);
    }

    public PolarisDialog messageInfo(String header, String message) {
        if (header == null) {
            header = "Information";
        }

        if (message == null) {
            message = "This is an Information Message";
        }

        return this.createMessage(PolarisDialog.Type.INFORMATION, header, message);
    }

    public PolarisDialog messageError(String header, String message) {
        if (header == null) {
            header = "Error";
        }

        if (message == null) {
            message = "This is an Error Message";
        }

        return this.createMessage(PolarisDialog.Type.ERROR, header, message);
    }

    public Optional<ButtonType> messageConfirm(String header, String message, ButtonType... buttons) {
        if (header == null) {
            header = "Confirm";
        }

        if (message == null) {
            message = "This is a Confirmation Message";
        }
        //----------------------------------------------------------------------
        if (buttons == null) {
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            buttons = new ButtonType[]{yesButton, cancelButton};
        } else if (buttons.length == 0) {
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            buttons = new ButtonType[]{yesButton, cancelButton};
        }
        //----------------------------------------------------------------------

        PolarisDialog dialog = this.createMessage(PolarisDialog.Type.CONFIRMATION, header, message);
        dialog.setButtons(buttons);
        Optional<ButtonType> res = dialog.showAndWait();
        return res;
    }

    public PolarisDialog messageException(Exception ex, String header, String message) {
        if (header == null) {
            header = "Exception";
        }

        if (message == null) {
            message = "The system has encountered a problem.";
        }

        Stage stage;

        try {
            stage = this.getStage();
        } catch (ClassCastException | NoSceneException | NoStageException e) {
            stage = null;
        }

        return PolarisDialog.exceptionDialog(ex)
                .setContentText(message)
                .setHeaderText(header)
                .setOwner(stage)
                .setTitle(this.dialogMessageTitle);
    }

}
