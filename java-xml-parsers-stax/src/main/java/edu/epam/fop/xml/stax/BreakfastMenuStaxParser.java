package edu.epam.fop.xml.stax;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class BreakfastMenuStaxParser {

	public static List<Food> parseBreakfastMenu(String pathToXml) throws XMLStreamException, IOException {
		List<Food> foodList = new ArrayList<>();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(pathToXml));
		String id = null;
		String name = null;
		String price = null;
		String description = null;
		String calories = null;
		String previousElement = null;
		while (reader.hasNext()) {
			int eventType = reader.next();
			switch (eventType) {
				case XMLStreamConstants.START_ELEMENT:
					previousElement = reader.getLocalName();
					if (previousElement.equalsIgnoreCase("food")) {
						id = reader.getAttributeValue(null, "id");
					}
					break;

				case XMLStreamConstants.CHARACTERS:
					String text = reader.getText().replaceAll("\\s+", " ").trim();
					if (!text.isEmpty()) {
						if (previousElement.equalsIgnoreCase("name")) {
							name = text;
						}
						if (previousElement.equalsIgnoreCase("price")) {
							price = text;
						}
						if (previousElement.equalsIgnoreCase("description")) {
							description = text;
						}
						if (previousElement.equalsIgnoreCase("calories")) {
							calories = text;
						}
					}
					break;

				case XMLStreamConstants.END_ELEMENT:
					previousElement = reader.getLocalName();
					if (previousElement.equalsIgnoreCase("food")) {
						Food food = new Food(id, name, price, description, calories);
						foodList.add(food);
					}
					break;
			}
		}
		return foodList;
	}
}