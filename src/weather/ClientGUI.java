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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

/*
 * This class is the GUI of the client application which lets the 
 * user to enter coordinates and gets weather report by calling 
 * Client.java. Results are displayed on the text area
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	Client client = new Client(); // Instantiating a client object

	JPanel panel; // GUI panel
	private JTextArea text, lat, lon; // Text areas for printing results and for entering latitude and longitude
	private JLabel lblLatittude, lblLongitude, lblEnterCoordinatesHere;  //to hold labels
	private JButton btnGetWeatherReport, btnRefresh, btnResetFields;  //buttons to get the weather report, refresh and to reset GUI

	//Default constructor called when a ClientGUI object is created
	public ClientGUI() {
		//GUI panel
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		//Scroll pane at the center to hold text area where results are displayed
		JScrollPane scrollPane = new JScrollPane();
		text = new JTextArea();
		scrollPane.setViewportView(text);
		scrollPane.setBounds(30, 75, 475, 350);
		DefaultCaret caret = (DefaultCaret) text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panel.add(scrollPane);
		
		//Label for latitude
		lblLatittude = new JLabel("Latittude");
		lblLatittude.setBounds(252, 16, 82, 20);
		panel.add(lblLatittude);
		
		//Text area to enter latitude value
		lat = new JTextArea();
		lat.setBounds(338, 16, 96, 22);
		panel.add(lat);
		
		//Label for longitude
		lblLongitude = new JLabel("Longitude");
		lblLongitude.setBounds(252, 38, 77, 22);
		panel.add(lblLongitude);
		
		//Text area to enter longitude value
		lon = new JTextArea();
		lon.setBounds(338, 39, 96, 22);
		panel.add(lon);
		
		//Label for 'Enter Coordinates here'
		lblEnterCoordinatesHere = new JLabel("Enter Coordinates here");
		lblEnterCoordinatesHere.setBounds(42, 16, 180, 20);
		panel.add(lblEnterCoordinatesHere);
		
		//Button to get the weather results
		btnGetWeatherReport = new JButton("Get Weather Report");
		btnGetWeatherReport.setBounds(30, 437, 180, 29);
		btnGetWeatherReport.addActionListener(this);
		panel.add(btnGetWeatherReport);
		
		//Button to refresh
		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(356, 441, 96, 29);
		btnRefresh.addActionListener(this);
		panel.add(btnRefresh);
		
		//Button used to rest the GUI
		btnResetFields = new JButton("Reset");
		btnResetFields.addActionListener(this);
		btnResetFields.setBounds(449, 16, 84, 34);
		panel.add(btnResetFields);

		setVisible(true);
		setTitle("Weather Client Application");  //setting the title of the GUI
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 570, 538);  //setting the size of the GUI window
	}

	public static void main(String args[]) {
		new ClientGUI();  //creating a GUI
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * This method is called when an action is performed on the GUI like
	 * clicking on any button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource(); // Extracting action object
		//When the event is clicking the 'Get Weather Report' or 'Refresh' button
		if (o == btnGetWeatherReport || o == btnRefresh) {
			double latitude = Double.parseDouble(lat.getText());  //Parsing the latitude value to double
			double longitude = Double.parseDouble(lon.getText());  //Parsing the longitude value to double
			text.setText("");  //setting the text area to empty 
			client.sendReport(latitude, longitude, this);  //calling the client method by passing the coordinates and GUI object
		}
		//When the event is clicking the 'Reset' button
		if (o == btnResetFields) {
			lat.setText("");
			lon.setText("");
			text.setText("");
		}
	}
	
	/*
	 * This method is used to add the data to the text area of the GUI window
	 */
	public void display(String result) {
		text.append(result);
	}
}
