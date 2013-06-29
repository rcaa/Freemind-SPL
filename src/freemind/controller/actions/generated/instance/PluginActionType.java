//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.1-05/30/2003 05:06 AM(java_re)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.02.23 at 11:11:43 GMT+01:00 
//


package freemind.controller.actions.generated.instance;


/**
 * Java content class for anonymous complex type.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{}plugin_mode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{}plugin_menu" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{}plugin_property" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="key_stroke" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="base" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="documentation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="icon_path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instanciation">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="Once"/>
 *             &lt;enumeration value="OnceForRoot"/>
 *             &lt;enumeration value="OnceForAllNodes"/>
 *             &lt;enumeration value="Other"/>
 *             &lt;enumeration value="ApplyToRoot"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PluginActionType {


    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getDocumentation();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setDocumentation(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getBase();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setBase(java.lang.String value);

    /**
     * Gets the value of the PluginProperty property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there's any setter method for the PluginProperty property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPluginProperty().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link freemind.controller.actions.generated.instance.PluginPropertyType}
     * 
     */
    java.util.List getPluginProperty();

    /**
     * Gets the value of the PluginMenu property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there's any setter method for the PluginMenu property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPluginMenu().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link freemind.controller.actions.generated.instance.PluginMenuType}
     * 
     */
    java.util.List getPluginMenu();

    /**
     * Gets the value of the PluginMode property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there's any setter method for the PluginMode property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPluginMode().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link freemind.controller.actions.generated.instance.PluginModeType}
     * 
     */
    java.util.List getPluginMode();

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getLabel();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setLabel(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getClassName();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setClassName(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getIconPath();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setIconPath(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getKeyStroke();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setKeyStroke(java.lang.String value);

    /**
     * 
     * @return possible object is
     * {@link java.lang.String}
     */
    java.lang.String getInstanciation();

    /**
     * 
     * @param value allowed object is
     * {@link java.lang.String}
     */
    void setInstanciation(java.lang.String value);

}