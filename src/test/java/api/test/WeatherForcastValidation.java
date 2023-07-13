package api.test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import api.pojo.WeatherForecast;
import api.endpoint.Routes;
import api.utilities.Utilities;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class WeatherForcastValidation {

	WeatherForecast weatherPayload;
	Response jsonPath;
	SoftAssert assertion;

	@BeforeClass
	public void setUpData() throws IOException {
		weatherPayload = new WeatherForecast();
		weatherPayload.setKey(Utilities.getPropertiesFileData("api_key"));
		assertion = new SoftAssert();
	}

	@Test(priority = 1)
	public void getWeatherForcastResponse() throws IOException {
		RestAssured.baseURI = Utilities.getPropertiesFileData("base_url");
		Response response = given().queryParam("key", weatherPayload.getKey()).when().get(Routes.getResource());
		jsonPath = response;
	}

	@Test(priority = 2)
	public void validateWeatherResponse() throws IOException {
		assertion.assertEquals(jsonPath.getStatusCode(), 200);
		assertion.assertEquals(jsonPath.header("Content-Type"), Utilities.getPropertiesFileData("contentType"));
		assertion.assertEquals(jsonPath.header("X-Powered-By"), Utilities.getPropertiesFileData("xPoweredBy"));

		JSONObject jobj = new JSONObject(jsonPath.asString());
		System.out.println(jobj.getJSONArray("days").length());

		LinkedList<String> dateCapture = new LinkedList<String>();
		for (int i = 0; i < jobj.getJSONArray("days").length(); i++) {
			String datetime = jobj.getJSONArray("days").getJSONObject(i).get("datetime").toString();
			dateCapture.add(datetime);
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		if (dateCapture.contains(dateFormat.format(now)))
			assertion.assertTrue(true);
		else
			assertion.assertFalse(false);

		if (jobj.getJSONObject("currentConditions").getString("sunrise")
				.contains(Utilities.getPropertiesFileData("sunrise")))
			assertion.assertTrue(true);
		else
			assertion.assertTrue(false);

		if (jobj.getJSONObject("currentConditions").getString("sunset")
				.contains(Utilities.getPropertiesFileData("sunset")))
			assertion.assertTrue(true);
		else
			assertion.assertTrue(false);

		assertion.assertAll();
	}

}
