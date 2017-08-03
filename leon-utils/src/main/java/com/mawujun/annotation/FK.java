package com.mawujun.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface FK {
	/**
	 * 类,关联到哪个类上面
	 * @return
	 */
    Class cls();
    /**
     * 或者直接指定表名
     * @author mawujun qq:16064988 mawujun1234@163.com
     * @return
     */
    String table() default "";
    
    String column() default "";

}
