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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.lrivera.multiconverterSvc.formatter.XmlFormatter;

@RestController
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BuildProperties buildProperties;

	@PostMapping(
			path = "/converter", 
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<Object> converter(
			@RequestBody String data,
			@RequestHeader("Content-Type") String contentType,
			@RequestHeader("Accept") String accept) throws Exception {
		
		HttpStatus httpResponseStatus = HttpStatus.BAD_REQUEST;
		Object dataResponse = "";
		
		logger.info("CONVERTER INPUT from:{}, to:{}, data:{}", contentType, accept, data);
		
		// Input is XML
		switch (contentType) {
		case MediaType.APPLICATION_XML_VALUE:
			try {
				XmlFormatter formatter = new XmlFormatter(data);
				
				// Check which format convert TO
				if(MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(accept)) {
					dataResponse = formatter.convertToCSV();
				}else if(MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(accept)) {
					dataResponse = formatter.convertToJSON();
				}
				httpResponseStatus = HttpStatus.OK;
			} catch (Exception e) {
				logger.error(e.getMessage());
				dataResponse = "XML body is not valid";
			}
			break;

		case MediaType.APPLICATION_JSON_VALUE:
			// Input is JSON
			// TODO
			break;
		}
		
		logger.info("httpResponseStatus: {}",  httpResponseStatus);
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
		logger.info("HttpStatus: {}",  HttpStatus.OK);
		
		return new ResponseEntity<Object>(resp, HttpStatus.OK);
	}
}
