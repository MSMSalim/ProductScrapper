package integrationTest;

import com.sainsburys.productscrapper.Application;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class ApplicationIntegrationTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void should_contain_result_in_the_console_output_response() {

        // Arrange
        String scrapeUrl = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";

        // Act
        Application.main(scrapeUrl, "sampleOutPut");
        String consoleOutput = outputCapture.toString();

        // Assert
        assertThat(consoleOutput.contains("results")).isTrue();
    }

    @Test
    public void should_contain_expected_first_product_in_the_output() {
        // Arrange
        String scrapeUrl = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";

        String expectedFirstProduct = "{\n" +
                "    \"title\" : \"Sainsbury's Apricot Ripe & Ready x5\",\n" +
                "    \"description\" : \"Apricots\",\n" +
                "    \"size\" : \"38.27Kb\",\n" +
                "    \"unit_price\" : 3.50\n" +
                "  }";

        // Act
        Application.main(scrapeUrl, "sampleOutPut");
        String consoleOutput = outputCapture.toString();
        String actualFirstProduct = getFirstProduct(consoleOutput);

        // Assert
        assertThat(actualFirstProduct).isEqualToIgnoringWhitespace(expectedFirstProduct);

    }

    private String getFirstProduct(String output) {

        Pattern pattern = Pattern.compile("\\{(?:[^{}])*\\}");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(0);
        }

        return StringUtils.EMPTY;
    }

}