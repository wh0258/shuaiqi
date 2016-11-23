/**
 * 
 */
package com.cnpc.pms.exp.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.exp.inf.dto.ExpertCustomValidation;
import com.cnpc.pms.exp.inf.dto.ExpertReferValidation;
import com.cnpc.pms.exp.inf.dto.ExpertRegValidation;
import com.cnpc.pms.exp.inf.dto.ExpertUniqValidation;


/**
 * 专家导入的util类.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author yfei
 * @since 2011-8-8
 */
public abstract class ExpImporterUtil {

	/** manager后缀 */
	private static final String MANAGER_POSTFIX = "Manager";
	/** 默认的分隔符 */
	private static final String DEFAULT_SEPERATOR = ";";
	/**
	 * the log
	 * */
	private final static Logger LOG = LoggerFactory.getLogger(ExpImporterUtil.class);

	/**
	 * 通过entity名称得到manger名称.
	 * 
	 * @param entityClazz
	 *            实体类名
	 * @return 实体对应的manger名称
	 */
	public static String getManagerName(String entityClazz) {
		//String entityName = SpringHelper.getShortNameAsProperty(entityClazz);
		//String managerName = entityName + MANAGER_POSTFIX;
		//return managerName;
		return "";
	}

	/**
	 * 
	 * 把日期格式化为参数"dateFormat"指定的格式,返回Date类型的结果
	 * 
	 * @param content
	 *            the content 要转换的信息
	 * @param dateFormat
	 *            the dateFormat 要转成的日期格式
	 * @return Date the date 返回转换后的日期
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static Date convertDate(String content, String dateFormat) throws Exception {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		String dateString = content;
		Date date = null;
		if(dateString!=null){
			date = df.parse(dateString);
		}
		return date;
	}

	/**
	 * 
	 * 把专家性别的"男,女"映射为数字"0,1"
	 * 
	 * @param content
	 *            the content 要转换的信息
	 * @return String the string 返回转换后的信息0表示男1表示女
	 * @throws Exception
	 *             the exception 异常信息
	 */
	/*public static String convertSex(String content) throws Exception {
		String resContont = null;
		if (StringUtils.isNotBlank(content)) {
			if (content.equals(SpringHelper.getMessage("expSex.male"))) {
				resContont = ExpCommonConstant.EXP_SEX_MALE;
			}
			if (content.equals(SpringHelper.getMessage("expSex.female"))) {
				resContont = ExpCommonConstant.EXP_SEX_FEMALE;
			}
		}
		return resContont;
	}*/

	/**
	 * 根据从数据库中读出的上传文件的相对路径，获取文件全路径
	 * 
	 * @param relativePathFromDB
	 *            the relativePathFromDB 文件的相对路径
	 * @return String the string 获取文件全路径
	 * @throws Exception
	 *             the exception 异常信息
	 */
	/*public static String getUploadedImportFilePath(String relativePathFromDB) throws Exception {
		String filePath = null;
		// 得到当前操作系统的文件分隔符
		String separator = System.getProperty("file.separator");
		// 把application.property中配置的路径分隔符"//"替换为当前操作系统的分隔符
		String filePathRoot = PropertiesUtil.getValue("file.root").replace("//", separator);
		
		// 把数据库中存放的路径的分隔符"\"替换为当前操作系统的分隔符
		// relativePathFromDB = relativePathFromDB.replace("\\", separator).replace("/", separator);
		filePath = filePathRoot + relativePathFromDB;
		return filePath;
	}*/

	/**
	 * 根据验证规则的xml文件名称，获取文件全路径
	 * 
	 * @param xmlFileName
	 *            the xmlFileName 证规则的xml文件名称
	 * @return String the string 文件全路径
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static String getValiadationXmlPath(String xmlFileName) throws Exception {
		String xmlFilePath = null;
		// 通过该方式得到
		Resource[] resources = ConfigurationUtil.getAllResources("/conf/" + xmlFileName);
		// 因为只有一个文件,通过resources[0]得到
		xmlFilePath = resources[0].getURI().getPath();
		LOG.error("=====================path" + xmlFilePath);
		return xmlFilePath;
	}

	/**
	 * 
	 * 根据验证规则的类型名称获得验证规则对象
	 * 
	 * @param type
	 *            the type 验证规则类型名称
	 * @param childchildchilds
	 *            the childchildchilds xml子分支信息
	 * @return Object the object 返回验证规则对象
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static Object getValidateByType(String type, List<Element> childchildchilds) throws Exception {
		// 如果是正则表达式验证
		if (("reg").equals(type)) {
			ExpertRegValidation regValidation = new ExpertRegValidation();
			String[] regs = new String[childchildchilds.size()];
			for (int n = 0; n < childchildchilds.size(); n++) {
				Element childchildchild = childchildchilds.get(n);
				String evalue = childchildchild.getText();
				regs[n] = evalue;
			}
			regValidation.setRegs(regs);
			return regValidation;
		}
		// 如果是数据唯一性验证
		if (("uniq").equals(type)) {
			ExpertUniqValidation uniqValidation = new ExpertUniqValidation();
			Element childchildchild = childchildchilds.get(0);
			String classNameValue = childchildchild.getText();
			childchildchild = childchildchilds.get(1);
			String fieldNameValue = childchildchild.getText();
			uniqValidation.setClassName(classNameValue);
			uniqValidation.setFieldName(fieldNameValue);
			return uniqValidation;
		}
		// 如果是外键关联验证
		if (("refer").equals(type)) {
			ExpertReferValidation referValidation = new ExpertReferValidation();
			Element childchildchild = childchildchilds.get(0);
			String classNameValue = childchildchild.getText();
			childchildchild = childchildchilds.get(1);
			String fieldNameValue = childchildchild.getText();
			referValidation.setClassName(classNameValue);
			referValidation.setFieldName(fieldNameValue);
			return referValidation;
		}
		// 如果是自定义验证
		if (("custom").equals(type)) {
			ExpertCustomValidation customValidation = new ExpertCustomValidation();
			Element childchildchild = childchildchilds.get(0);
			String classNameValue = childchildchild.getText();
			childchildchild = childchildchilds.get(1);
			String methodNameValue = childchildchild.getText();
			customValidation.setClassName(classNameValue);
			customValidation.setMethodName(methodNameValue);
			return customValidation;
		}
		return null;
	}

	/**
	 * 
	 * 从xml文件中得到校验规则
	 * 
	 * @param xmlFilePath
	 *            the xmlFilePath 存放验证规则的xml文件的路径
	 * @return Map<String, List<Object>> the map<String, List<Object>> 返回存放验证规则的map
	 * @throws Exception
	 *             the exception 异常信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<Object>> getValidationXmlMap(String xmlFilePath) throws Exception {
		// 通过该方式得到
		Resource[] resources = ConfigurationUtil.getAllResources("/conf/" + xmlFilePath);
		HashMap<String, List<Object>> xmlMap = new HashMap<String, List<Object>>();
		// 因为只有一个文件,通过resources[0]得到
		if (resources.length == 0) {
			throw new Exception("读取校验文件失败");
		}
		SAXReader parser = new SAXReader();
		// doc对象对应具体一个xml文件
		//Log.error("===============before read in getValidationXmlMap");
		Document doc = parser.read(resources[0].getInputStream());
		Element root = doc.getRootElement(); // validators
		//Log.error("===============after read in getValidationXmlMap");
		List<Element> childs = root.elements();
		//Log.error("===============before iterator in getValidationXmlMap");
		for (int i = 0; i < childs.size(); i++) {
			Element child = childs.get(i); // 得到一个header
			Attribute a = child.attribute("name");
			String value = a.getValue();
			// 得到该header下的所有validator
			List<Element> childchilds = child.elements();
			// Map中一个value中存的验证规则List
			List<Object> validationList = new ArrayList<Object>();
			for (int m = 0; m < childchilds.size(); m++) {
				Element childchild = childchilds.get(m);
				Attribute b = childchild.attribute("type");
				String type = b.getValue();
				// 得到该validator下的所有验证规则
				List<Element> childchildchilds = childchild.elements();
				//Log.error("===============before getValidateByType");
				Object validateObj = getValidateByType(type, childchildchilds);
				//Log.error("===============after getValidateByType");
				if (validateObj != null) {
					validationList.add(validateObj);
				}
			}
			// 把得到的验证规则对象存入Map中
			xmlMap.put(value, validationList);
		}
		//Log.error("===============after iterator in getValidationXmlMap");
		return xmlMap;

	}

	/**
	 * 得到"从事起始年"的值
	 * 
	 * @param sheet
	 *            the sheet 要操作的Excel工作区
	 * @param col
	 *            the col 当前操作的列
	 * @return String[] thw string[] 从事起始年（数组形式，各年限之间以逗号隔开）
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static String[] getBeginYearContent(Sheet sheet, Cell[] col) throws Exception {
		int relationColNum = 0;
		// 得到相"从事起始年"列的列号,存入relationColNum
		for (int n = 0; n < sheet.getRow(0).length; n++) {
//			if ((SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_ASSISTCATEGORYBEGINYEAR"))
//																								.equals(sheet.getRow(0)[n]
//																															.getContents())) {
//				relationColNum = n;
//				break;
//			}
		}
		// 得到从事起始年的值
		String beginYearContent = col[relationColNum].getContents().replace(" ", "");
		// 得到以分号分隔开的字符数组
		String[] beginYearContents = beginYearContent.split(DEFAULT_SEPERATOR);
		return beginYearContents;
	}

	/**
	 * 得到"工作单位"列所对应的"是否外部专家"列的值
	 * 
	 * @param content
	 *            the content 专家工作单位信息
	 * @param sheet
	 *            the sheet 当前操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 行号
	 * @return String the string 返回"是否外部专家"列的值
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static String getWorkUnitSource(Sheet sheet, int rowNum) throws Exception {
		// 相关联的单元格的列号
		int relationColNum = 0;
		for (int n = 0; n < sheet.getRow(0).length; n++) {
//			if ((SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_SOURCE"))
//																							.equals(sheet.getRow(0)[n]
//																														.getContents())) {
//				relationColNum = n;
//				break;
//			}
		}
		boolean res = false;
		res = (ExpCommonConstant.EXPERT_SOURCE_IN).equals(sheet.getRow(rowNum)[relationColNum].getContents());
		// 如果是内部单位编码
		if (res) {
			return ExpCommonConstant.EXPERT_SOURCE_IN;
		} else {
			return ExpCommonConstant.EXPERT_SOURCE_OUT;
		}
	}

	/**
	 * 
	 * 初始化Excel表头列，设置每个汉字表头所对应的数字编码
	 * 
	 * @return Map<String, Integer> the map<String, Integer> 表头信息
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static Map<String, Integer> initExcelHeaderMap() throws Exception {
		Map<String, Integer> headerMap = new HashMap<String, Integer>();
		//以下是国际化属性文件取常量值，我们可根据自己的文件，直接设置
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_SER_NUM"), 0);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_NAME"), 1);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_SEX"), 2);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_NATION"), 3);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_POLITICS"), 4);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_IDCARDNUM"), 5);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_HEALTH"), 6);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_BIRTHDAY"), 7);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_ADDRESS"), 8);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_POSTCODE"), 9);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_MOBILE"), 10);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_PHONE"), 11);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_FAX"), 12);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_EMAIL"), 13);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_SPECIALITY"), 14);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_DUTY"), 15);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_DUTYNAME"), 16);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_WORKSTATE"), 17);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_JOINWORK"), 18);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_WORKUNIT"), 19);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_UNITCODE"), 20);		
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_SOURCE"), 21);
//		
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_LEVEL"), 22);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_RECOMMENDORG"), 23);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION"), 24);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION2"), 25);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION3"), 26);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_MAINCATEGORYCODE"), 27);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_MAINCATEGORYBEGINYEAR"), 28);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_ASSISTCATEGORYCODE"), 29);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_ASSISTCATEGORYBEGINYEAR"), 30);
//		headerMap.put(SpringHelper.getMessage("expInfoImport.excelHeader.EXP_IMPORT_EXP_RECOMMENDORGCODE"), 31);
		
//			#专家信息导入Excel表头
//			expInfoImport.excelHeader.EXP_IMPORT_SER_NUM = 序号
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_NAME = 专家姓名*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_SEX = 专家性别*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_NATION = 民族
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_POLITICS = 政治面貌
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_IDCARDNUM = 身份证*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_HEALTH = 身体状况
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_BIRTHDAY = 出生日期
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_ADDRESS = 通讯地址
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_POSTCODE = 邮编
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_MOBILE = 手机号码*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_PHONE = 固定电话*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_FAX = 传真
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_EMAIL = 电子邮箱
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_SPECIALITY = 专业特长*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_DUTY = 职务*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_DUTYNAME = 职称*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_WORKSTATE = 任职状态*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_JOINWORK = 参加工作时间
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_WORKUNIT = 工作单位*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_UNITCODE= 工作单位组织机构代码*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_SOURCE = 是否外部专家*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_PROCUREMENTTYPE = 项目类别*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_SORT = 专家类别
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_LEVEL = 专家管理分级*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_RECOMMENDORG = 推荐单位*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_RECOMMENDORGCODE = 推荐单位组织机构代码*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION = 常住地*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION2 = 常住地2
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_LOCATION3 = 常住地3
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_MAINCATEGORYCODE = 主评审专业*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_MAINCATEGORYBEGINYEAR = 主评专业从事起始年*
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_ASSISTCATEGORYCODE=辅评审专业
//			expInfoImport.excelHeader.EXP_IMPORT_EXP_ASSISTCATEGORYBEGINYEAR=辅评审专业从事起始年
//
//			expInfoImport.error.readXML = 读取验证规则文件(xml)时出错:
//			expInfoImport.error.readFile = 读取导入文件时出错:
//			expInfoImport.error.validate = 执行校验和保存数据时出错:
//			expInfoImport.error.excelHeadError = 执行校验和保存数据时出错:
//
//			expInfoImport.message.row = 行,
//			expInfoImport.message.column = 列:
//			expInfoImport.message.formatError = 格式错误;
//			expInfoImport.message.isExist = 数据已存在;
//			expInfoImport.message.foreignKeyError = 的外键值不存在;
//			expInfoImport.message.notNull = 不能为空;
//			#评审专业校验
//			expInfoImport.message.assistCategoryNotExist = 与辅评专业相关的列不存在;
//			expInfoImport.message.categoryError = 与评审专业相关的项目类别列有错误;
//			expInfoImport.message.categoryMax = 辅评评审专业不能多于
//			expInfoImport.message.categoryUnit = 个;
//			expInfoImport.message.categoryCodeKey = 评审专业列,评审专业编码: 
//			expInfoImport.message.categoryCode = 评审专业列:评审专业代码
//			expInfoImport.message.NotFillProcurement = 与项目类别不对应;
//			#worktime校验
//			expInfoImport.message.beginYearRelated = 与从事起始年列相关的辅评审专业列有错误;
//			expInfoImport.message.beginYearCount = 从事起始年的数目和辅评评审专业不一致;
//			expInfoImport.message.beginYear = 从事起始年列
//			expInfoImport.message.beginYearFomatError = ,不是一个合理的年份
//			#是否外部专家校验
//			expInfoImport.message.sourceNotExist = 与工作单位相关的是否外部专家列不存在;
//			expInfoImport.message.sourceError = 与工作单位相关的是否外部专家列不存在;
//			expInfoImport.message.workunitCode = 工作单位列:编码
//			#职称校验
//			expInfoImport.message.businessMax = 职称列:职称个数不能超过
//			expInfoImport.message.businessUnit = 个;
//			expInfoImport.message.businessCode = 职称列:编码
//			#姓名和身份证联合校验
//			expInfoImport.message.idCardNumAndNameUpdate =更新至身份证与姓名都相同的旧版本数据
//			expInfoImport.message.idCardNumAndNameUpdateError =发现与旧版本数据存在相同的身份证但姓名却不同，请验证数据
//			expInfoImport.message.idCardNumExistsError =身份证已存在


		return headerMap;
	}

	/**
	 * 
	 * 设置"评审专业"列和"从事年限"列
	 * 
	 * @param expertInfo
	 *            the expertInfo 专家信息
	 * @param content
	 *            the content 专家评审专业信息
	 * @param sheet
	 *            the sheet 要操作的Excel工作区
	 * @param rowNum
	 *            the rowNum 行号
	 * @return ExpertInfo the expertInfo 返回设置后的专家信息
	 * @throws Exception
	 *             the exception 异常信息
	 */

//	public static ExpInfo setAssistCategoryCodeAndBeginYear(ExpInfo expertInfo, String content, Sheet sheet, int rowNum)
//		throws Exception {
//		String[] contents4 = content.split(DEFAULT_SEPERATOR);
//		String[] beginYears=getBeginYearContent(sheet, sheet.getRow(rowNum));	
//		int i = 1;
//		for (int k = 0; k < contents4.length; k++) {
//			if(i==1){
//				expertInfo.setAssistCategoryCode1(contents4[k]);
//				expertInfo.setWorkBeginYear1(beginYears[k]);
//			}
//			if(i==2){
//				expertInfo.setAssistCategoryCode2(contents4[k]);
//				expertInfo.setWorkBeginYear2(beginYears[k]);
//			}
//			if(i==3){
//				expertInfo.setAssistCategoryCode3(contents4[k]);
//				expertInfo.setWorkBeginYear3(beginYears[k]);
//			}
//			if(i==4){
//				expertInfo.setAssistCategoryCode4(contents4[k]);
//				expertInfo.setWorkBeginYear4(beginYears[k]);
//			}
//			i++;
//		}	
//		return expertInfo;
//	}

	/**
	 * 
	 * 设置"职称"列,最多有三个，分别对应dutyName1,dutyName2,dutyName3
	 * 
	 * @param expertInfo
	 *            the expertInfo 专家信息
	 * @param content
	 *            the content 要设置的职称信息
	 * @return ExpertInfo the expertInfo 返回专家信息
	 * @throws Exception
	 *             the exception 异常信息
	 */
//	public static ExpInfo setDutys(ExpInfo expertInfo, String content) throws Exception {
//		if (StringUtils.isBlank(content)) {
//			return expertInfo;
//		}
//		String[] contents = content.split(DEFAULT_SEPERATOR);
//		for (int k = 0; k < contents.length; k++) {
//			if (k == 0) {
//				expertInfo.setDutyName1(contents[k]);
//			}
//			if (k == 1) {
//				expertInfo.setDutyName2(contents[k]);
//			}
//			if (k == 2) {
//				expertInfo.setDutyName3(contents[k]);
//			}
//		}
//		return expertInfo;
//	}

	/**
	 * 设置返回给前台页面的结果Map的内容,并将该Map以JSON字符串形式返回
	 * 
	 * @param resultMap
	 *            the resultMap 待返回的结果Map
	 * @param errorMessage
	 *            the errorMessage 错误信息
	 * @param successFlag
	 *            the successFlag 成功标记
	 * @return String the string 返回结果的string形式
	 * @throws Exception
	 *             the exception 异常信息
	 */
	public static String setResultMap(Map<String, Object> resultMap, String errorMessage, boolean successFlag)
		throws Exception {

		if (!"".equals(errorMessage) && (errorMessage != null)) {
			resultMap.put("error", errorMessage);
		}
		resultMap.put("success", successFlag);
		return StrUtil.toJson(resultMap);
	}

	/**
	 * 
	 * 设置"工作单位"列
	 * 
	 * @param expertInfo
	 *            the expertInfo 专家信息
	 * @param content
	 *            the content 专家工作单位信息
	 * @param sheet
	 *            the sheet 要操作的Excel工作区
	 * @param i
	 *            the i 当前行号
	 * @return ExpertInfo the expertInfo 返回设置后的专家信息
	 * @throws Exception
	 *             the exception 异常信息
	 */
//	public static ExpInfo setWorkUnits(ExpInfo expertInfo, String content, Sheet sheet, int i) throws Exception {
//		// 得到"是否外部专家"列的值
//		String resStr = getWorkUnitSource(sheet, i);
//		// 如果是内部单位编码
//		if ((ExpCommonConstant.EXPERT_SOURCE_IN).equals(resStr)) {
//			// 通过组织机构code得到Id,存储Id
//			expertInfo.setWorkUnit(content);
//			// expertInfo.setWorkUnit(content); // 设置workUnit
//		} else {
//			expertInfo.setOuterUnit(content); // 设置outerUnit
//		}
//		return expertInfo;
//	}
	
	/**
	 * 根据推荐单位编码得到推荐单位的组织机构路径(path)
	 * @param orgCode 推荐单位编码
	 * @return 推荐单位的组织机构路径
	 * add by 杨飞 2012-07-26
	 */
//	public static String getRecommonOrgPath(String orgCode){
//		//得到组织机构实体
//		IFilter filter = FilterFactory.getSimpleFilter("code",orgCode);
//		PurStruOrgManager orgManager = (PurStruOrgManager)SpringHelper.getBean("purStruOrgManager");
//		PurStruOrg org = (PurStruOrg) orgManager.getUniqueObject(filter);
//		// 得到组织机构路径
//		return org==null?"":org.getPath();
//	}
}
