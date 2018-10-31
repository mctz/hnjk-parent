package scriptlets;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: WebappScriptlet.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class WebappScriptlet extends JRDefaultScriptlet {

	/**
	 * 
	 */
	@Override
	public void afterGroupInit(String groupName) throws JRScriptletException {
		String allCities = (String) this.getVariableValue("AllCities");
		String city = (String) this.getFieldValue("City");
		StringBuffer sbuffer = new StringBuffer();

		if (allCities != null) {
			sbuffer.append(allCities);
			sbuffer.append(", ");
		}

		sbuffer.append(city);
		this.setVariableValue("AllCities", sbuffer.toString());
	}

	/**
	 * 
	 */
	public String hello() throws JRScriptletException {
		return "Hello! I'm the report's scriptlet object.";
	}

}
