package com.singulee.carschool.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Send {

	/**
	 * 发送验证码
	 * @param theCode 生成的验证码
	 * @param phone   对方的手机号
	 * @return
	 */
	public static boolean sendCode(int theCode,String phone)
	{
        // http://www.ihuyi.com/sms.html  互亿无线  返回结果解析为xml文件，可以通过dom4j解析
		// 生成验证码
		/*
		   1.用户点击发送验证码
		   2.验证用户手机号是否为注册用户
		   3.生成验证码，并且绑定此手机号(cookie session sql)  扩展：有效时间1分钟，1分钟之后失效
		   4.发送验证码
		   5.用户接收到验证码，输入验证码，点击登录
		   6.后台服务验证用户的手机号和验证码是否匹配，如果匹配登录成功，否则登录失败
		 */
		//int mobile_code = (int)((Math.random() * 9 + 1) * 100000);
		String content = "您的验证码是：" + theCode + "。请不要把验证码泄露给其他人。";//注意这个是模板内容，只能修改参数
		// 添加参数
		HashMap<String, String> params = new HashMap<>();
		params.put("account",StaticInfo.APIID);                              //查看用户名是登录用户中心->验证码短信->产品总览->APIID
		params.put("password",StaticInfo.APIKEY);    //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
		params.put("mobile",phone);                             // 发送给哪一个手机号
		params.put("content",content);                                   // 发送的文字

		// 实例化HTTP方法
		HttpPost request = new HttpPost();
		// 封装请求地址
		try {
			request.setURI(new URI("http://106.ihuyi.cn/webservice/sms.php?method=Submit"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
		// 设置参数
		List<NameValuePair> nameValuePairsList = new ArrayList<>();
		// 封装数据值
		for (String name : params.keySet()) {
			String value = params.get(name);
			nameValuePairsList.add(new BasicNameValuePair(name, value));  // gun
		}
		HttpClient client = new DefaultHttpClient();
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairsList, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){	//请求成功
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity()
						.getContent(),"utf-8"));
				StringBuilder sb = new StringBuilder("");
				String line = "";
				String NL = System.getProperty("line.separator");   // apache建议回车使用line.separator(回车)     \n
				while ((line = in.readLine()) != null) {
					sb.append(line).append(NL);    // 每添加一个值，追加一个回车
				}
				in.close();
				String SubmitResult = sb.toString();
				System.out.println("服务器返回结果内容：" + SubmitResult);
				Document doc = DocumentHelper.parseText(SubmitResult);
				Element root = doc.getRootElement();

				String code = root.elementText("code");
				String msg = root.elementText("msg");
				String smsid = root.elementText("smsid");

				System.out.println("回执状态码：" + code);
				System.out.println("回执消息：" + msg);
				System.out.println("回执ID：" + smsid);

				if("2".equals(code)){
					return true;
				}
				else
				{
					return  false;
				}
			}
			else
			{
				return  false;
			}
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
			return false;
		} finally{
			// Release connection
			request.releaseConnection();
			client.getConnectionManager().shutdown();
		}

	}
}