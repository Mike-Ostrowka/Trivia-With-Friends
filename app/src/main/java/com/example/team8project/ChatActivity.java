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

    // use username in MemberData object
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    MemberData data = new MemberData(username, getRandomColor());

    // if not join the global chat room retrieve the channel ID of the desired chat room
    String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
    if (!(sessionId == null)) {
      channelID = sessionId;
    }

    // open up a new scaledrone chat room of the desired channel ID using your own credentials
    scaledrone = new Scaledrone(channelID, data);
    scaledrone.connect(new Listener() {
      @Override
      public void onOpen() {
        System.out.println("Scaledrone connection open");
        // Since the ChatActivity itself already implement RoomListener we can pass it as a target
        Room room = scaledrone.subscribe(roomName, ChatActivity.this, new SubscribeOptions(25));

        // History Room is a beta feature of scaledrone, should retrieve all messages submitted
        // to scaledrone using the respective channel key
        room.listenToHistoryEvents(new HistoryRoomListener() {
          @Override
          public void onHistoryMessage(Room room, com.scaledrone.lib.Message message) {
            final ObjectMapper mapper1 = new ObjectMapper();
            try {
              // bugs in scaledrone do not always allow messages to be
              // read so return from this function if bugs present
              if (message.getMember() == null) {
                return;
              }
              // message.getMember().clientData is a MemberData object, let's parse it as such
              final MemberData data1 = mapper1
                  .treeToValue(message.getMember().getClientData(), MemberData.class);

              // message.getData() is a JsonNode of the Member object, parse it as such
              final JsonNode jsonNode = mapper1
                  .readTree(String.valueOf(message.getData()));

              // construct a Message object from the scaledrone library stored messages
              final Message messageSent = new Message(jsonNode.get("text").asText(), data1,
                  data1.getName().equals(data.getName()));

              // run stored messages on UI thread
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  messageAdapter.add(messageSent);
                  // scroll the ListView to the last added element
                  messagesView.setSelection(messagesView.getCount() - 1);
                }
              });
            } catch (JsonProcessingException e) {
              e.printStackTrace(); // catch for ObjectMapper
            } catch (IOException e) {
              e.printStackTrace(); // catch for JsonNode
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

      // receivedMessage.getData is a JsonNode of the Message object, parse it as such
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

  // send a message as a Message object
  public void sendMessage(View view) {
    String messageText = editText.getText().toString();

    // filter the message input from the user
    messageText = BadWordFilter.getCensoredText(messageText,
        getApplicationContext(),
        getString(R.string.censored_chat));

    // retrieve username of sending user to add to Json node
    loginPreferences session = new loginPreferences(getApplicationContext());
    String username = session.getusername();
    MemberData data = new MemberData(username, getRandomColor());

    // create new Message object with associated text, MemberData, and tell message a
    // dapter it was sent by the current user
    Message message = new Message(messageText, data, true);
    if (messageText.length() > 0) {
      // publish the roomName and Message obejct to scaldrone library as a Json Node object
      scaledrone.publish("observable-room", message);
      editText.getText().clear();
    }
  }

  // get a random color using hex values
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

// Member Data class record by library, holds username and color
class MemberData {
  private String name;
  private String color;

  // constrcutor
  public MemberData(String name, String color) {
    this.name = name;
    this.color = color;
  }

  // Add an empty constructor so we can later parse JSON into MemberData using Jackson
  public MemberData() {
  }

  // getter functions
  public String getName() {
    return name;
  }
  public String getColor() {
    return color;
  }
}

// Code above was modified from the following reference:
//Scaledrone, “Android Chat Tutorial: Building A Realtime Messaging App,”
//    Scaledrone Blog, 05-Feb-2019. [Online]. Available:
//    https://www.scaledrone.com/blog/android-chat-tutorial/. [Accessed: 09-Apr-2021].

