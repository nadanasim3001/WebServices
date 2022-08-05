package com.demo.ExcelProject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CSVParser parser = new CSVParser(new FileReader("C:/Users/junai/Downloads/Movie_Database_3.csv"),
				CSVFormat.DEFAULT.withHeader());
		
		String st = "C:\\Users/junai/Downloads\\Movie_Database_3.csv";
		
		st=st.replace('\\', '/');
		//System.out.println(st);
		
		String ar[]= st.split("/");
		String tableN=ar[ar.length-1];
		
		
		tableN=tableN.substring(0, tableN.length()-4);
		
		//System.out.println(tableN);
		
		
		Map<String, Integer> retMap = parser.getHeaderMap();

		List<CSVRecord> retMap3 = parser.getRecords();

		// System.out.println(retMap3.get(1));
		// CSVRecord st = retMap3.get(0).get(null);

		// System.out.println(retMap3);
		//System.out.println(retMap3.get(0));
		//testCSV();
System.out.println("\'");
		Map<Integer, String> retMap2 = new HashMap<>();
		// System.out.println(retMap);
		String response = "";
		// System.out.println(retMap.size());

		

		Map<Integer, String> myNewHashMap = new HashMap<>();
		for (Map.Entry<String, Integer> entry : retMap.entrySet()) {
			myNewHashMap.put(entry.getValue(), entry.getKey());
		}
		// System.out.println(myNewHashMap);
		for (int i = 0; i < myNewHashMap.size(); i++) {
			response = response + myNewHashMap.get(i) + "##";
			//System.out.println(response);
			// retMap2.put(retMap.get, response)
		}
		response = response.substring(0, response.length() - 2);

		// System.out.println(myNewHashMap);
	 //System.out.println(response);
	}
	
	

	public static void testCSV() throws IOException {

		 
		StringBuilder sb=null;
		int count=0;
		//Reader reader = Files.newBufferedReader(Paths.get("C:/Users/junai/Downloads/Document3.csv"));
		Reader in = new FileReader("C:/Users/junai/Downloads/BulkEmployees.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		for (CSVRecord record : records) {
			String values="";
			if(count>0) {
			for(int i=0; i<record.size(); i++) {
				values=values+record.get(i)+",";
			}
			 sb = new StringBuilder(values);
			sb.deleteCharAt(values.length()-1);
			//sb=sb.append("~~");
		    
		    
			} 
		    count++;
		    System.out.println(sb);
		}
		
		//System.out.println(record.size());
		   
		/*
		 * public static String
		 * parseCSV2("C://Users//junai//Downloads//BulkEmployees.csv") throws
		 * FileNotFoundException, IOException { CSVParser parser = new CSVParser(new
		 * FileReader(fileLocation), CSVFormat.DEFAULT.withHeader()); String st2 =
		 * parser.getHeaderMap().toString(); return "ok"; }
		 */
	}

}
