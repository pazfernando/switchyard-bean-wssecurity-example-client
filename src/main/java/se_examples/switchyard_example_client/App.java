package se_examples.switchyard_example_client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.ws.security.SecurityConstants;

import switchyard.example.com.switchyard_example._1.DummyGuy;
import switchyard.example.com.switchyard_example._1_0.DummyPortType;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {
			String serviceURL = "http://localhost:8080/switchyard-example/Dummy";
			QName serviceName = new QName("urn:com.example.switchyard:switchyard-example:1.0", "Dummy");
			URL wsdlURL = new URL(serviceURL + "?wsdl");
			Service service = Service.create(wsdlURL, serviceName);
			DummyPortType proxy = (DummyPortType)service.getPort(DummyPortType.class);
			
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.CALLBACK_HANDLER, new KeystorePasswordCallback());
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.SIGNATURE_PROPERTIES,
			     Thread.currentThread().getContextClassLoader().getResource("META-INF/alice.properties"));
			((BindingProvider)proxy).getRequestContext().put(SecurityConstants.SIGNATURE_USERNAME, "alice");
			
			Client client = ClientProxy.getClient(proxy);
			Endpoint cxfEndpoint = client.getEndpoint();
			
			cxfEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());
			cxfEndpoint.getInInterceptors().add(new LoggingInInterceptor());
			
			DummyGuy dummy = new DummyGuy();
			dummy.setName("Sandra");
			dummy.setLastName("Bullock");
			System.out.println(proxy.sayHello(dummy));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
