package com.mawujun.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DateUtils {
	static SimpleDateFormat yyyy_MM_dd;
	static SimpleDateFormat yyyyMMddHHmmss;
	static HashMap<String,SimpleDateFormat> format_cache=new HashMap<String,SimpleDateFormat>();
	static {
		yyyy_MM_dd=new SimpleDateFormat("yyyy-MM-dd");
		format_cache.put("yyyy-MM-dd", yyyy_MM_dd);
		yyyyMMddHHmmss=new SimpleDateFormat("yyyyMMddHHmmss");
		format_cache.put("yyyyMMddHHmmss", yyyyMMddHHmmss);
	}
	public static String format(Date date,String format) {
		if(date==null){
			return null;
		}
		if(format_cache.get(format)!=null){
			return format_cache.get(format).format(date);
		} else {
			SimpleDateFormat yyyy_MM_dd=new SimpleDateFormat(format);
			String r= yyyy_MM_dd.format(date);
			format_cache.put(format, yyyy_MM_dd);
			return r;
		}
	}
	/**
	 * 默认的格式yyyy-MM-dd
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		if(date==null){
			return null;
		}
		return yyyy_MM_dd.format(date);
	}
	/**
	 * 格式yyyyMMddHHmmss
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param date
	 * @return
	 */
	public static String format4Id(Date date) {
		if(date==null){
			return null;
		}
		return yyyyMMddHHmmss.format(date);
	}
	/**
	 * 格式yyyyMMddHHmmss
	 * @author mawujun qq:16064988 mawujun1234@163.com
	 * @param date
	 * @return
	 */
	public static String format4Id() {
		return yyyyMMddHHmmss.format(new Date());
	}
	
	
	 //-----------------------------------------------------------------------
    /**
     * <p>Parses a string representing a date by trying a variety of different parsers.</p>
     * 
     * <p>The parse will try each parse pattern in turn.
     * A parse is only deemed successful if it parses the whole of the input string.
     * If no parse patterns match, a ParseException is thrown.</p>
     * The parser will be lenient toward the parsed date.
     * 
     * @param str  the date to parse, not null
     * @param parsePatterns  the date format patterns to use, see SimpleDateFormat, not null
     * @return the parsed date
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws ParseException if none of the date patterns were suitable (or there were none)
     */
    public static Date parseDate(String str, String... parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, parsePatterns, true);
    }
	
	   /**
     * <p>Parses a string representing a date by trying a variety of different parsers.</p>
     * 
     * <p>The parse will try each parse pattern in turn.
     * A parse is only deemed successful if it parses the whole of the input string.
     * If no parse patterns match, a ParseException is thrown.</p>
     * 
     * @param str  the date to parse, not null
     * @param parsePatterns  the date format patterns to use, see SimpleDateFormat, not null
     * @param lenient Specify whether or not date/time parsing is to be lenient.
     * @return the parsed date
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws ParseException if none of the date patterns were suitable
     * @see java.util.Calender#isLenient()
     */
    private static Date parseDateWithLeniency(
            String str, String[] parsePatterns, boolean lenient) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        
        SimpleDateFormat parser = new SimpleDateFormat();
        parser.setLenient(lenient);
        ParsePosition pos = new ParsePosition(0);
        for (String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            
            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2"); 
            }

            Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
	
}
