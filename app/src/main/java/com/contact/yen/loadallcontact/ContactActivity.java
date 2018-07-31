package com.contact.yen.loadallcontact;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    public static final String URI = "content://contacts/people";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initViews();
    }

    private void initViews() {
        List<Contact> contacts = getContacts(URI);
        if (contacts != null) {
            updateRecyclerContact(contacts);
        } else {
            Toast.makeText(this, "Contact is empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param contacts
     */
    public void updateRecyclerContact(List<Contact> contacts) {
        ListView listViewContact = findViewById(R.id.list_contact);
        ArrayAdapter adapter =
                new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, contacts);
        listViewContact.setAdapter(adapter);
    }


    /**
     * @param uriPath : Current path
     * @return : List contact in current path
     */
    public List<Contact> getContacts(String uriPath) {
        if (uriPath == null) {
            return null;
        }
        List<Contact> result = new ArrayList<>();
        Uri uri = Uri.parse(uriPath);

        CursorLoader loader = new CursorLoader(this, uri, null, null, null, null);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = getContact(cursor);
            if (contact != null) {
                result.add(contact);
            }
        }
        cursor.close();
        return result;
    }

    /**
     * get Contact form cursor
     *
     * @param cursor : Current cursor
     * @return : Contact
     */
    public Contact getContact(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Contact contact = null;
        int indexID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        if (indexID != -1 && indexName != -1) {
            int id = cursor.getInt(indexID);
            String name = cursor.getString(indexName).toString();
            contact = new Contact(id, name);
        }
        cursor.moveToNext();
        return contact;
    }


}
