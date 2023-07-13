package api.endpoint;

import java.io.IOException;
import api.utilities.Utilities;

public class Routes {	

	public static String cityname;
	
	public static String getResource() throws IOException {
		cityname = Utilities.getPropertiesFileData("city");
		String Resource = "/VisualCrossingWebServices/rest/services/timeline/" + cityname;
		return Resource;
	}	
}
