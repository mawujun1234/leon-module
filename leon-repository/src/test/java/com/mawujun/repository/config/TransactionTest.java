package com.mawujun.repository.config;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mawujun.repository.EntityTest;

@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional(transactionManager= "transactionManager") 
@ContextConfiguration(classes={RepositoryConfig.class})
public class TransactionTest {
	//static ApplicationContext context;
	@Autowired
	EntityTestService entityTestService;
	@BeforeClass  
    public static void setUpBeforeClass() throws Exception { 
		 //context = new AnnotationConfigApplicationContext(RepositoryConfig.class);
    }  
	
	@Test  
	@Rollback(value=true)
    public void transaction() {  
		//EntityTestService entityTestService=context.getBean(EntityTestService.class);
		entityTestService.deleteAll();
		assertEquals(0,entityTestService.queryAll().size());
		try {
		entityTestService.transaction(true);
		} catch (Exception e){
			e.printStackTrace();
		}

		assertEquals(0,entityTestService.queryAll().size());
    }  
	
	@Test  
	@Rollback(value=true)
    public void transaction1() {  
		//EntityTestService entityTestService=context.getBean(EntityTestService.class);
		entityTestService.deleteAll();
		assertEquals(0,entityTestService.queryAll().size());
		try {
		entityTestService.transaction(false);
		} catch (Exception e){
			e.printStackTrace();
		}

		assertEquals(2,entityTestService.queryAll().size());
    }  
}
