package gov.mea.psp.instantApps.service;

import java.util.Map;

public interface IAAuditServiceInterface {

	public Map<String, Object> getAuditFileDetails(Map<String, String> formData);

	public boolean makeExcelForMe(Map<String, Object> excelData, String getARN) throws Exception;
}
