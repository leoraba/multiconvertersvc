package com.lrivera.multiconverterSvc.formatter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
		}
		
		try {
			
			//Sanitize data
			data = data.replaceAll(">\\s*<", "><").replaceAll("(\\r|\\n|\\r\\n)+", "").trim();
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(data.getBytes());

			Document document = documentBuilder.parse(inputStream);
			
			this.document = document;
			logger.debug("XML document loaded successfully");
			
		} catch (ParserConfigurationException e) {
			throw  e;
		} catch (SAXException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public String convertToCSV() {
		
		logger.debug("Converting to CSV...");
		
		StringBuilder builderResponse = new StringBuilder();
		
		List<String> titleList = new ArrayList<>();
		Map<String, String[]> valueMap = new HashMap<>();
		int currentDataRow = 0;
		int totalNodes = 0;
		
		if(document != null && document.getFirstChild() != null) {
			totalNodes = document.getFirstChild().getChildNodes().getLength();
			for (int children = 0; children < totalNodes; children++) {
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
								String[] arrayByTitle = valueMap.get(titleName);
								arrayByTitle[currentDataRow] = item2.getFirstChild().getNodeValue();
							} else {
								String[] arrayByTitle = new String[totalNodes];
								arrayByTitle[currentDataRow] = item2.getFirstChild().getNodeValue();
								valueMap.put(titleName, arrayByTitle);
							}
						}
					}
					currentDataRow++;
				}
			}
			
			logger.debug("titleList: {}, valueMap: {}, dataRowCnt: {}", titleList, valueMap, currentDataRow);
			
			if(titleList != null) {
				// First line adding the titles
				builderResponse.append(titleList.stream().collect(Collectors.joining(",")) + "\n");
				
				for(int dataRow = 0; dataRow < totalNodes; dataRow++) {
					List<String> row = new ArrayList<>();
					for (String title : titleList) {
						String[] arrayByTitle = valueMap.get(title);
						// Removing NULL values
						String value = Objects.toString(arrayByTitle[dataRow], "");
						row.add(value);
					}
					builderResponse.append(row.stream().collect(Collectors.joining(",")) + "\n");
				}
			}
		}
		
		return builderResponse.toString();
	}
	
	public Map<String, List<Map<String, String>>> convertToJSON() {
		
		logger.debug("Converting to JSON...");
		
		Map<String, List<Map<String, String>>> resultWrapper = new HashMap<>();
		List<Map<String, String>> resultList = new ArrayList<>();
		
		int dataRowCnt = 0;

		if(document != null && document.getFirstChild() != null) {
			
			String rootDocumentName = document.getFirstChild().getNodeName();
			
			for (int children = 0; children < document.getFirstChild().getChildNodes().getLength(); children++) {
				Node item = document.getFirstChild().getChildNodes().item(children);
				
				Map<String, String> myNodeMap = new HashMap<>();
				
				if(item.hasChildNodes()) {
					for (int indexChild = 0; indexChild < item.getChildNodes().getLength(); indexChild++) {
						Node item2 = item.getChildNodes().item(indexChild);
						if(item2.hasChildNodes() && item2.getFirstChild().getNodeType() == Node.TEXT_NODE) {
							String titleName = item2.getNodeName();
							String elementValue = item2.getFirstChild().getNodeValue();
							myNodeMap.put(titleName, elementValue);
						}
					}
					dataRowCnt++;
				}
				
				resultList.add(myNodeMap);
			}
			
			logger.debug("resultList: {}, dataRowCnt: {}", resultList, dataRowCnt);
			resultWrapper.put(rootDocumentName, resultList);
			
		}
		
		
		return resultWrapper;
	}
}
