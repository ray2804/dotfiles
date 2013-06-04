/*
 *  RapidMiner
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.rapidminer.operator.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeDateFormat;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.conditions.EqualTypeCondition;
import com.rapidminer.tools.DateParser;
import com.rapidminer.tools.I18N;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.io.Encoding;

/**
 * <p>This operator can be used to write data into Microsoft Excel spreadsheets. 
 * This operator creates Excel files readable by Excel 95, 97, 2000, XP, 2003 
 * and newer. Missing data values are indicated by empty cells.</p>
 *
 * @author Ingo Mierswa, Nils Woehler
 */
public class ExcelExampleSetWriter extends AbstractStreamWriter {

	private static final String RAPID_MINER_DATA = "RapidMiner Data";

	/** The parameter name for &quot;The Excel spreadsheet file which should be written.&quot; */
	public static final String PARAMETER_EXCEL_FILE = "excel_file";

	public static final String FILE_FORMAT_XLS = "xls";
	public static final String FILE_FORMAT_XLSX = "xlsx";
	public static final String[] FILE_FORMAT_CATEGORIES = new String[] { FILE_FORMAT_XLS, FILE_FORMAT_XLSX };
	public static final int FILE_FORMAT_XLS_INDEX = 0;
	public static final int FILE_FORMAT_XLSX_INDEX = 1;

	public static final String PARAMETER_FILE_FORMAT = "file_format";
	public static final String PARAMETER_DATE_FORMAT = "date_format";
	public static final String PARAMETER_NUMBER_FORMAT = "number_format";
	public static final String PARAMETER_SHEET_NAME = "sheet_name";

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_NUMBER_FORMAT = "#.0";

	public ExcelExampleSetWriter(OperatorDescription description) {
		super(description);
	}

	/**
	 * Writes the example set into a excel file with XLS format. 
	 * If you want to write it in XLSX format use {@link #writeXLSX(ExampleSet, String, String, String, OutputStream)}
	 */
	public static void write(ExampleSet exampleSet, Charset encoding, OutputStream out) throws IOException, WriteException {
		try {
			// .xls files can only store up to 256 columns, so throw error in case of more
			if (exampleSet.getAttributes().allSize() > 256) {
				throw new IllegalArgumentException(I18N.getMessage(I18N.getErrorBundle(), "export.excel.excel_xls_file_exceeds_column_limit"));
			}
			
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding(encoding.name());
			ws.setLocale(Locale.US);

			WritableWorkbook workbook = Workbook.createWorkbook(out, ws);
			WritableSheet s = workbook.createSheet(RAPID_MINER_DATA, 0);
			writeDataSheet(s, exampleSet);
			workbook.write();
			workbook.close();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				// silent. exception will trigger warning anyway
			}
		}
	}

	private static void writeDataSheet(WritableSheet s, ExampleSet exampleSet) throws WriteException {

		// Format the Font
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);

		Iterator<Attribute> a = exampleSet.getAttributes().allAttributes();
		int counter = 0;
		while (a.hasNext()) {
			Attribute attribute = a.next();
			s.addCell(new Label(counter++, 0, attribute.getName(), cf));
		}

		NumberFormat nf = new NumberFormat(DEFAULT_NUMBER_FORMAT);
		WritableCellFormat nfCell = new WritableCellFormat(nf);
		WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(wf2);

		DateFormat df = new DateFormat(DateParser.DEFAULT_DATE_TIME_FORMAT);

		WritableCellFormat dfCell = new WritableCellFormat(df);
		int rowCounter = 1;
		for (Example example : exampleSet) {
			a = exampleSet.getAttributes().allAttributes();
			int columnCounter = 0;
			while (a.hasNext()) {
				Attribute attribute = a.next();
				if (!Double.isNaN(example.getValue(attribute))) {
					if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attribute.getValueType(), Ontology.NOMINAL)) {
						s.addCell(new Label(columnCounter, rowCounter, replaceForbiddenChars(example.getValueAsString(attribute)), cf2));
					} else if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attribute.getValueType(), Ontology.DATE_TIME)) {
						DateTime dateTime = new DateTime(columnCounter, rowCounter, new Date((long) example.getValue(attribute)), dfCell);
						s.addCell(dateTime);
					} else if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attribute.getValueType(), Ontology.NUMERICAL)) {
						Number number = new Number(columnCounter, rowCounter, example.getValue(attribute), nfCell);
						s.addCell(number);
					} else {
						// default: write as a String
						s.addCell(new Label(columnCounter, rowCounter, replaceForbiddenChars(example.getValueAsString(attribute)), cf2));
					}
				}
				columnCounter++;
			}
			rowCounter++;
		}
	}

	private static String replaceForbiddenChars(String originalValue) {
		return originalValue.replace((char) 0, ' ');
	}

	@Override
	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		types.add(makeFileParameterType());

		types.add(new ParameterTypeCategory(PARAMETER_FILE_FORMAT, "Defines the file format the excel file should be saved as.", FILE_FORMAT_CATEGORIES, FILE_FORMAT_XLS_INDEX,
				false));

		List<ParameterType> encodingTypes = Encoding.getParameterTypes(this);
		for (ParameterType type : encodingTypes) {
			type.registerDependencyCondition(new EqualTypeCondition(this, PARAMETER_FILE_FORMAT, FILE_FORMAT_CATEGORIES, false, new int[] { FILE_FORMAT_XLS_INDEX }));
		}
		types.addAll(encodingTypes);

		List<ParameterType> xlsxTypes = new LinkedList<ParameterType>();
		ParameterTypeString sheetName = new ParameterTypeString(PARAMETER_SHEET_NAME,
				"The name of the created sheet. Note that sheet name is Excel must not exceed 31 characters.", RAPID_MINER_DATA);
		sheetName.setExpert(false);
		xlsxTypes.add(sheetName);
		xlsxTypes.add(new ParameterTypeDateFormat(PARAMETER_DATE_FORMAT, "The parse format of the date values. Default: for example \"yyyy-MM-dd HH:mm:ss\".", DEFAULT_DATE_FORMAT,
				true));
		xlsxTypes.add(new ParameterTypeString(PARAMETER_NUMBER_FORMAT, "Specifies the number format for date entries. Default: \"#.0\"", DEFAULT_NUMBER_FORMAT, true));
		for (ParameterType type : xlsxTypes) {
			type.registerDependencyCondition(new EqualTypeCondition(this, PARAMETER_FILE_FORMAT, FILE_FORMAT_CATEGORIES, false, new int[] { FILE_FORMAT_XLSX_INDEX }));
		}
		types.addAll(xlsxTypes);

		return types;
	}

	@Override
	String[] getFileExtensions() {
		return new String[] { FILE_FORMAT_XLS, FILE_FORMAT_XLSX };
	}

	@Override
	String getFileParameterName() {
		return PARAMETER_EXCEL_FILE;
	}

	@Override
	void writeStream(ExampleSet exampleSet, OutputStream outputStream) throws OperatorException {
		File file = getParameterAsFile(PARAMETER_EXCEL_FILE, true);

		if (getParameterAsString(PARAMETER_FILE_FORMAT).equals(FILE_FORMAT_XLSX)) {

			String dateFormat = isParameterSet(PARAMETER_DATE_FORMAT) ? getParameterAsString(PARAMETER_DATE_FORMAT) : null;
			String numberFormat = isParameterSet(PARAMETER_NUMBER_FORMAT) ? getParameterAsString(PARAMETER_NUMBER_FORMAT) : null;
			String sheetName = getParameterAsString(PARAMETER_SHEET_NAME);

			if (sheetName.length() > 31) {
				throw new UserError(this, "excel_sheet_name_too_long", sheetName, sheetName.length());
			}

			try {
				writeXLSX(exampleSet, sheetName, dateFormat, numberFormat, outputStream);
			} catch (Exception e) {
				throw new UserError(this, 303, file.getName(), e.getMessage());
			}

		} else {
			WorkbookSettings ws = new WorkbookSettings();
			Charset encoding = Encoding.getEncoding(this);
			ws.setEncoding(encoding.name());
			ws.setLocale(Locale.US);

			try {
				write(exampleSet, encoding, outputStream);
			} catch (Exception e) {
				throw new UserError(this, 303, file.getName(), e.getMessage());
			}
		}

	}

	/**
	 * Writes the example set into a excel file with XLSX format. 
	 * If you want to write it in XLS format use {@link #write(ExampleSet, Charset, OutputStream)}.
	 */
	public static void writeXLSX(ExampleSet exampleSet, String sheetName, String dateFormat, String numberFormat, OutputStream outputStream) throws WriteException, IOException {
		// .xlsx files can only store up to 16384 columns, so throw error in case of more
		if (exampleSet.getAttributes().allSize() > 16384) {
			throw new IllegalArgumentException(I18N.getMessage(I18N.getErrorBundle(), "export.excel.excel_xlsx_file_exceeds_column_limit"));
		}
					
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
			dateFormat = dateFormat == null ? DEFAULT_DATE_FORMAT : dateFormat;

			numberFormat = numberFormat == null ? "#.0" : numberFormat;

			writeXLSXDataSheet(workbook, sheet, dateFormat, numberFormat, exampleSet);
			workbook.write(outputStream);
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}

	private static void writeXLSXDataSheet(org.apache.poi.ss.usermodel.Workbook wb, Sheet sheet, String dateFormat, String numberFormat, ExampleSet exampleSet)
			throws WriteException {

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		// create the header
		Iterator<Attribute> a = exampleSet.getAttributes().allAttributes();
		int columnCounter = 0;
		int rowCounter = 0;
		Row headerRow = sheet.createRow(rowCounter);
		while (a.hasNext()) {
			Attribute attribute = a.next();
			Cell headerCell = headerRow.createCell(columnCounter);
			headerCell.setCellValue(attribute.getName());
			headerCell.setCellStyle(headerStyle);
			columnCounter++;
		}
		rowCounter++;

		// body font
		Font bodyFont = wb.createFont();
		bodyFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

		CreationHelper createHelper = wb.getCreationHelper();

		// number format
		CellStyle numericalStyle = wb.createCellStyle();
		numericalStyle.setDataFormat(createHelper.createDataFormat().getFormat(numberFormat));
		numericalStyle.setFont(bodyFont);

		// date format
		CellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
		dateStyle.setFont(bodyFont);

		// create nominal cell style
		CellStyle nominalStyle = wb.createCellStyle();
		nominalStyle.setFont(bodyFont);

		// fill body
		for (Example example : exampleSet) {

			// create new row
			Row bodyRow = sheet.createRow(rowCounter);

			// iterate over attributes and save examples
			a = exampleSet.getAttributes().allAttributes();
			columnCounter = 0;
			while (a.hasNext()) {
				Attribute attribute = a.next();
				Cell currentCell = bodyRow.createCell(columnCounter);
				if (!Double.isNaN(example.getValue(attribute))) {
					if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attribute.getValueType(), Ontology.DATE_TIME)) {
						Date dateValue = example.getDateValue(attribute);
						currentCell.setCellValue(dateValue);
						currentCell.setCellStyle(dateStyle);
					} else if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attribute.getValueType(), Ontology.NUMERICAL)) {
						double numericalValue = example.getNumericalValue(attribute);
						currentCell.setCellValue(numericalValue);
						currentCell.setCellStyle(numericalStyle);
					} else {
						currentCell.setCellValue(replaceForbiddenChars(example.getValueAsString(attribute)));
						currentCell.setCellStyle(nominalStyle);
					}
				}
				columnCounter++;
			}
			rowCounter++;
		}
	}
}
