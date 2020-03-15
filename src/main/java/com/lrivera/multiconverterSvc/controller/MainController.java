package com.lrivera.multiconverterSvc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.InfoProperties.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lrivera.multiconverterSvc.formatter.XmlFormatter;
import com.lrivera.multiconverterSvc.util.Formats;

@RestController
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BuildProperties buildProperties;

	@PostMapping(
			path = "/converter/from/{from}/to/{to}", 
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.ALL_VALUE})
	public ResponseEntity<Object> converter(@PathVariable String from, @PathVariable String to, @RequestBody String data) throws Exception {
		
		HttpStatus httpResponseStatus = HttpStatus.BAD_REQUEST;
		Object dataResponse = "";
		
		logger.info("CONVERTER INPUT from:{}, to:{}, data:{}", from, to, data);
		
		// Input is XML
		if(Formats.XML.toString().equalsIgnoreCase(from)) {
			XmlFormatter formatter = new XmlFormatter(data);
			
			// Check which format convert TO
			if(Formats.CSV.toString().equalsIgnoreCase(to)) {
				dataResponse = formatter.convertToCSV();
			}else if(Formats.JSON.toString().equalsIgnoreCase(to)) {
				dataResponse = formatter.convertToJSON();
			}
			httpResponseStatus = HttpStatus.OK;
			
		}else if(Formats.JSON.toString().equalsIgnoreCase(from)) {
			// Input is JSON
		}
		logger.info("dataResponse: {}, httpResponseStatus: {}", dataResponse,  httpResponseStatus);
		return new ResponseEntity<Object>(dataResponse, httpResponseStatus);
	}
	
	@GetMapping(path = "/converter/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ping() {
		logger.info("PING");
		Map<String, String> resp = new HashMap<>();
		
		Iterator<Entry> propertiesIterator = buildProperties.iterator();
		
		while(propertiesIterator.hasNext()) {
			Entry prop = (Entry) propertiesIterator.next();
			String value = prop.getValue();
			if(prop.getKey().equals("time")) {
				Date date = new Date();
				date.setTime(Long.parseLong(prop.getValue()));
				value = date.toString();
			}
			resp.put(prop.getKey(), value);
		}
		logger.info("resp: {}, HttpStatus: {}", resp,  HttpStatus.OK);
		
		return new ResponseEntity<Object>(resp, HttpStatus.OK);
	}
}
