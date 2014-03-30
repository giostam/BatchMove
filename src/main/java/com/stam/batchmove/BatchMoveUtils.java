/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stam.batchmove;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author StamaterisG
 */
public class BatchMoveUtils {

    public static File findFileByName(List<File> files, String fileName) {
        File file = null;

        for (File f : files) {
            if (f.isFile()) {
                String fn = f.getName();
                String fileNameWithoutExt = FilenameUtils.removeExtension(fn);
                if (fileName.equals(fn)) {
                    file = f;
                    break;
                } else if (fileName.equals(fileNameWithoutExt)) {
                    file = f;
                    break;
                }
            }
        }

        return file;
    }

    public static void showFilesFrame(Object[][] data, String[] columnNames, final JFrame callerFrame) {
        final FilesFrame filesFrame = new FilesFrame();

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        final JTable table = new JTable(model);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.setDefaultRenderer(table.getColumnClass(i), renderer);
        }
//            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setMaxWidth(140);
        table.getColumnModel().getColumn(3).setMaxWidth(90);

        JPanel tblPanel = new JPanel();
        JPanel btnPanel = new JPanel();

        tblPanel.setLayout(new BorderLayout());
        if (table.getRowCount() > 15) {
            JScrollPane scrollPane = new JScrollPane(table);
            tblPanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            tblPanel.add(table.getTableHeader(), BorderLayout.NORTH);
            tblPanel.add(table, BorderLayout.CENTER);
        }

        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        filesFrame.setMinimumSize(new Dimension(800, 600));
        filesFrame.setLayout(new BorderLayout());
        filesFrame.add(tblPanel, BorderLayout.NORTH);
        filesFrame.add(btnPanel, BorderLayout.SOUTH);

        final JLabel resultsLabel = new JLabel();

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filesFrame.setVisible(false);
                callerFrame.setVisible(true);
            }
        });

        JButton moveBtn = new JButton("Copy");
        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Choose target directory");
                int selVal = fileChooser.showOpenDialog(null);
                if (selVal == JFileChooser.APPROVE_OPTION) {
                    File selection = fileChooser.getSelectedFile();
                    String targetPath = selection.getAbsolutePath();

                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    int nRow = dtm.getRowCount();
                    int copied = 0;
                    for (int i = 0; i < nRow; i++) {
                        Boolean selected = (Boolean) dtm.getValueAt(i, 0);
                        String filePath = dtm.getValueAt(i, 1).toString();

                        if (selected) {
                            try {
                                FileUtils.copyFileToDirectory(new File(filePath), new File(targetPath));
                                dtm.setValueAt("Copied", i, 3);
                                copied++;
                            } catch (Exception ex) {
                                Logger.getLogger(SelectionFrame.class.getName()).log(Level.SEVERE, null, ex);
                                dtm.setValueAt("Failed", i, 3);
                            }
                        }
                    }
                    resultsLabel.setText(copied + " files copied. Finished!");
                }
            }
        });
        btnPanel.add(cancelBtn);
        btnPanel.add(moveBtn);
        btnPanel.add(resultsLabel);

        filesFrame.revalidate();
        filesFrame.setVisible(true);

        callerFrame.setVisible(false);
    }
}
