//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.1-05/30/2003 05:06 AM(java_re)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.02.23 at 11:11:43 GMT+01:00 
//


package freemind.controller.actions.generated.instance;


/**
 * Java content class for plugin_action element declaration.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;element name="plugin_action">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;choice minOccurs="0">
 *             &lt;element ref="{}plugin_mode" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/choice>
 *           &lt;choice minOccurs="0">
 *             &lt;element ref="{}plugin_menu" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/choice>
 *           &lt;choice minOccurs="0">
 *             &lt;element ref="{}plugin_property" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *         &lt;attribute name="key_stroke" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="base" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="documentation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="icon_path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="class_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="instanciation">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *               &lt;enumeration value="Once"/>
 *               &lt;enumeration value="OnceForRoot"/>
 *               &lt;enumeration value="OnceForAllNodes"/>
 *               &lt;enumeration value="Other"/>
 *               &lt;enumeration value="ApplyToRoot"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/attribute>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface PluginAction
    extends javax.xml.bind.Element, freemind.controller.actions.generated.instance.PluginActionType
{


}
