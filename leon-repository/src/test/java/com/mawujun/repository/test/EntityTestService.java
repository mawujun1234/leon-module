package com.mawujun.repository.test;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class EntityTestService {
//	@Autowired
//	EntityTestRepository entityTestRepository;
	@Autowired
	DataSource dataSource;
	@Autowired
	SessionFactory sessionFactory;
	
//	@Override
//	public IRepository<EntityTest, String> getRepository() {
//		// TODO Auto-generated method stub
//		return entityTestRepository;
//	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void transaction(String id1,String id2){
		Session session=sessionFactory.getCurrentSession();
		//Transaction tran=session.beginTransaction();
		EntityTest entity=new EntityTest();
		entity.setFirstName("ma");
		entity.setLastName("wujun");
		entity.setEmail("160649888@163.com");
		entity.setSex(true);
		//entityTestRepository.create(entity);
		session.save(entity);
		//tran.commit();
		
		if(true){
			//throw new RuntimeException("");
		}
		
		//tran=session.beginTransaction();
		EntityTest entity1=new EntityTest();
		entity1.setFirstName("ma");
		entity1.setLastName("wujun");
		entity1.setEmail("160649888@163.com");
		//entityTestRepository.create(entity);
		session.save(entity1);
		//tran.commit();
		

//		Connection conn = DataSourceUtils.getConnection(dataSource);
//		try {
//			conn.commit();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		if(true){
//			throw new RuntimeException("");
//		}
//		
//		EntityTest entity1=new EntityTest();
//		entity1.setFirstName("ma1");
//		entity1.setLastName("wujun1");
//		entity1.setEmail("160649888@163.com1");
//		entityTestRepository.create(entity1);
		
		
//		EntityTest entity1=new EntityTest();
//		//entity1.setVersion(id1);
//		entity1.setId(id1);
//		EntityTest entity2=new EntityTest();
//		entity2.setId(id2);
//		//entity2.setVersion(1);
//		
//		entityTestRepository.delete(entity1);
//		
//		if(true){
//			throw new RuntimeException("");
//		}
//		entityTestRepository.delete(entity2);
	}

}
