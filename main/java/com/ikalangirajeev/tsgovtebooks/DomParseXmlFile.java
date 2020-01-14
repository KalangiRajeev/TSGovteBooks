package com.ikalangirajeev.tsgovtebooks;

import android.content.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class ParseXmlFile {
    private String filename;
    private Context context;
    private ArrayList<EbookItem> eBookArrayList = new ArrayList<>();

    ParseXmlFile(Context context, String filename) {
        this.filename = filename;
        this.context = context;
    }

    ArrayList<EbookItem> getParsedList() {
        try {
            InputStream inputStream = context.getAssets().open(filename);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            element.normalize();
            NodeList nodeList = document.getElementsByTagName("para");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element1 = (Element) node;
                    String p_name = getValue("para_name", element1);
                    String p_desc = getValue("para_desc", element1);
                    eBookArrayList.add(new EbookItem(p_name.trim(), p_desc.trim()));
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    return eBookArrayList;
    }

    private String getValue(String para_name, Element element) {
        NodeList nodeList = element.getElementsByTagName(para_name).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
