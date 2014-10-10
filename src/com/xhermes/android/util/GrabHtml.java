package com.xhermes.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;


public class GrabHtml {
	private String url;
	private String html;
	private static  String getHtml(String urlString) {  
		try {
			StringBuffer html = new StringBuffer();  
			java.net.URL url = new java.net.URL(urlString);  //���� String ��ʾ��ʽ���� URL ����
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();// ����һ�� URLConnection ��������ʾ�� URL �����õ�Զ�̶�������ӡ�
			java.io.InputStreamReader isr = new java.io.InputStreamReader(conn.getInputStream());//���شӴ˴򿪵����Ӷ�ȡ����������
			java.io.BufferedReader br = new java.io.BufferedReader(isr);//����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ���������

			String temp;
			while ((temp = br.readLine()) != null) {  //���ж�ȡ�����
				if(!temp.trim().equals("")){
					html.append(temp).append("\n");  //����ÿ�к���
				}
			}
			br.close();   //�ر�
			isr.close();  //�ر�
			return html.toString();   //���ش����������ݵ��ַ�����ʾ��ʽ��
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private static String makeUrlString(String carName){
		//�����Ѻ�ͼƬ���������ȡ����ͼƬ
		return "http://s.auto.sohu.com/search.at?suggest=" + carName + "+%CD%BC%C6%AC";
	}
	public List<String> pre_parser() throws ParserException{
		/**����Url����parser����**/    
		Parser parser =  new Parser(url);    

		/**���ñ��룬������Url����һ�� **/    
		parser.setEncoding("utf-8");    

		/** ����һ��Htmlҳ����� **/    
		HtmlPage htmlPage = new HtmlPage(parser);    
		parser.visitAllNodesWith(htmlPage);    

		/** ��ȡBody�������еĽڵ㣬���������������״�ṹ **/    
		NodeList list = htmlPage.getBody();    

		/** ����һ��Filter�����ڹ��˽ڵ�,�˴�������硰<img></img>�������Ľڵ� **/    
		NodeFilter filter = new TagNameFilter("a");    

		/** �õ����˺�Ľڵ� **/    
		list = list.extractAllNodesThatMatch(filter, true);    
		String regExHtml = "http://db.auto.sohu.com/photo/pic_t[0-9]+\\.shtml";
		Pattern pat_html = Pattern.compile(regExHtml);
		List<String> html_list = new ArrayList<String>();
		for(int c =0; c < list.size(); c ++){    
			LinkTag aTag=(LinkTag)list.elementAt(c);  
			String href_url = aTag.getAttribute("href");
			if(href_url!=null){
				Matcher mat_html = pat_html.matcher(href_url);
				if(mat_html.matches())
					html_list.add(href_url);
			}
		}    
		return html_list;    
	}
	public List<String> parser() throws ParserException{    
		List<String> html_list = this.pre_parser();
		String real_url = "";
		String regEx = "http://(m1|m2).auto.itc.cn/car/[0-9]+/[0-9]+/[0-9]+/Img[0-9]+_[0-9]+\\.(JPG|jpg|PNG|png)(\\?[0-9]+)?";
		if(html_list.size()>0)
			real_url = html_list.get(0);
		else
			real_url = url;
		/**����Url����parser����**/    
		Parser parser =  new Parser(real_url);    

		/**���ñ��룬������Url����һ�� **/    
		parser.setEncoding("utf-8");    

		/** ����һ��Htmlҳ����� **/    
		HtmlPage htmlPage = new HtmlPage(parser);    
		parser.visitAllNodesWith(htmlPage);    

		/** ��ȡBody�������еĽڵ㣬���������������״�ṹ **/    
		NodeList list = htmlPage.getBody();    

		/** ����һ��Filter�����ڹ��˽ڵ�,�˴�������硰<img></img>�������Ľڵ� **/    
		NodeFilter filter = new TagNameFilter("IMG");    

		/** �õ����˺�Ľڵ� **/    
		list = list.extractAllNodesThatMatch(filter, true);    
		Pattern pat = Pattern.compile(regEx); 
		List<String> result_list = new ArrayList<String>();
		for(int c =0; c < list.size(); c ++){    
			ImageTag imageTag=(ImageTag)list.elementAt(c);  
			/** ���ͼƬ������Url **/  
			String image_url = imageTag.getImageURL();
//			System.out.println(image_url);  
			//��ʼ����ƥ�䣬�ų���վLOGO�ͳ��� 
			Matcher mat = pat.matcher(image_url); 
			if(mat.matches()){
				result_list.add(image_url);
			}
		}    
		return result_list;       
	}    
	public GrabHtml(String carName){
		this.url = makeUrlString(carName);
		this.html = getHtml(this.url);
	}
}

