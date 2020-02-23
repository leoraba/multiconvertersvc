package com.lrivera.multiconverterSvc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.InfoProperties.Entry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BuildProperties buildProperties;

	@GetMapping(
			path = "/converter", 
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<Object> converter(@RequestParam String from, @RequestParam String to, @RequestBody Object data) {
		
		logger.debug("from: " + from);
		logger.debug("to: " + to);
		logger.debug("data: " + data);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ping() {
		logger.debug("PING");
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
		
		return new ResponseEntity<Object>(resp, HttpStatus.OK);
	}
}
