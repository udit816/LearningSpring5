package gov.mea.psp.instantApps;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.mea.psp.instantApps.service.IAAuditService;
import gov.mea.psp.instantApps.util.InstantAppConstants;
import gov.mea.psp.instantApps.util.PropertiesCache;

public class GenerateAuditNCRReport extends JFrame {

	private static final Logger log = LogManager.getLogger(GenerateAuditNCRReport.class);
	private static final long serialVersionUID = 7829188574242992980L;
	private JFrame iaForm;
	private JTextField controlName_textField;
	private JTextField auditLocation_textField;

	private String auditPeriod;
	private String auditStatus;
	private String entityName;
	private String currentStatus;
	private String modeOfAudit;
	private String iso270012013;
	private String nameofAuditor;
	private String iso90012008;
	private String controlName;
	private String auditLocation;
	private Map<String, String> formData;
	private Map<String, Object> excelData;
	
	/**
	 * Launch the Form application.
	 */
	public static void main(String[] args) {
		PropertiesCache.loadInstance();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerateAuditNCRReport window = new GenerateAuditNCRReport();
					window.iaForm.setVisible(true);
					window.iaForm.setTitle("Form Details");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GenerateAuditNCRReport() {
		initialize();
	}

	/**
	 * Initialize the contents of the iaForm.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		iaForm = new JFrame();
		iaForm.setResizable(false);
		iaForm.getContentPane().setForeground(new Color(51, 0, 153));
		iaForm.getContentPane().setFont(new Font("Calibri", Font.BOLD, 14));
		iaForm.setBounds(new Rectangle(10, 10, 800, 600));
		iaForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		iaForm.getContentPane().setLayout(null);

		// Heading for my form window
		JLabel lblPleaseFillIn = new JLabel("Please fill in the requirements for Audit_NCR report generation");
		lblPleaseFillIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseFillIn.setFont(new Font("Calibri", Font.BOLD, 14));
		lblPleaseFillIn.setBounds(60, 11, 663, 31);
		iaForm.getContentPane().add(lblPleaseFillIn);

		// Audit Period Label & JComboBox 1
		JLabel lblAuditPeriod = new JLabel("Audit Period*");
		lblAuditPeriod.setFont(new Font("Calibri", Font.BOLD, 14));
		lblAuditPeriod.setBounds(60, 70, 112, 31);
		iaForm.getContentPane().add(lblAuditPeriod);

		final JComboBox auditPeriod_dropDown = new JComboBox(InstantAppConstants.AUDITPERIODVALUES);
		auditPeriod_dropDown.setBounds(182, 75, 178, 20);
		iaForm.getContentPane().add(auditPeriod_dropDown);
		auditPeriod_dropDown.setSelectedIndex(-1);
		auditPeriod_dropDown.setRenderer(new PromptComboBoxRenderer("Select Audit Period"));
		auditPeriod_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				auditPeriod = (String) auditPeriod_dropDown.getSelectedItem();
			}
		});

		// Audit Status Label & JComboBox 2
		JLabel lblAuditStatus = new JLabel("Audit Status");
		lblAuditStatus.setFont(new Font("Calibri", Font.BOLD, 14));
		lblAuditStatus.setBounds(60, 130, 112, 31);
		iaForm.getContentPane().add(lblAuditStatus);

		final JComboBox auditStatus_dropDown = new JComboBox(InstantAppConstants.AUDITSTATUSVALUES);
		auditStatus_dropDown.setBounds(182, 135, 178, 20);
		iaForm.getContentPane().add(auditStatus_dropDown);
		auditStatus_dropDown.setSelectedIndex(-1);
		auditStatus_dropDown.setRenderer(new PromptComboBoxRenderer("Select Audit Status"));
		auditStatus_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				auditStatus = (String) auditStatus_dropDown.getSelectedItem();
			}
		});

		// NCR Status Label & JComboBox 3
		JLabel lblcurrentStatus = new JLabel("Current Status");
		lblcurrentStatus.setFont(new Font("Calibri", Font.BOLD, 14));
		lblcurrentStatus.setBounds(60, 190, 112, 31);
		iaForm.getContentPane().add(lblcurrentStatus);

		final JComboBox currentStatus_dropDown = new JComboBox(InstantAppConstants.NCRSTATUSVALUES);
		currentStatus_dropDown.setBounds(182, 195, 178, 20);
		iaForm.getContentPane().add(currentStatus_dropDown);
		currentStatus_dropDown.setSelectedIndex(-1);
		currentStatus_dropDown.setRenderer(new PromptComboBoxRenderer("Select NCR Status"));
		currentStatus_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentStatus = (String) currentStatus_dropDown.getSelectedItem();
			}
		});

		// Entity name Label & JComboBox 4
		JLabel lblEntityName = new JLabel("Entity Name");
		lblEntityName.setFont(new Font("Calibri", Font.BOLD, 14));
		lblEntityName.setBounds(60, 250, 112, 31);
		iaForm.getContentPane().add(lblEntityName);

		final JComboBox entityName_dropDown = new JComboBox(InstantAppConstants.ENTITYVALUES);
		entityName_dropDown.setBounds(182, 255, 178, 20);
		iaForm.getContentPane().add(entityName_dropDown);
		entityName_dropDown.setSelectedIndex(-1);
		entityName_dropDown.setRenderer(new PromptComboBoxRenderer("Select an Entity Name"));
		entityName_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				entityName = (String) entityName_dropDown.getSelectedItem();
			}
		});

		// Mode of Audit Label & JComboBox 5
		JLabel lblModeOfAudit = new JLabel("Mode of Audit");
		lblModeOfAudit.setFont(new Font("Calibri", Font.BOLD, 14));
		lblModeOfAudit.setBounds(60, 310, 112, 31);
		iaForm.getContentPane().add(lblModeOfAudit);

		final JComboBox modeOfAudit_dropDown = new JComboBox(InstantAppConstants.MODEOFAUDITVALUES);
		modeOfAudit_dropDown.setBounds(182, 315, 178, 20);
		iaForm.getContentPane().add(modeOfAudit_dropDown);
		modeOfAudit_dropDown.setSelectedIndex(-1);
		modeOfAudit_dropDown.setRenderer(new PromptComboBoxRenderer("Select Mode of Audit"));
		modeOfAudit_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modeOfAudit = (String) modeOfAudit_dropDown.getSelectedItem();
			}
		});

		// Auditor Name Label & JComboBox 6
		JLabel lblAuditorName = new JLabel("Auditor Name");
		lblAuditorName.setFont(new Font("Calibri", Font.BOLD, 14));
		lblAuditorName.setBounds(423, 70, 112, 31);
		iaForm.getContentPane().add(lblAuditorName);

		final JComboBox auditorName_dropDown = new JComboBox(InstantAppConstants.NAMEOFAUDITORVALUES);
		auditorName_dropDown.setBounds(545, 75, 178, 20);
		iaForm.getContentPane().add(auditorName_dropDown);
		auditorName_dropDown.setSelectedIndex(-1);
		auditorName_dropDown.setRenderer(new PromptComboBoxRenderer("Select Auditor Name"));
		auditorName_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nameofAuditor = (String) auditorName_dropDown.getSelectedItem();
			}
		});

		// Audit Period Label & JComboBox 7
		JLabel lblIso = new JLabel("ISO270012013");
		lblIso.setFont(new Font("Calibri", Font.BOLD, 14));
		lblIso.setBounds(423, 130, 112, 31);
		iaForm.getContentPane().add(lblIso);

		final JComboBox iso270012013_dropDown = new JComboBox(InstantAppConstants.ISO270012013VALUES);
		iso270012013_dropDown.setBounds(545, 135, 178, 20);
		iaForm.getContentPane().add(iso270012013_dropDown);
		iso270012013_dropDown.setSelectedIndex(-1);
		iso270012013_dropDown.setRenderer(new PromptComboBoxRenderer("Select an Item"));
		iso270012013_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iso270012013 = (String) iso270012013_dropDown.getSelectedItem();
			}
		});

		// Audit Period Label & JComboBox 8
		JLabel lblIso_1 = new JLabel("ISO90012008");
		lblIso_1.setFont(new Font("Calibri", Font.BOLD, 14));
		lblIso_1.setBounds(423, 190, 112, 31);
		iaForm.getContentPane().add(lblIso_1);

		final JComboBox iso90012008_dropDown = new JComboBox(InstantAppConstants.ISO90012008VALUES);
		iso90012008_dropDown.setBounds(545, 195, 178, 20);
		iaForm.getContentPane().add(iso90012008_dropDown);
		iso90012008_dropDown.setSelectedIndex(-1);
		iso90012008_dropDown.setRenderer(new PromptComboBoxRenderer("Select an Item"));
		iso90012008_dropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iso90012008 = (String) iso90012008_dropDown.getSelectedItem();
			}
		});

		// Audit Period Label & JComboBox 9
		JLabel lblControlName = new JLabel("Control Name");
		lblControlName.setFont(new Font("Calibri", Font.BOLD, 14));
		lblControlName.setBounds(423, 250, 112, 31);
		iaForm.getContentPane().add(lblControlName);

		// Filling in the default message in the JTextField("Default message")
		controlName_textField = new JTextField("Enter Control Name");
		controlName_textField.setBounds(545, 255, 178, 20);
		iaForm.getContentPane().add(controlName_textField);
		controlName_textField.setColumns(10);

		// To remove text when the text filed is in focus for editing
		controlName_textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				controlName_textField.setText("");
			}

			public void focusLost(FocusEvent e) {
				return;
			}
		});

		// Audit Period Label & JComboBox 10
		JLabel lblAuditLocation = new JLabel("Audit location");
		lblAuditLocation.setFont(new Font("Calibri", Font.BOLD, 14));
		lblAuditLocation.setBounds(423, 310, 112, 31);
		iaForm.getContentPane().add(lblAuditLocation);

		// Filling in the default message in the JTextField("Default message")
		auditLocation_textField = new JTextField("Enter Audit Location");
		auditLocation_textField.setColumns(10);
		auditLocation_textField.setBounds(545, 315, 178, 20);
		iaForm.getContentPane().add(auditLocation_textField);

		// To remove text when the text filed is in focus for editing
		auditLocation_textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				auditLocation_textField.setText("");
			}

			public void focusLost(FocusEvent e) {
				return;
			}
		});

		// NOTE display for mandatory Items
		JLabel lblItemMarkedWith = new JLabel("NOTE : Items marked with an asterik  (*) are mandatory fields.");
		lblItemMarkedWith.setForeground(new Color(204, 0, 0));
		lblItemMarkedWith.setFont(new Font("Calibri", Font.BOLD, 14));
		lblItemMarkedWith.setBounds(60, 376, 663, 31);
		iaForm.getContentPane().add(lblItemMarkedWith);

		// Button to reset details filled in the form
		JButton btnReset = new JButton("Reset");
		btnReset.setForeground(new Color(51, 0, 153));
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnReset.setBounds(423, 448, 89, 23);
		iaForm.getContentPane().add(btnReset);

		// Reset Button Listener ::: resetting values on click
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iso90012008_dropDown.setSelectedIndex(-1);
				iso270012013_dropDown.setSelectedIndex(-1);
				auditorName_dropDown.setSelectedIndex(-1);
				modeOfAudit_dropDown.setSelectedIndex(-1);
				entityName_dropDown.setSelectedIndex(-1);
				currentStatus_dropDown.setSelectedIndex(-1);
				auditStatus_dropDown.setSelectedIndex(-1);
				auditPeriod_dropDown.setSelectedIndex(-1);
				auditLocation_textField.setText("Enter Audit Location");
				controlName_textField.setText("Enter Control Name");
			}
		});

		// Button to submit details filled in the form
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setForeground(new Color(51, 0, 153));
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSubmit.setBounds(271, 448, 89, 23);
		iaForm.getContentPane().add(btnSubmit);
		// Submit Button Listener ::: Submitting values on click of Submit
		// button
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null == auditPeriod_dropDown.getSelectedItem()) {
					JOptionPane.showMessageDialog(null,
							"Audit Period is a mandatory field. Please select a value to proceed.");
				} else {

					auditLocation = auditLocation_textField.getText().equals("Enter Audit Location") ? null	: auditLocation_textField.getText();
					controlName = controlName_textField.getText().equals("Enter Control Name") ? null : controlName_textField.getText();

					formData = new HashMap<String, String>();

					formData.put("ia.auditPeriod", auditPeriod);
					formData.put("ia.auditStatus", auditStatus);
					formData.put("em.entityName", entityName);
					formData.put("ncr.currentStatus", currentStatus);
					formData.put("ia.modeofaudit", modeOfAudit);
					formData.put("ncr.iSO270012013", iso270012013);
					formData.put("ia.nameoftheAuditor", nameofAuditor);
					formData.put("ncr.iSOModel90012008", iso90012008);
					formData.put("ncr.controlName", controlName);
					formData.put("ia.auditLocation", auditLocation);

					log.info("formData ::::::   " + formData);
					try {
						excelData = new IAAuditService().getAuditFileDetails(formData);
						if (excelData.containsKey("exception")) {
							JOptionPane.showMessageDialog(null, (String) excelData.get("exception"));
						} else {
							List<Map<String, String>> tempListObj = (List<Map<String, String>>) excelData
									.get("auditDetails");

							if (tempListObj != null && !tempListObj.isEmpty()) {

								int tempListObjSize = tempListObj.size();

								Object[][] dataArr = new Object[tempListObjSize][6];
								Object[] tempStrArr = new Object[6];
								Object[] arrOfRowValues = null;

								for (int i = 0; i < tempListObjSize; i++) {
									arrOfRowValues = tempListObj.get(i).values().toArray();
									tempStrArr[0] = (i + 1);
									tempStrArr[1] = arrOfRowValues[0];
									tempStrArr[2] = arrOfRowValues[2];
									tempStrArr[3] = arrOfRowValues[8];
									tempStrArr[4] = arrOfRowValues[18];
									tempStrArr[5] = "View Report";
									for (int j = 0; j < tempStrArr.length; j++) {
										dataArr[i][j] = tempStrArr[j];
									}
								}
								JOptionPane.showMessageDialog(null, "Form Data Submitted");
								new CreateTable().makeTable(excelData, dataArr);
								iaForm.setVisible(false);
								iaForm.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "No Data Available. Please Reset and Try again!");
							}
						}
					} catch (Exception e) {
						log.info("Exception is :::", e);
						JOptionPane.showMessageDialog(null, "Error in fetching data!!!");
					}
				}
			}
		});
	}

	public void removeMinMaxClose(Component comp) {
		if (comp instanceof AbstractButton) {
			comp.getParent().remove(comp);
		}
		if (comp instanceof Container) {
			Component[] comps = ((Container) comp).getComponents();
			for (int x = 0, y = comps.length; x < y; x++) {
				removeMinMaxClose(comps[x]);
			}
		}
	}
}

class PromptComboBoxRenderer extends BasicComboBoxRenderer {

	private static final long serialVersionUID = -6907264842390917638L;
	private String prompt;

	/*
	 * Set the text to display when no item has been selected.
	 */
	public PromptComboBoxRenderer(String prompt) {
		this.prompt = prompt;
	}

	/*
	 * Custom rendering to display the prompt text when no item is selected
	 */
	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value == null)
			setText(prompt);
		return this;
	}
} 