package gov.mea.psp.instantApps.service;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.mea.psp.instantApps.dao.IAAuditNoSpringDao;
import gov.mea.psp.instantApps.util.PropertiesCache;
import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class IAAuditService implements IAAuditServiceInterface {

	private static final Logger log = LogManager.getLogger(IAAuditService.class);

	public Map<String, Object> getAuditFileDetails(Map<String, String> formData) {
		return new IAAuditNoSpringDao().getExcelData(formData);
	}

	@SuppressWarnings("unchecked")
	public boolean makeExcelForMe(Map<String, Object> excelData, String audRefNo) throws Exception {

		log.info("Inside write reports of makeExcelForMe. | audRefNo -->" + audRefNo);

		boolean isTemplateDirPresent = false;
		boolean isTemplateFilePresent = false;

		File directory = new File(PropertiesCache.getProperty("ia.template.directory.path"));
		if (!directory.exists()) {
			log.info("Directory not present, creating it!!!");
			if (directory.mkdirs()) {
				isTemplateDirPresent = true;
				log.info("Directory successfully Created!!!");
			} else {
				log.info("Not able to create directory!!!");
			}
		} else {
			isTemplateDirPresent = true;
			log.info("Directory is already created!!!");
		}
		if (isTemplateDirPresent) {

			File auditFile = new File(directory.getAbsolutePath() + System.getProperty("file.separator")
					+ PropertiesCache.getProperty("ia.template.name"));

			if (!auditFile.exists()) {
				log.info("Creating file!!!");
				BufferedInputStream buffInpStream = new BufferedInputStream(this.getClass().getClassLoader()
						.getResourceAsStream(PropertiesCache.getProperty("ia.template.path.in.jar")));

				try {
					BufferedOutputStream buffOutSream = new BufferedOutputStream(new FileOutputStream(auditFile));
					int data = 0;
					while ((data = buffInpStream.read()) != -1) {
						buffOutSream.write(data);
					}
					buffInpStream.close();
					buffOutSream.close();
					isTemplateFilePresent = true;
					log.info("Template file is created!!!");

				} catch (IOException ioExec) {
					log.error("Exception in creating template file!!!", ioExec);
					throw ioExec;
				}
			} else {
				isTemplateFilePresent = true;
				log.info("File is already created!!!");
			}

			if (isTemplateFilePresent) {

				try {
					WorkbookSettings wbSettings = new WorkbookSettings();
					wbSettings.setLocale(new Locale("en", "EN"));

					File file;
					File filename;

					List<Map<String, String>> getAuditDetails = (ArrayList<Map<String, String>>) excelData
							.get("auditDetails");
					List<Map<String, String>> getNCRDetails = (ArrayList<Map<String, String>>) excelData
							.get("ncrDetails");

					String generatedExcelFileName = null;
					String appModels = null;
					String audRef = null;
					StringBuilder temp = null;

					if (!getAuditDetails.isEmpty()) {

						file = new File(auditFile.getAbsolutePath());

						int auditDataposition = 0;

						for (Map<String, String> obj : getAuditDetails) {
							Object[] tempArr = obj.values().toArray();
							if (audRefNo.equals((String) tempArr[0])) {
								break;
							}
							auditDataposition++;
						}
						generatedExcelFileName = "IAAuditNCR_"
								+ (String) getAuditDetails.get(auditDataposition).get("auditReference") + ".xls";
						filename = new File(
								"InstantAppReports" + System.getProperty("file.separator") + generatedExcelFileName);

						Workbook wb = Workbook.getWorkbook(file);
						WritableWorkbook workbook = Workbook.createWorkbook(filename, wb, wbSettings);
						WritableSheet auditSheet = workbook.getSheet(0);

						temp = new StringBuilder("");
						if ("1".equals((String) getAuditDetails.get(auditDataposition).get("iso90012008"))) {
							temp.append("ISO90012008, ");
						}
						if ("1".equals((String) getAuditDetails.get(auditDataposition).get("isoiec270012013"))) {
							temp.append("ISOIEC270012013, ");
						}
						if ("1".equals((String) getAuditDetails.get(auditDataposition).get("iso2000012011"))) {
							temp.append("ISO2000012011");
						}

						appModels = temp.toString();
						log.info("appModels --" + appModels);
						if (!appModels.trim().isEmpty()
								&& (appModels.trim().length() - 1) == (appModels.lastIndexOf(","))) {
							appModels = appModels.substring(0, appModels.lastIndexOf(",")).trim();
						}

						log.info("Map data ::" + getAuditDetails.get(auditDataposition));

						addItemToSheet(auditSheet, 1, 2, (String) getAuditDetails.get(auditDataposition).get("auditReference"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 3, (String) getAuditDetails.get(auditDataposition).get("entityName"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 4, (String) getAuditDetails.get(auditDataposition).get("auditPeriod"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 5, (String) getAuditDetails.get(auditDataposition).get("dateOfAudit"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 6, (String) getAuditDetails.get(auditDataposition).get("nameoftheAuditor"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 7, (String) getAuditDetails.get(auditDataposition).get("nameoftheAuditee"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 8, (String) getAuditDetails.get(auditDataposition).get("auditLocation"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 11, (String) getAuditDetails.get(auditDataposition).get("entityBrief"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 20, appModels, getNormalFormatText());
						addItemToSheet(auditSheet, 1, 23, (String) getAuditDetails.get(auditDataposition).get("documentsReferredintheAudit"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 34, (String) getAuditDetails.get(auditDataposition).get("bestPracticesIdentifiedintheA"), getNormalFormatText());
						addItemToSheet(auditSheet, 1, 46, (String) getAuditDetails.get(auditDataposition).get("majorIssues"), getNormalFormatText());

						WritableSheet ncrSheet = workbook.getSheet(1);

						audRef = (String) getAuditDetails.get(auditDataposition).get("auditReference");

						if (!getNCRDetails.isEmpty()) {
							int row = 4;
							for (Map<String, String> obj : getNCRDetails) {

								if (audRef.equals(obj.get("auditReference"))) {

									addItemToSheet(ncrSheet, 0, row, ((Integer) (row - 3)).toString(), getRightFormatText());
									addItemToSheet(ncrSheet, 1, row, (String) obj.get("auditReference"), getNormalFormatText()); // AUDITREFERENCE
									addItemToSheet(ncrSheet, 2, row, (String) obj.get("nCRID"), getNormalFormatText()); // NCRID
									addItemToSheet(ncrSheet, 3, row, (String) obj.get("processArea"), getNormalFormatText()); // PROCESSAREA
									addItemToSheet(ncrSheet, 4, row, (String) obj.get("findingDetails"), getNormalFormatText()); // FINDINGDETAILS
									addItemToSheet(ncrSheet, 5, row, (String) obj.get("findingCategory"), getNormalFormatText()); // FINDINGCATEGORY
									addItemToSheet(ncrSheet, 6, row, (String) obj.get("currentStatus"), getNormalFormatText()); // CURRENTSTATUS
									addItemToSheet(ncrSheet, 7, row, "1".equals((String) obj.get("findingaccepted")) ? "Yes" : "No", getNormalFormatText()); // FINDINGACCEPTED
									addItemToSheet(ncrSheet, 8, row, (String) obj.get("iSOModel90012008"), getNormalFormatText()); // ISOMODEL90012008
									addItemToSheet(ncrSheet, 9, row, (String) obj.get("iSO270012013"), getNormalFormatText()); // ISO270012013
									addItemToSheet(ncrSheet, 10, row, (String) obj.get("iSOModel2000012011"), getNormalFormatText()); // ISOMODEL2000012011
									addItemToSheet(ncrSheet, 11, row, (String) obj.get("additionalRemarks"), getNormalFormatText()); // ADDITIONALREMARKS
									addItemToSheet(ncrSheet, 12, row, (String) obj.get("causeofNonConformance"), getNormalFormatText()); // CAUSEOFNONCONFORMANCE
									addItemToSheet(ncrSheet, 13, row, (String) obj.get("proposedActualCorrectiveActio"), getNormalFormatText()); // PROPOSEDACTUALCORRECTIVEACTIO
									addItemToSheet(ncrSheet, 14, row, (String) obj.get("actionBy"), getNormalFormatText()); // ACTIONBY
									addItemToSheet(ncrSheet, 15, row, (String) obj.get("verifiedBy"), getNormalFormatText()); // VERIFIEDBY
									addItemToSheet(ncrSheet, 16, row, (String) obj.get("plannedclosuredate"), getNormalFormatText()); // PLANNEDCLOSUREDATE
									addItemToSheet(ncrSheet, 17, row, (String) obj.get("actualclosuredate"), getNormalFormatText()); // ACTUALCLOSUREDATE
									addItemToSheet(ncrSheet, 18, row, (String) obj.get("controlName"), getNormalFormatText()); // CONTROLNAME
									addItemToSheet(ncrSheet, 19, row, (String) obj.get("nameoftheauditor"), getNormalFormatText()); // NAMEOFTHEAUDITOR
									addItemToSheet(ncrSheet, 20, row, (String) obj.get("entitylead"), getNormalFormatText()); // ENTITYLEAD
									row++;
								}
							}
						}
						workbook.write();
						workbook.close();
						Desktop.getDesktop().open(filename);

						log.info("Exiting write reports of makeExcelForMe ");
					} else {
						log.info("getAuditDetails is null");
					}
				} catch (IOException ioExec) {
					log.error("Exception in generating excel sheet", ioExec);
					throw ioExec;
				} catch (BiffException biffExec) {
					log.error("Exception in generating excel sheet", biffExec);
					throw biffExec;
				} catch (WriteException writeExec) {
					log.error("Exception in generating excel sheet", writeExec);
					throw writeExec;
				} catch (JXLException jxlExec) {
					log.error("Exception in generating excel sheet", jxlExec);
					throw jxlExec;
				} catch (Exception e) {
					log.error("Exception in generating excel sheet", e);
					throw e;
				}

			}
		}
		return isTemplateDirPresent && isTemplateFilePresent == true ? true : false;
	}

	private void addItemToSheet(WritableSheet sheet, int column, int row, String s, WritableCellFormat format)
			throws Exception {

		Label label = null;
		if (null != s) {
			s = s.trim();
		}

		try {
			label = new Label(column, row, s, format);
			sheet.addCell(label);
			CellView cell = sheet.getColumnView(column);
			cell.setAutosize(false);
			sheet.setColumnView(column, cell);

		} catch (Exception e) {
			log.error("Exception Occurred in addItemToSheet Outer : " + e);
			throw e;
		}
	}

	private WritableCellFormat getNormalFormatText() throws IOException, WriteException, BiffException, Exception {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		WritableCellFormat times = new WritableCellFormat(times10pt);
		times.setWrap(true);
		times.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		times.setAlignment(Alignment.LEFT);
		times.setVerticalAlignment(VerticalAlignment.CENTRE);
		times.setBackground(Colour.WHITE);
		return times;
	}

	private WritableCellFormat getRightFormatText() throws IOException, WriteException, BiffException, Exception {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		WritableCellFormat times = new WritableCellFormat(times10pt);
		times.setWrap(true);
		times.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		times.setAlignment(Alignment.RIGHT);
		times.setVerticalAlignment(VerticalAlignment.CENTRE);
		times.setBackground(Colour.WHITE);
		return times;
	}
}
