package com.cmpt276.team8project;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class QuestionXMLParser {

  private final List<Question> questionBank;
  public int sizeOfBank;

  // constructor
  public QuestionXMLParser() {
    this.questionBank = new ArrayList<>();
    this.sizeOfBank = 0;
  }

  public void parser() {
    try {
      // load questions xml file into inputstream
      InputStream file = getClass().getClassLoader().getResourceAsStream("questions.xml");
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(file);
      doc.getDocumentElement().normalize();
      // start at root node of xml file
      NodeList nodeList = doc.getElementsByTagName("question");

      // iterate through the xml file from node to node
      // nodes are marked with a <question> tag
      for (int i = 0; i < nodeList.getLength(); ++i) {
        Node node = nodeList.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element tElement = (Element) node;

          // construct a Question object that takes in (String question, String answerOne, String answerTwo,
          // String answerThree, String answerFour, String correctAnswer)
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

  // getter function to retrieve question bank
  public Question[] getQuestionBank(Question[] bank) {
    return this.questionBank.toArray(bank);
  }
}


