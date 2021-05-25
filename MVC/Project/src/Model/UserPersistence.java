package Model;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class UserPersistence {
    private static String filename = "db.xml";

    public UserPersistence() {

    }

    public Users readUsers() {
        Users users = new Users();
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element element = (Element) userList.item(i);
                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                String role = element.getElementsByTagName("role").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                User user = new User(id, name, password, role);
                users.getUsersList().add(user);
            }
        } catch (Exception e) {
        }
        return users;
    }

    public int getMaxId() {
        Users users = readUsers();
        int max = 0;
        for (User user : users.getUsersList()) {
            if (Integer.parseInt(user.getId()) > max) {
                max = Integer.parseInt(user.getId());
            }
        }
        return max;
    }

    public boolean addUser(User user, String cakeShopName) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            Element newUser = document.createElement("user");

            Text lb = document.createTextNode("\n");

            Element id = document.createElement("id");
            user.setId(String.valueOf(getMaxId() + 1));
            id.appendChild(document.createTextNode(user.getId()));

            newUser.appendChild(id);
            newUser.appendChild(lb);

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(user.name));

            newUser.appendChild(name);
            newUser.appendChild(lb);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(user.getPassword()));
            newUser.appendChild(password);
            newUser.appendChild(lb);

            Element role = document.createElement("role");
            role.appendChild(document.createTextNode(user.getRole()));
            newUser.appendChild(role);
            newUser.appendChild(lb);

            root.appendChild(newUser);

            NodeList cakeShops = root.getElementsByTagName("cakeshop");
            for (int i = 0; i < cakeShops.getLength(); i++) {
                Element element = (Element) cakeShops.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(cakeShopName)) {
                    Element newUserId = document.createElement("idUser");
                    newUserId.appendChild(document.createTextNode(user.getId()));
                    element.appendChild(newUserId);
                    element.appendChild(lb);
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

    public boolean updateUser(String name, User user) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            NodeList allUserNodes = root.getElementsByTagName("user");
            for (int i = 0; i < allUserNodes.getLength(); i++) {
                Element element = (Element) allUserNodes.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                    element.getElementsByTagName("name").item(0).setTextContent(user.getName());
                    element.getElementsByTagName("password").item(0).setTextContent(user.getPassword());
                    element.getElementsByTagName("role").item(0).setTextContent(user.getRole());
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

    public User getUserByName(String name1) {
        Users users = readUsers();
        for (User user : users.getUsersList()) {
            if (user.name.equals(name1)) {
                return user;
            }
        }
        return null;
    }

    public User getUserById(String id1) {
        Users users = readUsers();
        for (User user : users.getUsersList()) {
            if (user.getId().equals(id1)) {
                return user;
            }
        }
        return null;
    }

    public boolean deleteUser(String name) {
        File xmlDoc = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDoc);
            Element root = document.getDocumentElement();

            NodeList allUserNodes = root.getElementsByTagName("user");
            for (int i = 0; i < allUserNodes.getLength(); i++) {
                Element element = (Element) allUserNodes.item(i);
                if (element.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                    root.removeChild(element);
                }
            }
            User user = getUserByName(name);
            NodeList allWorkplaces = root.getElementsByTagName("cakeshop");
            for (int i = 0; i < allWorkplaces.getLength(); i++) {
                Element element = (Element) allWorkplaces.item(i);
                NodeList nodeList = element.getElementsByTagName("idUser");
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Element element1 = (Element) nodeList.item(j);
                    if (element1.getTextContent().equals(user.getId())) {
                        element.removeChild(element1);
                    }
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

}
