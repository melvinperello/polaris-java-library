package afterschoolcreatives.polaris.javafx.scene.control.simpletable;

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
