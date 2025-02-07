package edu.epam.fop.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class BreakfastMenuHandler extends DefaultHandler {
	private final List<Food> foodList = new ArrayList<>();
	private String id;
	private String name;
	private String price;
	private String description;
	private String calories;
	private final StringBuilder currentValue = new StringBuilder();
	public List<Food> getFoodList() {
		return foodList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		currentValue.setLength(0);
		if (qName.equalsIgnoreCase("food")) {
			id = attributes.getValue("id");
		}
		if (qName.equalsIgnoreCase("salary")) {
			price = attributes.getValue("currency");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String trimmed = currentValue.toString().replaceAll("\\s+", " ").trim();
		if (qName.equalsIgnoreCase("name")) {
			name = trimmed;
		}
		if (qName.equalsIgnoreCase("price")) {
			price = trimmed;
		}
		if (qName.equalsIgnoreCase("description")) {
			description = trimmed;
		}
		if (qName.equalsIgnoreCase("calories")) {
			calories = trimmed;
		}
		if (qName.equalsIgnoreCase("food")) {
			Food food = new Food(id, name, price, description, calories);
			foodList.add(food);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		currentValue.append(ch, start, length);
	}
}