package com.xhermes.android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FaultCodeXMLUtil {
	private Document document;

	public FaultCodeXMLUtil(){
		SAXReader saxReader = new SAXReader();
		try {
			InputStream abpath = getClass().getResourceAsStream("/assets/obdfaultcode.xml");
			document = saxReader.read(abpath);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<FaultCodeIterator> getAllFaultCodes(){
		ArrayList<FaultCodeIterator> list = new ArrayList<FaultCodeIterator>();
		Element root = document.getRootElement();
		Iterator<Element> iter = root.elementIterator();
		while(iter.hasNext()){
			Element t = iter.next();
			FaultCodeIterator fc = new FaultCodeIterator();
			String[] strs = t.getStringValue().split("\n");
			fc.setIndex(strs[1].trim());
			fc.setFaultDetail(strs[2].trim());
			fc.setClassify(strs[3].trim());
			fc.setPriorty(strs[4].trim());
			fc.setSolution(strs[5].trim());
			fc.setAssortment(strs[6].trim());
			list.add(fc);
		}
		return list;
	}
	private byte[] InputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}
}
