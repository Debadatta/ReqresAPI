package StepDefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import com.aventstack.extentreports.Status;
import com.cucumber.listener.Reporter;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.sf.saxon.lib.Logger;
import Lib.Log;

public class StepDefinitions {
	public static String apiEndPointUri;
	public static String testName;
	public static String CONTENT_TYPE;
	public static String STATUS_CODE;
	public static String FILE_PATH;
	public static String REQUESTBODY;
	public static String RESPONSEBODY;
	public static Response response;
	private String CONFIG_PROPERTIES_PATH = "/src/test/java/Configuration/config.properties";
	
	Properties obj = new Properties();
	
	@Before
	public void setup() throws IOException {
		 
		FileInputStream objfile = new FileInputStream(System.getProperty("user.dir")+ CONFIG_PROPERTIES_PATH);									
	    obj.load(objfile);   
	    DOMConfigurator.configure(obj.getProperty("LOG4J"));
	    Log.startTestCase("Reqres API Test");
	}
 
	@Given("^I want to set URL as \"([^\"]*)\" for test case \"([^\"]*)\"$")
	public void setAPIEndpointURL(String URL, String testCaseName) {
		String apiHostName = obj.getProperty("API_URL");
		apiEndPointUri = String.format("%s%s", apiHostName, URL);
		testName = testCaseName;
		Reporter.addStepLog(Status.PASS + " :: Cucumber Hostname URL is :: " + apiEndPointUri);
		Log.info("Cucumber Hostname URL is :: " + apiEndPointUri);
		Log.info("Cucumber Test case name is :: " + testName);
	}

	@When("^I set header content type as \"([^\"]*)\"$")
	public void setHeader(String contentType) {
		if (contentType != null && !contentType.isEmpty()) {
			CONTENT_TYPE = contentType;
			Reporter.addStepLog(Status.PASS + " :: content type is :: " + CONTENT_TYPE);
			Log.info("Content type is :: " + CONTENT_TYPE);
		} else {
			Reporter.addStepLog(Status.FAIL + " :: content type cannot be null or empty!");
			Log.error("Content type cannot be null or empty!");
		}
	}

	@And("^I hit the API with requestbody \"([^\"]*)\" and request method is \"([^\"]*)\"$")
	public void submitRequest(String requestBodyPath, String requestType) throws Throwable {
		RestAssured.baseURI = apiEndPointUri;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", CONTENT_TYPE);

		if (requestBodyPath != null && !requestBodyPath.isEmpty() && requestType.equalsIgnoreCase("POST")
				|| requestType.equalsIgnoreCase("PUT")) {

			JSONParser jsonParser = new JSONParser();
			FILE_PATH = System.getProperty("user.dir") + obj.getProperty("SRC_PATH")+ requestBodyPath;
			Log.info("Path of requestbody file is :: " + FILE_PATH);
			try (FileReader reader = new FileReader(FILE_PATH)) {
				Object obj = jsonParser.parse(reader);
				REQUESTBODY = obj.toString();
				Log.info("Request Body is :: " + REQUESTBODY);

			} catch (FileNotFoundException | ParseException exc) {
				exc.printStackTrace();
			}

			if (REQUESTBODY.length() > 0) {
				request.body(REQUESTBODY);
				response = request.post();
			} else {
				Reporter.addStepLog(Status.FAIL + " :: Request Body cannot be null or empty!");
				Log.error(" Request Body cannot be null or empty!");
			}

		} else if (requestType.equalsIgnoreCase("GET")) {
			response = request.get();
		} else if (requestType.equalsIgnoreCase("DELETE")) {
			response = request.delete();
		}

		STATUS_CODE = String.valueOf(response.getStatusCode());
		RESPONSEBODY = response.getBody().asString();
		Reporter.addStepLog(Status.PASS + " :: Request successfully processed");
		Reporter.addStepLog("Response is :: " + RESPONSEBODY);
	}

	@Then("^I try to verify the status code is \"([^\"]*)\"$")
	public void verifyStatusCode(String statusCode) {
		if (statusCode.equals(String.valueOf(STATUS_CODE))) {
			Assert.assertEquals(STATUS_CODE, statusCode);
			Reporter.addStepLog(Status.PASS + " :: Status Code is :: " + STATUS_CODE);
			Log.info("Status Code is :: " + STATUS_CODE);
		} else {
			Assert.assertEquals(STATUS_CODE, statusCode);
			Reporter.addStepLog(Status.FAIL + " :: Status Code is :: " + STATUS_CODE);
			Log.error("Status Code is not as expected :: " + STATUS_CODE);
		}
	}

	@And("^I try to verify the content type contains \"([^\"]*)\"$")
	public void verifyContentType(String contentType) {
		String responseContentType = response.getContentType();		
		Boolean validContentType = responseContentType.contains(contentType);
		if(validContentType) {
			Assert.assertTrue(validContentType);
			Reporter.addStepLog(Status.PASS + " :: Content Type contains :: " + contentType);
			Log.info("Content Type contains as expected :: " + responseContentType);
		} else {
			Assert.assertFalse(true);
			Reporter.addStepLog(Status.FAIL + " :: Content Type does not contain :: " + contentType);		
			Log.error("Content Type does not contain as expected :: " + contentType);
		}		
		
	}
	
	@And("^I try to verify the response value \"([^\"]*)\" is \"([^\"]*)\"$")
	public void verifyResponseValue(String responseKey, String value) throws Throwable {
		Object obj = responseKey;
		JSONParser parser = new JSONParser();
		JSONObject responseObject = (JSONObject) parser.parse(RESPONSEBODY);
		Object key = (Object) responseObject.get(responseKey);
		compareResponseValues(String.valueOf(value), String.valueOf(key), responseKey);
	}
	
	@And("^I try to verify the response of \"([^\"]*)\" is \"([^\"]*)\"$")
	public void iTryToVerifyResponseValue(String responseKey, String value) throws Throwable {
		Object obj = responseKey;
		JSONParser parser = new JSONParser();
		JSONObject responseObject = (JSONObject) parser.parse(RESPONSEBODY);
		JSONObject dataObj = (JSONObject) responseObject.get("data");
		compareResponseValues(String.valueOf(value), String.valueOf(dataObj.get(responseKey)), responseKey);
	}

	@And("^I try to verify the total count is \"([^\"]*)\"$")
	public void verifyTotalCount(String value) throws Throwable {		
		JSONParser parser = new JSONParser();
		JSONObject responseObject = (JSONObject) parser.parse(RESPONSEBODY);
		Object key = (Object) responseObject.get("total");
		Reporter.addStepLog("Actual Total value is  :: " + String.valueOf(key));
		Reporter.addStepLog("Expected Total value is  :: " + String.valueOf(value));
		Assert.assertEquals(String.valueOf(value), String.valueOf(key));
	}
	
	private void compareResponseValues(String expected, String actual, String responseKey) {
		Reporter.addStepLog("Actual Value is  :: " + actual);
		Reporter.addStepLog("Expected Value is  :: " + expected);
		Log.info("Actual Value is  :: " + actual);
		Log.info("Expected Value is  :: " + expected);
		if (expected.equals(actual)) {
			Assert.assertEquals(actual, expected);
			Reporter.addStepLog(Status.PASS + " " + responseKey + " : Expected value : " + expected
					+ " mathches with Actual Value : " + actual);
		} else {
			Reporter.addStepLog(Status.FAIL + " " + responseKey + " : Expected value : " + expected
					+ " do not matches with Actual Value : " + actual);
			Log.error("Actual Value is  :: " + actual + " does not match with "+expected);
			Assert.assertEquals(actual, expected);
		}
	}

}
