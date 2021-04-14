package com.example.team8project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import com.scaledrone.lib.HistoryRoomListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.scaledrone.lib.SubscribeOptions;
import java.io.IOException;
import java.util.Random;

public class ChatActivity extends AppCompatActivity implements
    RoomListener {

  private String channelID = "laNRgaDxIWeMCBIb";
  private final String roomName = "observable-room";
  private EditText editText;
  private Scaledrone scaledrone;
  private MessageAdapter messageAdapter;
  private ListView messagesView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    //load toolbar
    Toolbar myToolbar = findViewById(R.id.toolbar_profile);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    //load preferences
    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean soundSwitch = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SOUND_SWITCH, false);
    //if switch value is false, disable music
    if (soundSwitch) {
      playMusic();
    }

    // This is where we write the message
    editText = (EditText) findViewById(R.id.editText);
    messageAdapter = new MessageAdapter(this);
    messagesView = (ListView) findViewById(R.id.messages_view);
    messagesView.setAdapter(messageAdapter);

    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    MemberData data = new MemberData(username, getRandomColor());

    String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
    if (!(sessionId == null)) {
      channelID = sessionId;
    }

    scaledrone = new Scaledrone(channelID, data);
    scaledrone.connect(new Listener() {
      @Override
      public void onOpen() {
        System.out.println("Scaledrone connection open");
        // Since the ChatActivity itself already implement RoomListener we can pass it as a target
        Room room = scaledrone.subscribe(roomName, ChatActivity.this, new SubscribeOptions(25));

        room.listenToHistoryEvents(new HistoryRoomListener() {
          @Override
          public void onHistoryMessage(Room room, com.scaledrone.lib.Message message) {
            final ObjectMapper mapper1 = new ObjectMapper();
            try {
              if (message.getMember() == null) {
                return;
              }
              final MemberData data1 = mapper1
                  .treeToValue(message.getMember().getClientData(), MemberData.class);
              final JsonNode jsonNode = mapper1
                  .readTree(String.valueOf(message.getData()));
              final Message messageSent = new Message(jsonNode.get("text").asText(), data1,
                  data1.getName().equals(data.getName()));

              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  messageAdapter.add(messageSent);
                  // scroll the ListView to the last added element
                  messagesView.setSelection(messagesView.getCount() - 1);
                }
              });
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
      }

      @Override
      public void onOpenFailure(Exception ex) {
        System.err.println(ex);
      }

      @Override
      public void onFailure(Exception ex) {
        System.err.println(ex);
      }

      @Override
      public void onClosed(String reason) {
        System.err.println(reason);
      }
    });
  }

  // Successfully connected to Scaledrone room
  @Override
  public void onOpen(Room room) {
    System.out.println("Connected to room");
  }

  // Connecting to Scaledrone room failed
  @Override
  public void onOpenFailure(Room room, Exception ex) {
    System.err.println(ex);
  }

  // Received a message from Scaledrone room
  @Override
  public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
    // To transform the raw JsonNode into a POJO we can use an ObjectMapper
    final ObjectMapper mapper = new ObjectMapper();
    try {
      // member.clientData is a MemberData object, let's parse it as such
      final MemberData data = mapper
          .treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
      final JsonNode jsonNode = mapper
          .readTree(String.valueOf(receivedMessage.getData()));

      // if the clientID of the message sender is the same as our's it was sent by us
      boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
      // since the message body is a simple string in our case we can use json.asText() to parse it as such
      // if it was instead an object we could use a similar pattern to data parsing
      final Message message = new Message(jsonNode.get("text").asText(), data,
          belongsToCurrentUser);
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          messageAdapter.add(message);
          //scroll the ListView to the last added element
          messagesView.setSelection(messagesView.getCount() - 1);
        }
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(View view) {
    String messageText = editText.getText().toString();
    messageText = BadWordFilter.getCensoredText(messageText,
        getApplicationContext(),
        getString(R.string.censored_chat));

    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    MemberData data = new MemberData(username, getRandomColor());

    Message message = new Message(messageText, data, true);
    if (messageText.length() > 0) {
      scaledrone.publish("observable-room", message);
      editText.getText().clear();
    }
  }

  private String getRandomColor() {
    Random r = new Random();
    StringBuffer sb = new StringBuffer("#");
    while (sb.length() < 7) {
      sb.append(Integer.toHexString(r.nextInt()));
    }
    return sb.toString().substring(0, 7);
  }

  private void playMusic() {
    int song = R.raw.smooth_jazz;
    HXMusic.music().load(song).gapless(true).looped(true).play(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    HXSound.clear();
    HXMusic.stop();
    HXMusic.clear();
  }
}


class MemberData {

  private String name;
  private String color;

  public MemberData(String name, String color) {
    this.name = name;
    this.color = color;
  }

  // Add an empty constructor so we can later parse JSON into MemberData using Jackson
  public MemberData() {
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }
}

// Code below was modified from the following reference:
//Scaledrone, “Android Chat Tutorial: Building A Realtime Messaging App,”
//    Scaledrone Blog, 05-Feb-2019. [Online]. Available:
//    https://www.scaledrone.com/blog/android-chat-tutorial/. [Accessed: 09-Apr-2021].

