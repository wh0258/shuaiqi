/**
 * 
 */
package com.cnpc.pms.exp.common.util;

/**
 * 专家模块通用变量.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author yangfei
 * @since 2011-4-10
 */
public abstract class ExpCommonConstant {

	/** 系统远程接口开关 */
	public static final String SYSTEM_INTERFACE_SIT_FLAG = "systemInterfaceSitFlag";
	/** 专家准入审批模版 */
	public static final String APPROVAL_DOCUMENT_TYPE_ACCESS = "FLOW_TYPE_13";
	/** 专家变更审批模版 */
	public static final String APPROVAL_DOCUMENT_TYPE_CHANGE = "FLOW_TYPE_45";
	/** 重抽审批模版 */
	public static final String APPROVAL_DOCUMENT_TYPE_RESEL = "FLOW_TYPE_14";
	/** 抽取结果审批模版 */
	public static final String APPROVAL_DOCUMENT_TYPE_RESULT = "FLOW_TYPE_15";
	/** 作废审批模版 */
	public static final String APPROVAL_DOCUMENT_TYPE_PROJECTDROP = "FLOW_TYPE_16";
	/** 通过 */
	public static final String APPROVALTYPE_APPROVED = "0001";
	/** 拒绝 */
	public static final String APPROVALTYPE_REJECT = "0002";

	/** 抽取项目状态 0暂存1 抽取中 2逻辑删除 3 重抽申请 4结果确认申请 5 结果确认审批通过 6 作废 7作废申请 */
	/** 抽取结果项目状态-新建、暂存 */
	public static final String SELPROJECTSTATE_XJ = "0";
	/** 抽取结果项目状态-抽取中 */
	public static final String SELPROJECTSTATE_CQ = "1";
	/** 抽取结果项目状态-逻辑删除 */
	public static final String SELPROJECTSTATE_SC = "2";
	/** 抽取结果项目状态-重抽申请 */
	public static final String SELPROJECTSTATE_CC = "3";
	/** 抽取结果项目状态-结果确认申请 */
	public static final String SELPROJECTSTATE_DD = "4";
	/** 抽取结果项目状态-结果确认审批通过 */
	public static final String SELPROJECTSTATE_SP = "5";
	/** 抽取结果项目状态-作废 */
	public static final String SELPROJECTSTATE_ZF = "6";
	/** 抽取结果项目状态-作废申请 */
	public static final String SELPROJECTSTATE_ZFSQ = "7";
	/** 抽取结果项目状态-重抽审批退回 */
	public static final String SELPROJECTSTATE_CCREJECTED = "8";
	/** 抽取结果项目状态-不存在 这个只是当作一个特殊状态用 不需要设置数据字典 */
	public static final String SELPROJECTSTATE_NONE = "-1";

	/** 抽取结果专家状态-新建、暂存（直接抽取时初步选定使用） */
	public static final String SELRESULTSTATE_XJ = "0";
	/** 抽取结果专家状态-选用 */
	public static final String SELRESULTSTATE_XY = "1";
	/** 抽取结果专家状态-弃用 */
	public static final String SELRESULTSTATE_QY = "2";
	/** 抽取结果专家状态-回避 */
	public static final String SELRESULTSTATE_HB = "3";

	/** 标段是否选中-0 未选中 */
	public static final int SELPROJECTSTAGE_NOTSEL = 0;
	/** 标段是否选中-1 选中 */
	public static final int SELPROJECTSTAGE_SEL = 1;

	/** 专家信息状态-暂存 、新建 */
	public static final String EXPERTSTATE_XJ = "0";
	/** 专家信息专家状态-通过 */
	public static final String EXPERTSTATE_TG = "1";
	/** 专家信息专家状态-已暂停 */
	public static final String EXPERTSTATE_ZT = "2";
	/** 专家信息专家状态-已退出 */
	public static final String EXPERTSTATE_TC = "3";
	/** 专家信息专家状态-已删除 */
	public static final String EXPERTSTATE_SC = "4";
	/** 专家信息专家状态-待审批 */
	public static final String EXPERTSTATE_DSP = "5";
	/** 专家信息专家状态-退回 */
	public static final String EXPERTSTATE_TH = "6";
	/*** 专家信息专家状态-变更待审批 */
	public static final String EXPERTSTATE_CHANGE = "7";

	/** 专家信息：专家类别-技术 */
	public static final String EXPERTSOURCE_TECH = "0";
	/** 专家信息：专家类别-商务 */
	public static final String EXPERTSOURCE_BUSINESS = "1";

	/** 专家信息：内外部专家-内部 */
	public static final String EXPERT_SOURCE_IN = "0";
	/** 专家信息：内外部专家-外部 */
	public static final String EXPERT_SOURCE_OUT = "1";

	/** 专家信息：等级-总部级 */
	public static final String EXPERT_LEVEL_HQ = "0";
	/** 专家信息：等级-所属企业 */
	public static final String EXPERT_LEVEL_COMP = "1";
	/** 专家信息：等级-所属其他 */
	public static final String EXPERT_LEVEL_OTHER = "2";
	/** 采购类别-不分项目类别，-1*/
	public static final String EXPERT_PROCUREMENTTYPE_NONE = "-1";
	/** 采购类别-物资 0 工程1 服务2  集中采购3*/
	public static final String EXPERT_PROCUREMENTTYPE_WZ = "0";
	/** 采购类别-物资 0 工程1 服务2  集中采购3*/
	public static final String EXPERT_PROCUREMENTTYPE_GC = "1";
	/** 采购类别-物资 0 工程1 服务2  集中采购3*/
	public static final String EXPERT_PROCUREMENTTYPE_FW = "2";
	/** 采购类别-物资 0 工程1 服务2  集中采购3*/
	public static final String EXPERT_PROCUREMENTTYPE_JZ = "3";
	/** 采购类别代码开头-物资 B 工程 A 服务C */
	public static final String EXPERT_PROCUREMENTTYPE_GCSTART = "A";
	/** 采购类别代码开头-物资 B 工程 A 服务C */
	public static final String EXPERT_PROCUREMENTTYPE_FWSTART = "C";
	/** 采购类别代码开头-物资 B 工程 A 服务C */
	public static final String EXPERT_PROCUREMENTTYPE_WZSTART = "B";
	/** 暂存按钮 */
	public static final String BUTTON_FOR_ZX = "1";
	/** 保存按钮 */
	public static final String BUTTON_FOR_BC = "2";
	/** 抽取方式 0 随机抽取 1直接选取 */
	public static final String EXPERT_SELMETHOD_RANDOM = "0";
	/** 抽取方式 0 随机抽取 1直接选取 */
	public static final String EXPERT_SELMETHOD_SELF = "1";

	/** 招标项目级别 1 1级 2 2级 3 其他 */
	public static final String EXPERT_BIDPROJECT_LEVEL1 = "1";
	/** 招标项目级别 1 1级 2 2级 3 其他 */
	public static final String EXPERT_BIDPROJECT_LEVEL2 = "2";
	/** 招标项目级别 1 1级 2 2级 3 其他 */
	public static final String EXPERT_BIDPROJECT_LEVEL3 = "3";

	/** 专家性别男 */
	public static final String EXP_SEX_MALE = "0";
	/** 专家性别女 */
	public static final String EXP_SEX_FEMALE = "1";

	/** 删除标记 0:未删除 */
	public static final String EXP_NOT_DELETED = "0";
	/** 删除标记 1:已删除 */
	public static final String EXP_DELETED = "1";

	/** 评审通过标记 0:未通过 */
	public static final String EXP_EXZAMINE_NOT_PASSED = "0";
	/** 评审通过标记 1:通过 */
	public static final String EXP_EXZAMINE_PASSED = "1";

	/**
	 * 教育情况：0 博士 1 硕士 2 本科 3 专科 4 其他
	 */
	public static final String EDU_LEVEL_0 = "0";
	/**
	 * 教育情况：0 博士 1 硕士 2 本科 3 专科 4 其他
	 */
	public static final String EDU_LEVEL_1 = "1";
	/**
	 * 教育情况：0 博士 1 硕士 2 本科 3 专科 4 其他
	 */
	public static final String EDU_LEVEL_2 = "2";
	/**
	 * 教育情况：0 博士 1 硕士 2 本科 3 专科 4 其他
	 */
	public static final String EDU_LEVEL_3 = "3";
	/**
	 * 教育情况：0 博士 1 硕士 2 本科 3 专科 4 其他
	 */
	public static final String EDU_LEVEL_4 = "4";

	/** 表示为专家图片 */
	public static final int IS_PHOTO = 1;
	/** 表示不是专家图片 */
	public static final int IS_NOT_PHOTO = 0;
	/**
	 * 抽取方式 0 招标采购 1其他
	 */
	public static final String EXP_SELPROJECT_METHODBID="0";
	
	public static final String EXP_SELPROJECT_METHODOTHER="1";
	
	/** 专家导入数据状态：未更新 */
	public static final String EXP_IMPORT_DATA_TEMP = "0001";
	/** 专家导入数据状态：已更新 */
	public static final String EXP_IMPORT_DATA_FORMAL = "0002";

	/** 是否自注册（0否1是），为数据权限用 */
	public static final String EXPINFO_ISREGISTER_TRUE = "1";
	/** 是否自注册（0否1是），为数据权限用 */
	public static final String EXPINFO_ISREGISTER_FALSE = "0";

	/** 抽取方案状态 0001 新建 0002暂存 0003正常 0004结果确认申请 0005结果已确认 0006作废申请 0007已作废. */
	/**抽取方案状态：新建*/
	public static final String EXP_PLANSTATUS_NEW = "0001";
	/**抽取方案状态：暂存*/
	public static final String EXP_PLANSTATUS_TEMP = "0002";
	/**抽取方案状态：正常*/
	public static final String EXP_PLANSTATUS_NORMAL = "0003";
	/**抽取方案状态：结果确认申请*/
	public static final String EXP_PLANSTATUS_CONFIRM = "0004";
	/**抽取方案状态：结果已确认*/
	public static final String EXP_PLANSTATUS_CONFIRMED = "0005";
	/**抽取方案状态：作废申请*/
	public static final String EXP_PLANSTATUS_ABANDONE = "0006";
	/**抽取方案状态：已作废*/
	public static final String EXP_PLANSTATUS_ABANDONED = "0007";
	
	/** 抽取状态 0001新建 0002抽取中 0003重抽申请 0004重抽退回. */
	/** 抽取状态：新建*/
	public static final String EXP_SELSTATUS_NEW = "0001";
	/** 抽取状态：抽取中*/
	public static final String EXP_SELSTATUS_EXTRACTING = "0002";
	/** 抽取状态：重抽申请*/
	public static final String EXP_SELSTATUS_APPLY = "0003";
	/** 抽取状态：重抽退回*/
	public static final String EXP_SELSTATUS_RETURN = "0004";
	
	/** 专家类型 0001技术类型 0002商业类型*/
	/** 专家类型：技术类型 */
	public static final String EXP_EXPTYPE_TECH = "0001";
	/** 专家类型：商业类型 */
	public static final String EXP_EXPTYPE_BUSNI = "0002";
	
	/** 申请类型 0001确认申请 0002作废申请 */
	/** 申请类型：确认申请 */
	public static final String EXP_APPLYTYPE_CONFIRM = "0001";
	/** 申请类型：作废申请 */
	public static final String EXP_APPLYTYPE_ABANDON = "0002";
	
	/**
	 * 专家动态管理-申请流程状态
	 */
	/** 专家动态申请流程状态-新建 、暂存 */
    public static final String EXP_APPLYSTATUES_NEWCREATE = "0001";
    /** 专家动态申请流程状态-待审批 */
    public static final String EXP_APPLYSTATUES_PENDINGCHECK = "0002";
    /** 专家动态申请流程状态-退回 */
    public static final String EXP_APPLYSTATUES_REJECT = "0003";
    /*** 专家动态申请流程状态-审批通过 */
    public static final String EXP_APPLYSTATUES_CHECKED = "0004";
    
    /** 抽取过程-抽取结果状态 0001 选用 0002 弃用 0009 直接抽取的临时状态*/
    public static final String EXP_SELPROCESS_STATUS_XY = "0001";
    /** 抽取过程-抽取结果状态 0001 选用 0002 弃用0009 直接抽取的临时状态*/
    public static final String EXP_SELPROCESS_STATUS_QY = "0002";
    /** 抽取过程-抽取结果状态 0001 选用 0002 弃用0009 直接抽取的临时状态*/
    public static final String EXP_SELPROCESS_STATUS_TEMP = "0009";

}
