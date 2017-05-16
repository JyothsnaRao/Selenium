package com.selenium.envirta.utilities.reporters;

import com.selenium.envirta.utilities.AbstractTestCase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

/**
 * Exports the results of the test run to Excel form. Lists summary of tests
 * run, divided by test suite, then result (pass/fail/skip), and on a second tab
 * lists failed tests, the last entry logged by the reporter, and the screen
 * shot taken when the test failed. Differentiates between tests run with
 * different parameters. Is not intended to give extremely detailed results of
 * failures.
 * 
 * @author S Rayani
 * 
 */
public class ExcelReporter extends ExtendedLogger implements IReporter {

	private Workbook results;
	private Sheet summarySheet;
	private Sheet failureDetailsSheet;

	private Row currentSummaryRow;
	private Cell currentSummaryCell;
	private Row currentFailureDetailsRow;
	private Cell currentFailureDetailsCell;

	private int currentSummaryRowNum;
	private int currentFailureDetailsRowNum;

	private CellStyle headerStyle;
	private CellStyle passStyle;
	private CellStyle failStyle;
	private CellStyle skippedStyle;
	private CellStyle tableCellStyle;

	private String[] summaryColumnHeaders = { "Name", "Parameters" };

	/**
	 * Parses test result data and generates an excel file with results.
	 */
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {

		try {

			// Create new excel file for results
			results = new XSSFWorkbook();

			// Initialize cell trackers
			currentSummaryRowNum = 0;
			currentFailureDetailsRowNum = 0;

			// Set CellStyles
			setStyles();

			// Create Summary Sheet and Failed Tests sheet in workbook
			summarySheet = results.createSheet("Test Summary");
			failureDetailsSheet = results.createSheet("Failure Details");

			// Make a separate results section for each suite
			for (ISuite i : suites) {
				// Only record suites with results
				if (!i.getResults().isEmpty()) {
					// suite.getResults returns map of test case names to
					// ISuiteResult, which represents the aggregate results of a
					// 'test' as defined on the xml. So for a factory that
					// makes 4 test objects, ISuiteResult for that test will
					// have 4 results in it(split among passedtests,
					// failedtests, and skippedtests).

					Map<String, ISuiteResult> suiteTests = i.getResults();
					Map<String, IResultMap> passedTests = new TreeMap<String, IResultMap>();
					Map<String, IResultMap> failedTests = new TreeMap<String, IResultMap>();
					Map<String, IResultMap> skippedTests = new TreeMap<String, IResultMap>();

					// Splits out results into three different maps: passed
					// tests, failed tests, and skipped tests
					for (String res : suiteTests.keySet()) {
						ITestContext tc = suiteTests.get(res).getTestContext();
						passedTests.put(res, tc.getPassedTests());
						failedTests.put(res, tc.getFailedTests());
						skippedTests.put(res, tc.getSkippedTests());
					}

					int numOfPassedTests = getNumOfTests(passedTests);
					int numOfFailedTests = getNumOfTests(failedTests);
					int numOfSkippedTests = getNumOfTests(skippedTests);

					// Configure Summary excel cell with suite name at (row, 0)
					String suiteName = i.getName();
					if (currentSummaryRowNum > 0) {
						currentSummaryRowNum = currentSummaryRowNum + 2;
					}
					currentSummaryRow = summarySheet
							.createRow(currentSummaryRowNum);
					currentSummaryCell = currentSummaryRow.createCell(0);
					currentSummaryCell.setCellStyle(headerStyle);
					currentSummaryCell.setCellValue(suiteName);

					// Configure pass cells

					// Create passed test identifier
					currentSummaryRowNum += 2;
					currentSummaryRow = summarySheet
							.createRow(currentSummaryRowNum);
					currentSummaryCell = currentSummaryRow.createCell(0);
					currentSummaryCell.setCellStyle(passStyle);
					currentSummaryCell.setCellValue("Passed Tests:");
					currentSummaryCell = currentSummaryRow.createCell(1);
					currentSummaryCell.setCellStyle(passStyle);
					currentSummaryCell.setCellValue(numOfPassedTests);

					// create passed header and result rows if passed results
					// exist
					// Create pass header
					if (numOfPassedTests != 0) {
						currentSummaryRowNum++;
						currentSummaryRow = summarySheet
								.createRow(currentSummaryRowNum);
						for (int c = 0; c < summaryColumnHeaders.length; c++) {
							currentSummaryCell = currentSummaryRow
									.createCell(c);
							currentSummaryCell.setCellStyle(passStyle);
							currentSummaryCell
									.setCellValue(summaryColumnHeaders[c]);
						}

						// Fill out Passed test results
						fillOutResultsInExcel(summarySheet, passedTests);
					}

					currentSummaryRowNum++;

					// Create failed test identifier
					currentSummaryRowNum++;
					currentSummaryRow = summarySheet
							.createRow(currentSummaryRowNum);
					currentSummaryCell = currentSummaryRow.createCell(0);
					currentSummaryCell.setCellStyle(failStyle);
					currentSummaryCell.setCellValue("Failed Tests:");
					currentSummaryCell = currentSummaryRow.createCell(1);
					currentSummaryCell.setCellStyle(failStyle);
					currentSummaryCell.setCellValue(numOfFailedTests);

					// create header and result rows if there are failed results
					// Create fail header
					if (numOfFailedTests != 0) {
						currentSummaryRowNum++;
						currentSummaryRow = summarySheet
								.createRow(currentSummaryRowNum);
						for (int c = 0; c < summaryColumnHeaders.length; c++) {
							currentSummaryCell = currentSummaryRow
									.createCell(c);
							currentSummaryCell.setCellStyle(failStyle);
							currentSummaryCell
									.setCellValue(summaryColumnHeaders[c]);
						}

						// Fill out Failed test results
						fillOutResultsInExcel(summarySheet, failedTests);
					}

					// Fill out Failure Details sheet
					addFailureDetails(i, failedTests);
					currentSummaryRowNum++;

					// Create skipped test identifier
					currentSummaryRowNum++;
					currentSummaryRow = summarySheet
							.createRow(currentSummaryRowNum);
					currentSummaryCell = currentSummaryRow.createCell(0);
					currentSummaryCell.setCellStyle(skippedStyle);
					currentSummaryCell.setCellValue("Skipped Tests:");
					currentSummaryCell = currentSummaryRow.createCell(1);
					currentSummaryCell.setCellStyle(skippedStyle);
					currentSummaryCell.setCellValue(numOfSkippedTests);

					// Create skip header and test result rows, if there are
					// skipped tests
					// Create header
					if (numOfSkippedTests != 0) {
						currentSummaryRowNum++;
						currentSummaryRow = summarySheet
								.createRow(currentSummaryRowNum);
						for (int c = 0; c < summaryColumnHeaders.length; c++) {
							currentSummaryCell = currentSummaryRow
									.createCell(c);
							currentSummaryCell.setCellStyle(skippedStyle);
							currentSummaryCell
									.setCellValue(summaryColumnHeaders[c]);
						}

						// Fill out skipped test results
						fillOutResultsInExcel(summarySheet, skippedTests);
					}
				}
			}

			// Resize columns so data fits
			for (int col = 0; col <= summarySheet.getRow(2).getLastCellNum(); col++) {
				summarySheet.autoSizeColumn(col);
			}
			if (failureDetailsSheet.getRow(2) == null) {
				failureDetailsSheet.autoSizeColumn(0);
			} else {
				for (int col = 0; col <= failureDetailsSheet.getRow(2)
						.getLastCellNum(); col++) {
					failureDetailsSheet.autoSizeColumn(col);
				}
			}

			// Write excel File
			String currentDate = getTodaysDate();
			String fileName = xmlSuites.get(0).getName() + "_Results_Summary"
					+ "_" + currentDate + ".xlsx";
			fileName = fileName.replaceAll(" ", "_");
			String folderPath = createFolder(xmlSuites.get(0), outputDirectory);
			FileOutputStream fileOut = null;
			fileOut = new FileOutputStream(folderPath + fileName);
			results.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			System.out.println("print failed");
			System.out.println(e.getMessage() + e.getStackTrace());
		}

	}

	/**
	 * Sets the style for each type of cell in the report (header cells, passed
	 * header cells, failed header cells, skipped header cells, and general
	 * table cells)
	 */
	private void setStyles() {

		Font whiteFont = results.createFont();
		whiteFont.setColor(HSSFColor.WHITE.index);

		headerStyle = results.createCellStyle();
		headerStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
		headerStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		headerStyle.setFillPattern(HSSFColor.YELLOW.index);
		headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		headerStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		headerStyle.setBorderRight(CellStyle.BORDER_MEDIUM);

		passStyle = results.createCellStyle();
		passStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
		passStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		passStyle.setFillPattern(HSSFColor.GREEN.index);
		passStyle.setBorderBottom(CellStyle.BORDER_THIN);
		passStyle.setBorderTop(CellStyle.BORDER_THIN);
		passStyle.setBorderLeft(CellStyle.BORDER_THIN);
		passStyle.setBorderRight(CellStyle.BORDER_THIN);
		passStyle.setFont(whiteFont);

		failStyle = results.createCellStyle();
		failStyle.setFillBackgroundColor(HSSFColor.RED.index);
		failStyle.setFillForegroundColor(HSSFColor.RED.index);
		failStyle.setFillPattern(HSSFColor.RED.index);
		failStyle.setBorderBottom(CellStyle.BORDER_THIN);
		failStyle.setBorderTop(CellStyle.BORDER_THIN);
		failStyle.setBorderLeft(CellStyle.BORDER_THIN);
		failStyle.setBorderRight(CellStyle.BORDER_THIN);
		failStyle.setFont(whiteFont);

		skippedStyle = results.createCellStyle();
		skippedStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		skippedStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		skippedStyle.setFillPattern(HSSFColor.WHITE.index);
		skippedStyle.setBorderBottom(CellStyle.BORDER_THIN);
		skippedStyle.setBorderTop(CellStyle.BORDER_THIN);
		skippedStyle.setBorderLeft(CellStyle.BORDER_THIN);
		skippedStyle.setBorderRight(CellStyle.BORDER_THIN);

		tableCellStyle = results.createCellStyle();
		tableCellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		tableCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		tableCellStyle.setFillPattern(HSSFColor.WHITE.index);
		tableCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		tableCellStyle.setBorderTop(CellStyle.BORDER_THIN);
		tableCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		tableCellStyle.setBorderRight(CellStyle.BORDER_THIN);
		tableCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
	}

	/**
	 * Computes total number of tests.
	 * 
	 * @param testMap
	 * @return
	 */
	private int getNumOfTests(Map<String, IResultMap> testMap) {
		int counter = 0;
		for (IResultMap irm : testMap.values()) {
			counter += irm.size();
		}
		return counter;
	}

	/**
	 * Fills out the results part of a table with the data in testMap, on excel
	 * Sheet sheet.
	 * 
	 * @param sheet
	 * @param testMap
	 */
	private void fillOutResultsInExcel(Sheet sheet,
			Map<String, IResultMap> testMap) {

		for (String testName : testMap.keySet()) {
			for (ITestResult result : testMap.get(testName).getAllResults()) {
				currentSummaryRowNum++;
				currentSummaryRow = sheet.createRow(currentSummaryRowNum);

				currentSummaryCell = currentSummaryRow.createCell(0);
				currentSummaryCell.setCellStyle(tableCellStyle);
				currentSummaryCell.setCellValue(testName);

				currentSummaryCell = currentSummaryRow.createCell(1);
				currentSummaryCell.setCellStyle(tableCellStyle);
				currentSummaryCell.setCellValue(getRelevantParams(result));
				
				System.out.println("Test Results: " + getRelevantParams(result));
			}
		}
	}

	/**
	 * Adds each failed test case in failedTests and it's associated screen shot
	 * to the failure details sheet.
	 * 
	 * @param iSuite
	 * @param failedTests
	 * @throws IOException
	 */
	private void addFailureDetails(ISuite iSuite,
			Map<String, IResultMap> failedTests) throws IOException {

		if (failedTests.size() > 0) {

			String suiteName = iSuite.getName();
			
			System.out.println("Name of the failed suite: " + suiteName);

			// Configure Failure Details excel sheet with suite name at (row, 0)
			if (currentFailureDetailsRowNum > 0) {
				currentFailureDetailsRowNum = currentFailureDetailsRowNum + 2;
			}
			currentFailureDetailsRow = failureDetailsSheet
					.createRow(currentFailureDetailsRowNum);
			currentFailureDetailsCell = currentFailureDetailsRow.createCell(0);
			currentFailureDetailsCell.setCellStyle(headerStyle);
			currentFailureDetailsCell.setCellValue(suiteName);

			currentFailureDetailsRowNum++;
			
			

			// Make CreationHelper and DrawingPatriarch for putting screenshots
			// in excel file.
			CreationHelper helper = results.getCreationHelper();
			Drawing drawing = failureDetailsSheet.createDrawingPatriarch();

			// Put screenshot for each failed test in the suite in the excel.
			for (String testName : failedTests.keySet()) {

				if (!failedTests.get(testName).getAllResults().isEmpty()) {

					for (ITestResult result : failedTests.get(testName)
							.getAllResults()) {

						// Sets test case header for screenshot.
						currentFailureDetailsRowNum++;
						currentFailureDetailsRow = failureDetailsSheet
								.createRow(currentFailureDetailsRowNum);

						currentFailureDetailsCell = currentFailureDetailsRow
								.createCell(0);
						currentFailureDetailsCell.setCellStyle(failStyle);
						currentFailureDetailsCell.setCellValue(testName);

						currentFailureDetailsCell = currentFailureDetailsRow
								.createCell(1);
						currentFailureDetailsCell.setCellStyle(failStyle);

						currentFailureDetailsCell
								.setCellValue(getRelevantParams(result));

						// Printed last logged step
						currentFailureDetailsRowNum++;

						// Merge cells for specified length.
						int mergeLength = 15;

						currentFailureDetailsRow = failureDetailsSheet
								.createRow(currentFailureDetailsRowNum);

						// create each cell to be merged, then format as a table
						// cell and add wrap. Needed so formatting shows up in
						// merged cells.
						for (int i = 0; i < mergeLength; i++) {
							currentFailureDetailsCell = currentFailureDetailsRow
									.createCell(i);
							currentFailureDetailsCell
									.setCellStyle(tableCellStyle);
							currentFailureDetailsCell.getCellStyle()
									.setWrapText(true);
							if (i == 0) {
								// Put value in
								currentFailureDetailsCell
										.setCellValue(getLastTestAction(result));
							}
						}

						failureDetailsSheet
								.addMergedRegion(new CellRangeAddress(
										currentFailureDetailsRowNum,
										currentFailureDetailsRowNum, 0,
										(mergeLength - 1)));

						// Put screenshot related to this ITestResult in excel
						currentFailureDetailsRowNum += 2;

						// Input byte[] from Selenium screenshot and write it as
						// a PNG, outputting to a ByteArrayOutputStream, then
						// converting back to a byte[]. Was done to deal with
						// excel being unable to read the bytes from the
						// selenium output.
						ByteArrayInputStream bais = new ByteArrayInputStream(
								screenshotsMap.get(result.getInstance()
										.toString()));
						BufferedImage ss = ImageIO.read(bais);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(ss, "png", baos);
						byte[] formattedImage = baos.toByteArray();

						// An excel row is equal to 20 BufferedImage units.
						double divider = (double) ss.getHeight() / 20.0;
						int rowsNeeded = (int) Math.ceil(divider);

						bais.close();
						baos.close();

						int pictureIdx = results.addPicture(formattedImage,
								Workbook.PICTURE_TYPE_PNG);

						ClientAnchor anchor = helper.createClientAnchor();
						anchor.setAnchorType(ClientAnchor.DONT_MOVE_AND_RESIZE);
						anchor.setCol1(0);
						anchor.setRow1(currentFailureDetailsRowNum);

						Picture pict = drawing
								.createPicture(anchor, pictureIdx);
						pict.resize();

						currentFailureDetailsRowNum += (rowsNeeded + 1);

					}
				}
			}
		}
	}

	/**
	 * Gets differentiating parameters from a test instance. Specifically, the
	 * browser, user, and environment parameters.
	 * 
	 * @param result
	 * @return
	 */
	private String getRelevantParams(ITestResult result) {
		HashMap<String, String> params = ((AbstractTestCase) result
				.getInstance()).returnInstanceParameters();
		return (params.get("environment") + ", " + params.get("browser") + ", " + params
				.get("user"));
	}

	/**
	 * Gets the last Reporter entry for the test.
	 * 
	 * @param result
	 * @return
	 */
	private String getLastTestAction(ITestResult result) {
		List<String> testResults = getOutput(result);
		if (testResults.size() > 0) {
			String lastOne = testResults.get((testResults.size() - 1));
			return lastOne;
		} else {
			return "No step recorded.";
		}
	}

}
