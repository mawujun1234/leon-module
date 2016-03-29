现在的前提是简化javabean，不使用复杂的领域模型

如果是默认的代码模板情况，只需要第一步，建一个GeneratorMain就可以了。相当于直接读取jar中ftl文件，然后进行生成
否则还要建立自己的代码模板类。

另一种方式就是在leon-generator中建立模板(不是当前项目)，然后建立generator.properties，这种情况是在开发环境下

使用方法:
1:建立一个GeneratorMain类
public class GeneratorMain {
	static GeneratorService generatorService=new GeneratorService();
	public static void main(String[] args) throws ClassNotFoundException, IOException, TemplateException {
		// TODO Auto-generated method stub
		generatorService.generatorAllFile(Provice.class,String.class);
	}

}

2：拷贝generator.properties， 配置generator.properties，修改生成的路径和模板的位置
	classpathftldir=/templates/default
	output=d:/webapp-generator-output
	如果使用默认的模板，那这个文件就不需要拷贝了，只要有第一步就行了
	
3：拷贝*.ftl文件,放到src/main/resource目录下面，路径和上面的classpathftldir定义是一样的
	修改ftl文件，修改成自己需要的样子。
	