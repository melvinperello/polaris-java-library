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
package org.afterschoolcreatives.polaris.javafx.scene.control.simpletable;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;

@Deprecated
public class SimpleTableRow extends VBox {

    /**
     * CSS Styling Class names.
     */
    // for contents only
    private final String rowCssClass = "simple-table-row";
    // for the extension row
    private final String rowExtensionCssClass = "simple-table-row-extension";

    private final String rowAll = "simple-table-row-both";

    /**
     *
     */
    private HBox rowControls;
    private Pane rowExtension;

    private HashMap<String, Object> rowMetaData;

    /**
     * Constructor.
     */
    public SimpleTableRow() {
        this.rowMetaData = new HashMap<>();

        //
        this.rowControls = new HBox();
        VBox.setVgrow(this.rowControls, Priority.NEVER);
        this.rowControls.getStyleClass().add(this.rowCssClass);
        this.rowControls.getStyleClass().add(this.rowAll);
        // bind width
        this.rowControls.prefWidthProperty().bind(this.prefWidthProperty());
        this.rowControls.minWidthProperty().bind(this.minWidthProperty());
        this.rowControls.maxWidthProperty().bind(this.maxWidthProperty());
        this.getChildren().add(this.rowControls);

    }

    /**
     * Add Additional Data in this row.
     *
     * @return
     */
    public HashMap<String, Object> getRowMetaData() {
        return rowMetaData;
    }

    /**
     * Add new Cell to this row.
     *
     * @param cell new cell control.
     */
    public void addCell(SimpleTableCell cell) {
        // bind the cell's height properties to this row.
        cell.prefHeightProperty().bind(this.rowControls.prefHeightProperty());
        cell.maxHeightProperty().bind(this.rowControls.maxHeightProperty());
        cell.minHeightProperty().bind(this.rowControls.minHeightProperty());
        // add this cell to row.
        this.rowControls.getChildren().add(cell);
    }

    /**
     * Node Methods.
     */
    private String rowIdentifier;

    public String getRowIdentifier() {
        return rowIdentifier;
    }

    public void setRowIdentifier(String rowIdentifier) {
        this.rowIdentifier = rowIdentifier;
    }

    public void setRowHeight(double height) {
        this.rowControls.setPrefHeight(height);
        this.rowControls.setMaxHeight(height);
        this.rowControls.setMinHeight(height);
    }

    //-------------------------------------------------------------------
    public SimpleTableCell getCell(int index) {
        return (SimpleTableCell) this.rowControls.getChildren().get(index);
    }

    public <T> T getCellContent(int index) {
        return (T) this.getCell(index).getContent();
    }
    //-------------------------------------------------------------------

    /**
     * Row Extension.
     *
     * @param pane
     */
    public void setRowExtension(Pane pane) {
        this.rowExtension = pane;
        this.rowExtension.getStyleClass().add(this.rowExtensionCssClass);
        this.rowExtension.getStyleClass().add(this.rowAll);
        this.rowExtension.prefWidthProperty().bind(this.prefWidthProperty());
        this.rowExtension.minWidthProperty().bind(this.minWidthProperty());
        this.rowExtension.maxWidthProperty().bind(this.maxWidthProperty());

    }

    public boolean isExtensionShown() {
        return this.getChildren().contains(this.rowExtension);
    }

    public void showExtension() {
        if (!this.getChildren().contains(this.rowExtension)) {
            this.getChildren().add(this.rowExtension);
        }
    }

    public void hideExtension() {
        this.getChildren().remove(this.rowExtension);
    }

}
