
package com.cm;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SyncChannels", targetNamespace = "http://cm.com/", wsdlLocation = "http://localhost:8080/scs/SyncChannels?wsdl")
public class SyncChannels_Service
    extends Service
{

    private final static URL SYNCCHANNELS_WSDL_LOCATION;
    private final static WebServiceException SYNCCHANNELS_EXCEPTION;
    private final static QName SYNCCHANNELS_QNAME = new QName("http://cm.com/", "SyncChannels");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/scs/SyncChannels?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SYNCCHANNELS_WSDL_LOCATION = url;
        SYNCCHANNELS_EXCEPTION = e;
    }

    public SyncChannels_Service() {
        super(__getWsdlLocation(), SYNCCHANNELS_QNAME);
    }

//    public SyncChannels_Service(WebServiceFeature... features) {
//        super(__getWsdlLocation(), SYNCCHANNELS_QNAME, features);
//    }

    public SyncChannels_Service(URL wsdlLocation) {
        super(wsdlLocation, SYNCCHANNELS_QNAME);
    }

//    public SyncChannels_Service(URL wsdlLocation, WebServiceFeature... features) {
//        super(wsdlLocation, SYNCCHANNELS_QNAME, features);
//    }

    public SyncChannels_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

//    public SyncChannels_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }

    /**
     * 
     * @return
     *     returns SyncChannels
     */
    @WebEndpoint(name = "SyncChannelsPort")
    public SyncChannels getSyncChannelsPort() {
        return super.getPort(new QName("http://cm.com/", "SyncChannelsPort"), SyncChannels.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SyncChannels
     */
    @WebEndpoint(name = "SyncChannelsPort")
    public SyncChannels getSyncChannelsPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://cm.com/", "SyncChannelsPort"), SyncChannels.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SYNCCHANNELS_EXCEPTION!= null) {
            throw SYNCCHANNELS_EXCEPTION;
        }
        return SYNCCHANNELS_WSDL_LOCATION;
    }

}