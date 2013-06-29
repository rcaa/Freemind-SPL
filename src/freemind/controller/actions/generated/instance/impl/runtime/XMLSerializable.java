//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.1-05/30/2003 05:06 AM(java_re)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.02.23 at 11:11:43 GMT+01:00 
//

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package freemind.controller.actions.generated.instance.impl.runtime;

import org.xml.sax.SAXException;

/**
 * For a generated class to be serializable, it has to
 * implement this interface.
 * 
 * @author Kohsuke Kawaguchi
 */
public interface XMLSerializable
{
    /**
     * Serializes child elements and texts into the specified target.
     */
    void serializeElementBody( XMLSerializer target ) throws SAXException;

    /**
     * Serializes itself as an attribute body into the specified target.
     */
    void serializeAttributeBody( XMLSerializer target ) throws SAXException;
    
    /**
     * Serializes attributes into the specified target.
     */
    void serializeAttributes( XMLSerializer target ) throws SAXException;
    
    /**
     * Declares all the namespace URIs this object is using at
     * its top-level scope into the specified target.
     */
    void serializeURIs( XMLSerializer target ) throws SAXException;

}
