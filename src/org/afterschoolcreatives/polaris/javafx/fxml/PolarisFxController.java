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

import org.afterschoolcreatives.polaris.java.PolarisException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public abstract class PolarisFxController implements Initializable {

    private Parent root;

    /**
     * Method from initializable. implementation for sub classes. this method is
     * automatically called when the FXML file was loaded.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Override if necessary.
    }

    /**
     * Some components are not allowed to be called during initialization.
     * override this method to do something when the initialization is complete.
     */
    protected abstract void setup();

    /**
     * The Implementation of the PolarisFxController class is to have the
     * controller and the FXML file in the same location. therefore it is
     * assumed that the FXML file is in the same location of the class package.
     *
     * @return
     */
    private String getFxmlPath() {
        return "/" + this.getClass().getName().replace('.', '/') + ".fxml";
    }

    /**
     * Loads the FXML file that is assigned to the sub class of this controller.
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("UI_INHERITANCE_UNSAFE_GETRESOURCE")
    public <T> T load() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(this.getFxmlPath()));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.setup();
            return (T) this.root;
        } catch (IOException ex) {
            throw new PolarisException("Cannot load the FXML.", ex);
        }
    }

    /**
     * Get the assigned root and cast to Pane Object.
     *
     * @return
     */
    public Pane getRootPane() {
        return (Pane) root;
    }

    /**
     * Gets the root object.
     *
     * @param <T>
     * @return
     */
    public <T> T getRoot() {
        return (T) this.root;
    }

    /**
     * Gets the current stage of this root.
     *
     * @return
     */
    public Stage getStage() {
        try {
            return (Stage) this.getRootPane().getScene().getWindow();
        } catch (ClassCastException e) {
            throw new PolarisException("Cannot get the current stage.", e);
        }
    }

    /**
     * Changes the root of the currently active scene.
     *
     * @param newRoot the new root.
     * @throws NullPointerException when there is no active scene.
     */
    public void changeRoot(Parent newRoot) throws NullPointerException {
        Stage currentStage = this.getStage();
        this.getRootPane().setPrefWidth(currentStage.getWidth());
        this.getRootPane().setPrefHeight(currentStage.getHeight());
        this.getRootPane().getScene().setRoot(newRoot);
    }

}
