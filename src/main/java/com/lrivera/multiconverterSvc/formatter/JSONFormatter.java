package com.lrivera.multiconverterSvc.formatter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONFormatter {
	
	private JSONObject document;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private JSONFormatter() {}
	
	public JSONFormatter(String data) throws Exception {
		if(data == null) {
			throw new Exception("JSON data is null");
		}
		
		try {
			document = new JSONObject(data);
			logger.debug("JSON document loaded successfully");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String convertToXml() {
		logger.debug("Converting to XML...");
		
		return XML.toString(document);
	}
	
	public String convertToCSV() {
		logger.debug("Converting to CSV...");
		
		StringBuilder builderResponse = new StringBuilder();
		
		List<String> titleList = new ArrayList<>();
		Map<String, List<String>> valueMap = new HashMap<>();
		int dataRowCnt = 0;
		
		Map<String, Object> mapDocument = document.toMap();
		
		for (String documentName : mapDocument.keySet()) {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> dataMap = (List<Map<String, String>>) mapDocument.get(documentName);
			
			for (Map<String, String> el : dataMap) {
				
				for (String keyName : el.keySet()) {
					String value = el.get(keyName);
					if(!titleList.contains(keyName)) {
						titleList.add(keyName);
					}
					
					if(valueMap.get(keyName) != null) {
						List<String> listByTitle = valueMap.get(keyName);
						listByTitle.add(value);
					} else {
						List<String> listByTitle = new ArrayList<>();
						listByTitle.add(value);
						valueMap.put(keyName, listByTitle);
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
		
		return builderResponse.toString();
	}
	
	
}
