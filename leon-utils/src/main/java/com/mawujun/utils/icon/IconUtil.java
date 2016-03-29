package com.mawujun.utils.icon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.mawujun.utils.page.Pager;

public class IconUtil {
	/**
	 * 读取icon文件夹内的内容
	 * @param filePath
	 */
	public static Pager<Icon>  readIcon(String filePath,String wildcard ,String contextPath,Pager page) {
		//Pager<Icon> result=new Pager<Icon>(page);
		List<Icon> result_list=new ArrayList<Icon>();
		
		File file=new File(filePath);
		IOFileFilter fileFilter=null;
		if(wildcard ==null || "".equals(wildcard.trim())){
			//fileFilter=FileFilterUtils.suffixFileFilter("png");
			wildcard="*";
		} //else {
		//	wildcard+="*.png";
		//}
			//fileFilter=FileFilterUtils.andFileFilter(FileFilterUtils.prefixFileFilter(prefixName),FileFilterUtils.suffixFileFilter("png"));
			//fileFilter=FileFilterUtils.andFileFilter(new WildcardFileFilter(wildcard),FileFilterUtils.suffixFileFilter("png"));
			
		
		fileFilter = new WildcardFileFilter(wildcard);

		//}
		
		List<File> list=(List<File>) FileUtils.listFiles(file,fileFilter, null);
		Collections.sort(list, NameFileComparator.NAME_COMPARATOR);
		for(int i=page.getStart();(i<list.size()&&i<page.getStart()+page.getLimit());i++){
			//System.out.println(list.get(i).getName());
			Icon icon=new Icon();
			icon.setFileName(list.get(i).getName());
			//icon.setPath(filePath+System.getenv("file.separator")+icon.getFileName());
			
			result_list.add(icon);
		}
		page.setRoot(result_list);
		
		page.setTotal(list.size());
		return page;	
	}
}
