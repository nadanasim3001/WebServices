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
public class App2 {
	public static void main(String[] args) throws FileNotFoundException, IOException {

		String a = "JOB_HISTORY####EMPLOYEE_ID,START_DATE,END_DATE,JOB_ID,DEPARTMENT_ID ####  200,01-JUL-02,31-DEC-06,AC_ACCOUNT,90####NUMBER,DATE,DATE,VARCHAR2,NUMBER";

		String ar[] = a.split("####");

		System.out.println(ar[0]);
		System.out.println(ar[1]);
		System.out.println(ar[2].trim());
		System.out.println(ar[3]);

		String tableName = ar[0];
		String rows[] = ar[1].split(",");
		String tableData[] = ar[2].split(",");
		String tableDataType[] = ar[3].split(",");

		String rowName = "";
		String valueName = "";
		String v = ") values (";
		String ends = "');";
		String dateTypeFlag = "";

		/*
		 * insert into EMPLOYEES (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL,
		 * PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY, COMMISSION_PCT, MANAGER_ID,
		 * DEPARTMENT_ID) values (100, 'Steven', 'King', 'SKING', '515.123.4567',
		 * to_date('17-06-2003', 'dd-mm-yyyy'), 'AD_PRES', 24000.00, null, null, 90);
		 */

		String aa = "insert into " + tableName + " (";

		System.out.println(aa);

		for (int i = 0; i < rows.length; i++) {

			rowName = rowName + "," + rows[i];
			if (!tableDataType[i].equals("DATE")) {

				valueName = valueName + "','" + tableData[i].trim();

			}
			if (tableDataType[i].trim().equals("DATE")) {
				// valueName=valueName+"','"+tableData[i].trim();
				dateTypeFlag = "T";
				valueName = valueName + "',to_date('" + tableData[i].trim() + "','dd-mm-yyyy')";

				// System.out.println("substringTest-------"+valueName.substring(valueName.length()-1,
				// valueName.length()));
			}
		}

		rowName = rowName.substring(1);
		valueName = valueName.substring(2);

		String fromReplace = "\\)',to_date\\('";
		String toReplace = "\\),to_date\\('";

		System.out.println(v + valueName);
		String query = aa + rowName + v + valueName + ends;
		System.out.println(query);
		String masterQuery = query.replaceAll(fromReplace, toReplace);
		
		 fromReplace = "-yyyy'\\)',";
		 toReplace = "-yyyy'\\),";
		 masterQuery = query.replaceAll(fromReplace, toReplace);

		System.out.println(masterQuery);

	}

}
