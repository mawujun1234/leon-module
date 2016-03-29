package com.mawujun.repository.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.mawujun.repository.DialectEnum;
import com.mawujun.repository.mybatis.DatabaseIdProviderCustom;
import com.mawujun.repository.mybatis.PageInterceptor;
import com.mawujun.repository.mybatis.YesNoTypeHandler;



//http://10176523.cn/archives/36/ 改成整合成使用hibernate5 试试，看看有没有问题
@Configuration
@EnableTransactionManagement(mode=AdviceMode.PROXY)
//@EnableAspectJAutoProxy(proxyTargetClass=false)
@ComponentScan(basePackages="com.mawujun.repository.test")
//includeFilters = @Filter(type = FilterType.ANNOTATION, value = {Repository.class}))
@MapperScan("com.mawujun.repository.test")
public class RepositoryConfig  implements TransactionManagementConfigurer{
	private String jdbc_packagesToScan="com.mawujun.repository.test";
	private String jdbc_db_type="oracle";
	
	private String jdbc_driver="oracle.jdbc.driver.OracleDriver";//"org.h2.Driver";
	private String jdbc_url="jdbc:oracle:thin:@127.0.0.1:1521:ems";//"jdbc:h2:mem:BaseProject;DB_CLOSE_DELAY=-1;MVCC=TRUE";
	private String jdbc_username="ems";//"sa";
	private String jdbc_password="ems";
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		//MySqlSessionFactoryBean sqlSessionFactoryBean=new MySqlSessionFactoryBean();
		//sqlSessionFactoryBean.setSessionFactory(sessionFactory());
		
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		
		sqlSessionFactoryBean.setDataSource(dataSource());
		
		
		sqlSessionFactoryBean.setTypeHandlers(new TypeHandler[]{new YesNoTypeHandler()});
		
		PageInterceptor pageInterceptor=new PageInterceptor();
		pageInterceptor.setDialectClass(DialectEnum.valueOf(jdbc_db_type).get_mybatis_dialect());
		sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
		
		DatabaseIdProviderCustom provider=new DatabaseIdProviderCustom();
		provider.setDatabaseId(jdbc_db_type);
		sqlSessionFactoryBean.setDatabaseIdProvider(provider);
		
		//sqlSessionFactoryBean.setMapperLocations(mapperLocations);
		
		return sqlSessionFactoryBean.getObject();
	}
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurer() {
//		MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
//		mapperScannerConfigurer.setBasePackage(jdbc_packagesToScan);
//		mapperScannerConfigurer.setAnnotationClass(org.springframework.stereotype.Repository.class);
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//		return mapperScannerConfigurer;
//	}
	
	@Bean
	public DataSource dataSource() throws SQLException {
		//https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_DruidDataSource%E5%8F%82%E8%80%83%E9%85%8D%E7%BD%AE
		DruidDataSource datasource=new DruidDataSource();
		//BasicDataSource datasource=new BasicDataSource();
		datasource.setDriverClassName(jdbc_driver);
		datasource.setUrl(jdbc_url);
		datasource.setUsername(jdbc_username);
		datasource.setPassword(jdbc_password);
		
		datasource.setDefaultAutoCommit(false);
		
		//通常来说，只需要修改initialSize、minIdle、maxActive。
		datasource.setInitialSize(1);
		datasource.setMinIdle(1);
		datasource.setMaxActive(50);
		//datasource.setMaxWait(600000);
		
		//如果用Oracle，则把poolPreparedStatements配置为true，mysql可以配置为false。分库分表较多的数据库，建议配置为false。
		datasource.setPoolPreparedStatements(true);
		
		
		//datasource.setFilters("stat,slf4j");
//		Log4jFilter log_filter=new Log4jFilter();
//		log_filter.setResultSetLogEnabled(false);
		Slf4jLogFilter log_filter=new Slf4jLogFilter();
		log_filter.setResultSetLogEnabled(true);
		ArrayList<com.alibaba.druid.filter.Filter> filter_list=new ArrayList<com.alibaba.druid.filter.Filter>();
		filter_list.add(log_filter);
		datasource.setProxyFilters(filter_list);
		return datasource;
	}
	
	
	
	@Bean
	public SessionFactory sessionFactory() throws IOException, SQLException {
		LocalSessionFactoryBean localSessionFactoryBean=new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource());
		localSessionFactoryBean.setPackagesToScan(jdbc_packagesToScan);
		
		Properties hibernateProperties=new Properties();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.put("hibernate.dialect", DialectEnum.valueOf(jdbc_db_type).get_hibernate_dialect());
		
		hibernateProperties.put("hibernate.show_sql", false);
		hibernateProperties.put("hibernate.format_sql", false);
		hibernateProperties.put("use_sql_comments", false);
		hibernateProperties.put("hibernate.max_fetch_depth", 2);
		hibernateProperties.put("hibernate.jdbc.batch_size", 30);
		
		hibernateProperties.put("hibernate.generate_statistics", false);
		hibernateProperties.put("hibernate.cache.use_structured_entries", false);
		
		//hibernateProperties.put("hibernate.connection.release_mode", "after_statement");
		//hibernateProperties.put("transaction.auto_close_session", false);
		//hibernateProperties.put("hibernate.connection.autocommit", true);
		
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		localSessionFactoryBean.afterPropertiesSet();
		return (SessionFactory)localSessionFactoryBean.getObject();
	}
	
	
	@Bean
	public PlatformTransactionManager transactionManager() throws IOException, SQLException {
		HibernateTransactionManager transactionManager=new HibernateTransactionManager();
		//transactionManager.setDataSource(dataSource());
		transactionManager.setSessionFactory(sessionFactory());
		//transactionManager.setRollbackOnCommitFailure(true);
		//transactionManager.set
		
		
		
		return transactionManager;
	}
	
	@Override
    public PlatformTransactionManager annotationDrivenTransactionManager(){
		PlatformTransactionManager transactionManager=null;
        try {
        	transactionManager= transactionManager();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return transactionManager;
    }

}
