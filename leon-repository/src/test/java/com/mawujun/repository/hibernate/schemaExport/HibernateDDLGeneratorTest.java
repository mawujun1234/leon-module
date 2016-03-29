package com.mawujun.repository.hibernate.schemaExport;

import org.junit.Assert;
import org.junit.Test;

import com.mawujun.repository.DialectEnum;

public class HibernateDDLGeneratorTest {

	@Test
	public void getDDL(){
		String ddl=HibernateDDLGenerator.executeReturn(DialectEnum.h2, Model.class);
		Assert.assertNotNull(ddl);
	}
	
	@Test
	public void generate(){
		HibernateDDLGenerator.execute(DialectEnum.h2,null, Model.class);
	}
	
	@Test
	public void generateAllClass() throws Exception{
		HibernateDDLGenerator.execute(DialectEnum.h2,null, "com.mawujun.repository.hibernate.schemaExport");
	}

}
