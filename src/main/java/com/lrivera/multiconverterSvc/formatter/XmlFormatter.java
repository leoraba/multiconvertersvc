package com.lrivera.multiconverterSvc.formatter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlFormatter {
	private Document document;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	private XmlFormatter() {}
	
	public XmlFormatter(String data) throws Exception {
		if(data == null) {
			throw new Exception("XML data is null");
		} else {
			try {
				data = data.replaceAll(">\\s*<", "><").replaceAll("(\\r|\\n|\\r\\n)+", "").trim();
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				InputStream inputStream = new ByteArrayInputStream(data.getBytes());

				
				Document document = documentBuilder.parse(inputStream);
				
				this.document = document;
				logger.debug("Document loaded successfully");
				
			} catch (ParserConfigurationException e) {
				throw  e;
			} catch (SAXException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	public String convertToCSV() {
		
		logger.debug("Converting to CSV...");
		
		StringBuilder builderResponse = new StringBuilder();
		
		List<String> titleList = new ArrayList<>();
		Map<String, List<String>> valueMap = new HashMap<>();
		int dataRowCnt = 0;
		
		if(document != null && document.getFirstChild() != null) {
			
			for (int children = 0; children < document.getFirstChild().getChildNodes().getLength(); children++) {
				Node item = document.getFirstChild().getChildNodes().item(children);
				if(item.hasChildNodes()) {
					for (int indexChild = 0; indexChild < item.getChildNodes().getLength(); indexChild++) {
						Node item2 = item.getChildNodes().item(indexChild);
						if(item2.hasChildNodes() && item2.getFirstChild().getNodeType() == Node.TEXT_NODE) {
							String titleName = item2.getNodeName();
							if(!titleList.contains(titleName)) {
								titleList.add(titleName);
							}
							if(valueMap.get(titleName) != null) {
								List<String> listByTitle = valueMap.get(titleName);
								listByTitle.add(item2.getFirstChild().getNodeValue());
							} else {
								List<String> listByTitle = new ArrayList<>();
								listByTitle.add(item2.getFirstChild().getNodeValue());
								valueMap.put(titleName, listByTitle);
							}
						}
					}
					dataRowCnt++;
				}
			}
			
			logger.debug("titleList: {}, valueMap: {}, dataRowCnt: {}", titleList, valueMap, dataRowCnt);
			
			if(titleList != null) {
				builderResponse.append(titleList.stream().collect(Collectors.joining(",")) + "\n");
				
				for(int dataRow = 0; dataRow < dataRowCnt; dataRow++) {
					List<String> row = new ArrayList<>();
					for (String title : titleList) {
						List<String> listByTitle = valueMap.get(title);
						row.add(listByTitle.get(dataRow));
					}
					builderResponse.append(row.stream().collect(Collectors.joining(",")) + "\n");
				}
			}
			
		}
		
		return builderResponse.toString();
	}
	
	public Object convertToJSON() {
		// TODO
		return "";
	}
}
