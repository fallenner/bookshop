package cn.hnust.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class EmailUtil {
	private static Properties emailProp=new Properties();
	private static String from_address;
	private static String password;
	private static String host;
	private static String title;
	private static String content;
	static{
		Properties prop=new Properties();
		InputStream is=null;
		is=EmailUtil.class.getResourceAsStream("/email.properties");
		System.out.println(is);
		try {
			prop.load(is);
			host=prop.getProperty("host").toString();
			emailProp.put("mail.smtp.host", prop.getProperty("host"));
			emailProp.put("mail.smtp.auth", true);
			from_address= prop.getProperty("from_address").toString();
			password= prop.getProperty("password").toString();
			title= prop.getProperty("title").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void sendMail(HttpServletRequest request,String truename,String eamil,String username,String code) throws Exception{
		 Session session=Session.getInstance(emailProp);
		 MimeMessage message = new MimeMessage(session);
		 Address address = new InternetAddress(from_address);
		 message.setFrom(address);
		 Address toAddress = new InternetAddress(eamil);
		 message.setRecipient(MimeMessage.RecipientType.TO, toAddress);
		 message.setSubject(title);
		 if(StringUtils.isEmpty(truename)){
			 content="<p>亲爱的"+username+"，感谢您加入我们书店会员大家庭，请点击下面的连接激活账号，谢谢</p><a href='"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+
						"/user/active_account?username="+username+"&code="+code+"'>点我激活账号</a>";
		 }else{
			 content="<p>亲爱的"+truename+"，感谢您加入我们书店会员大家庭，请点击下面的连接激活账号，谢谢</p><a href='"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+
						"/user/active_account?username="+username+"&code="+code+"'>点我激活账号</a>";
		 }
		 message.setContent(content,"text/html;charset=UTF-8");
		 Transport transport =session.getTransport("smtp");
		 transport.connect(host, from_address, password);
		 transport.sendMessage(message, message.getAllRecipients());
		 transport.close(); 
	}
}
