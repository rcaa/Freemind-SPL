//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.1-05/30/2003 05:06 AM(java_re)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.02.23 at 11:11:43 GMT+01:00 
//


package freemind.controller.actions.generated.instance;


/**
 * Java content class for hook_node_action element declaration.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;element name="hook_node_action">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{}node_action">
 *         &lt;sequence>
 *           &lt;element ref="{}node_list_member" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;attribute name="hook_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;attribute name="content" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;/extension>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface HookNodeAction
    extends javax.xml.bind.Element, freemind.controller.actions.generated.instance.HookNodeActionType
{


}