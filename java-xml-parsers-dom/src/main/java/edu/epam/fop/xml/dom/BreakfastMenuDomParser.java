package edu.epam.fop.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BreakfastMenuDomParser {
	public static List<Food> parseBreakfastMenu(String pathToXml) throws Exception {
		List<Food> foodList = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(pathToXml));
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement();
		NodeList foodNodes = root.getElementsByTagName("food");
		for (int i = 0; i < foodNodes.getLength(); i++) {
			Node foodNode = foodNodes.item(i);
			if (foodNode.getNodeType() == Node.ELEMENT_NODE) {
				Element foodElement = (Element) foodNode;
				String id = foodElement.getAttribute("id");
				Element nameElement = (Element) foodElement.getElementsByTagName("name").item(0);
				String name = nameElement.getTextContent().replaceAll("\\s+", " ").trim();
				Element priceElement = (Element) foodElement.getElementsByTagName("price").item(0);
				String price = priceElement.getTextContent().replaceAll("\\s+", " ").trim();
				Element descriptionElement = (Element) foodElement.getElementsByTagName("description").item(0);
				String description = descriptionElement.getTextContent().replaceAll("\\s+", " ").trim();
				Element caloriesElement = (Element) foodElement.getElementsByTagName("calories").item(0);
				String calories = caloriesElement.getTextContent().replaceAll("\\s+", " ").trim();
				Food food = new Food(id, name, price, description, calories);
				foodList.add(food);
			}
		}
		return foodList;
	}
}