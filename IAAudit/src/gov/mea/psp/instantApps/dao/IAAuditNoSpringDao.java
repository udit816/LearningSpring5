package gov.mea.psp.instantApps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class IAAuditNoSpringDao {

	private static final Logger log = LogManager.getLogger(IAAuditNoSpringDao.class);

	public Map<String, Object> getExcelData(Map<String, String> formData) {

		log.info("Inside getExcelData");

		/* We can use single prepared statement for multiple queries, but here, multiple(read as two)
		 * statement objects are used for simplicity purposes and the difference in performance is not huge.
		 * */

		PreparedStatement statementForAuditData = null;
		PreparedStatement statementForNCRData = null;

		Connection conectionObj = new IAConnection().getConnection();
		Map<String, Object> excelData = new HashMap<String, Object>();

		//Checks if connection to database is created or not
		if (conectionObj == null) {
			excelData.put("exception", "Error while connection to DataBase!!!");
			return excelData;
		} else {
			List<Map<String, String>> getAuditDetails = null;
			List<Map<String, String>> getNCRDetails = null;
			Map<String,String> auditDetailsMap = null;
			Map<String,String> ncrDetailsMap = null;

			StringBuilder queryForAuditDataParam = null;
			List<Object> tempList = new ArrayList<Object>();

			/**
			 * Creates dynamic query based on the parameters received
			 */
			for(Entry<String, String> mapObj : formData.entrySet()) {

				if (null != mapObj.getValue() && !"".equals(mapObj.getValue().trim())) {

					if (null == queryForAuditDataParam) {
						queryForAuditDataParam = new StringBuilder(mapObj.getKey()).append(" = ? ");
					} else {
						queryForAuditDataParam.append("AND ").append(mapObj.getKey()).append(" = ? ");
					}
					tempList.add(mapObj.getValue());
				}
			}
			/**
			 * Database query for fetching audit data from the database
			 */
			StringBuilder queryForAuditData = new StringBuilder("SELECT DISTINCT IA.AUDITREFERENCE, EM.ENTITYNAME, IA.AUDITPERIOD, IA.DATEOFAUDIT, IA.NAMEOFTHEAUDITOR, ");
			queryForAuditData.append("IA.NAMEOFTHEAUDITEE, IA.AUDITLOCATION, IA.NUMBEROFNCRSOPENFROMPREVIOUSA, IA.AUDITSTATUS, IA.ENTITYBRIEF, IA.BESTPRACTICESIDENTIFIEDINTHEA, ");
			queryForAuditData.append("IA.BESTPRACTICESSHAREDBYAUDITOR, IA.PLANNEDDATE, IA.ACTUALSTARTDATE, IA.ACTUALENDDATE, IA.DOCUMENTSREFERREDINTHEAUDIT, IA.MAJORISSUES, ");
			queryForAuditData.append("IA.SUMMARYOFNCRSOPENFROMPREVIOUS, IA.MODEOFAUDIT, IA.ISO90012008, IA.ISOIEC270012013, IA.ISO2000012011, IA.UPDATETIME, IA.ENTITYID ");
			queryForAuditData.append("FROM INITIATEAUDIT IA, ENTITYMASTER EM, INITIATENCR NCR WHERE IA.ENTITYID = EM.ENTITYID AND " + queryForAuditDataParam.toString());

			log.info("queryForAuditData ::: "+ queryForAuditData);

			try {
				statementForAuditData = conectionObj.prepareStatement(queryForAuditData.toString());

				int temp = 1;
				/**
				 * Setting parameters for the above defined query
				 */
				for (Object obj : tempList) {
					if (obj instanceof Integer) {
						statementForAuditData.setInt(temp, (Integer)obj);
					} else {
						statementForAuditData.setString(temp, (String)obj);
					}
					temp++;
				}
				/**
				 * Creates result of queryForAuditDataQuery
				 */
				ResultSet rs = statementForAuditData.executeQuery();
				if (!rs.isBeforeFirst()) {

				} else {
					getAuditDetails = new ArrayList<Map<String, String>>();

					ResultSetMetaData meta = rs.getMetaData();

					while (rs.next()) {
						/**
						 * Created LinkedHashMapObjects for each row and keep Column Names as keys and Column Values as Values
						 * ResultSetMetaData provides the columns names by getColumnName's value 
						 */
						auditDetailsMap = new LinkedHashMap<String, String>();
						for (int i = 1; i <= meta.getColumnCount(); i++) {
							auditDetailsMap.put(meta.getColumnName(i), rs.getString(meta.getColumnName(i)));
						}
						/**
						 * Put LinkedHashMap in a list to store all rows
						 */
						getAuditDetails.add(auditDetailsMap);
					}

					if (!getAuditDetails.isEmpty()) {

						StringBuilder queryForNCRDataParam = null;
						tempList = new ArrayList<Object>();
						/**
						 * Creates dynamic query based on the audit reference number received from the above query
						 */
						for (int i = 0; i < getAuditDetails.size(); i++) {
							if (null == queryForNCRDataParam) {
								queryForNCRDataParam = new StringBuilder("?");
							} else {
								queryForNCRDataParam.append(", ?");
							}
							tempList.add(getAuditDetails.get(i).get("auditReference"));
						}
						/**
						 * Database query for fetching audit data from the database
						 */
						StringBuilder queryForNCRData = new StringBuilder("SELECT AUDITREFERENCE, PROCESSAREA, FINDINGDETAILS, FINDINGCATEGORY, CURRENTSTATUS, ");
						queryForNCRData.append("ADDITIONALREMARKS, CAUSEOFNONCONFORMANCE, PROPOSEDACTUALCORRECTIVEACTIO, PLANNEDCLOSUREDATE, ACTUALCLOSUREDATE, ");
						queryForNCRData.append("ACTIONBY, VERIFIEDBY, FINDINGACCEPTED, ISO270012013, NCRID, ISOMODEL90012008, ISOMODEL2000012011, CONTROLNAME, ");
						queryForNCRData.append("NAMEOFTHEAUDITOR, ENTITYLEAD FROM INITIATENCR WHERE AUDITREFERENCE IN (" + queryForNCRDataParam.toString() + ")");

						statementForNCRData = conectionObj.prepareStatement(queryForNCRData.toString());
						temp = 1;
						/**
						 * Setting parameters for the above defined query
						 */
						for (Object obj : tempList) {
							statementForNCRData.setString(temp, (String)obj);
							temp++;
						}
						/**
						 * Creates result of queryForAuditDataQuery
						 */
						rs = statementForNCRData.executeQuery();

						getNCRDetails = new ArrayList<Map<String, String>>();

						meta = rs.getMetaData();

						while (rs.next()) {
							/**
							 * Created LinkedHashMapObjects for each row and keep Column Names as keys and Column Values as Values
							 * ResultSetMetaData provides the columns names by getColumnName's value 
							 */
							ncrDetailsMap = new LinkedHashMap<String, String>();
							for (int i = 1; i <= meta.getColumnCount(); i++) {
								ncrDetailsMap.put(meta.getColumnName(i), rs.getString(meta.getColumnName(i)));
							}
							/**
							 * Put LinkedHashMap in a list to store all rows
							 */
							getNCRDetails.add(ncrDetailsMap);
						}

						excelData.put("auditDetails", getAuditDetails);
						excelData.put("ncrDetails", getNCRDetails);

						/**
						 * Close resultset
						 */
						if (rs != null) {
							try {
								rs.close();
							} catch (SQLException sqlExc) {
								log.error("ResultSet object could not be released!!! | Exception trace is :::", sqlExc);
								excelData.put("exception", "ResultSet object could not be released.");
							}
						}
					}
				} 
			} catch (SQLException sqlException) {
				log.error("Error in querying Database, Please check the SQL queries!!! | Exception trace is", sqlException);
				excelData.put("exception", "Error in querying Database, Please check the SQL queries.");
			} finally {
				if (statementForNCRData != null) {
					try {
						statementForNCRData.close();
					}catch (SQLException ncrCloseEx) {
						log.error("Error in closing Statement Object!!! | Exception trace is", ncrCloseEx);
						excelData.put("exception", "Error in accessing DataBase.");
					}
				}
				if (statementForAuditData != null) {
					try {
						statementForAuditData.close();
					}catch (SQLException auditCloseEx) {
						log.error("Error in closing Statement Object!!! | Exception trace is", auditCloseEx);
						excelData.put("exception", "Error in accessing DataBase.");
					}
				}
				if (conectionObj != null) {
					try {
						conectionObj.close();
					}catch (SQLException conObjCloseEx) {
						log.error("Error in closing Database connection!!! | Exception trace is", conObjCloseEx);
						excelData.put("exception", "Error in accessing DataBase.");
					}
				}
			}
			return excelData;
		}
	}
}
