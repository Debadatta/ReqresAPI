package runners;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import com.cucumber.listener.Reporter;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty" }, 
					features = "src/test/java/Features", 
					glue = {"StepDefinitions" }, tags = { "@smoke" }, 
					plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/reports/cucumber_report.html"}, 
					monochrome = true)
public class TestRunner {
	@AfterClass
	public static void writeExtentReport() throws IOException {
		Properties obj = new Properties();
		FileInputStream objfile = new FileInputStream(System.getProperty("user.dir")+"/src/test/java/Configuration/config.properties");									
	    obj.load(objfile);   
		Reporter.loadXMLConfig(new File(obj.getProperty("EXTENT_REPORT_CONFIG")));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", "Windows 10");
		Reporter.setTestRunnerOutput("Sample test runner output message");
	}
}
