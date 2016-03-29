package com.mawujun.repository;

/**
 * DB2	org.hibernate.dialect.DB2Dialect
DB2 AS/400	org.hibernate.dialect.DB2400Dialect
DB2 OS390	org.hibernate.dialect.DB2390Dialect
Firebird	org.hibernate.dialect.FirebirdDialect
FrontBase	org.hibernate.dialect.FrontbaseDialect
HypersonicSQL	org.hibernate.dialect.HSQLDialect
Informix	org.hibernate.dialect.InformixDialect
Interbase	org.hibernate.dialect.InterbaseDialect
Ingres	org.hibernate.dialect.IngresDialect
Microsoft SQL Server 2005	org.hibernate.dialect.SQLServer2005Dialect
Microsoft SQL Server 2008	org.hibernate.dialect.SQLServer2008Dialect
Mckoi SQL	org.hibernate.dialect.MckoiDialect
MySQL	org.hibernate.dialect.MySQLDialect
MySQL with InnoDB	org.hibernate.dialect.MySQL5InnoDBDialect
MySQL with MyISAM	org.hibernate.dialect.MySQLMyISAMDialect
Oracle 8i	org.hibernate.dialect.Oracle8iDialect
Oracle 9i	org.hibernate.dialect.Oracle9iDialect
Oracle 10g	org.hibernate.dialect.Oracle10gDialect
Pointbase	org.hibernate.dialect.PointbaseDialect
PostgreSQL 8.1	org.hibernate.dialect.PostgreSQL81Dialect
PostgreSQL 8.2 and later	org.hibernate.dialect.PostgreSQL82Dialect
Progress	org.hibernate.dialect.ProgressDialect
SAP DB	org.hibernate.dialect.SAPDBDialect
Sybase ASE 15.5	org.hibernate.dialect.SybaseASE15Dialect
Sybase ASE 15.7	org.hibernate.dialect.SybaseASE157Dialect
Sybase Anywhere	org.hibernate.dialect.SybaseAnywhereDialect
 * @author mawujun email:16064988@163.com qq:16064988
 *
 */
public enum DialectEnum {
	mysql(org.hibernate.dialect.MySQL5InnoDBDialect.class,com.mawujun.repository.mybatis.dialect.MySQLDialect.class), 
	//MySQLDialect_mybatis(com.mawujun.repository.mybatis.dialect.MySQLDialect.class),
	
	oracle(org.hibernate.dialect.Oracle10gDialect.class,com.mawujun.repository.mybatis.dialect.OracleDialect.class), 
	//OracleDialect_mybatis(com.mawujun.repository.mybatis.dialect.OracleDialect.class),
	
	h2(org.hibernate.dialect.H2Dialect.class,com.mawujun.repository.mybatis.dialect.H2Dialect.class), 
	//H2Dialect_mybatis(com.mawujun.repository.mybatis.dialect.H2Dialect.class),
	
	db2(org.hibernate.dialect.DB2Dialect.class,com.mawujun.repository.mybatis.dialect.DB2Dialect.class), 
	//DB2Dialect_mybatis(com.mawujun.repository.mybatis.dialect.DB2Dialect.class),
	
	derby(org.hibernate.dialect.DerbyTenSevenDialect.class,com.mawujun.repository.mybatis.dialect.DerbyDialect.class), 
	//DerbyDialect_mybatis(com.mawujun.repository.mybatis.dialect.DerbyDialect.class),
	
	hsql(org.hibernate.dialect.HSQLDialect.class,com.mawujun.repository.mybatis.dialect.HSQLDialect.class), 
	//HSQLDialect_mybatis(com.mawujun.repository.mybatis.dialect.HSQLDialect.class),
	
	postgres(org.hibernate.dialect.PostgresPlusDialect.class,com.mawujun.repository.mybatis.dialect.PostgreSQLDialect.class), 
	//PostgreSQLDialect_mybatis(com.mawujun.repository.mybatis.dialect.PostgreSQLDialect.class),
	
	sqlserver(org.hibernate.dialect.SQLServer2008Dialect.class,com.mawujun.repository.mybatis.dialect.SQLServer2005Dialect.class), 
	//SQLServer2005Dialect_mybatis(com.mawujun.repository.mybatis.dialect.SQLServer2005Dialect.class),
	
	sybase(org.hibernate.dialect.SybaseDialect.class,com.mawujun.repository.mybatis.dialect.SybaseDialect.class);
	//SybaseDialect_mybatis(com.mawujun.repository.mybatis.dialect.SybaseDialect.class);


	private Class<?> dialect_mybatis;
	private Class<?> dialect_hibernate;

	private DialectEnum(Class<?> dialect_hibernate,Class<?> dialect_mybatis) {
		this.dialect_hibernate = dialect_hibernate;
		this.dialect_mybatis = dialect_mybatis;
	}
	
	public String get_hibernate_dialect(){
		return this.dialect_hibernate.getName();
	}
	
	public String get_mybatis_dialect(){
		return this.dialect_mybatis.getName();
	}


}
