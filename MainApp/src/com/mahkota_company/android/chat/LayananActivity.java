package com.mahkota_company.android.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.mahkota_company.android.R;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class LayananActivity extends Activity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    EditText username, password, host, port, service;
    String Username, Password;
    public static final String HOST = "xmpp.jp";
    public static final int PORT = 5222;
    public static final String SERVICE = "xmpp.jp";
    private final String SELECT_SQL = "SELECT * FROM LOGIN";
    private SQLiteDatabase db;
    private Cursor c;
    private ArrayList<String> messages = new ArrayList();
    private Handler mHandler = new Handler();
    private Spinner mRecipient;
    private EditText mSendText;
    private ListView mList;
    private XMPPConnection connection;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        Log.i("XMPPClient", "onCreate called");
        setContentView(R.layout.layanan);
        connect();

        openDatabase();
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToLast();

        username = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        host = (EditText) findViewById(R.id.host);
        port = (EditText) findViewById(R.id.port);
        service = (EditText) findViewById(R.id.service);

        //mList.setSelection(mList.getCount() - 1);
        mRecipient = (Spinner) this.findViewById(R.id.recipient);
        Log.i("XMPPClient", "mRecipient = " + mRecipient);
        mSendText = (EditText) this.findViewById(R.id.sendText);
        Log.i("XMPPClient", "mSendText = " + mSendText);
        mList = (ListView) this.findViewById(R.id.listMessages);
        Log.i("XMPPClient", "mList = " + mList);
        setListAdapter();

        showRecords();

        Username = username.getText().toString();
        Password = password.getText().toString();
        // Set a listener to send a chat text message
        Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String to = mRecipient.getSelectedItem().toString();
                String text = "cs." + mSendText.getText().toString();

                Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
                Message msg = new Message(to, Message.Type.chat);
                msg.setBody(text);
                connection.sendPacket(msg);
                messages.add(connection.getUser() + ":");
                messages.add(text);
                mSendText.setText("");
                setListAdapter();

            }
        });

        Button ceksal = (Button) this.findViewById(R.id.Ceksal);
        ceksal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String to = mRecipient.getSelectedItem().toString();
                String text = "sal";

                Log.i("LayananActivity", "Sending text [" + text + "] to [" + to + "]");
                Message msg = new Message(to, Message.Type.chat);
                msg.setBody(text);
                connection.sendPacket(msg);
                messages.add(connection.getUser() + ":");
                messages.add(text);
                mSendText.setText("");
                setListAdapter();
            }
        });
    }

    private void connect() {
        final ProgressDialog dialog = ProgressDialog.show(this,
                "Connecting...", "Please wait...", false);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a connection
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        HOST, PORT, SERVICE);
                XMPPConnection connection = new XMPPConnection(connConfig);

                try {
                    connection.connect();
                    Log.i("XMPPChatDemoActivity",
                            "Connected to " + connection.getHost());
                } catch (XMPPException ex) {
                    Log.e("XMPPChatDemoActivity", "Failed to connect to "
                            + connection.getHost());
                    Log.e("XMPPChatDemoActivity", ex.toString());
                    setConnection(null);
                }
                try {
                    // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                    connection.login(Username, Password);
                    Log.i("XMPPChatDemoActivity",
                            "Logged in as " + connection.getUser());

                    // Set the status to available
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnection(connection);

                    Roster roster = connection.getRoster();
                    Collection<RosterEntry> entries = roster.getEntries();
                    for (RosterEntry entry : entries) {
                        Log.d("XMPPChatDemoActivity",
                                "--------------------------------------");
                        Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
                        Log.d("XMPPChatDemoActivity",
                                "User: " + entry.getUser());
                        Log.d("XMPPChatDemoActivity",
                                "Name: " + entry.getName());
                        Log.d("XMPPChatDemoActivity",
                                "Status: " + entry.getStatus());
                        Log.d("XMPPChatDemoActivity",
                                "Type: " + entry.getType());
                        Presence entryPresence = roster.getPresence(entry
                                .getUser());

                        Log.d("XMPPChatDemoActivity", "Presence Status: "
                                + entryPresence.getStatus());
                        Log.d("XMPPChatDemoActivity", "Presence Type: "
                                + entryPresence.getType());
                        Presence.Type type = entryPresence.getType();
                        if (type == Presence.Type.available)
                            Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                        Log.d("XMPPChatDemoActivity", "Presence : "
                                + entryPresence);

                    }
                } catch (XMPPException ex) {
                    Log.e("XMPPChatDemoActivity", "Failed to log in as "
                            + Username);
                    Log.e("XMPPChatDemoActivity", ex.toString());
                    setConnection(null);
                }

                dialog.dismiss();
            }
        });
        t.start();
        dialog.show();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("login.db", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String name = c.getString(1);
        String pass = c.getString(2);
        String shost = c.getString(3);
        String sport = c.getString(4);
        String serv = c.getString(5);
        username.setText(name);
        password.setText(pass);
        host.setText(shost);
        port.setText(sport);
        service.setText(serv);

    }
    public void setConnection
            (XMPPConnection
                     connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        Log.i("LayananActivity", "Got text [" + message.getBody() + "] from [" + fromName + "]");
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
        }
    }

    private void setListAdapter
            () {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.multi_line_list_item,
                messages);
        mList.setAdapter(adapter);
        mList.setSelection(mList.getCount() - 1);
    }

}