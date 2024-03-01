package org.universityofsouthampton.runwayredeclarationtool.utility;

import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Runways")
public class RunwayXMLLoader {

    private List<Runway> runwayList = new ArrayList<>();

    @XmlElement(name = "Runway")
    public List<Runway> getRunwayList() {
        return runwayList;
    }

    public void setRunwayList(List<Runway> runwayList) {
        this.runwayList = runwayList;
    }

    public void exportToXML(String filePath) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(RunwayXMLLoader.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(this, new File(filePath));
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static RunwayXMLLoader importFromXML(String filePath) {
        RunwayXMLLoader runways = null;
        try {
            JAXBContext context = JAXBContext.newInstance(RunwayXMLLoader.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            runways = (RunwayXMLLoader) unmarshaller.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return runways;
    }
}
