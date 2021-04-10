package com.example.team8project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class QuestionXMLParser {

    private final List<Question> questionBank;
    public int sizeOfBank;

    public QuestionXMLParser() {
        this.questionBank = new ArrayList<>();
        this.sizeOfBank = 0;
    }

    public void parser() {
        try {

            InputStream file = getClass().getClassLoader().getResourceAsStream("questions.xml");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("question");

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element tElement = (Element) node;

                    Question newQuestion = new Question(
                            tElement.getElementsByTagName("questionText").item(0).getTextContent(),
                            tElement.getElementsByTagName("answerOne").item(0).getTextContent(),
                            tElement.getElementsByTagName("answerTwo").item(0).getTextContent(),
                            tElement.getElementsByTagName("answerThree").item(0).getTextContent(),
                            tElement.getElementsByTagName("answerFour").item(0).getTextContent(),
                            tElement.getElementsByTagName("correctAnswer").item(0).getTextContent()
                    );
                    this.questionBank.add(newQuestion);
                }
            }
            file.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        this.sizeOfBank = this.questionBank.size();
    }

    public Question[] getQuestionBank(Question[] bank) {
        return this.questionBank.toArray(bank);
    }
}


