package com.spssol.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelInfo {

	/**
	 * 
	 * @Description: 获取所有的sheet名称
	 * @author big
	 * @date 2015年11月3日 下午1:19:33
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static List<String> getSheet(String path)
			throws FileNotFoundException, IOException {
		File file = new File(path);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		int n = wb.getNumberOfSheets();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < n; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			list.add(sheet.getSheetName());
		}
		return list;
	}

	/**
	 * 获取当前sheet的值
	 *
	 * @Description: TODO(用一句话描述该文件做什么)
	 * @author big
	 * @date 2015年11月3日 下午1:34:29
	 * @version V1.0
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String[][] getData(String path, int n)
			throws FileNotFoundException, IOException {
		File file = new File(path);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheetAt(n);

		int rowcount = sheet.getLastRowNum();// 取得有效的行数
		int colcount = sheet.getRow(0).getPhysicalNumberOfCells();// 总列数
		String[][] str = new String[rowcount][colcount];
		HSSFRow row = null;
		for (int i = 0; i < rowcount; i++) {
			row = sheet.getRow(i); // 获得第i行
			for (int j = 0; j < colcount; j++) {
				str[i][j] = getCellFormatValue(row.getCell(j)).trim();
			}
		}
		return str;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		getData("C:/Users/bigta/Desktop/65岁和65岁以上的人口（占总人口的百分比）.xls", 0);
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}
}
