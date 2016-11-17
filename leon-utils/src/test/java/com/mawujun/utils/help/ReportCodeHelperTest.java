package com.mawujun.utils.help;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReportCodeHelperTest {
	@Test
	public void generate3(){
		//ReportCodeHelper.setMin(35);
		 assertEquals(48,"0".charAt(0));
		 assertEquals(49,"1".charAt(0));
		 assertEquals(57,"9".charAt(0));
		 assertEquals(97,"a".charAt(0));
		 assertEquals(122,"z".charAt(0));
		 assertEquals("000",ReportCodeHelper.generate3(null,false));
		 assertEquals("001",ReportCodeHelper.generate3("000",false));
		 assertEquals("002",ReportCodeHelper.generate3("001",false));
		 assertEquals("010",ReportCodeHelper.generate3("009",false));
		 assertEquals("102",ReportCodeHelper.generate3("101",false));
		 
		 assertEquals("101-000",ReportCodeHelper.generate3("101",true));
	
	}
	
	@Test
	public void generate4(){
		//ReportCodeHelper.setMin(35);
		 assertEquals(48,"0".charAt(0));
		 assertEquals(49,"1".charAt(0));
		 assertEquals(57,"9".charAt(0));
		 assertEquals(97,"a".charAt(0));
		 assertEquals(122,"z".charAt(0));
		 assertEquals("0000",ReportCodeHelper.generate4(null,false));
		 assertEquals("0001",ReportCodeHelper.generate4("0000",false));
		 assertEquals("0002",ReportCodeHelper.generate4("0001",false));
		 assertEquals("0010",ReportCodeHelper.generate4("0009",false));
		 assertEquals("0009-0000",ReportCodeHelper.generate4("0009",true));
			
	
	}
//
//	//@Test  
//	public void generate3(){
//		//ReportCodeHelper.setMin(35);
//		 assertEquals(48,"0".charAt(0));
//		 assertEquals(49,"1".charAt(0));
//		 assertEquals(57,"9".charAt(0));
//		 assertEquals(97,"a".charAt(0));
//		 assertEquals(122,"z".charAt(0));
//		 assertEquals("###",ReportCodeHelper.generate3(null));
//		 assertEquals("##$",ReportCodeHelper.generate3("###"));
//		 assertEquals("#$#",ReportCodeHelper.generate3("##}"));
//		 assertEquals("$##",ReportCodeHelper.generate3("#}}"));
//		 
//
//		 assertEquals("###.##$",ReportCodeHelper.generate3("###.###"));
//		 assertEquals("###.#$#",ReportCodeHelper.generate3("###.##}"));
//		 assertEquals("###.$##",ReportCodeHelper.generate3("###.#}}"));
//	}
//	
//	@Test  
//	public void generate(){
//		 assertEquals("####",ReportCodeHelper.generate(null,4));
//		 assertEquals("###$",ReportCodeHelper.generate("###",4));
//		 assertEquals("##$#",ReportCodeHelper.generate("##}",4));
//		 assertEquals("#$##",ReportCodeHelper.generate("#}}",4));
//		 assertEquals("$###",ReportCodeHelper.generate("}}}",4));
//		 
//
//		 assertEquals("####-###$",ReportCodeHelper.generate("####-###",4));
//		 assertEquals("####-##$#",ReportCodeHelper.generate("####-##}",4));
//		 assertEquals("####-#$##",ReportCodeHelper.generate("####-#}}",4));
//		 assertEquals("####-$###",ReportCodeHelper.generate("####-}}}",4));
//		 
//		 assertEquals("#####-#$###",ReportCodeHelper.generate("#####-##}}}",4));
//	}
//	
//	//@Test(expected=ArithmeticException.class)  
//	public void testArithmeticException(){
//		assertEquals("###",ReportCodeHelper.generate3("}}}"));
//	}
//	
//	//@Test(expected=ArithmeticException.class)  
//	public void testArithmeticException1(){
//		assertEquals("###",ReportCodeHelper.generate3("}!}"));
//	}

}
