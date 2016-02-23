package com.dc.esc.journallog.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

/**
 * 流水对象
 * */
public class JournalLog implements Serializable {
	/** ESC状态-失败 */
	public static final char ESCSTATUS_FAIL = '1';
	/** ESC状态-成功 */
	public static final char ESCSTATUS_SUCC = '0';
	/** 业务状态-成功 */
	public static final char BUSINESSSTATUS_FAIL = '1';
	/** 业务状态-成功 */
	public static final char BUSINESSSTATUS_SUCC = '0';

	public JournalLog() {
		super();
		TRANSDATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		TRYCOUNT = 0;
		ESCSTATUS = ESCSTATUS_SUCC;
	}

	/** 终端号 */
	private String TERMINALNO;
	/** 流水号 */
	private String ESCFLOWNO;
	/** 流水位置 C端还是P端 */
	private String FLOWLOCATION;
	/** 交易日期 yyyy-MM-dd */
	private String TRANSDATE;
	/** 服务ID */
	private String SERVICEID;
	/** 渠道ID */
	private String CHANNELID;
	/** 服务系统ID */
	private String SYSTEMID;
	/** ESC处理状态 */
	private char ESCSTATUS;
	// 业务相关
	/** 消费者流水号 */
	private String CONSUMERFLOWNO;
	/** 提供者流水号 */
	private String PROVIDERFLOWNO;
	/** 业务流水号 */
	private String BUSINESSFLOWNO;
	/** 响应码 */
	private String BUSINESSRESPCODE;
	/** 响应消息 */
	private String BUSINESSRESPMSG;
	/** 业务状态 */
	private char BUSINESSSTATUS;

	/** 时间戳1 接收时间 */
	private Timestamp TRANSSTAMP1;
	/** 时间戳2 发送时间 */
	private Timestamp TRANSSTAMP2;
	/** 时间戳3 返回时间 */
	private Timestamp TRANSSTAMP3;
	/** 时间戳4 响应时间 */
	private Timestamp TRANSSTAMP4;

	/** 补登次数 */
	private int TRYCOUNT;

	public int getTRYCOUNT() {
		return TRYCOUNT;
	}

	public void setTRYCOUNT(int tRYCOUNT) {
		TRYCOUNT = tRYCOUNT;
	}

	public String getTERMINALNO() {
		return TERMINALNO;
	}

	public void setTERMINALNO(String tERMINALNO) {
		TERMINALNO = tERMINALNO;
	}

	public String getESCFLOWNO() {
		return ESCFLOWNO;
	}

	public void setESCFLOWNO(String eSCFLOWNO) {
		ESCFLOWNO = eSCFLOWNO;
	}

	public Timestamp getTRANSSTAMP1() {
		return TRANSSTAMP1;
	}

	public void setTRANSSTAMP1(Timestamp tRANSSTAMP1) {
		TRANSSTAMP1 = tRANSSTAMP1;
	}

	public Timestamp getTRANSSTAMP2() {
		return TRANSSTAMP2;
	}

	public void setTRANSSTAMP2(Timestamp tRANSSTAMP2) {
		TRANSSTAMP2 = tRANSSTAMP2;
	}

	public Timestamp getTRANSSTAMP3() {
		return TRANSSTAMP3;
	}

	public void setTRANSSTAMP3(Timestamp tRANSSTAMP3) {
		TRANSSTAMP3 = tRANSSTAMP3;
	}

	public Timestamp getTRANSSTAMP4() {
		return TRANSSTAMP4;
	}

	public void setTRANSSTAMP4(Timestamp tRANSSTAMP4) {
		TRANSSTAMP4 = tRANSSTAMP4;
	}

	public String getFLOWLOCATION() {
		return FLOWLOCATION;
	}

	public void setFLOWLOCATION(String fLOWLOCATION) {
		FLOWLOCATION = fLOWLOCATION;
	}

	public String getTRANSDATE() {
		return TRANSDATE;
	}

	public void setTRANSDATE(String tRANSDATE) {
		TRANSDATE = tRANSDATE;
	}

	public String getSERVICEID() {
		return SERVICEID;
	}

	public void setSERVICEID(String sERVICEID) {
		SERVICEID = sERVICEID;
	}

	public String getCHANNELID() {
		return CHANNELID;
	}

	public void setCHANNELID(String cHANNELID) {
		CHANNELID = cHANNELID;
	}

	public String getSYSTEMID() {
		return SYSTEMID;
	}

	public void setSYSTEMID(String sYSTEMID) {
		SYSTEMID = sYSTEMID;
	}

	public char getESCSTATUS() {
		return ESCSTATUS;
	}

	public void setESCSTATUS(char eSCSTATUS) {
		ESCSTATUS = eSCSTATUS;
	}

	public String getCONSUMERFLOWNO() {
		return CONSUMERFLOWNO;
	}

	public void setCONSUMERFLOWNO(String cONSUMERFLOWNO) {
		CONSUMERFLOWNO = cONSUMERFLOWNO;
	}

	public String getPROVIDERFLOWNO() {
		return PROVIDERFLOWNO;
	}

	public void setPROVIDERFLOWNO(String pROVIDERFLOWNO) {
		PROVIDERFLOWNO = pROVIDERFLOWNO;
	}

	public String getBUSINESSFLOWNO() {
		return BUSINESSFLOWNO;
	}

	public void setBUSINESSFLOWNO(String bUSINESSFLOWNO) {
		BUSINESSFLOWNO = bUSINESSFLOWNO;
	}

	public String getBUSINESSRESPCODE() {
		return BUSINESSRESPCODE;
	}

	public void setBUSINESSRESPCODE(String bUSINESSRESPCODE) {
		BUSINESSRESPCODE = bUSINESSRESPCODE;
	}

	public String getBUSINESSRESPMSG() {
		return BUSINESSRESPMSG;
	}

	public void setBUSINESSRESPMSG(String bUSINESSRESPMSG) {
		BUSINESSRESPMSG = bUSINESSRESPMSG;
	}

	public char getBUSINESSSTATUS() {
		return BUSINESSSTATUS;
	}

	public void setBUSINESSSTATUS(char bUSINESSSTATUS) {
		BUSINESSSTATUS = bUSINESSSTATUS;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ESCFLOWNO == null) ? 0 : ESCFLOWNO.hashCode());
		result = prime * result
				+ ((FLOWLOCATION == null) ? 0 : FLOWLOCATION.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj.getClass() == JournalLog.class) {
			JournalLog jl = (JournalLog) obj;
			if (jl.getFLOWLOCATION().equals(FLOWLOCATION)
					&& jl.getESCFLOWNO().equals(ESCFLOWNO)) {
				return true;
			}
		}
		return false;
	}

	public String getKey() {
		return ESCFLOWNO + "@" + FLOWLOCATION;
	}
}
