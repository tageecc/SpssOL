package com.spssol.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			HSSFSheet sheet = wb.getSheetAt(0);
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
	public static void getData(int i,String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheetAt(i);
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		getSheet("C:/Users/bigta/Desktop/65岁和65岁以上的人口（占总人口的百分比）.xls");
	}
}
