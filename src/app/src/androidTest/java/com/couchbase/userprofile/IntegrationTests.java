package com.couchbase.userprofile;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;
import com.couchbase.userprofile.util.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class IntegrationTests {

    private Context context;

    private static final String TEST_USER1 = "testUser1@demo.com";
    private static final String TEST_USER2 = "testUser2@demo.com";

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_IMAGE_DATA = "imageData";

    private static final String VALUE_NAME = "Bob Smith";
    private static final String VALUE_ADDRESS = "123 No Where";

    private static final String IMAGE_TYPE = "image/png";

    @Test
    public void getDatabase()
    {
        //arrange
        DatabaseManager databaseManager = getDatabaseManager(TEST_USER1);

        //act
        Database database =  DatabaseManager.getDatabase();

        //assert
        assertNotNull(database);

        cleanUp();
    }

    @Test
    public void saveProfile()
    {
        //arrange
        byte[] imageBytes = null;
        DatabaseManager databaseManager = getDatabaseManager(TEST_USER1);

        //act
        Database database = DatabaseManager.getDatabase();
        String docId = databaseManager.getCurrentUserDocId();

        Map<String, Object> profile = new HashMap<>();
        profile.put(FIELD_EMAIL, docId);
        profile.put(FIELD_ADDRESS, VALUE_ADDRESS);
        profile.put(FIELD_NAME, VALUE_NAME);

        //get bitmap image to save
        imageBytes = getImageBytes();
        assertNotNull(imageBytes);
        profile.put(FIELD_IMAGE_DATA, new com.couchbase.lite.Blob(IMAGE_TYPE, imageBytes));

        MutableDocument mutableDocument = new MutableDocument(docId, profile);

        //assert
        try{
            database.save(mutableDocument);
        } catch (CouchbaseLiteException e){
            assertTrue(false);
        }
    }

    @Test
    public void getProfileTestUser1()
    {
        //arrange
        saveProfile();

        DatabaseManager databaseManager = DatabaseManager.getSharedInstance();
        Database database = DatabaseManager.getDatabase();
        String docId = databaseManager.getCurrentUserDocId();

        //act
        Document document = database.getDocument(docId);

        Blob imageBlob = (Blob)document.getBlob(FIELD_IMAGE_DATA);
        byte[] fileBytes = getImageBytes();
        byte[] imageBytes = imageBlob.getContent();
        String email = document.getString(FIELD_EMAIL).replace("user::", "");
        String name = document.getString(FIELD_NAME);
        String address = document.getString(FIELD_ADDRESS);

        //assert
        assertNotNull(imageBlob);
        assertNotNull(imageBytes);

        assertArrayEquals(fileBytes, imageBytes);
        assertEquals(VALUE_NAME, name);
        assertEquals(VALUE_ADDRESS, address);
        assertEquals(TEST_USER1, email);

        cleanUp();
    }

    @Test
    public void getProfileTestUser2()
    {
        //arrange
        saveProfile();

        DatabaseManager databaseManager = DatabaseManager.getSharedInstance();
        Database database = DatabaseManager.getDatabase();
        String docId = TEST_USER2;

        //act
        Document document = database.getDocument(docId);

        //assert
        assertNull(document);

        cleanUp();
    }

    private void cleanUp()
    {
        try{
            //get pointers to database
            DatabaseManager instance = DatabaseManager.getSharedInstance();
            Database database = DatabaseManager.getDatabase();

            //get info to delete database with
            String name = database.getName();
            File path = new File(database.getPath().replace(("/" + name + ".cblite2"), ""));

            //remove database
            database.close();
            Database.delete(name, path);

            //set user back to null
            instance.currentUser = null;

        } catch (CouchbaseLiteException e){
        }
    }

    private DatabaseManager getDatabaseManager(String databaseName)
    {
        //arrange
        context = ApplicationProvider.getApplicationContext();
        DatabaseManager instance = DatabaseManager.getSharedInstance();
        instance.initCouchbaseLite(context);
        instance.openOrCreateDatabaseForUser(context, databaseName);

        return instance;
    }

    private byte[] getImageBytes()
    {
        //arrange
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

