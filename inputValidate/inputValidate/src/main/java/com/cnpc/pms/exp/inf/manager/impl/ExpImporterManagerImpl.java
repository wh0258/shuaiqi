package com.cnpc.pms.exp.inf.manager.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.exp.common.util.ExpImporterUtil;
import com.cnpc.pms.exp.inf.dto.ExpertCustomValidation;
import com.cnpc.pms.exp.inf.dto.ExpertReferValidation;
import com.cnpc.pms.exp.inf.dto.ExpertRegValidation;
import com.cnpc.pms.exp.inf.dto.ExpertUniqValidation;


/**
 * 该导入功能目前仅支持微软Excel-2003格式的文件。 Copyright(c) 2011 China National Petroleum
 * Corporation , http://www.cnpc.com.cn
 * 
 * @author Song Shitao
 * @since 2011-3-22
 */
public class ExpImporterManagerImpl {

	/** 专家信息导入,评审专业个数最大值 :9 */
	private static final int EXP_IMPORT_MAX_CATEGORY = 9;
		
	/** 专家信息导入,辅评专业个数最大值 :4 */
	private static final int EXP_IMPORT_MAX_ASSISTCATEGORY = 4;

	/** 专家信息导入,职称个数最大值 :3 */
	private static final int EXP_IMPORT_MAX_DUTY = 3;

	/** 专家信息导入，截取的message最大长度 :1000 */
	private static final int EXP_IMPORT_MSG_LEN = 1300;
	/**
	 * the message.存放出错信息
	 */
	private String message = null;
	/**
	 * the resMap.存放返回结果
	 */
	private Map<String, Object> resMap = null;
	/**
	 * the wrongRowColNumberSet 存放出错的行号和列号,行号和列号之间用逗号","分隔
	 */
	private Set<String> wrongRowColNumberSet = null;
	/**
	 * the wrongRowNumberSet 存放出错的行号
	 */
	private Set<Integer> wrongRowNumberSet = null;
	/**
	 * the log
	 * */
	private final Logger log = LoggerFactory.getLogger(ExpImporterManagerImpl.class);

	/**
	 * 
	 * 执行检查和导入Excel表格
	 * 
	 * @param attId
	 *            the attId 导入文件id
	 * @param filePath
	 *            the filePath 导入文件路径
	 * @param sheetNum
	 *            the sheetNum 要操作的Excel工作区号
	 * @param validationXmlMap
	 *            the validationXmlMap 验证规则map
	 * @param fileDesc
	 *            the fileDesc 导入文件描述
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkAndImportExcel(String attId, String filePath, int sheetNum,
		Map<String, List<Object>> validationXmlMap, String fileDesc) throws Exception {
		// 初始化全局变量
		initGlobalVariables();
		// Excel工作区
		Workbook book = null;
		// 读Excel文件
		log.error("===========================get worksheet");
		book = Workbook.getWorkbook(new File(filePath));
		// 获得工作表个数
		if (book == null) {
			return;
		}
		Sheet sheet = book.getSheet(sheetNum);
		log.error("===========================before iterator columns of sheet");
		for (int i = 0; i < sheet.getColumns(); i++) {
			// 获得列
			Cell[] col = sheet.getColumn(i);
			// 存放针对该表头的所有验证规则
			List<Object> validationList = new ArrayList<Object>();
			String headerName = null;
			//i是列号，j是行号
			for (int j = 0; j < col.length; j++) {
				// 获得单元格内容
				String content = col[j].getContents();
				// 如果是表头单元格,且表头单元格验证通过
				if ((j == 0) && validationXmlMap.containsKey(content)) {
					headerName = content;
					// 得到该表头的验证规则list
					validationList = validationXmlMap.get(content);
					// 继续下一循环
					continue;
				}
				// 如果是表头单元格,但是表头单元格验证没有通过
				if ((j == 0) && !validationXmlMap.containsKey(content)) {
					throw new Exception("${expInfoImport.error.excelHeadError}");
				}
				// 如果不是表头而且内容不为空
				if ((j != 0) && StringUtils.isNotBlank(content)) {
					// 去空格
					content = content.trim();
				}
				// 如果不是表头且内容为空，且该单元格可以为空
				if ((j != 0) && StringUtils.isBlank(content) && !headerName.endsWith("*")) {
					// 继续循环下一循环
					continue;
				}
				// 如果不是表头且内容为空，但该单元格要求为非空
				if ((j != 0) && StringUtils.isBlank(content) && headerName.endsWith("*")) {
//					this.message +=
//									j + SpringHelper.getMessage("expInfoImport.message.row") + headerName + SpringHelper.getMessage("expInfoImport.message.column")
//													+ SpringHelper.getMessage("expInfoImport.message.notNull");
					this.message +="message";
					// 存储错误的行号和列号。中间用逗号隔开
					this.wrongRowColNumberSet.add(String.valueOf(j) + "," + String.valueOf(i));
					this.wrongRowNumberSet.add(j);
					// 继续下一循环
					continue;
				}
				if (!validationList.isEmpty()) {
					checkWithValidationList(validationList, content, headerName, j, i, sheet);
				}
			}
		}
		log.error("===========================after iterator columns of sheet");
		log.debug("final message=======" + message);
		// 调用持久化方法把excel内容存入数据库
		//saveExcelToDB(attId, sheet, fileDesc);
		// 关闭Workbook
		if (book != null) {
			book.close();
		}
	}

	/**
	 * 
	 * 根据得到的自定义验证方法名进行反射调用来校验
	 * 
	 * @param objectValidation
	 *            the objectValidation 验证规则
	 * @param content
	 *            content 要验证的信息
	 * @param rowNum
	 *            rowNum 行号
	 * @param colNum
	 *            colNum 列号
	 * @param sheet
	 *            sheet 要操作的Excel工作区
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkWithCustomValidation(Object objectValidation, String content, int rowNum, int colNum, Sheet sheet)
		throws Exception {
		//Object managerObj = SpringHelper.getBean(((ExpertCustomValidation) objectValidation).getClassName());
		//Class<? extends Object> clazz = managerObj.getClass();	
		Object managerObj = Class.forName(((ExpertCustomValidation) objectValidation).getClassName()).newInstance();
		Class<? extends Object> clazz = managerObj.getClass();
		Method method =
						clazz.getDeclaredMethod(((ExpertCustomValidation) objectValidation).getMethodName(),
							new Class[] {String.class, Sheet.class, int.class, Cell[].class });
		Object resobj = method.invoke(managerObj, new Object[] {content, sheet, rowNum, sheet.getRow(rowNum) });
		if (resobj == null) {
			this.wrongRowColNumberSet.add(String.valueOf(rowNum) + "," + String.valueOf(colNum));
			this.wrongRowNumberSet.add(rowNum);
		}
	}

	/**
	 * 
	 * 根据得到的外键关联验证规则进行校验
	 * 
	 * @param objectValidation
	 *            the objectValidation 要验证的信息
	 * @param content
	 *            content 要验证的信息
	 * @param rowNum
	 *            rowNum 行号
	 * @param colNum
	 *            colNum 列号
	 * @param headerName
	 *            headerName 表头名
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkWithReferValidation(Object objectValidation, String content, int rowNum, int colNum,
		String headerName) throws Exception {
//		String managerName = ExpImporterUtil.getManagerName(((ExpertReferValidation) objectValidation).getClassName());
//		String fieldName = ((ExpertReferValidation) objectValidation).getFieldName();
//		IManager manager = (IManager) SpringHelper.getBean(managerName);
//		IFilter filter = FilterFactory.getSimpleFilter(fieldName, content);
		//List<?> list = manager.getObjects(filter);
		String entityClass = ((ExpertReferValidation) objectValidation).getClassName();
		String fieldName = ((ExpertReferValidation) objectValidation).getFieldName();
		List<?> list = null;//请根据实体类、查询的字段，调用DAO的etAllByCondition(content)方法，获取关联字典表里面是否有content这个值。
							//查询的字段通过fieldName查询
		if (list.size() == 0) {
			this.message += "message";
//							rowNum + 1 + SpringHelper.getMessage("expInfoImport.message.row") + headerName
//											+ SpringHelper.getMessage("expInfoImport.message.column") + content
//											+ SpringHelper.getMessage("expInfoImport.message.foreignKeyError");
			log.debug("the message====================" + message + "=============at " + new Date());
			this.wrongRowColNumberSet.add(String.valueOf(rowNum) + "," + String.valueOf(colNum));
			this.wrongRowNumberSet.add(rowNum);
		}

	}

	/**
	 * 
	 * 根据得到的正则验证规则进行校验
	 * 
	 * @param objectValidation
	 *            the objectValidation 验证规则
	 * @param content
	 *            content 要验证的内容
	 * @param rowNum
	 *            rowNum 行数
	 * @param colNum
	 *            colNum 列数
	 * @param headerName
	 *            headerName 表头名
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkWithRegValidation(Object objectValidation, String content, int rowNum, int colNum,
		String headerName) throws Exception {
		String[] regs = ((ExpertRegValidation) objectValidation).getRegs();
		boolean isMatched = true;
		for (int ii = 0; ii < regs.length; ii++) {
			Pattern regex = Pattern.compile(regs[ii]);
			Matcher matcher = regex.matcher(content);
			isMatched = matcher.matches();
			if (!isMatched) {
				this.message += "message";
//								rowNum + 1 + SpringHelper.getMessage("expInfoImport.message.row") + headerName
//												+ SpringHelper.getMessage("expInfoImport.message.column")
//												+ SpringHelper.getMessage("expInfoImport.message.formatError");
				this.wrongRowColNumberSet.add(String.valueOf(rowNum) + "," + String.valueOf(colNum));
				this.wrongRowNumberSet.add(rowNum);
			}
		}
	}

	/**
	 * 
	 * 根据得到的唯一性验证规则进行校验
	 * 
	 * @param objectValidation
	 *            the objectValidation 验证规则
	 * @param content
	 *            content 要验证的信息
	 * @param rowNum
	 *            rowNum 行号
	 * @param colNum
	 *            colNum 列号
	 * @param headerName
	 *            headerName 表头名称
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkWithUniqValidation(Object objectValidation, String content, int rowNum, int colNum,
		String headerName) throws Exception {
//		String managerName = ExpImporterUtil.getManagerName(((ExpertUniqValidation) objectValidation).getClassName());
//		String fieldName = ((ExpertUniqValidation) objectValidation).getFieldName();
//		IManager manager = (IManager) SpringHelper.getBean(managerName);
//		IFilter filter = FilterFactory.getSimpleFilter(fieldName, content);
		//List<?> list = manager.getObjects(filter);
		List<?> list = null;//此处的查询，类似checkWithReferValidation，只是这里查询出来的数据，要求是唯一的。
		if (list.size() > 0) {
			this.message += "message";
//							rowNum + 1 + SpringHelper.getMessage("expInfoImport.message.row") + headerName
//											+ SpringHelper.getMessage("expInfoImport.message.column") + 
//											SpringHelper.getMessage("expInfoImport.message.isExist");
			this.wrongRowColNumberSet.add(String.valueOf(rowNum) + "," + String.valueOf(colNum));
			this.wrongRowNumberSet.add(rowNum);
		}
	}
	
	/**
	 * 
	 * 根据从xml文件中得到的验证规则对单元格内容进行校验
	 * 
	 * @param validationList
	 *            the validationList 验证规则
	 * @param content
	 *            content 要验证的信息
	 * @param headerName
	 *            headerName 表头名称
	 * @param rowNum
	 *            rowNum 行号
	 * @param colNum
	 *            colNum 列号
	 * @param sheet
	 *            sheet 要操作的Excel工作区
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void checkWithValidationList(List<Object> validationList, String content, String headerName, int rowNum,
		int colNum, Sheet sheet) throws Exception {
		Iterator<Object> it = validationList.iterator();
		while (it.hasNext()) {
			Object objectValidation = it.next();
			// 如果是正则验证
			if (objectValidation instanceof ExpertRegValidation) {
				log.error("===========================checkWithRegValidation of " + rowNum + "行" + colNum + "列"
								+ headerName);
				// 根据得到的正则验证规则进行校验
				checkWithRegValidation(objectValidation, content, rowNum, colNum, headerName);
			}
			// 如果是唯一性验证
			if (objectValidation instanceof ExpertUniqValidation) {
				log.error("===========================checkWithUniqValidation of " + rowNum + "行" + colNum + "列"
								+ headerName);
				// 根据得到的唯一性验证规则进行校验
				checkWithUniqValidation(objectValidation, content, rowNum, colNum, headerName);
			}
			// 如果是外键关联验证
			if (objectValidation instanceof ExpertReferValidation) {
				log.error("===========================checkWithReferValidation of " + rowNum + "行" + colNum + "列"
								+ headerName);
				// 根据得到的外键关联验证规则进行校验
				checkWithReferValidation(objectValidation, content, rowNum, colNum, headerName);
			}
			// 如果是自定义验证
			if (objectValidation instanceof ExpertCustomValidation) {
				log.error("===========================checkWithCustomValidation of " + rowNum + "行" + colNum + "列");
				// 根据得到的自定义验证方法名进行校验
				checkWithCustomValidation(objectValidation, content, rowNum, colNum, sheet);
			}
		}
	}

	/**
	 * 根据上传的导入文件id，获取文件路径,读取第一个sheet
	 * 执行检查和导入Excel表格的入口函数
	 * 
	 * @param attId
	 *            the attId 附件id
	 * @param relativePathFromDB
	 *            数据库中的路径
	 * @param sheetNum
	 *            the sheetNum 信息的列数
	 * @param xmlFileName
	 *            the xmlFileName 验证的文件名称
	 * @param fileDesc
	 *            the fileDesc 导入文件的相关说明
	 * @return String the result string 返回操作完成的信息
	 * @throws Exception
	 *             the Exception
	 */
	public String importExpInfo(String attId, String relativePathFromDB, int sheetNum, String xmlFileName,
		String fileDesc) throws Exception {
		// String xmlFilePath = null;
		Map<String, List<Object>> validationXmlMap = null;
		try {
			// log.error("===========================before get xmlFilePath");
			// xmlFilePath = ExpImporterUtil.getValiadationXmlPath(xmlFileName);
			// log.error("===========================after get xmlFilePath");
			log.error("===========================before get validationXmlMap");
			validationXmlMap = ExpImporterUtil.getValidationXmlMap(xmlFileName);
			log.error("===========================after get validationXmlMap");
		} catch (Exception e) {
			e.printStackTrace();
			return ExpImporterUtil.setResultMap(this.resMap, "${expInfoImport.error.readXML}" + e.getMessage(), false);
		}
		String filePath = null;
		try {
			log.error("===========================before get filePath");
			filePath = "";//ExpImporterUtil.getUploadedImportFilePath(relativePathFromDB);
			log.error("===========================after get filePath");
		} catch (Exception e) {
			e.printStackTrace();
			return ExpImporterUtil.setResultMap(this.resMap, "${expInfoImport.error.readFile}" + e.getMessage(), false);
		}
		try {
			log.error("===========================before checkAndImportExcel");
			checkAndImportExcel(attId, filePath, sheetNum, validationXmlMap, fileDesc);
			log.error("===========================after checkAndImportExcel");
		} catch (Exception e) {
			
			return ExpImporterUtil.setResultMap(this.resMap, "${expInfoImport.error.validate}" + e.getMessage(), false);
		}
		return ExpImporterUtil.setResultMap(this.resMap, "", true);
	}

	/**
	 * 
	 * 初始化全局变量
	 * 
	 * @throws Exception
	 *             the exception 异常信息
	 */
	private void initGlobalVariables() throws Exception {
		this.message = "";
		this.resMap = new HashMap<String, Object>();
		this.wrongRowColNumberSet = new HashSet<String>();
		this.wrongRowNumberSet = new HashSet<Integer>();
	}

	/**
	 * 
	 * 保存excel的数据到数据库
	 * 
	 * @param attId
	 *            the attId 附件id
	 * @param sheet
	 *            the sheet 要操作的excel工作区
	 * @param fileDesc
	 *            the fileDesc 附件说明
	 * @return String the string 返回操作后的信息
	 * @throws Exception 
	 * @throws Exception
	 *             the exception 异常信息
	 */


	
	

	
	/**
	 * 辅评审专业验证。验证数量及关联
	 * （专家修改_专家导入新增）
	 * @param content 辅评专业信息
	 * @param sheet  
	 * @param rowNum 当前行号
	 * @param col    当前列
	 * @return
	 * @throws Exception
	 */
	public String verifyAssistCategorys(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
		String[] contents = content.split(";");
		
		return "success";
	}
	public String verifyIdcardnum(String content,Sheet sheet,int rowNum,Cell[]col){
		
		return"success";
	}
	/**
	 * 
	 * 自定义验证 评审专业
	 * 
	 * @param content
	 *            the content 评审专业信息
	 * @param sheet
	 *            the sheet 当前操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 当前行号
	 * @param col
	 *            the col 当前列的信息
	 * @return String the string 返回验证结果
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public String verifyCategory(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
	
		return "success";
	}

	/**
	 * 
	 * 自定义验证 职称
	 * 
	 * @param content
	 *            the content 职称信息
	 * @param sheet
	 *            the sheet 当前操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 行号
	 * @param col
	 *            the col 当前列信息
	 * @return String the string 验证结果
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public String verifyDutyName(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
		String[] contents = content.split(";");
		
		return "success";
	}
	public String verifyAssistOrUpdateForIdCard(String content, Sheet sheet, int rowNum, Cell[] cols) throws Exception {
		
		return"success";
	}
	/**
	 * 辅评审专业从事起始年验证。验证格式、数量对应
	 * （专家修改_专家导入新增）
	 * @param content 辅评专业从事起始年信息
	 * @param sheet  
	 * @param rowNum 当前行号
	 * @param col    当前列
	 * @return
	 * @throws Exception
	 */
	public String verifyAssistCategorysBeginYear(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
		boolean existFlag = false;
		// 相关联的单元格的列号
		int relationColNum = 0;
		
		return "success";
	}

	/**
	 * 
	 * 自定义验证 工作时间
	 * 
	 * @param content
	 *            the content 工作时间信息
	 * @param sheet
	 *            the sheet 当前操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 行号
	 * @param col
	 *            the col 当前列信息
	 * @return String the string 验证结果
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public String verifyWorkTime(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
		boolean existFlag = false;
		// 相关联的单元格的列号
		int relationColNum = 0;
		
		return "success";
	}

	/**
	 * 
	 * 自定义验证 工作单位
	 * 
	 * @param content
	 *            the content 工作单位信息
	 * @param sheet
	 *            the sheet 当前操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 行号
	 * @param col
	 *            the col 当前列信息
	 * @return String the string 验证结果
	 * 
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public String verifyWorkUnit(String content, Sheet sheet, int rowNum, Cell[] col) throws Exception {
		// 相关联的单元格的列号
		int relationColNum = 0;
		
		return "success";
	}

	
	public static void main(String args[]){  
		Pattern pa = Pattern.compile("0|1|(0;1)|(1;0)|(0;)");
		System.out.println(pa.matcher("0;2").matches());
	}
}
