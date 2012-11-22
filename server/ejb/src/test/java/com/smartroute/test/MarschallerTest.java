package com.smartroute.test;

import com.smartroute.scheduler.jni.ObjectFactory;
import com.smartroute.scheduler.jni.XContract;
import com.smartroute.scheduler.jni.XContractStation;
import com.smartroute.scheduler.jni.XLoad;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MarschallerTest {
    
    private static final boolean SEALED = true;    
    private static final BigDecimal LATITUDE = new BigDecimal(1L);
    private static final int MINUTE_FROM = 51; 
    private static final int MINUTE_UNTIL = 55; 
    private static final BigInteger UNITS = new BigInteger("123");

    
    @Test
    public void marshall() throws JAXBException, SAXException, DatatypeConfigurationException {
        ObjectFactory factory = new ObjectFactory();
        XContract contract = factory.createXContract();
        contract.setSealed(SEALED);
        
        XContractStation contractStation = factory.createXContractStation();
        contractStation.setKind(XLoad.LOAD);
        contractStation.setLatitude(LATITUDE);
        contractStation.setLongitude(new BigDecimal(2L));
        contractStation.setUnits(UNITS);
        
        contractStation.setTimeFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(2010, 11, 30, 23, MINUTE_FROM, 22, 0, 0));
        contractStation.setTimeUntil(DatatypeFactory.newInstance().newXMLGregorianCalendar(2010, 11, 30, 23, MINUTE_UNTIL, 22, 0, 0));
        
        contract.getStation().add(contractStation);
        contract.getStation().add(contractStation);
        
        
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        Schema schema = schemaFactory.newSchema(
                this.getClass().getClassLoader().getResource("contract_to_estimate.xsd")); 

        JAXBContext jaxbContext = JAXBContext.newInstance(XContract.class.getPackage().getName());

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setSchema(schema);
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(factory.createContract(contract), stringWriter);
        
        System.out.println(stringWriter.toString());
    }

    @Test
    public void unmarshall() throws JAXBException, SAXException {
        JAXBContext jaxbContext = JAXBContext.newInstance(XContract.class.getPackage().getName());

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = schemaFactory.newSchema(
                this.getClass().getClassLoader().getResource("contract_to_estimate.xsd")); 
        unmarshaller.setSchema(schema);
        
        
        @SuppressWarnings("unchecked")
        JAXBElement<XContract> unmarshalledObject = (JAXBElement<XContract>) unmarshaller.unmarshal(
                this.getClass().getClassLoader().getResource("xml/contract1.xml"));
        
        MatcherAssert.assertThat(unmarshalledObject.getValue(), Matchers.instanceOf(XContract.class));
        
        XContract contract = unmarshalledObject.getValue();
        
        assertThat(contract.isSealed(), is(SEALED));
        assertThat(contract.getStation(), Matchers.hasSize(2));
        assertThat(contract.getStation().get(1).getLatitude(), Matchers.equalTo(LATITUDE));
        assertThat(contract.getStation().get(1).getTimeFrom().getMinute(), Matchers.equalTo(MINUTE_FROM));
        assertThat(contract.getStation().get(0).getTimeUntil().getMinute(), Matchers.equalTo(MINUTE_UNTIL));
        assertThat(contract.getStation().get(0).getUnits(), Matchers.equalTo(UNITS));
    }
    
}

