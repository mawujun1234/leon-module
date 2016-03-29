package com.mawujun.repository.idEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.mawujun.repository.hibernate.validate.ValidateEntity;

/**
 * 自动增长identity
 * 统一定义id的entity基类.是使用自动生成主键作为生成策略
 * 适用于MySQL、DB2、MS SQL Server，采用数据库生成的主键，用于为long、short、int类型生成唯一标识
 * 使用SQL Server 和 MySQL 的自增字段，这个方法不能放到 Oracle 中，Oracle 不支持自增字段，要设定sequence（MySQL 和 SQL Server 中很常用）
 * 
 * @author mawujun
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class AutoIdEntity  implements IdEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.hibernate.annotations.AccessType("property")
	protected Integer id;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutoIdEntity other = (AutoIdEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}