package com.jdbcdemo.jdbcdemo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.jdbcdemo.jdbcdemo.coreCall.BaseServicesCall;
import com.jdbcdemo.jdbcdemo.coreCall.CoreServiceCall;
import com.jdbcdemo.jdbcdemo.dto.BaseOutput;
import com.jdbcdemo.jdbcdemo.dto.BulkEmployeesResponse;
import com.jdbcdemo.jdbcdemo.dto.DepartmentDetailsResponse;
import com.jdbcdemo.jdbcdemo.dto.EmployeeDetailsResponse;
import com.jdbcdemo.jdbcdemo.dto.InsertEmployeeList;
import com.jdbcdemo.jdbcdemo.dto.InsertEmployeeResponse;
import com.jdbcdemo.jdbcdemo.dto.JobDetails;
import com.jdbcdemo.jdbcdemo.interfaces.CreateEmployeeInBulkIF;
import com.jdbcdemo.jdbcdemo.interfaces.IFN01;
import com.jdbcdemo.jdbcdemo.interfaces.IFN02;
import com.jdbcdemo.jdbcdemo.interfaces.IFN03;
import com.jdbcdemo.jdbcdemo.interfaces.IServices;

@Component
public class Services implements IServices, IFN02, IFN03 {
	@Autowired
	CoreServiceCall bs;

	public EmployeeDetailsResponse getEmployeeLists(String empId) {

		EmployeeDetailsResponse response = new EmployeeDetailsResponse();
		CoreServiceCall bs = new CoreServiceCall();
		response = bs.getEmployeeLists(empId);

		return response;
	}

	@Override
	public InsertEmployeeResponse insertNewEmployee(InsertEmployeeList request) {
		// InsertEmployeeList employeeList = new ArrayList<>();
		InsertEmployeeResponse response = new InsertEmployeeResponse();
		// CoreServiceCall bs = new CoreServiceCall();
		String empId = "";
		response = bs.insertNewEmployee(request);

		return response;

	}

	@Override
	public EmployeeDetailsResponse getEmployeeDetails(String empId) {
		// TODO Auto-generated method stub
		EmployeeDetailsResponse response = new EmployeeDetailsResponse();

		response = bs.getEmployeeDetails(empId);

		float sal = response.getSalary();
		List<Float> salaryList = new ArrayList<>();
		salaryList.add(sal);

		System.out.println("printting to check lambda");

		if (checkEvenSalary(salaryList))

		// salaryList.stream().filter(i -> i % 2 == 1).forEach(System.out::println);
		// System.out.println();
		{
			return response;
		}

		else {
			response.setErrorDesc("Odd Salary");
			return response;
		}
	}

	private static boolean checkEvenSalary(List<Float> salary) {
		return salary.stream().allMatch(i -> i % 2 == 0);

	}

	@Override
	public BaseOutput removeEmployee(String empId) {
		BaseOutput response = new BaseOutput();
		response = bs.removeEmployee(empId);
		return response;
	}

	public BaseOutput freezeEmployee(String empId) {

		IFN01 fo = (String emId) -> {
			CoreServiceCall bss = new CoreServiceCall();
			BaseOutput response = new BaseOutput();
			response = bss.freezeEmployee(empId);

			return response;

		};

		return fo.freezeEmployee(empId);

	}

	public BulkEmployeesResponse createBulkEmployee(List<InsertEmployeeList> employeeList) {
		BulkEmployeesResponse response = new BulkEmployeesResponse();

		List<String> emailList = new ArrayList<>();

		for (InsertEmployeeList eList : employeeList) {
			emailList.add(eList.getEmail());
		}
		if (validateEmailId(emailList)) {
			CreateEmployeeInBulkIF cf = (List<InsertEmployeeList> empList) -> {

				CoreServiceCall bst = new CoreServiceCall();
				InsertEmployeeResponse empResponse = new InsertEmployeeResponse();
				List<InsertEmployeeResponse> empList5 = new ArrayList<>();
				for (InsertEmployeeList empList3 : empList.stream().toList()) {

					empResponse = bst.insertNewEmployee(empList3);

					empList5.add(empResponse);
					response.setNewEmployeesResponse(empList5);

				}

				return response;
			};

			return cf.createBulkEmployees(employeeList);

		}
		response.setErrorCode(1001);
		response.setErrorDesc("email is not from well knowns TLDs");
		return response;
	}

	private boolean validateEmailId(List<String> emailList) {
		// TODO Auto-generated method stub
		return emailList.stream()
				.allMatch(mail -> mail.contains("@gmail.") || mail.contains("@yahoo.") || mail.contains("@outlook."));
	}

	public Double sumOfDeptWiseSalary(String deptId) {

		CoreServiceCall bst = new CoreServiceCall();
		List<Double> sumOfSalary = new ArrayList<>();

		sumOfSalary = bst.sumOfSalary(deptId, "department_id");

		BinaryOperator<Double> accumulator = new BinaryOperator<Double>() {

			@Override
			public Double apply(Double t, Double u) {
				// TODO Auto-generated method stub
				return t + u;
			}

		};
		Double sum;

		sum = extracted2(sumOfSalary);
		return sum;
	}

	private Double extracted(List<Double> sumOfSalary) {
		return sumOfSalary.stream().reduce(0D, (x, y) -> x + y);
	}

	private Double extracted2(List<Double> sumOfSalary) {
		return sumOfSalary.stream().reduce(0D, (x, y) -> x - y);
	}

	public Double wiseCalcuation(String wise, String type, String id) {

		CoreServiceCall bst = new CoreServiceCall();
		List<Double> salaryList = new ArrayList<>();

		salaryList = bst.sumOfSalary(id, wise);

		Double response = 0D;

		if (type.equals("sum"))
			response = extracted(salaryList);
		if (type.equals("average")) {
			Long size = salaryList.stream().count();

			response = (double) Math.round(salaryList.stream().reduce(0D, (x, y) -> (x + y)) / size);

		}
		return response;

	}

	public List<JobDetails> jobDetails(String minSalary) {

		List<JobDetails> jobList = new ArrayList<>();
		CoreServiceCall bst = new CoreServiceCall();
		jobList = bst.jobDetails(minSalary);
		List<JobDetails> jobList2 = new ArrayList<>();

		jobList.sort(Comparator.comparing(JobDetails::getJobId).reversed());
		List<String> jobList4 = new ArrayList<>();
		// ----------------------------------------------------------------------------------------
		Function<JobDetails, String> mapper = JobDetails::getJobId;
		Function<JobDetails, String> mapper2 = new Function<JobDetails, String>() {

			@Override
			public String apply(JobDetails t) {
				// TODO Auto-generated method stub
				return t.getJobId();
			}

		};
		Predicate<String> predicate = i -> i.contains("ager");
		Predicate<String> predicate2 = new Predicate<String>() {

			@Override
			public boolean test(String t) {

				return t.contains("ager") == false;
			}

		};

		Collector<String, ?, List<String>> listColl = Collectors.toList();

		jobList4 = jobList.stream().map(mapper2).filter(predicate2).collect(listColl);
		Consumer<String> action = new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println(t + "--");

			}

		};
		jobList.stream().map(mapper2).filter(predicate2).forEach(action);
		System.out.println(jobList4);

		Supplier<Integer> supp = () -> {

			Random rand = new Random();
			return rand.nextInt(10000, 999999);
		};

		System.out.println("The generated random number is---->" + supp.get());
		// ----------------------------------------------------------------------------------------

		return jobList;

	}

	public List<Integer> empIdList(String salary) {
		List<Integer> empIdList = new ArrayList<>();
		CoreServiceCall bst = new CoreServiceCall();
		List<Integer> response = new ArrayList<>();
		empIdList = bst.getEmpIdAccToSalary(salary);
		response = empIdList.stream().sorted().collect(Collectors.toList());

		return response;
	}

	@Override
	public List<Integer> empIdListNew(String salary) {
		List<Integer> response = new ArrayList<>();
		List<Integer> response3 = new ArrayList<>();
		CoreServiceCall bst = new CoreServiceCall();
		IFN02 fn02 = (String sal) -> {
			List<Integer> response2 = new ArrayList<>();
			response2 = bst.getEmpIdAccToSalary(salary);
			return response2;
		};
		response = fn02.empIdListNew(salary);
		response3 = response;

		Predicate<Integer> predicateType = i -> i % 2 == 1;
		extractedType(response3, predicateType);

		return response;
	}

	private void extractedType(List<Integer> response3, Predicate<Integer> predicateType) {
		response3.stream().filter(predicateType).forEach(System.out::println);
	}

	@Override
	public DepartmentDetailsResponse getDeptDetails(String deptId) {
		DepartmentDetailsResponse response = new DepartmentDetailsResponse();
		CoreServiceCall bst = new CoreServiceCall();

		IFN03 obj = (String deptId2) -> {

			System.out.println("Inside lambda Calling of functional interface");
			return bst.getDeptDetails(deptId);

		};
		response = obj.getDeptDetails(deptId);

		Function<DepartmentDetailsResponse, String> getAddress = new Function<DepartmentDetailsResponse, String>() {

			@Override
			public String apply(DepartmentDetailsResponse t) {
				Predicate<String> checkVowels = new Predicate<String>() {

					@Override
					public boolean test(String t) {
						// TODO Auto-generated method stub
						return t.contains("Centre");
					}

				};

				String add = "";
				if (checkVowels.test(t.getAddress())) {
					add = "OK";
				}

				return add;
			}

		};

		Consumer<String> printIt = new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println(t);

			}

		};

		response.setErrorDesc(getAddress.apply(response));

		return response;
	}

	public BaseOutput tableAndDataCreate(String location) throws FileNotFoundException, IOException {

		BaseOutput response = new BaseOutput();
		String errorDesc = "Success";

		// String st = "C:\\Users/junai/Downloads\\BulkEmployees.csv";

		location = location.replace('\\', '/');
		System.out.println(location);

		String ar[] = location.split("/");
		String tableN = ar[ar.length - 1];

		tableN = tableN.substring(0, tableN.length() - 4);

		System.out.println(tableN);

		char tableCheckFlag = 'T';

		tableCheckFlag = CoreServiceCall.checkTableExistence(tableN);
		if (tableCheckFlag == 'F') {
			errorDesc = createTable(tableN, location);
		}

		if (errorDesc.equals("Success")) {
			response = insertDataInTable(tableN, location);
		}

		return response;

	}

	public static String createTable(String tableName, String fileLocation) throws FileNotFoundException, IOException {

		CSVParser parser = new CSVParser(new FileReader(fileLocation), CSVFormat.DEFAULT.withHeader());

		Map<String, Integer> retMap = parser.getHeaderMap();

		List<CSVRecord> retMap3 = parser.getRecords();

		String response = "Success";

		// System.out.println(retMap3.get(1));
		// CSVRecord st = retMap3.get(0).get(null);

		// System.out.println(retMap3);
		// System.out.println(retMap3.get(0));
		// testCSV();

		Map<Integer, String> retMap2 = new HashMap<>();
		// System.out.println(retMap);
		String columnNames = "";
		// System.out.println(retMap.size());

		Map<Integer, String> myNewHashMap = new HashMap<>();
		for (Map.Entry<String, Integer> entry : retMap.entrySet()) {
			myNewHashMap.put(entry.getValue(), entry.getKey());
		}
		// System.out.println(myNewHashMap);
		for (int i = 0; i < myNewHashMap.size(); i++) {
			columnNames = columnNames + myNewHashMap.get(i) + "##";
			// System.out.println(response);
			// retMap2.put(retMap.get, response)
		}
		columnNames = columnNames.substring(0, columnNames.length() - 2);
		columnNames = columnNames.replace(' ', '_');

		try {
			response = CoreServiceCall.createTable(tableName, columnNames);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

	public BaseOutput insertDataInTable(String tableName, String location) {

		StringBuilder sb = null;
		BaseOutput response = new BaseOutput();
		int count = 0;
		int i;
		// Reader reader =
		// Files.newBufferedReader(Paths.get("C:/Users/junai/Downloads/Document3.csv"));
		try {
			Reader in = new FileReader(location);
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
			for (CSVRecord record : records) {
				String values = "";
				if (count > 0) {
					for ( i = 0; i <record.size()-1; i++) {
						values = values + record.get(i) + "~~";
					}
					values=values.replace('\'', ' ');
					values=values.replace('(', ' ');
					values=values.replace(')', ' ');
					values=values.replace(',', ' ');
					values=values.replace('/', ' ');
					//values=values.replace('-', ' ');
					//values=values.replace('#', ' ');
					if(i==record.size()-1) {
						values= values + record.get(i);
					}
					sb = new StringBuilder(values);
					//sb.deleteCharAt(values.length() - 3);

				}

				System.out.println(sb);
				if (count > 0) {
					try {
						CoreServiceCall csc = new CoreServiceCall();
						response = csc.insertDataInTable(tableName, sb.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				count++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

}
