/*
 * Copyright 2022 Autognizant.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.autognizant.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Provides utility to perform read/write operations on Excel file.
 */
public class HSSFExcelUtils {

	private String sFile;
	private FileInputStream inputExcelFile;
	private FileOutputStream outputExcelFile;
	private HSSFSheet ExcelWSheet;
	private HSSFWorkbook ExcelWBook;
	private HSSFCell Cell;
	private HSSFRow Row;
	private HSSFCellStyle CellStyle;
	private HSSFFont Font;
	private Map<String,Map<String,String>> excelData;

	/**
	 * Sets the excel file on which excel operations to be performed.
	 * @param Path Absolute Path of the excel file.
	 * @param sSheetName Excel sheet name.
	 */
	public void setExcelFile(String Path,String sSheetName){
		try {
			sFile = Path;
			// Open the Excel file
			inputExcelFile = new FileInputStream(sFile);
			// Access the required test data sheet
			ExcelWBook = new HSSFWorkbook(inputExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sSheetName);		         
		} catch (Exception e){
			Log.error("ExcelUtil Error", e);
		}
	}    

	/**
	 * Sets the excel file with given template on which excel operations to be performed.
	 * @param Path Absolute Path of the excel file.
	 * @param sTemplateFile Name of the Template file.
	 * @param sSheetName Excel sheet name.
	 */
	public void setExcelFileWithTemplate(String Path,String sTemplateFile,String sSheetName){
		sFile = Path;
		//ExcelWBook = new HSSFWorkbook( OPCPackage.open("Resources//"+sTemplateFile+".xlsx") );
		//ExcelWSheet = ExcelWBook.getSheet(sSheetName);
	}

	/**
	 * Saves the excel file.
	 */
	public void saveExcelFile(){
		try{
			outputExcelFile = new FileOutputStream(sFile);
			ExcelWBook.write(outputExcelFile);
			outputExcelFile.close();
		}catch(Exception e){   
			Log.error("ExcelUtil Error", e);
		}
	}    

	/**
	 * Get Cell data.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 * This function commented because newer version of apache poi is not supported in C24 jars
	/*
	public String getCellData(int RowNum, int ColNum){
		try{        	   
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			Object  result=null;
			switch (Cell.getCellTypeEnum()) {
			case NUMERIC: // numeric value in Excel
//				result = Cell.getNumericCellValue();
                DataFormatter dataFormatter = new DataFormatter();
                String valueAsSeenInExcel = dataFormatter.formatCellValue(Cell);
                result = valueAsSeenInExcel;				
				break;
			case STRING: // String Value in Excel 
				result = Cell.getStringCellValue();
				break;
			case BLANK:
				result = "";
				break;
			case BOOLEAN: //boolean value 
				result= Cell.getBooleanCellValue();
				break;
			default:  
				Log.info("There is no support for this type of cell");
				throw new RuntimeException("There is no support for this type of cell");                        
			}
			return result.toString();
		}catch (Exception e){
			Log.error("ExcelUtil Error", e);
			return"";
		}
	}*/   

	/**
	 * Get Cell data.
	 * @param RowNum Row Number of the Cell.
	 * @param ColNum Column Number of the Cell.
	 * @return Returns the Cell data.
	 */
	public String getCellData(int RowNum, int ColNum){
		try{        	   
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			CellType type = Cell.getCellType();
			Object  result;
			switch (type) {
			case NUMERIC: // numeric value in Excel
			case FORMULA: // precomputed value based on formula
//				result = Cell.getNumericCellValue();
//              DataFormatter dataFormatter = new DataFormatter();
//              String valueAsSeenInExcel = dataFormatter.formatCellValue(Cell);
//              result = valueAsSeenInExcel;
				Cell.setCellType(CellType.STRING);
				result = Cell.getStringCellValue();
				break;
			case STRING: // String Value in Excel 
				result = Cell.getStringCellValue();
				break;
			case BLANK:
				result = "";
			case BOOLEAN: //boolean value 
				result= Cell.getBooleanCellValue();
				break;
			case ERROR:
			default:  
				throw new RuntimeException("There is no support for this type of cell");                        
			}
			return result.toString();
		}catch (Exception e){
			Log.error("ExcelUtil Error", e);
			return"";
		}
	}

	/**
	 * Set Cell data.
	 * @param sData Data(String) to be set in the Cell
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 */	
	public void setCellData(String sData,  int RowNum, int ColNum) {
		try{
			Row  = ExcelWSheet.getRow(RowNum);
			//			Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
			Cell = Row.getCell(ColNum);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(sData);
			} else {
				Cell.setCellValue(sData);                   
			}
			formatCell(RowNum,ColNum);
		}catch(Exception e){
			Log.error("ExcelUtil Error", e);
			throw (e);
		}
	}

	/**
	 * Set Cell data.
	 * @param iData Data(int) to be set in the Cell
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 */	
	public void setCellData(int iData,  int RowNum, int ColNum){
		try{
			Row  = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(iData);
			} else {
				Cell.setCellValue(iData);                   
			}
			formatCell(RowNum,ColNum);
		}catch(Exception e){
			Log.error("ExcelUtil Error", e);			
			throw (e);
		}
	}

	/**
	 * Creates new row
	 * @param RowNum Row Number of the Cell
	 */	
	public void createNewRow(int RowNum){
		ExcelWSheet.createRow(RowNum);
	}

	/**
	 * Get row used in the excel sheet.
	 * @return Number of rows used in the excel sheet.
	 */	
	public int getRowUsed(){
		try{
			int RowCount = ExcelWSheet.getLastRowNum();
			return RowCount;
		}catch (Exception e){
			Log.error("ExcelUtil Error", e);
			throw (e);
		}
	}	  

	/**
	 * Get excel data in Object[][]
	 * @return Returns excel data in Object[][]
	 */	
	public Object[][] getExcelDataIntoArray(){
		String[][] tabArray = null;
		int RowCount = ExcelWSheet.getLastRowNum();
		int iNumberOfColumns = ExcelWSheet.getRow(0).getLastCellNum();
		tabArray = new String[RowCount+1][iNumberOfColumns];
		for(int i=0;i<tabArray.length;i++){
			for(int j=0;j<iNumberOfColumns;j++){
				tabArray[i][j] = getCellData(i, j);
			}
		}
		return(tabArray);
	}  

	/**
	 * Get excel data in Object[][]
	 * @return Returns excel data in Object[][]
	 */	
	public Map<String, Map<String, String>> getExcelDataIntoHashMap(){
		excelData = new HashMap<>();
		int iNumberOfColumns = ExcelWSheet.getRow(0).getLastCellNum();
		String[] columnNames = new String[iNumberOfColumns-1];
		for(int i=0;i<iNumberOfColumns-1;i++){
			columnNames[i] = getCellData(0,i+1);
		}
		int RowCount = ExcelWSheet.getLastRowNum();
		for(int i = 1; i < RowCount+1; i++){
			Map<String, String> row = new HashMap<String, String>(columnNames.length);
			for(int j=0; j<columnNames.length; j++) {
				row.put(columnNames[j],getCellData(i, j+1));
			}
			excelData.put(getCellData(i, 0), row);
		}
		return excelData;
	}  

	/**
	 * Creates clone of the excel sheet.
	 * @param iSheetIndex Sheet Index
	 */	
	public void createCloneSheet(int iSheetIndex){
		ExcelWBook.cloneSheet(iSheetIndex);
	}

	/**
	 * Deletes the given excel sheet.
	 * @param iSheetIndex Sheet Index
	 */	
	public void removeSheet(int iSheetIndex){
		ExcelWBook.removeSheetAt(iSheetIndex);
	}

	/**
	 * Returns number of sheets in excel file.
	 * @return Returns number of sheets in excel file.
	 */	
	public int getNumberOfSheets(){
		return ExcelWBook.getNumberOfSheets();
	}

	/**
	 * Format Cell.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 */	
	public void formatCell(int RowNum, int ColNum){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		CellStyle  = ExcelWBook.createCellStyle();
		//		CellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
		//		CellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
		//		CellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);            
		//		CellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);              
		//		CellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);	    
		Font = ExcelWBook.createFont();
		Font.setFontName("Calibri"); 
		//		Font.setFontHeight(12);
		CellStyle.setFont(Font);	   
		Cell.setCellStyle(CellStyle);
	}

	/**
	 * Set Cell border.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 */	
	public void setCellBorder(int RowNum, int ColNum){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		HSSFCellStyle ssCellStyle  = ExcelWBook.createCellStyle();
		//		ssCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
		//		ssCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);            
		//		ssCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);              
		//		ssCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
		Cell.setCellStyle(ssCellStyle);
	}

	/**
	 * Set Cell Alignment.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 * @param option option can be left/right/center.
	 */	
	public void setCellAlignment(int RowNum, int ColNum, String option){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		//		CellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
		//		CellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);            
		//		CellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);              
		//		CellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//		if (option=="left"){
		//			CellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//		}else if(option=="right"){
		//			CellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		//		}else if (option =="center"){
		//			CellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//		}
		Cell.setCellStyle(CellStyle);
	}

	/**
	 * Set Cell Background.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 * @param Red Red color code(int) in RGB format.
	 * @param Green Green color code(int) in RGB format.
	 * @param Blue Blue color code(int) in RGB format.
	 */	
	public void setCellBackgroundColor(int RowNum, int ColNum, int Red, int Green, int Blue){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		//		CellStyle.setFillForegroundColor(new HSSFColor(new java.awt.Color(Red, Green, Blue)));
		//		CellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Cell.setCellStyle(CellStyle);
	}

	/**
	 * Set HyperLink for Cell.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 * @param Path Path for HyperLink 
	 */	
	public void setHyperLink(int RowNum, int ColNum, String Path){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		Font.setColor(IndexedColors.BLUE.getIndex());
		Font.setUnderline(HSSFFont.U_SINGLE);
		CellStyle.setFont(Font);
		Cell.setCellStyle(CellStyle);
	}

	/**
	 * Set Link for document.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 * @param Path Path for HyperLink 
	 */	
	public void setLinkDocument(int RowNum, int ColNum, String Path){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		Font.setColor(IndexedColors.BLUE.getIndex());
		Font.setUnderline(HSSFFont.U_SINGLE);
		CellStyle.setFont(Font);
		Cell.setCellStyle(CellStyle);
	}

	/**
	 * Set color and underline for Cell.
	 * @param RowNum Row Number of the Cell
	 * @param ColNum Column Number of the Cell
	 */	
	public void setColorAndUnderline(int RowNum, int ColNum){
		Row  = ExcelWSheet.getRow(RowNum);
		Cell = Row.getCell(ColNum);
		Font.setColor(IndexedColors.BLUE.getIndex());
		Font.setUnderline(HSSFFont.U_SINGLE);
		CellStyle.setFont(Font);
	}

	/**
	 * Renames sheet.
	 * @param iSheetIndex Sheet Index to be renamed.
	 * @param sNewSheetName New Sheet Name
	 */	
	public void renameSheet(int iSheetIndex,String sNewSheetName){
		ExcelWBook.setSheetName(iSheetIndex, sNewSheetName);
	}

	/**
	 * Merge region in excel sheet.
	 * @param iFirstRow First Row
	 * @param iLastRow Last Row
	 * @param iFirstColumn First Column
	 * @param iLastColumn Last Column
	 */	
	public void mergeRegion(int iFirstRow,int iLastRow,int iFirstColumn,int iLastColumn){
		CellRangeAddress cellRangeAddress = new CellRangeAddress(iFirstRow, iLastRow, iFirstColumn, iLastColumn);
		ExcelWSheet.addMergedRegion(cellRangeAddress);		
	}

	/**
	 * Checks whether given data is present in specific column.
	 * @param sData Data
	 * @param colNum Column Number
	 * @return Returns True if the given data is present in mentioned column otherwise False.
	 */	
	public int getRowContains(String sData, int colNum){
		int i;
		try {
			int rowCount = getRowUsed();
			for ( i=0 ; i<rowCount; i++){
				if  (getCellData(i,colNum).equalsIgnoreCase(sData) || getCellData(i,colNum).contains(sData)){
					break;
				}
			}
			return i;
		}catch (Exception e){
			Log.error("ExcelUtil Error", e);
			return 0;
		}
	}	
}
