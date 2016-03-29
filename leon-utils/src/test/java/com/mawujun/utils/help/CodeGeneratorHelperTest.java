package com.mawujun.utils.help;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CodeGeneratorHelperTest {
	
	@BeforeClass
	public static void before(){
		CodeGeneratorHelper.setMin(35);
	}
	//@Test  
	public void generate3(){
		CodeGeneratorHelper.setMin(35);
		 assertEquals(48,"0".charAt(0));
		 assertEquals(49,"1".charAt(0));
		 assertEquals(57,"9".charAt(0));
		 assertEquals(97,"a".charAt(0));
		 assertEquals(122,"z".charAt(0));
		 assertEquals("###",CodeGeneratorHelper.generate3(null));
		 assertEquals("##$",CodeGeneratorHelper.generate3("###"));
		 assertEquals("#$#",CodeGeneratorHelper.generate3("##}"));
		 assertEquals("$##",CodeGeneratorHelper.generate3("#}}"));
		 

		 assertEquals("###.##$",CodeGeneratorHelper.generate3("###.###"));
		 assertEquals("###.#$#",CodeGeneratorHelper.generate3("###.##}"));
		 assertEquals("###.$##",CodeGeneratorHelper.generate3("###.#}}"));
	}
	
	@Test  
	public void generate(){
		 assertEquals("####",CodeGeneratorHelper.generate(null,4));
		 assertEquals("###$",CodeGeneratorHelper.generate("###",4));
		 assertEquals("##$#",CodeGeneratorHelper.generate("##}",4));
		 assertEquals("#$##",CodeGeneratorHelper.generate("#}}",4));
		 assertEquals("$###",CodeGeneratorHelper.generate("}}}",4));
		 

		 assertEquals("####-###$",CodeGeneratorHelper.generate("####-###",4));
		 assertEquals("####-##$#",CodeGeneratorHelper.generate("####-##}",4));
		 assertEquals("####-#$##",CodeGeneratorHelper.generate("####-#}}",4));
		 assertEquals("####-$###",CodeGeneratorHelper.generate("####-}}}",4));
		 
		 assertEquals("#####-#$###",CodeGeneratorHelper.generate("#####-##}}}",4));
	}
	
	//@Test(expected=ArithmeticException.class)  
	public void testArithmeticException(){
		assertEquals("###",CodeGeneratorHelper.generate3("}}}"));
	}
	
	//@Test(expected=ArithmeticException.class)  
	public void testArithmeticException1(){
		assertEquals("###",CodeGeneratorHelper.generate3("}!}"));
	}

}
