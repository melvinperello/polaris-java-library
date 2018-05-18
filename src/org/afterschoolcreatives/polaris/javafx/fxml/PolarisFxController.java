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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.afterschoolcreatives.polaris.java.exceptions.NoSceneException;
import org.afterschoolcreatives.polaris.java.exceptions.NoStageException;
import org.afterschoolcreatives.polaris.java.exceptions.PolarisRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public abstract class PolarisFxController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(PolarisFxController.class);

    public PolarisFxController() {
        this.root = null;
        this.externalLocation = null;
    }

    private Parent root;

    /**
     * External Location outside the class path that represents this
     * controller's FXML.
     */
    private String externalLocation;

    /**
     * Set the External Location outside the class path that represents this
     * controller's FXML directory.
     *
     * Example: Polaris.FXML is location at view/FXMLS/Polaris.FXML. you should
     * only put "view/FXMLS"
     *
     * @param externalLocation external String Location outside SRC.
     */
    public final void setExternalLocation(String externalLocation) {
        this.externalLocation = externalLocation + File.separator + this.getClass().getSimpleName() + ".fxml";
    }

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
        String path = "/" + this.getClass().getName().replace('.', '/') + ".fxml";
        LOGGER.debug("FXML Path: {}", path);
        return path;
    }

    /**
     * Loads the FXML file that is assigned to the sub class of this controller.
     *
     * @param <T>
     * @return
     */
    public <T> T load() {
        // get URL
        URL fxmlURL = PolarisFxController.class.getResource(this.getFxmlPath());
        //----------------------------------------------------------------------
        if (this.externalLocation != null) {
            try {
                URL extURL = Paths.get(externalLocation).toUri().toURL();
                fxmlURL = extURL;
                LOGGER.debug("Using external FXML Location: {}", extURL.getFile());
            } catch (MalformedURLException e) {
                LOGGER.error("External FXML Location was set but was invalid... defaulting to internal location.", e);
            }
        }
        //----------------------------------------------------------------------
        // Create FXML Loader.
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        // Set this controller
        fxmlLoader.setController(this);
        try {
            // load the FXML
            this.root = fxmlLoader.load();
        } catch (IOException ex) {
            throw new PolarisRuntimeException("Cannot load FXML [IO EXCEPTION]", ex);
        } catch (IllegalStateException ie) {
            throw new PolarisRuntimeException("Cannot load FXML [Illegal State Exception]", ie);
        }

        // call setup
        this.setup();
        // return root.
        return (T) this.root;
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
     * Retrieves this Node's Parent Scene.
     *
     * @return Scene
     * @throws NoSceneException if there is no scene.
     */
    public Scene getScene() throws NoSceneException {
        Scene scene = this.<Parent>getRoot().getScene();
        if (scene == null) {
            throw new NoSceneException("This node is not part of any Scene.");
        }
        return scene;
    }

    /**
     *
     * @return Stage
     * @throws NoSceneException if there is no scene.
     * @throws NoStageException if there is no stage.
     */
    public Stage getStage() throws NoSceneException, NoStageException, ClassCastException {
        Window window = this.getScene().getWindow();
        if (window == null) {
            throw new NoStageException("This scene is not part of any Window.");
        }
        try {
            return (Stage) this.getScene().getWindow();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot cast this window into a stage.");
        }
    }

    /**
     * Changes the root of the currently active scene.
     *
     * @param newRoot the new root.
     * @throws NoSceneException if there is no scene.
     * @throws NoStageException if there is no stage.
     */
    public void changeRoot(Parent newRoot) throws NoSceneException, NoStageException {
        Stage currentStage = this.getStage();
        this.getRootPane().setPrefWidth(currentStage.getWidth());
        this.getRootPane().setPrefHeight(currentStage.getHeight());
        this.getScene().setRoot(newRoot);
    }

}
