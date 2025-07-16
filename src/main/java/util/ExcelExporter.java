package util;

import java.io.*;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ExcelExporter {
    public static void exportTable(JTable table, String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(new File(filePath))) {
            TableModel model = table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                pw.print(model.getColumnName(i) + "\t");
            }
            pw.println();
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    pw.print(model.getValueAt(row, col) + "\t");
                }
                pw.println();
            }
        }
    }
}
