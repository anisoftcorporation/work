/*
 * This code extracts individual XMLs from bulk XMls exported from debug page of each object type.
 * The generated XMls don't have the standard DTD and waveset DTD portions at top hence they are not re-importable. These are just to look at and refer 
 */
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SIMUtil {

	
	
  public static void main(String argv[]) {

   parseBulk("AdminRole");
   parseBulk("Configuration");
   parseBulk("EmailTemplate");
   parseBulk("Resource");
   parseBulk("Rule");
   parseBulk("TaskDefinition");
   parseBulk("UserForm");
   parseBulk("Role");
   
  }

/*
 * Below method is the core parser 
 */
  public static void parseBulk(String type)
  {
	  try {

			File fXmlFile = new File("C:\\input\\BulkCodes\\"+type+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
					
			
			doc.getDocumentElement().normalize();
            String parseType= "";
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		   if(type.equalsIgnoreCase("UserForm"))
		   {
			   parseType="Configuration";
		   }
		   else
		   {
			   parseType = type;
		   }
			NodeList nList = doc.getElementsByTagName(parseType);
					
			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				Element ele = (Element)nNode;
				System.out.println(ele.getAttribute("name"));
				
				String name="";
				try{
					name = ele.getAttribute("displayName");
				}catch(Exception as){}
				if((null==name)||name.equalsIgnoreCase(""))
				{
					name = ele.getAttribute("name");
				}
				String preciseType=ele.getAttribute("wstype");
				PrintWriter writer = new PrintWriter("C:\\input\\SourceCodes\\"+type+"\\"+name+".xml", "UTF-8");
				//System.out.println("\nCurrent Element :" + );
				writer.println(nodeToString(nNode));
				writer.close();
			  
				
			}
			
		    } catch (Exception e) {
			System.out.println(e);
		    }
  }
  /*
   * Below method get the XMl contents from within the tag and returns
   */
  
  public static String nodeToString(Node node) {
	  StringWriter sw = new StringWriter();
	  try {
	    Transformer t = TransformerFactory.newInstance().newTransformer();
	    t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    t.transform(new DOMSource(node), new StreamResult(sw));
	  } catch (TransformerException te) {
	    System.out.println("nodeToString Transformer Exception");
	  }
	  return sw.toString();
	}

}
