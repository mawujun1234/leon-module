//package com.mawujun.utils.sftp;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//
//public class Ftp2Linux_pc {
//	private static Log log = LogFactory.getLog(Ftp2Linux_pc.class);
//	private static String linux_tomcat_install_dir="/opt/apache-tomcat-8.0.36";
//	private static String linux_webapps_dir=linux_tomcat_install_dir+"/webapps";
//	private static String od_file_local="E:\\eclipse\\aaa\\orderingSystem\\target\\od.war";
//	private static String od_file_remote=linux_webapps_dir+"/od.war";
//	private static String linux_od_folder=linux_webapps_dir+"/od";
//	private static String stop_tomcat_cmd="sh "+linux_tomcat_install_dir+"/bin/shutdown.sh";
//	private static String start_tomcat_cmd="sh "+linux_tomcat_install_dir+"/bin/startup.sh";
//	
//	
//	private static List<Map<String,String>> list=new ArrayList<Map<String,String>>();
//	static{
//		Map<String,String> params=new HashMap<String,String>();
//		params.put("host", "192.168.188.36");
//		params.put("username", "root");
//		params.put("password", "youngor");
//		list.add(params);
//		
//		Map<String,String> params1=new HashMap<String,String>();
//		params1.put("host", "192.168.188.37");
//		params1.put("username", "root");
//		params1.put("password", "youngor");
//		list.add(params1);
////		
////		Map<String,String> params2=new HashMap<String,String>();
////		params2.put("host", "192.168.188.24");
////		params2.put("username", "root");
////		params2.put("password", "youngor");
////		list.add(params2);
//	}
//
//	static SimpleDateFormat yyyyMMddHHmm=new SimpleDateFormat("yyyyMMddHHmm");
//	public static void main(String[] args) throws Exception {
//		for(Map<String,String> map:list){
//			log.info("正在连接"+map.get("host"));
//			ChannelSftp sftp = null;
//			Session session = null;
//			session =SFTPUtil.connect(map.get("host"), 22, map.get("username"), map.get("password"));
//			//先停服务器
//			log.info("正在停止服务器："+map.get("host"));
//			execCmd(session,stop_tomcat_cmd);
//			log.info("成功停止服务器："+map.get("host"));
//			
//			log.info("备份文件"+od_file_remote+":"+map.get("host"));
//			execCmd(session,"mv "+od_file_remote+" "+od_file_remote+yyyyMMddHHmm.format(new Date()));
//			log.info("成功备份文件"+od_file_remote+":"+map.get("host"));
//            
//			log.info("正在开始上传文件:"+od_file_local);
//			Channel channel = session.openChannel("sftp");
//			channel.connect();
//			sftp = (ChannelSftp) channel;
//			SFTPUtil.upload(linux_webapps_dir, od_file_local, sftp);
//			log.info("成功上传文件："+od_file_local);
//			
////			//先备份photo的照片，再删除
//			//或者说，启动tomcat后，建立软连接，连接到外部的目录中去
//			
//			log.info("正在删除文件夹："+linux_od_folder);
//			//sftp.rmdir(linux_dir_od);
//			execCmd(session,"rm -rf "+linux_od_folder);
//			log.info("暂停10000ms");
//			Thread.sleep(10000);
//			execCmd(session,"rm -rf "+linux_od_folder);//执行2次，
//			log.info("成功删除文件夹："+linux_od_folder);
//			
//			log.info("暂停30000ms");
//			Thread.sleep(30000);//暂停10s，再启动试试,一个是删除需要时间，
//			//启动服务器
//			log.info("正在启动服务器："+map.get("host"));
//			execCmd(session,start_tomcat_cmd);
//			log.info("成功启动服务器："+map.get("host"));
//			
//			
//			log.info("暂停10000ms");
//			Thread.sleep(10000);
//			log.info("执行：channel.disconnect()");
//			channel.disconnect();
//			log.info("执行：session.disconnect()");
//			session.disconnect();
//			log.info("执行：sftp.disconnect()");
//			sftp.disconnect();
//			log.info("=============================================================================================");
//			log.info("=============================================================================================");
//			log.info("=============================================================================================");
//			log.info("=============================================================================================");
//		}
//		
//		
//	}
//	
//	private static void execCmd(Session session,String command) throws JSchException{
//		BufferedReader reader = null;
//		Channel channel = null;
//
//		try {
//			if (command != null) {
//				channel = session.openChannel("exec");
//				((ChannelExec) channel).setCommand(command);
//				
//
//				channel.setInputStream(null);
//				((ChannelExec) channel).setErrStream(System.err);
//
//				channel.connect();
//				InputStream in = channel.getInputStream();
//				reader = new BufferedReader(new InputStreamReader(in,Charset.forName("GBK")));
//				String buf = null;
//				while ((buf = reader.readLine()) != null) {
//					//System.out.println(buf);
//					log.info(buf);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error(e);
//		} catch (JSchException e) {
//			e.printStackTrace();
//			log.error(e);
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				log.error(e);
//			}
//			channel.disconnect();
//			//session.disconnect();
//		}
//	}
//
//}
