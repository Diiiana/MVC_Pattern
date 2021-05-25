package Model;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class CakePersistence {

    private static String filename = "db.xml";

    public CakePersistence() {

    }

    public Cakes readFile() {
        Cakes allCakes = new Cakes();
        try {
            File xmlDoc = new File(filename);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            NodeList nodeList = document.getElementsByTagName("cake");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList elements = element.getElementsByTagName("name");
                    for (int j = 0; j < elements.getLength(); j++) {
                        String id = element.getElementsByTagName("id").item(j).getTextContent();
                        String name = element.getElementsByTagName("name").item(j).getTextContent();
                        int disponibility = Integer.parseInt(element.getElementsByTagName("disponibility").item(j).getTextContent());
                        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(element.getElementsByTagName("availability").item(j).getTextContent());
                        float price = Float.parseFloat(element.getElementsByTagName("price").item(j).getTextContent().trim());
                        Cake cake = new Cake(id, name, disponibility, date, price);
                        allCakes.getCakeList().add(cake);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return allCakes;
    }

    public Cake searchCakeWithName(String name, String id) {
        CakeShop cakeShop = cakeShopWhereUserWorks(id);
        for (Cake cake : cakeShop.getMenu().getCakeList()) {
            if (cake.getName().equals(name)) {
                return cake;
            }
        }
        return null;
    }

    public Cake searchCakeWithId(String id) {
        Cakes cakes = readFile();
        for (Cake cake : cakes.getCakeList()) {
            if (cake.getId().equals(id)) {
                return cake;
            }
        }
        return null;
    }

    public double maxIn() {
        double max = 0;
        Cakes cakes = readFile();
        for (Cake cake : cakes.getCakeList()) {
            if (Double.parseDouble(cake.getId().trim()) > max) {
                max = Double.parseDouble(cake.getId().trim());
            }
        }
        return max;
    }

    public boolean addCake(Cake cake) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            Element newCake = document.createElement("cake");

            Element id = document.createElement("id");
            cake.setId(String.valueOf(maxIn() + 1));
            id.appendChild(document.createTextNode(cake.getId()));
            newCake.appendChild(id);

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(cake.getName()));
            newCake.appendChild(name);

            Element disponibility = document.createElement("disponibility");
            disponibility.appendChild(document.createTextNode(String.valueOf(cake.getDisponibility())));
            newCake.appendChild(disponibility);

            Element availability = document.createElement("availability");
            DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            availability.appendChild(document.createTextNode(formatter1.format(cake.getAvailability())));
            newCake.appendChild(availability);

            Element price = document.createElement("price");
            price.appendChild(document.createTextNode(String.valueOf(cake.getPrice())));
            newCake.appendChild(price);

            root.appendChild(newCake);

            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("db.xml");
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public CakeShops getCakeShops() {
        CakeShops allCakeShops = new CakeShops();
        try {
            File xmlDoc = new File("db.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            NodeList nodeList = document.getElementsByTagName("cakeshop");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    CakeShop cakeShop = new CakeShop();
                    cakeShop.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    NodeList elements = element.getElementsByTagName("idCake");
                    for (int j = 0; j < elements.getLength(); j++) {
                        String id = (String) elements.item(j).getTextContent();
                        Cake cake = searchCakeWithId(id);
                        if (cake != null) {
                            cakeShop.getMenu().getCakeList().add(cake);
                        }
                    }

                    NodeList userElements = element.getElementsByTagName("idUser");
                    for (int j = 0; j < userElements.getLength(); j++) {
                        String id = (String) userElements.item(j).getTextContent();
                        UserPersistence userPersistence = new UserPersistence();
                        User user = userPersistence.getUserById(id);
                        if (user != null) {
                            cakeShop.getUsers().getUsersList().add(user);
                        }
                    }
                    allCakeShops.getCakeShopList().add(cakeShop);
                }
            }
        } catch (Exception ex) {
        }
        return allCakeShops;
    }

    public Cakes getCakesFromCakeShop(String name) {
        Cakes cakes = new Cakes();
        try {
            File xmlDoc = new File("db.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            NodeList nodeList = document.getElementsByTagName("cakeshop");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                        NodeList elements = element.getElementsByTagName("idCake");
                        for (int j = 0; j < elements.getLength(); j++) {
                            String id = (String) elements.item(j).getTextContent();
                            Cake cake = searchCakeWithId(id);
                            if (cake != null) {
                                cakes.getCakeList().add(cake);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return cakes;
    }

    public boolean deleteCake(String name, String userId) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            CakeShop cakeShop = cakeShopWhereUserWorks(userId);
            Cake cake = searchCakeWithName(name, userId);
            NodeList allCakeNodes = root.getElementsByTagName("cakeshop");
            for (int i = 0; i < allCakeNodes.getLength(); i++) {
                Element element = (Element) allCakeNodes.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(cakeShop.getName())) {
                    NodeList cakesList = element.getElementsByTagName("idCake");
                    for (int j = 0; j < cakesList.getLength(); j++) {
                        if (cakesList.item(j).getTextContent().equals(cake.getId())) {
                            element.removeChild(cakesList.item(j));
                        }
                    }
                }
            }
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(filename);
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean updateCake(String name, Cake cake) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();
            NodeList allUserNodes = root.getElementsByTagName("cake");
            for (int i = 0; i < allUserNodes.getLength(); i++) {
                Element element = (Element) allUserNodes.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                    element.getElementsByTagName("name").item(0).setTextContent(cake.getName());
                    element.getElementsByTagName("disponibility").item(0).setTextContent(String.valueOf(cake.getDisponibility()));
                    DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    element.getElementsByTagName("availability").item(0).setTextContent(formatter1.format(cake.getAvailability()));
                }
            }
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("db.xml");
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public Cakes returnByValability(String id) {
        CakeShop cakeShop = cakeShopWhereUserWorks(id);
        Cakes cakes = cakeShop.getMenu();
        Collections.sort(cakes.getCakeList());
        return cakes;
    }

    public Cakes filterBetweenPrices(double left, double right, String id) {
        Cakes cakes = cakeShopWhereUserWorks(id).getMenu();
        Cakes cakes1 = new Cakes();
        for (Cake cake : cakes.getCakeList()) {
            if (left == right && cake.getPrice() == left) {
                cakes1.getCakeList().add(cake);
            }
            if (((int) left) == 0 && right > 0) {
                if (cake.getPrice() <= right) {
                    cakes1.getCakeList().add(cake);
                }
            } else {
                if (cake.getPrice() <= right && cake.getPrice() >= left && left != right) {
                    cakes1.getCakeList().add(cake);
                }
            }
        }
        return cakes1;
    }

    public Cakes filterByDisponibility(int disponibilityField, String id) {
        Cakes cakes = cakeShopWhereUserWorks(id).getMenu();
        Cakes cakes1 = new Cakes();
        for (Cake cake : cakes.getCakeList()) {
            if (cake.getDisponibility() == disponibilityField) {
                cakes1.getCakeList().add(cake);
            }
        }
        return cakes1;
    }

    public String cakeByNameFromCakeShops(Cake cake) {
        String result = cake.getName() + " available at ";
        CakeShops cakeShops = getCakeShops();
        for (CakeShop cakeShop : cakeShops.getCakeShopList()) {
            for (Cake cake1 : cakeShop.getMenu().getCakeList()) {
                if (cake1.getName().equals(cake.getName())) {
                    result += cakeShop.getName() + "; ";
                }
            }
        }
        return result;
    }

    public boolean addCakeToCakeShop(String name, String id) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            NodeList allCakeShops = root.getElementsByTagName("cakeshop");
            for (int i = 0; i < allCakeShops.getLength(); i++) {
                Element element = (Element) allCakeShops.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                    Element id2 = document.createElement("idCake");
                    id2.appendChild(document.createTextNode(id));
                    element.appendChild(id2);
                    break;
                }
            }
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("db.xml");
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public CakeShop cakeShopWhereUserWorks(String id) {
        CakeShops cakeShops = getCakeShops();
        for (CakeShop cakeShop : cakeShops.getCakeShopList()) {
            for (User user : cakeShop.getUsers().getUsersList()) {
                if (user.getId().equals(id)) {
                    return cakeShop;
                }
            }
        }
        return null;
    }
}
