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
			java.net.URL url = new java.net.URL(urlString);  //根据 String 表示形式创建 URL 对象。
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();// 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
			java.io.InputStreamReader isr = new java.io.InputStreamReader(conn.getInputStream());//返回从此打开的连接读取的输入流。
			java.io.BufferedReader br = new java.io.BufferedReader(isr);//创建一个使用默认大小输入缓冲区的缓冲字符输入流。

			String temp;
			while ((temp = br.readLine()) != null) {  //按行读取输出流
				if(!temp.trim().equals("")){
					html.append(temp).append("\n");  //读完每行后换行
				}
			}
			br.close();   //关闭
			isr.close();  //关闭
			return html.toString();   //返回此序列中数据的字符串表示形式。
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private static String makeUrlString(String carName){
		//利用搜狐图片搜索引擎获取车辆图片
		return "http://s.auto.sohu.com/search.at?suggest=" + carName + "+%CD%BC%C6%AC";
	}
	public List<String> pre_parser() throws ParserException{
		/**根据Url创建parser对象**/    
		Parser parser =  new Parser(url);    

		/**设置编码，必须与Url编码一样 **/    
		parser.setEncoding("utf-8");    

		/** 构建一个Html页面对象 **/    
		HtmlPage htmlPage = new HtmlPage(parser);    
		parser.visitAllNodesWith(htmlPage);    

		/** 获取Body下面所有的节点，可以想象成类似树状结构 **/    
		NodeList list = htmlPage.getBody();    

		/** 建立一个Filter，用于过滤节点,此处获得形如“<img></img>”这样的节点 **/    
		NodeFilter filter = new TagNameFilter("a");    

		/** 得到过滤后的节点 **/    
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
		/**根据Url创建parser对象**/    
		Parser parser =  new Parser(real_url);    

		/**设置编码，必须与Url编码一样 **/    
		parser.setEncoding("utf-8");    

		/** 构建一个Html页面对象 **/    
		HtmlPage htmlPage = new HtmlPage(parser);    
		parser.visitAllNodesWith(htmlPage);    

		/** 获取Body下面所有的节点，可以想象成类似树状结构 **/    
		NodeList list = htmlPage.getBody();    

		/** 建立一个Filter，用于过滤节点,此处获得形如“<img></img>”这样的节点 **/    
		NodeFilter filter = new TagNameFilter("IMG");    

		/** 得到过滤后的节点 **/    
		list = list.extractAllNodesThatMatch(filter, true);    
		Pattern pat = Pattern.compile(regEx); 
		List<String> result_list = new ArrayList<String>();
		for(int c =0; c < list.size(); c ++){    
			ImageTag imageTag=(ImageTag)list.elementAt(c);  
			/** 输出图片的链接Url **/  
			String image_url = imageTag.getImageURL();
//			System.out.println(image_url);  
			//开始正则匹配，排除网站LOGO和车标 
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

