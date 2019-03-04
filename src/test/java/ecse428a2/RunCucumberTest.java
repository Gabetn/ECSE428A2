package ecse428a2;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class RunCucumberTest{
}
/**
The JUnit runner will by default use classpath:package.of.my.runner to look for features.
 You can also specify the location of the feature file(s) and glue file(s) you want Cucumber to use
 in the @CucumberOptions.
 */