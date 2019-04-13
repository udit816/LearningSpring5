package gov.mea.psp.instantApps;

import java.awt.Color;
import java.awt.Component;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.mea.psp.instantApps.service.IAAuditService;
import gov.mea.psp.instantApps.util.InstantAppConstants;

public class CreateTable extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CreateTable.class);
	private JFrame frmDataMatched;
	private JTable reportTable;

	/**
	 * Create the application.
	 * 
	 */

	public void makeTable(Map<String, Object> excelData, Object[][] dataArr) {

		final Map<String, Object> tempExcelData = excelData;// To be Modified
		frmDataMatched = new JFrame();
		frmDataMatched.setVisible(true);
		frmDataMatched.setResizable(false);
		frmDataMatched.getContentPane().setBackground(new Color(204, 255, 204));
		frmDataMatched.setTitle("Mapped Data");
		frmDataMatched.setBounds(100, 100, 850, 500);
		frmDataMatched.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmDataMatched.getContentPane().add(scrollPane);

		reportTable = new JTable();
		reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		reportTable.setBackground(new Color(204, 255, 255));
		reportTable.getTableHeader().setReorderingAllowed(false);
		reportTable.getTableHeader().setResizingAllowed(false);
		reportTable.setColumnSelectionAllowed(false);
		reportTable.setEnabled(false);
		reportTable.setModel(new DefaultTableModel(dataArr, InstantAppConstants.COLUMNNAMES) {

			private static final long serialVersionUID = 5355333427930048120L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		reportTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		reportTable.getColumnModel().getColumn(1).setPreferredWidth(250);
		reportTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		reportTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		reportTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		reportTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		reportTable.getColumn("View Report").setCellRenderer(new ButtonRenderer());
		reportTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (reportTable.columnAtPoint(e.getPoint()) == 5) {
					int row = reportTable.rowAtPoint(e.getPoint());
					int col = 1;
					JOptionPane.showMessageDialog(null,
							" Audit Reference selected is : " + reportTable.getValueAt(row, col).toString()
									+ ". Report will now be opened and downloaded");
					try {
						boolean status = new IAAuditService().makeExcelForMe(tempExcelData,
								reportTable.getValueAt(row, col).toString());
						if (!status) {
							JOptionPane.showMessageDialog(null, "Error in generating report!!!");
						}
					} catch (Exception exc) {
						log.info("After sending AuditReference number value ::: ", exc);
						JOptionPane.showMessageDialog(null, "Error in generating report!!!");
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Please click on View Report button to view and download report excel.");
				}
			}
		});
		scrollPane.setViewportView(reportTable);
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = -706888595137104292L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

}