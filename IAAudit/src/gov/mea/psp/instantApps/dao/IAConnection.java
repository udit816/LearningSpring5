package gov.mea.psp.instantApps.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.mea.psp.instantApps.util.PropertiesCache;

public class IAConnection {

	private static final Logger log = LogManager.getLogger(IAConnection.class);

	private Connection conn = null;

	public Connection getConnection() {

		try {
			Class.forName(PropertiesCache.getProperty("db.driverName"));
			conn = DriverManager.getConnection(PropertiesCache.getProperty("db.address"),
					PropertiesCache.getProperty("db.userName"), PropertiesCache.getProperty("db.password"));
		} catch (ClassNotFoundException classNotFoundException) {
			log.error("DataBase driver not found!!! | Exception trace is :::", classNotFoundException);
			return conn;
		} catch (SQLException sqlExe) {
			log.error("Connection to DataBase is failed!!! | Exception trace is :::", sqlExe);
		} catch (Exception exe) {
			log.error("Connection to DataBase is failed!!! | Exception trace is :::", exe);
		} 
		return conn;
	}
}