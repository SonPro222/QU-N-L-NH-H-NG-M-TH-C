
package util;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class XDialog {
    public static void alert(String message){
        XDialog.alert(message, "Thông báo!");
    }
    public static void alert(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static boolean confirm(String message){
        return XDialog.confirm(message, "Xác nhận!");
    }
    public static boolean confirm(String message, String title){
        int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == JOptionPane.YES_OPTION);
    }
    
    public static String prompt(String message){
        return XDialog.prompt(message, "Nhập vào!");
    }
    public static String prompt(String message, String title){
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
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
}