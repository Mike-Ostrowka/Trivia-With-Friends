package com.cmpt276.team8project;

// Message.java
public class Message {

  private final String text; // message body
  private final MemberData memberData; // data of the user that sent this message
  private final boolean belongsToCurrentUser; // is this message sent by us?

  // constructor
  public Message(String text, MemberData memberData, boolean belongsToCurrentUser) {
    this.text = text;
    this.memberData = memberData;
    this.belongsToCurrentUser = belongsToCurrentUser;
  }

  // getter functions
  public String getText() {
    return text;
  }

  public MemberData getMemberData() {
    return memberData;
  }

  public boolean isBelongsToCurrentUser() {
    return belongsToCurrentUser;
  }
}

// Code above was modified from the following reference:
//Scaledrone, “Android Chat Tutorial: Building A Realtime Messaging App,”
//    Scaledrone Blog, 05-Feb-2019. [Online]. Available:
//    https://www.scaledrone.com/blog/android-chat-tutorial/. [Accessed: 09-Apr-2021].