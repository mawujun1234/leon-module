package com.mawujun.repository.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.mawujun.repository.cnd.Cnd;
import com.mawujun.utils.file.FileUtils;
import com.mawujun.utils.page.Pager;
import com.mawujun.utils.test.DbunitBaseRepositoryTest;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(transactionManager= "transactionManager") 
@ContextConfiguration(classes={RepositoryConfig.class})
//@Commit
public class RepositoryConfigTest  {
	
	private String EntityTest_TableName="t_EntityTest";
	//private static BaseRepository<EntityTest,Integer> entityTestService;
	//private static SessionFactory sessionFactory;
	//private static SqlSessionFactory sqlSessionFactory;
	
//	private static class EntityTestRepository implements IRepository<EntityTest,Integer> {
//		
//	}
	@Autowired
	private EntityTestRepository entityTestRepository;
	
	//@Autowired
	//private EntityTestService entityTestService;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		DbunitBaseRepositoryTest.init("org.h2.Driver", "jdbc:h2:mem:BaseProject;DB_CLOSE_DELAY=-1;MVCC=TRUE", "sa", "");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	// 讀取指定的資料集合
	private static IDataSet getXMLDataSet(String file) throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream(file));
	}

	
	@Before
	public void before() throws Exception {
		//DatabaseOperation.CLEAN_INSERT是DELETE_ALL和 INSERT的绑定. 
		DatabaseOperation.CLEAN_INSERT.execute(DbunitBaseRepositoryTest.getDbConn(), getXMLDataSet(FileUtils.getCurrentClassPath(this)+"com/mawujun/repository/config/RepositoryDb.xml"));
	}
	@After
	public void after() throws Exception {
		//tx.commit();
	}
	
//	protected int countRowsInTable() throws SQLException {
//		//return DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName);
//		 return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, EntityTest_TableName);
//    }
//	//@Autowired
//	//DataSource dataSource;
//	
//	protected JdbcTemplate jdbcTemplate;
//
//	@Autowired
//	public void setDataSource(DataSource dataSource) {
//		this.jdbcTemplate = new JdbcTemplate(dataSource);
//	}
	

	@Test
	public void testGet() {
		//fail("Not yet implemented");
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity=entityTestRepository.get(1);
		assertEquals(new Integer(1),entity.getId());
		assertEquals("admin1",entity.getFirstName());
		assertEquals("123",entity.getLastName());
		assertEquals(1,entity.getVersion());
		assertEquals(true,entity.getSex());
		//tx.commit();
	}
	@Test
	public void testBooleanType() {
		//fail("Not yet implemented");
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity=entityTestRepository.get(1);
		assertEquals(new Integer(1),entity.getId());
		assertEquals("admin1",entity.getFirstName());
		assertEquals("123",entity.getLastName());
		assertEquals(1,entity.getVersion());
		assertEquals(true,entity.getSex());
		
		
		//tx.commit();
	}
	

	@Test
	public void testQueryPage() {
		//PageParam param=PageParam.getInstance(0, 10);
		//PageResult<EntityTest> lists=entityTestRepository.queryPage(param);
		Pager<EntityTest> pager=new Pager<EntityTest>();
		pager.setStart(0);
		pager.setLimit(10);
		Pager<EntityTest> lists=entityTestRepository.queryPage(pager);
		assertEquals(3,lists.getTotal());
		assertEquals(new Integer(1),lists.getRoot().get(0).getId());
		assertEquals(true,lists.getRoot().get(0).getSex());
		assertEquals(false,lists.getRoot().get(1).getSex());
	}
	@Test
	public void testSave() throws DataSetException, SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		//fail("Not yet implemented");
		EntityTest entity=new EntityTest();
		entity.setFirstName("ma");
		entity.setLastName("wujun");
		entity.setEmail("160649888@163.com");
		entityTestRepository.create(entity);
		//tx.commit();
		
		assertNotNull(entity.getId());
		assertEquals(new Integer(4), entity.getId());
		assertEquals(0,entity.getVersion());
		assertEquals(4,entityTestRepository.queryAll().size());
		
		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id="+entity.getId());
//		assertEquals(1,table.getRowCount());
//		assertEquals("ma",table.getValue(0, "firstName"));
//		assertEquals("wujun",table.getValue(0, "lastName"));
		
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testSaveException() throws Exception {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		//tx.begin();
		//fail("Not yet implemented");
		EntityTest entity1=new EntityTest();
		entity1.setFirstName("ma");
		entity1.setLastName("wujun");
		entity1.setEmail("11");//如果不是email的格式，就会爆实体类验证异常。
		try {
			//不回滚的话，后面的测试就执行部下去了，会爆org.hibernate.TransactionException: nested transactions not supported异常
			entityTestRepository.create(entity1);//这里就报错了
			//tx.commit();
		} catch(Exception e){
			//tx.rollback();
			throw e;
		}
		assertEquals(3,entityTestRepository.queryAll().size());
		
	}

	@Test
	public void testUpdate() throws SQLException, DataSetException {
		//获取事物，再提交事物为什么不放到@Befor和@After方法里呢？因为和dbunit整合的时候，并且h2采用了多版本提交的时候，就会发现更改的数据没有提交，测试就不会通过
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity=new EntityTest();
		entity.setFirstName("ma");
		entity.setLastName("wujun");
		entity.setEmail("160649888@163.com");
		entity.setId(1);
		entity.setVersion(1);
		entityTestRepository.update(entity);
		//tx.commit();
		
		EntityTest entity111=entityTestRepository.get(1);
		assertEquals("ma",entity111.getFirstName());
		assertEquals("wujun",entity111.getLastName());
		
//		assertEquals(3,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1");
//		assertEquals(1,table.getRowCount());
//		assertEquals("ma",table.getValue(0, "firstName"));
//		assertEquals("wujun",table.getValue(0, "lastName"));
		
	}

	@Test
	public void testDeleteT() throws SQLException, DataSetException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity=new EntityTest();
		entity.setVersion(1);//这里必须加上version，否则将会报错
		entity.setId(1);
		entityTestRepository.delete(entity);
		//tx.commit();
		
		assertEquals(2,entityTestRepository.queryAll().size());
		
//		assertEquals(2,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1");
//		assertEquals(0,table.getRowCount());
	}

	@Test
	public void testDeleteSerializable() throws DataSetException, SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		entityTestRepository.deleteById(1);
		//tx.commit();
		//EntityTest entity=entityTestService.get(1);
		assertNull(entityTestRepository.get(1));
		//assertEquals(2,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1");
		//assertEquals(0,table.getRowCount());
	}

	@Test
	public void testBatchSaveTArray() throws SQLException {
		//fail("Not yet implemented");
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest[] entitys=new EntityTest[10];
		for(int i=0;i<entitys.length;i++){
			EntityTest entity=new EntityTest();
			entity.setFirstName("1");
			entity.setLastName("1");
			entity.setEmail("1@163.com");
			entitys[i]=entity;
		}
		entityTestRepository.createBatch(entitys);
		
		assertEquals(13,entityTestRepository.queryAll().size());
		//tx.commit();
		//assertEquals(13,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
	}
	@Test
	public void testBatchSaveCollectionOfT() throws SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		List<EntityTest> entitys=new ArrayList<EntityTest>();
		for(int i=0;i<10;i++){
			EntityTest entity=new EntityTest();
			entity.setFirstName("1");
			entity.setLastName("1");
			entity.setEmail("1@163.com");
			entitys.add(entity);
		}
		entityTestRepository.createBatch(entitys);
		
		assertEquals(13,entityTestRepository.queryAll().size());
		//tx.commit();
		//assertEquals(13,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
	}

	@Test
	public void testBatchDeleteTArray() throws SQLException, DataSetException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity1=new EntityTest();
		entity1.setVersion(1);
		entity1.setId(1);
		EntityTest entity2=new EntityTest();
		entity2.setId(2);
		entity2.setVersion(1);
		
		entityTestRepository.deleteBatch(entity1,entity2);
		
		assertNull(entityTestRepository.get(1));
		assertNull(entityTestRepository.get(2));
		assertNotNull(entityTestRepository.get(3));
		assertEquals(1,entityTestRepository.queryAll().size());
		//tx.commit();
		//assertEquals(1,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
		
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 or id=2");
		//assertEquals(0,table.getRowCount());
	}
	
	

	@Test
	public void testBatchDeleteCollectionOfT() throws DataSetException, SQLException {
		//assertEquals(3,countRowsInTable());
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity1=new EntityTest();
		entity1.setId(1);
		entity1.setVersion(1);
		EntityTest entity2=new EntityTest();
		entity2.setId(2);
		entity2.setVersion(1);
		List<EntityTest> entitys=new ArrayList<EntityTest>();
		entitys.add(entity1);
		entitys.add(entity2);
		
		entityTestRepository.deleteBatch(entitys);
		
		assertNull(entityTestRepository.get(1));
		assertNull(entityTestRepository.get(2));
		assertNotNull(entityTestRepository.get(3));
		assertEquals(1,entityTestRepository.queryAll().size());
		

		//sessionFactory.commit(status);
		//TransactionSynchronizationManager.get
		//DataSourceUtils.getConnection(this.jdbcTemplate.getDataSource()).commit();
		
		//assertEquals(1,countRowsInTable());
		
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 or id=2");
		//assertEquals(0,table.getRowCount());
	}


	@Test
	public void testBatchDeleteIDArray() throws SQLException, DataSetException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		
		entityTestRepository.deleteBatch(1,2);
		
		assertEquals(1,entityTestRepository.queryAll().size());
		//tx.commit();
		//assertEquals(1,DbunitBaseRepositoryTest.getDbConn().getRowCount(EntityTest_TableName));
		
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 or id=2");
		//assertEquals(0,table.getRowCount());
	}
	@Test
	public void testBatchDeleteAll() throws DataSetException, SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		entityTestRepository.deleteAll();//.deleteAllBatch();
		//tx.commit();
		
		
		assertEquals(0,entityTestRepository.queryAll().size());
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
		//assertEquals(0,table.getRowCount());
	}
//	@Test
//	public void testBatchDeleteWhereInfoNormal() throws SQLException, DataSetException {
//		//fail("Not yet implemented");
//		//在这里测试所有的where条件，构成的sql而不单单是=,>,<这些二元操作符，还有in等这些
//		WhereInfo whereinfo=new WhereInfo("age",">","20");//WhereInfo.parse("age_gt", "20");
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
//		entityTestService.deleteBatch(whereinfo);
//		tx.commit();
//		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(1,table.getRowCount());
//		assertEquals("admin1",table.getValue(0, "firstName"));
//		assertEquals("123",table.getValue(0, "lastName"));
//		assertEquals("admin1@163.com",table.getValue(0, "email"));
//		assertEquals(20,table.getValue(0, "age"));
//	}
//	
//	@Test
//	public void testBatchDeleteWhereInfoBetween() throws SQLException, DataSetException {
//		//fail("Not yet implemented");
//		//在这里测试所有的where条件，构成的sql而不单单是=,>,<这些二元操作符，还有in等这些
//		WhereInfo whereinfo=new WhereInfo("age","between",new String[]{"20","30"});//WhereInfo.parse("age_between", "20,30");
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
//		entityTestService.deleteBatch(whereinfo);
//		tx.commit();
//		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(1,table.getRowCount());
//	}
//	
//	@Test
//	public void testBatchDeleteWhereInfoIn() throws SQLException, DataSetException {
//		//fail("Not yet implemented");
//		//在这里测试所有的where条件，构成的sql而不单单是=,>,<这些二元操作符，还有in等这些
//		WhereInfo whereinfo=new WhereInfo("age","in",new String[]{"20","30"});//WhereInfo.parse("age_in", "20,30");
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
//		entityTestService.deleteBatch(whereinfo);
//		tx.commit();
//		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(1,table.getRowCount());
//		
//		//ClassUtils.isPrimitiveOrWrapper(type)
//	}
	
	@Test
	public void testBatchDeletCnd() throws SQLException, DataSetException {
		//fail("Not yet implemented");
		//在这里测试所有的where条件，构成的sql而不单单是=,>,<这些二元操作符，还有in等这些
		//WhereInfo whereinfo=WhereInfo.parse("age_in", "20,30");
		
		
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		entityTestRepository.deleteBatch(Cnd.select().andIn("age", 20,30));
		//tx.commit();
		
		assertEquals(1,entityTestRepository.queryAll().size());
		//ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
		//assertEquals(1,table.getRowCount());
		
		//ClassUtils.isPrimitiveOrWrapper(type)
	}

	@Test
	public void testUpdateDynamic() throws DataSetException, SQLException{
		
		//注意测试，当某个字段为瞬时字段的时候要不要更新，和是否要更新version字段。注意hql怎么调用
		//再测试，当使用hql进行删除，更新后，再get或load出来的数据是否也变了，即测试hql是否会影响一级缓存，如果会影响，那使用hql后要对缓存进行clear。
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		EntityTest entity=new EntityTest();
		entity.setId(1);
		entity.setVersion(1);
		entity.setFirstName("update1New");
		entityTestRepository.updateIgnoreNull(entity);
		//tx.commit();
		
		EntityTest aa=entityTestRepository.get(1);
		assertEquals("update1New",aa.getFirstName());
		assertEquals("123",aa.getLastName());
		assertEquals("admin1@163.com",aa.getEmail());
		
//		ITable table1 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 ");
//		assertEquals(1,table1.getRowCount());
//		assertEquals("update1New",table1.getValue(0, "firstName"));
//		assertEquals("123",table1.getValue(0, "lastName"));
//		assertEquals("admin1@163.com",table1.getValue(0, "email"));
	}
	@Test
	public void testUpdateDynamic1() throws DataSetException, SQLException{
		//测试hql和缓存的关系
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		//EntityTest entity0=entityTestService.get(1);
		//assertEquals("admin1",entity0.getFirstName());
		
		EntityTest entity=new EntityTest();
		entity.setId(1);
		entity.setVersion(1);
		entity.setFirstName("update1New");
		entityTestRepository.updateIgnoreNull(entity);
		
		
		assertEquals(3,entityTestRepository.queryAll().size());
		EntityTest entity1=entityTestRepository.get(1);
		assertEquals("update1New",entity1.getFirstName());
		assertEquals("123",entity1.getLastName());
		assertEquals("admin1@163.com",entity1.getEmail());
		//tx.commit();
		
		
//		ITable table1 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 ");
//		assertEquals(1,table1.getRowCount());
//		assertEquals("update1New",table1.getValue(0, "firstName"));
//		assertEquals("123",table1.getValue(0, "lastName"));
//		assertEquals("admin1@163.com",table1.getValue(0, "email"));
	}

//	
//	@Test
//	public void testUpdateDynamicCnd() throws DataSetException, SQLException{
//		//fail("Not yet implemented");
//		
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		EntityTest entity=new EntityTest();
//		entity.setEmail("1111@11.com");
//		
//		//WhereInfo where=WhereInfo.parse("age_in", "20,30,40");
//		entityTestService.updateIgnoreNull(entity, Cnd.select().andIn("age", 20,30,40));
//		tx.commit();
//		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(3,table.getRowCount());
//		
//		for(int i=0;i<table.getRowCount();i++){
//			assertEquals("1111@11.com",table.getValue(i, "email"));
//		}
//	}

	@Test
	public void testBatchUpdateTArray() throws DataSetException, SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity1=new EntityTest();
		entity1.setId(1);
		entity1.setFirstName("update1");
		entity1.setVersion(1);
		EntityTest entity2=new EntityTest();
		entity2.setId(2);
		entity2.setFirstName("update2");
		entity2.setVersion(1);
		entityTestRepository.updateBatch(entity1, entity2);
		//tx.commit();
		
		EntityTest entity1_aa=entityTestRepository.get(1);
		assertEquals(2,entity1_aa.getVersion());
		assertEquals("update1",entity1_aa.getFirstName());
		
		EntityTest entity1_bb=entityTestRepository.get(2);
		assertEquals("update2",entity1_bb.getFirstName());
		assertEquals(2,entity1_bb.getVersion());
		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(3,table.getRowCount());
//		ITable table1 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 ");
//		assertEquals(1,table1.getRowCount());
//		assertEquals("update1",table1.getValue(0, "firstName"));
//		Assert.assertNull(table1.getValue(0, "email"));
//		assertEquals(2,table1.getValue(0, "version"));
//		ITable table2 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=2 ");
//		assertEquals(1,table2.getRowCount());
//		assertEquals("update2",table2.getValue(0, "firstName"));
//		assertEquals(2,table2.getValue(0, "version"));
//		Assert.assertNull(table2.getValue(0, "email"));

	}

	@Test
	public void testBatchUpdateCollectionOfT() throws DataSetException, SQLException {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		EntityTest entity1=new EntityTest();
		entity1.setId(1);
		entity1.setFirstName("update1");
		entity1.setVersion(1);
		EntityTest entity2=new EntityTest();
		entity2.setId(2);
		entity2.setFirstName("update2");
		entity2.setVersion(1);
		List<EntityTest> entitys=new ArrayList<EntityTest>();
		entitys.add(entity1);
		entitys.add(entity2);
		entityTestRepository.updateBatch(entitys);
		//tx.commit();
		
		EntityTest entity1_aa=entityTestRepository.get(1);
		assertEquals(2,entity1_aa.getVersion());
		assertEquals("update1",entity1_aa.getFirstName());
		
		EntityTest entity1_bb=entityTestRepository.get(2);
		assertEquals("update2",entity1_bb.getFirstName());
		assertEquals(2,entity1_bb.getVersion());
		
		
//		ITable table =DbunitBaseRepositoryTest.getDbConn().createTable(EntityTest_TableName);
//		assertEquals(3,table.getRowCount());
//		ITable table1 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=1 ");
//		assertEquals(1,table1.getRowCount());
//		assertEquals("update1",table1.getValue(0, "firstName"));
//		
//		assertEquals(2,table1.getValue(0, "version"));
//		ITable table2 =DbunitBaseRepositoryTest.getDbConn().createQueryTable(EntityTest_TableName, "select * from "+EntityTest_TableName+" where id=2 ");
//		assertEquals(1,table2.getRowCount());
//		assertEquals("update2",table2.getValue(0, "firstName"));
//		assertEquals(2,table2.getValue(0, "version"));
	

	}


	@Test
	public void testQueryAll() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		List<EntityTest> entitys=entityTestRepository.queryAll();
		//tx.commit();
		
		assertEquals(3,entitys.size());
	}

//	@Test
//	public void testQueryByExample() {
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		EntityTest entity=new EntityTest();
//		entity.setLastName("123");
//		List<EntityTest> entitys=entityTestService.queryByExample(entity);
//		tx.commit();
//		assertEquals(2,entitys.size());
//	}
//	
//	@Test
//	public void testQueryPage() {
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		WhereInfo whereinfo=new WhereInfo("age",">","20");//WhereInfo.parse("age_gt", "20");
//		WhereInfo whereinfo1=new WhereInfo("firstName","like","admin");//WhereInfo.parse("firstName_like", "admin");
//		WhereInfo whereinfo2=new WhereInfo("lastName","in",new String[]{"123","1234"});//WhereInfo.parse("lastName_in", "123","1234");
//		PageRequest page=new PageRequest();
//		page.setWheres(whereinfo,whereinfo1,whereinfo2);
//		page.setStratAndLimit(1, 10);
//		
//		QueryResult<EntityTest> entitys=entityTestService.queryPage(page);
//		tx.commit();
//		assertEquals(2,entitys.getTotalItems());
//		assertEquals(1,entitys.getTotalPages());
//	}
	
	@Test
	public void testQueryCnd() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.select().andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		List<EntityTest> entitys=entityTestRepository.query(cnd);
		//tx.commit();
		assertEquals(2,entitys.size());
		//assertEquals(1,entitys.getTotalPages());
	}
//	@Test
//	public void testQueryCnd_Map() {
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.select().andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234").addSelect("age","firstName").asc("age");
//		
//		List<Map> entitys=entityTestService.queryList(cnd,Map.class);
//		tx.commit();
//		
//		assertEquals(2,entitys.size());
//		assertEquals(true,entitys.get(0) instanceof Map);
//		assertEquals(30,entitys.get(0).get("age"));
//		assertEquals(40,entitys.get(1).get("age"));
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	@Test
//	public void testQueryCnd_M() {
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.select().andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234").addSelect("age","firstName").asc("age");
//		
//		List<EntityTest> entitys=entityTestService.queryList(cnd,EntityTest.class);
//		tx.commit();
//		
//		assertEquals(2,entitys.size());
//		assertEquals(true,entitys.get(0) instanceof EntityTest);
//		assertEquals((Integer)30,entitys.get(0).getAge());
//		assertEquals((Integer)40,entitys.get(1).getAge());
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	
//	@Test
//	public void testQueryCnd_BaseType() {
//		Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.select().andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234").addSelect("firstName").asc("age");
//		
//		List<String> entitys=entityTestService.queryList(cnd,String.class);
//		tx.commit();
//		
//		assertEquals(2,entitys.size());
//		assertEquals(true,entitys.get(0) instanceof String);
//		//assertEquals((Integer)30,entitys.get(0).getAge());
//		//assertEquals((Integer)40,entitys.get(1).getAge());
//		//assertEquals(1,entitys.getTotalPages());
//	}
	
	@Test
	public void testQueryCountCnd() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.count().andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		int counts=entityTestRepository.queryCount(cnd);
		//tx.commit();
		assertEquals(2,counts);
		//assertEquals(1,entitys.getTotalPages());
	}
	
	@Test
	public void testQueryCountCndProperty() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.count("id").andGT("age", 20).andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		int counts=entityTestRepository.queryCount(cnd);
		//tx.commit();
		assertEquals(2,counts);
		//assertEquals(1,entitys.getTotalPages());
	}
	
	@Test
	public void testQueryUniqueCnd() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.where().and("id","=", 1);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		EntityTest entitys=entityTestRepository.queryUnique(cnd);
		//tx.commit();
		assertEquals((Integer)1,entitys.getId());
		//assertEquals(1,entitys.getTotalPages());
	}
	
	@Test
	public void queryUniqueCnd_M() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.where().and("id","=", 1);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		EntityTest entitys=entityTestRepository.queryUnique(cnd,EntityTest.class);
		//tx.commit();
		assertEquals((Integer)1,entitys.getId());
		//assertEquals(1,entitys.getTotalPages());
	}
	
	@Test
	public void queryUniqueCnd_BaseType() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.where().and("id","=", 1).addSelect("id");//.andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		Integer entitys=entityTestRepository.queryUnique(cnd,Integer.class);
		//tx.commit();
		assertEquals((Integer)1,entitys);
		//assertEquals(1,entitys.getTotalPages());
	}
	@Test
	public void queryUniqueCnd_Map() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.where().and("id","=", 1).addSelect("id","firstName");//.andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		Map entitys=entityTestRepository.queryUnique(cnd,Map.class);
		//tx.commit();
		assertEquals((Integer)1,entitys.get("id"));
	}
	
	
	@Test
	public void testQueryMaxCnd() {
		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
		Cnd cnd=Cnd.select().andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
		
		Object entitys=entityTestRepository.queryMax("id",cnd);
		//tx.commit();
		assertEquals((Integer)3,entitys);
		//assertEquals(1,entitys.getTotalPages());
	}
//	@Test
//	public void testQueryMaxCnd1() {
//		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.max("id").andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
//		
//		Object entitys=entityTestService.queryMax(cnd);
//		//tx.commit();
//		assertEquals((Integer)3,entitys);
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	
//	@Test
//	public void testQueryMinCnd() {
//		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.select().andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
//		
//		Object entitys=entityTestService.queryMin("id",cnd);
//		//tx.commit();
//		assertEquals((Integer)1,entitys);
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	@Test
//	public void testQueryMinCnd1() {
//		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.min("id").andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
//		
//		Object entitys=entityTestService.queryMin(cnd);
//		//tx.commit();
//		assertEquals((Integer)1,entitys);
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	@Test
//	public void testQuerySumCnd() {
//		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.sum("id").andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
//		
//		Object entitys=entityTestService.queryMin(cnd);
//		//tx.commit();
//		assertEquals(6L,entitys);
//		//assertEquals(1,entitys.getTotalPages());
//	}
//	
//	@Test
//	public void testQueryAvgCnd() {
//		//Transaction tx = sessionFactory.getCurrentSession().beginTransaction(); 
//		Cnd cnd=Cnd.avg("id").andIn("id", 1,2,3);//.andLike("firstName", "admin").andIn("lastName", "123","1234");
//		
//		Object entitys=entityTestService.queryAvg(cnd);
//		//tx.commit();
//		assertEquals(2.0,entitys);
//		//assertEquals(1,entitys.getTotalPages());
//	}

	

}
