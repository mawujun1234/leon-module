package com.mawujun.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.mawujun.generator.model.FieldDefine;
import com.mawujun.pk.OrdSzrt.PK;


/**
 * 符合主键的demo1
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
@Entity(name="ord_ord_szrt")
@IdClass(PK.class)
public class OrdSzrt {
	
	@Id
	@FieldDefine(title="订货批号",hidden=true)
	@Column(length=36)
	private String ormtno;
	@Id
	@FieldDefine(title="订货单位",hidden=true)
	@Column(length=36)
	private String ordorg;
	@Id
	@FieldDefine(title="规格范围",hidden=true)
	@Column(length=36)
	private String sizegp;
	@Id
	@FieldDefine(title="规格代码",hidden=true)
	@Column(length=36)
	private String sizeno;
	@Id
	@FieldDefine(title="系列",hidden=true)
	@Column(length=36)
	private String spseno;
	@Id
	@FieldDefine(title="版型",hidden=true)
	@Column(length=36)
	private String versno;
	
	@FieldDefine(title="品牌",hidden=true)
	@Column(length=36)
	private String bradno;
	@FieldDefine(title="大类",hidden=true)
	@Column(length=36)
	private String spclno;
	
	@FieldDefine(title="规格类型",hidden=true)
	@Column(length=36)
	private String sizety;
	@FieldDefine(title="比例",hidden=true)
	private Double szrate;
	
	
	public OrdSzrt.PK geetPK(){
		OrdSzrt.PK pk=new OrdSzrt.PK();
		pk.setOrdorg(ordorg);
		pk.setOrmtno(ormtno);
		pk.setSizegp(sizegp);
		pk.setSizeno(sizeno);
		pk.setSpseno(spseno);
		pk.setVersno(versno);
		return pk;
	}
	
	public static class PK implements Serializable {
		/**
		 * @author mawujun qq:16064988 mawujun1234@163.com
		 */
		private static final long serialVersionUID = 1L;
		private String ormtno;
		private String ordorg;
		private String sizegp;
		private String sizeno;
		private String spseno;
		private String versno;
		public String getOrmtno() {
			return ormtno;
		}
		public void setOrmtno(String ormtno) {
			this.ormtno = ormtno;
		}
		public String getOrdorg() {
			return ordorg;
		}
		public void setOrdorg(String ordorg) {
			this.ordorg = ordorg;
		}
		public String getSizegp() {
			return sizegp;
		}
		public void setSizegp(String sizegp) {
			this.sizegp = sizegp;
		}
		public String getSizeno() {
			return sizeno;
		}
		public void setSizeno(String sizeno) {
			this.sizeno = sizeno;
		}
		public String getSpseno() {
			return spseno;
		}
		public void setSpseno(String spseno) {
			this.spseno = spseno;
		}
		public String getVersno() {
			return versno;
		}
		public void setVersno(String versno) {
			this.versno = versno;
		}
		
	}


	public String getOrmtno() {
		return ormtno;
	}


	public void setOrmtno(String ormtno) {
		this.ormtno = ormtno;
	}


	public String getOrdorg() {
		return ordorg;
	}


	public void setOrdorg(String ordorg) {
		this.ordorg = ordorg;
	}


	public String getSizegp() {
		return sizegp;
	}


	public void setSizegp(String sizegp) {
		this.sizegp = sizegp;
	}


	public String getSizeno() {
		return sizeno;
	}


	public void setSizeno(String sizeno) {
		this.sizeno = sizeno;
	}


	public String getBradno() {
		return bradno;
	}


	public void setBradno(String bradno) {
		this.bradno = bradno;
	}


	public String getSpclno() {
		return spclno;
	}


	public void setSpclno(String spclno) {
		this.spclno = spclno;
	}


	public String getSpseno() {
		return spseno;
	}


	public void setSpseno(String spseno) {
		this.spseno = spseno;
	}


	public String getVersno() {
		return versno;
	}


	public void setVersno(String versno) {
		this.versno = versno;
	}


	public String getSizety() {
		return sizety;
	}


	public void setSizety(String sizety) {
		this.sizety = sizety;
	}


	public Double getSzrate() {
		return szrate;
	}


	public void setSzrate(Double szrate) {
		this.szrate = szrate;
	}

}
