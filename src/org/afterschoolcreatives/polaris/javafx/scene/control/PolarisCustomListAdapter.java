/**
 * Information Retrieval Integrated System ( I.R.I.S. )
 * Republic of The Philippines, DOST Regional Office No. III
 * Provincial Science Technology Center, City of Malolos, Bulacan
 *
 * Afterschool Creatives "Captivating Creativity"
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

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 * Use List View as a custom table view.
 *
 * @author Jhon Melvin
 * @param <T> any type that implements LISTABLE interface.
 */
public class PolarisCustomListAdapter<T extends PolarisCustomListAdapter.Listable> {

    /**
     * Private No-Op constructor.
     */
    private PolarisCustomListAdapter() {
        // override to private no call.
        this.list = null;
        this.listView = null;
        this.customCellPrefHeight = 0.0;
    }

    //--------------------------------------------------------------------------
    // main declaration.
    private final ListView<T> listView;
    private final ObservableList<T> list;
    //--------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param listView
     * @param list
     */
    public PolarisCustomListAdapter(ListView<T> listView, ObservableList<T> list) {
        this.listView = listView;
        this.list = list;
        this.customCellPrefHeight = 0.0;
    }

    /**
     * Preferred height so that the size of the pane will match cell height.
     */
    private double customCellPrefHeight;

    /**
     * Sets the height of each list view cell. this must match the height of the
     * pane.
     *
     * @param customCellPrefHeight
     */
    public void setCustomCellPrefHeight(double customCellPrefHeight) {
        if (customCellPrefHeight < 0.00) {
            customCellPrefHeight = 0.00;
        }
        this.customCellPrefHeight = customCellPrefHeight;
    }

    /**
     * Customizes the list view.
     */
    public void customize() {
        this.listView.setItems(this.list);
        this.listView.setCellFactory((param) -> {
            PolarisListCell cell = new PolarisListCell();
            /**
             * Do not adjust preferred height if 0.00.
             */
            if (this.customCellPrefHeight != 0.00) {
                cell.setPrefHeight(this.customCellPrefHeight);
            }
            return cell;
        });
    }

    //--------------------------------------------------------------------------
    // static classes.
    //--------------------------------------------------------------------------
    /**
     * Custom class to override list view behavior.
     */
    private static class PolarisListCell extends ListCell {

        /**
         * Overrides the default behavior of the cell drawer of the list view.
         *
         * @param item
         * @param empty
         */
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            /**
             * If Cell was empty return.
             */
            if (empty) {
                this.setGraphic(null);
                return;
            }
            //------------------------------------------------------------------
            /**
             * If existing item was found.
             */
            if (item != null) {
                /**
                 * Must implement Polaris LISTABLE and Polaris FX Controller.
                 * the controller instance must be loaded.
                 */
                Listable listItem = (Listable) item;
                // bind pref width.
                listItem.getCellGraphic().prefWidthProperty().bind(this.prefWidthProperty());
                // load to cell.
                this.setGraphic(listItem.getCellGraphic());
            } else {
                /**
                 * Forces the list view to redraw the cells when the cell was
                 * freed.
                 */
                this.setGraphic(null);
            }

        }

    }

    //--------------------------------------------------------------------------
    /**
     * Interface class to implement the custom get cell graphic method.
     */
    @FunctionalInterface
    public interface Listable {

        /**
         * Get the custom cell graphic.
         *
         * @return Pane.
         */
        Pane getCellGraphic();
    }
    //--------------------------------------------------------------------------
} // end static
