package api.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import io.restassured.path.json.JsonPath;

public class Utilities {

	public static String getPropertiesFileData(String propKey) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//api//resources//globalData.properties");
		prop.load(fis);
		String value = prop.getProperty(propKey);
		return value;
	}

	public static String getJsonPath(String response, String key) {
		String resp = response;
		JsonPath js = new JsonPath(resp);
		return js.get(key).toString();
	}

}
