package com.example.team8project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class QuestionXMLParser {

  private List<Question> questionBank = new ArrayList<>();
  public int sizeOfBank = 0;

  private void parse() {
    try {
      File file = new File("questions.xml");

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
    } catch (Exception e) {
      System.out.println(e);
    }
    this.sizeOfBank = this.questionBank.size();
  }

  public Question[] getQuestionBank(Question[] bank) {
    return this.questionBank.toArray(bank);
  }

  // testing code
//  void main() {
//    QuestionXMLParser qxp = new QuestionXMLParser();
//    qxp.parse();
//    Question[] bank = new Question[qxp.sizeOfBank];
//    qxp.getQuestionBank(bank);
//    for(int i = 0; i < qxp.sizeOfBank; i++){
//      System.out.println(bank[0].getQuestion());
//      System.out.println(Arrays.toString(bank[0].getWrongAnswers()));
//      System.out.println(bank[0].getAnswer());
//    }
//  }
}


