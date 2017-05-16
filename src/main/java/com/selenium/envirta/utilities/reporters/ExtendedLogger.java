package com.selenium.envirta.utilities.reporters;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Maps;
import org.testng.xml.XmlSuite;

/**
 * Class that expands upon TestNG's Reporter class. Includes a method to store
 * screenshots while the test is running without writing to disk, as well as
 * creating a time-stamped folder for that run's results.
 * 
 * @author SRayani
 * 
 */
public class ExtendedLogger extends Reporter {

	protected static Map<String, byte[]> screenshotsMap = Maps.newHashMap();
	private static String folderName;

	/**
	 * Logs the screenshot and its associated ITestResult to a run-wide map. The
	 * ITestResult's toString() is the key for the map.
	 * 
	 * @param b
	 * @param m
	 */
	private static synchronized void log(byte[] b, ITestResult m) {
		screenshotsMap.put(m.getInstance().toString(), b);
	}

	/**
	 * Logs a screenshot that is in byte[] form.
	 * 
	 * @param b
	 */
	public static void logScreenshot(byte[] b) {
		log(b, getCurrentTestResult());
	}

	/**
	 * Gets the folder name for this test run.
	 * 
	 * @return
	 */
	public static String getFolderName() {
		return folderName;
	}

	/**
	 * Sets the folder name for this test run.
	 * 
	 * @param path
	 */
	public static void setFolderName(String path) {
		folderName = path;
	}

	/**
	 * Creates a folder for the test run results. Uses the first suite listed in
	 * the test run, and the date and time of when this method is called to
	 * generate a unique folder name.
	 * 
	 * @param xml
	 * @param outputDir
	 * @return
	 */
	protected String createFolder(XmlSuite xml, String outputDir) {

		Calendar currentDater = Calendar.getInstance();
		String currentDate = getTodaysDate();

		// Get current hour
		int hour = currentDater.get(11);
		// Get current minute
		int minute = currentDater.get(12);
		// Get current second
		int second = currentDater.get(13);

		String suiteName = xml.getName();
		suiteName = suiteName.replaceAll(" ", "_");
		String folderPath = outputDir + "\\" + suiteName + "_" + currentDate
				+ "_" + hour + "h" + minute + "m" + second + "s" + "\\";
		File newFolder = new File(folderPath);
		newFolder.mkdir();
		return folderPath;
	}

	/**
	 * Gets today's date in string form (YYYY-M-D)
	 * @return
	 */
	protected String getTodaysDate() {
		Calendar currentDater = Calendar.getInstance();
		// Get current year
		int year = currentDater.get(1);
		// Get current month, offset by 1 (January = 0)
		int month = currentDater.get(2) + 1;
		// Get current day of month
		int day = currentDater.get(5);
		System.out.println("printdate: " + year + "-" + month + "-" + day);
		return year + "-" + month + "-" + day;
	}

}
