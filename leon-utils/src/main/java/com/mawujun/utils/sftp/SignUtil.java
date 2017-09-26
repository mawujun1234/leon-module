//package com.mawujun.utils.sftp;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.URL;
//import java.security.KeyManagementException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.util.Arrays;
//import java.util.Date;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//
//import com.alibaba.fastjson.JSON;
//import com.mawujun.exception.BusinessException;
//
//public class SignUtil {
//	// 与接口配置信息中的Token要一致
//	// private static String token = "mawujun1234";
//	private static AccessToken accessToken= null;
//	private static Jsapi_ticket jsapi_ticket= null;
//	public static String noncestr="mawujun1234";
//	//public static String url="http://172.16.23.19:8086/mobile/index.html";
//	public static long timestamp=0l;
//	public static String APPID="wx71e0fce782f45e83";
//	private static String APPSECRECT="j0uSvR0zP1TbewpgWQ7eArUIaVhSeG4Ss6vPU9miTxS9KOMJiQk4BKIz9rr3hAs7";
//	private static String access_token_url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APPID&corpsecret=APPSECRECT";
//	private static String jsapi_ticket_url="https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";
//	
//	public static AccessToken getAccessToken() {
//		//weiXinConfig.getAccessToken();
//		if(accessToken!=null && !accessToken.isExpires()){
//			return accessToken;
//		}
//		
//		refreshtAccessToken();
//		
//		return accessToken;
//		
//		//String accessToken=json.getString("access_token");
//		//return accessToken;
//		
//	}
//	/**
//	 * 强制刷新AccessToken，并返回这个值
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @return
//	 */
//	public static AccessToken refreshtAccessToken() {
//		String access_token_url1=access_token_url.replace("APPID", APPID).replace("APPSECRECT", APPSECRECT);
//		String jsonstr=httpsRequest(access_token_url1,"GET",null);
//
//		accessToken=JSON.parseObject(jsonstr, AccessToken.class);
//		accessToken.setCreateDate(new Date());
//		//weiXinConfig.setAccessToken(accessToken);
//		return accessToken;
//	}
//	public static Jsapi_ticket getJsapi_ticket() {
//		//weiXinConfig.getAccessToken();
//		if(jsapi_ticket!=null && !jsapi_ticket.isExpires()){
//			return jsapi_ticket;
//		}
//		
//		refreshJsapi_ticket();
//		
//		return jsapi_ticket;
//		
//		//String accessToken=json.getString("access_token");
//		//return accessToken;
//		
//	}
//	/**
//	 * 强制刷新
//	 * @author mawujun qq:16064988 mawujun1234@163.com
//	 * @return
//	 */
//	public static Jsapi_ticket refreshJsapi_ticket() {
//		String access_token_url1=jsapi_ticket_url.replace("ACCESS_TOKEN", SignUtil.getAccessToken().getAccess_token());
//		String jsonstr=httpsRequest(access_token_url1,"GET",null);
//
//		jsapi_ticket=JSON.parseObject(jsonstr, Jsapi_ticket.class);
//		jsapi_ticket.setCreateDate(new Date());
//		//weiXinConfig.setAccessToken(accessToken);
//		return jsapi_ticket;
//	}
//	
//	public static String getSignature(String url){
//		timestamp=SignUtil.getJsapi_ticket().getCreateDate().getTime();
//		String string1="jsapi_ticket="+SignUtil.getJsapi_ticket().getTicket()+"&noncestr="+noncestr+"&timestamp="+timestamp
//				+"&url="+url;
//		MessageDigest md = null;
//		String tmpStr = null;
//
//		try {
//			md = MessageDigest.getInstance("SHA-1");
//			// 将三个参数字符串拼接成一个字符串进行sha1加密
//			byte[] digest = md.digest(string1.getBytes());
//			tmpStr = byteToStr(digest);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return tmpStr;
//	}
//
//	/**
//	 * 验证签名
//	 * 
//	 * @param signature
//	 * @param timestamp
//	 * @param nonce
//	 * @return
//	 */
//	public static boolean checkSignature(String signature, String timestamp, String nonce) {
//		String token =accessToken.getAccess_token();
//		String[] arr = new String[] { token, timestamp, nonce };
//		// 将token、timestamp、nonce三个参数进行字典序排序
//		Arrays.sort(arr);
//		StringBuilder content = new StringBuilder();
//		for (int i = 0; i < arr.length; i++) {
//			content.append(arr[i]);
//		}
//		MessageDigest md = null;
//		String tmpStr = null;
//
//		try {
//			md = MessageDigest.getInstance("SHA-1");
//			// 将三个参数字符串拼接成一个字符串进行sha1加密
//			byte[] digest = md.digest(content.toString().getBytes());
//			tmpStr = byteToStr(digest);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//
//		content = null;
//		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
//		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
//	}
//
//	/**
//	 * 将字节数组转换为十六进制字符串
//	 * 
//	 * @param byteArray
//	 * @return
//	 */
//	private static String byteToStr(byte[] byteArray) {
//		String strDigest = "";
//		for (int i = 0; i < byteArray.length; i++) {
//			strDigest += byteToHexStr(byteArray[i]);
//		}
//		return strDigest;
//	}
//
//	/**
//	 * 将字节转换为十六进制字符串
//	 * 
//	 * @param mByte
//	 * @return
//	 */
//	private static String byteToHexStr(byte mByte) {
//		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
//		char[] tempArr = new char[2];
//		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
//		tempArr[1] = Digit[mByte & 0X0F];
//
//		String s = new String(tempArr);
//		return s;
//	}
//	
//	public static String httpsRequest(String requestUrl,String requestMethod,String outpuStr){
//		//String access_token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
//		try {
//			URL url=new URL(requestUrl);
//			HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
//			//使用自定义的信任管理器
//			TrustManager[] tm={new MyX509TrustManager()};
//			SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
//			sslContext.init(null, tm, new java.security.SecureRandom());
//			SSLSocketFactory ssf=sslContext.getSocketFactory();
//			conn.setSSLSocketFactory(ssf);
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setUseCaches(false);
//			//设置请求方式
//			conn.setRequestMethod(requestMethod);
//			if(outpuStr!=null){
//				OutputStream outputstream=conn.getOutputStream();
//				outputstream.write(outpuStr.getBytes("UTF-8"));
//				outputstream.close();
//			}
//			
//			InputStream inputstream=conn.getInputStream();
//			InputStreamReader inputStreamReader=new InputStreamReader(inputstream,"UTF-8");
//			BufferedReader bufferreader=new BufferedReader(inputStreamReader);
//			
//			StringBuffer buffer=new StringBuffer();
//			String str=null;
//			while((str=bufferreader.readLine())!=null){
//				buffer.append(str);
//			}
//			
//			bufferreader.close();
//			inputStreamReader.close();
//			inputstream.close();
//			conn.disconnect();
//
//			//当微信发生错误的时候爆出异常
//			if(buffer.indexOf("errcode")!=-1){
//				//菜单正确删除的接口会返回{"errcode":0,"errmsg":"ok"}
//				if(buffer.indexOf("\"errmsg\":\"ok\"")==-1){
//					//logger.error(buffer.toString());
//					//ErrorMsg errorMsg=JSON.parseObject(jsonStr, ErrorMsg.class);
//					throw new BusinessException(buffer.toString());
//				}
//				
//			}
//			//JSONObject json=JSONObject.fromObject(buffer.toString());
//			return buffer.toString();
//		}  catch (IOException e) {
//			//logger.error(e);
//			throw new RuntimeException(e);
//		} catch (KeyManagementException e) {
//			//logger.error(e);
//			throw new RuntimeException(e);
//		} catch (NoSuchAlgorithmException e) {
//			//logger.error(e);
//			throw new RuntimeException(e);
//		} catch (NoSuchProviderException e) {
//			//logger.error(e);
//			throw new RuntimeException(e);
//		}
//	}
//}
