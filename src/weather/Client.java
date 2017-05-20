/* Name: Likitha Seeram (1001363714)
 * Assignment: CSE 5306 Lab#3
 * References: 
 * 1) http://stackoverflow.com/questions/8317957/how-to-instruct-eclipse-to-record-relative-paths-of-the-external-jars
 * 2) https://mvnrepository.com/artifact/javax.mail/mail/1.4.7
 * 3) http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java
 * 4) http://stackoverflow.com/questions/12395281/convert-double-to-bigdecimal-and-set-bigdecimal-precision
 * 5) https://www.mkyong.com/java/java-date-and-calendar-examples/
 * 6) http://www.java-samples.com/showtutorial.php?tutorialid=1733
 * 7) http://stackoverflow.com/questions/15940234/how-to-do-a-soap-web-service-call-from-java-class
 * 8) https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
 */

package weather;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.lang.String;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.*;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.*;

/*
 * This call takes the request from the GUI and makes a SOAP call to the 
 * web service to fetch the weather information
 */
public class Client {
	private URL url;  //variable used to store end point url of the web service
	private NdfdXMLBindingStub stub;  //An instance where the NDFDgen method is implemented which is used for calling the web service
	private ProductType product;  //An instance of the class ProductType which is used for calling NDFDgen
	private Date date;  //to get the current date and time
	private UnitType unit;  //An instance of the class UnitType which is used for calling NDFDgen
	private WeatherParametersType weatherParameters;  //An instance of the class WeatherParametersType which is used for calling NDFDgen
	private String result;  //A variable to store the xml result
	
	/*
	 * This method is called by GUI to get the weather report. 
	 * Input: Latitude, Longitude, GUI object
	 * In this method web service is called using the coordinates 
	 */
	public void sendReport(double latitude, double longitude, ClientGUI cg) {
		try {
			url = new URL("https://graphical.weather.gov:443/xml/SOAP_server/ndfdXMLserver.php");  //end-point url
			stub = new NdfdXMLBindingStub(url, null);  //Instantiating the instance by passing the url
			product = ProductType.fromValue("time-series");  //retrieving the product object 
			unit = UnitType.fromValue("e");  //retrieving the unit object 
			weatherParameters = new WeatherParametersType();  //instantiating the weatherParameters object
			Date date = new Date(System.currentTimeMillis());  //To get the current date and time 
			Calendar time = Calendar.getInstance();  //Creating a Calendar instance
			time.setTime(date);  //Setting the calendar instance to current time
			
			//NDFDgen method is called by passing all the required parameters. 
			//This method returns the xml which has all the weather information
			result = stub.NDFDgen(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude), product, time, time, unit, weatherParameters);
			
			//The following code is used for parsing the retrieved xml
			//javax.mail jar is used for parsing
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append(result);  //Appending the xml result to string builder
			ByteArrayInputStream input =  new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
			Document doc = builder.parse(input);  //parsing the input stream
			
			//Required weather conditions are extracted using their tag name
			String temperature = doc.getElementsByTagName("temperature").item(0).getTextContent();  //Extracting temperature parameter
			cg.display(temperature + "Fahrenheit\n");  //A method of GUI is called to display the result on GUI
			String cloud = doc.getElementsByTagName("cloud-amount").item(0).getTextContent();  //Extracting cloud-amount parameter
			cg.display(cloud + "percent\n");  //A method of GUI is called to display the result on GUI
			String precipitation = doc.getElementsByTagName("probability-of-precipitation").item(0).getTextContent();  //Extracting probability-of-precipitation parameter
			cg.display(precipitation + "percent\n");  //A method of GUI is called to display the result on GUI
			String speed = doc.getElementsByTagName("wind-speed").item(0).getTextContent();  //Extracting wind-speed parameter
			cg.display(speed + "knots\n");  //A method of GUI is called to display the result on GUI
			String humidity = doc.getElementsByTagName("direction").item(0).getTextContent();  //Extracting wind-direction parameter
			cg.display(humidity + "degrees\n");  //A method of GUI is called to display the result on GUI
		} catch (Exception e) {
			System.out.println("Exception occured due to:" + e);  //printing the exception on console
		}
	}
}
