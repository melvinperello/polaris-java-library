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

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Must be placed inside simple table view.
 *
 * @author Jhon Melvin
 */
@Deprecated
public class SimpleTable extends VBox {

    private final String tableCss = "simple-table";

    public SimpleTable() {
        this.getStyleClass().add(tableCss);
    }

    public void addRow(SimpleTableRow row) {
        row.prefWidthProperty().bind(this.prefWidthProperty());
        row.minWidthProperty().bind(this.minWidthProperty());
        row.maxWidthProperty().bind(this.maxWidthProperty());

        this.getChildren().add(row);
    }

    public void hideAllExtensions() {
        this.getChildren().forEach(row -> {
            ((SimpleTableRow) row).hideExtension();
        });
    }

    public void showAllExtensions() {
        this.getChildren().forEach(row -> {
            ((SimpleTableRow) row).showExtension();
        });
    }

    public ObservableList<Node> getRows() {
        return this.getChildren();
    }

    //-------------------------------------------------------------------------
    public static void fxTable(SimpleTable table, VBox parent) {
        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(table);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(parent);
    }

    public static SimpleTableRow fxRow(Pane root, double height) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(height);
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContentAsPane(root);
        row.addCell(cellParent);
        return row;
    }

}
