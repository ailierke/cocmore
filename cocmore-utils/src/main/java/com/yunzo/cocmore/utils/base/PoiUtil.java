package com.yunzo.cocmore.utils.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * POI工具类
 * 
 * @author xiaobo
 * 
 */
public class PoiUtil {

	/**
	 * 由于Excel当中的单元格Cell存在类型,若获取类型错误就会产生异常, 所以通过此方法将Cell内容全部转换为String类型
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String str = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			str = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			str = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			str = String.valueOf(cell.getCellFormula());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
				str = nf.format(cell.getNumericCellValue());
			} else if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期格式判断
				str = sdf.format(HSSFDateUtil.getJavaDate(cell
						.getNumericCellValue()));
			}
			// str = String.valueOf((long) cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			str = String.valueOf(cell.getStringCellValue());
			break;
		default:
			str = null;
			break;
		}
		return str;
	}

	/**
	 * 读取Excel文件
	 * 
	 * @param file
	 *            excel文件
	 * @return 二维列表
	 * @throws IOException
	 */
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(new FileInputStream(file));
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(new FileInputStream(file));
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取Excel文件
	 * 
	 * @param MultipartFile
	 * @return 每一行每一单元格数据
	 * @throws IOException
	 */
	// public static List<List<Object>> readExcelByMultipartFile(MultipartFile
	// file)
	// throws IOException {
	// String fileName = file.getOriginalFilename();
	// InputStream is = file.getInputStream();
	// // 判断Excel版本, 根据文件后缀判断版本
	// if (fileName.endsWith(".xls")) {
	// return read2003Excel(is);
	// } else if (fileName.endsWith(".xlsx")) {
	// return read2007Excel(is);
	// } else {
	// throw new IOException("不支持的文件类型");
	// }
	// }

	public static String exportExcel(List<Object> listObj) {
		return "";
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @param is
	 *            Excel文件流
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(InputStream is)
			throws IOException {
		System.out.println("读取office 2003 excel内容如下：");
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(is);
		HSSFSheet sheet = hwb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;// 单元格
		int rownum = sheet.getLastRowNum();// 获取行数
		// System.out.println("行数: " + rownum);
		for (int i = 0; i <= rownum; i++) {
			row = sheet.getRow(i);// 获取一行
			int coloumNum = row.getLastCellNum();
			// System.out.println("第" + i + "行: " + coloumNum + "列");
			List<Object> coloumList = new ArrayList<Object>();// 列值数组
			for (int j = 0; j < coloumNum; j++) {
				cell = row.getCell(j);// 获取单元格
				if (null == cell) {
					coloumList.add("");
					continue;
				}
				coloumList.add(getCellValue(cell));// 转换成字符串
				cell = null;
			}
			list.add(coloumList);
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 * 
	 * @param is
	 *            Excel文件流
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2007Excel(InputStream is)
			throws IOException {
		System.out.println("读取office 2007 excel内容如下：");
		List<List<Object>> list = new ArrayList<List<Object>>();
		XSSFWorkbook xwb = new XSSFWorkbook(is);
		XSSFSheet sheet = xwb.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;// 单元格
		int rownum = sheet.getLastRowNum();
		for (int i = 0; i <= rownum; i++) {
			row = sheet.getRow(i);// 获取一行
			int coloumNum = row.getLastCellNum();
			// System.out.println("第" + i + "行: " + coloumNum + "列");
			List<Object> coloumList = new ArrayList<Object>();// 列值数组
			for (int j = 0; j < coloumNum; j++) {
				cell = row.getCell(j);// 获取单元格
				if (null == cell) {
					coloumList.add("");
					continue;
				}
				coloumList.add(getCellValue(cell));// 转换成字符串
				cell = null;
			}
			list.add(coloumList);
		}
		return list;
	}

}
