package com.familymap.family_map.net;

import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Person;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import RequestResult.EventIDResult;
import RequestResult.EventResult;
import RequestResult.LoginRequest;
import RequestResult.PersonEventRequest;
import RequestResult.PersonIDRequest;
import RequestResult.PersonIDResult;
import RequestResult.PersonResult;
import RequestResult.RegisterRequest;
import RequestResult.UserResult;

/*
	The Client class shows how to call a web API operation from
	a Java program.
*/
public class ServerProxy {
    public static String serverHost;
    public static String serverPort;

    public Person login(LoginRequest r) {
        String jsonStr = EncoDecode.serialize(r);
        String respData = sendRequest("POST", true, jsonStr, "/user/login", null);
        if (respData == null || respData.contains("ERROR")) {
            return new Person(null, null, "Error, login failed!",
                    null, null, null, null, null);
        }
        UserResult uR = (UserResult) EncoDecode.deserialize(respData, UserResult.class);
        String authtoken = uR.getAuthtoken();
        String personID = uR.getPersonID();
        PersonIDRequest getUserInfo = new PersonIDRequest(personID, authtoken);
        jsonStr = EncoDecode.serialize(getUserInfo);
        respData = sendRequest("GET", false, jsonStr, "/person/" + personID, authtoken);
        PersonIDResult userPID = (PersonIDResult) EncoDecode.deserialize(respData, PersonIDResult.class);
        Person user = new Person(userPID.getFirstName(), userPID.getLastName(), userPID.getFirstName() + " " + userPID.getLastName()
                + " logged in!", userPID.getGender(), userPID.getPersonID(), userPID.getMotherID(), userPID.getFatherID(), userPID.getFatherID());
        if (user.getFirstName() != null) {
            DataCache.clear();
            DataCache.setUser(user);
        }
        PersonEventRequest getPeople = new PersonEventRequest(authtoken);
        jsonStr = EncoDecode.serialize(getPeople);
        respData = sendRequest("GET", false, jsonStr, "/person", authtoken);
        PersonResult people = (PersonResult) EncoDecode.deserialize(respData, PersonResult.class);
        PersonIDResult[] personas = new PersonIDResult[people.getData().length];
        for (int j = 0; j < personas.length; ++j) {
            jsonStr = EncoDecode.serialize(people.getData()[j]);
            PersonIDResult pID = (PersonIDResult) EncoDecode.deserialize(jsonStr, PersonIDResult.class);
            personas[j] = pID;
        }
        DataCache.setPeople(personas);
        PersonEventRequest getEvents = new PersonEventRequest(authtoken);
        jsonStr = EncoDecode.serialize(getEvents);
        respData = sendRequest("GET", false, jsonStr, "/event", authtoken);
        EventResult events = (EventResult) EncoDecode.deserialize(respData, EventResult.class);
        EventIDResult[] eventos = new EventIDResult[events.getData().length];
        for (int j = 0; j < eventos.length; ++j) {
            jsonStr = EncoDecode.serialize(events.getData()[j]);
            EventIDResult eID = (EventIDResult) EncoDecode.deserialize(jsonStr, EventIDResult.class);
            eventos[j] = eID;
        }
        DataCache.setEvents(eventos);
        DataCache.endSync();
        return user;
    }

    public Person register(RegisterRequest r) {
        String jsonStr = EncoDecode.serialize(r);
        String respData = sendRequest("POST", true, jsonStr,"/user/register", null);
        if (respData == null || respData.contains("ERROR")) {
            return new Person(null, null, "Error, register failed!",
                    null, null, null, null, null);
        }
        UserResult uR = (UserResult) EncoDecode.deserialize(respData, UserResult.class);
        String authtoken = uR.getAuthtoken();
        String personID = uR.getPersonID();
        PersonIDRequest getUserInfo = new PersonIDRequest(personID, authtoken);
        jsonStr = EncoDecode.serialize(getUserInfo);
        respData = sendRequest("GET", false, jsonStr, "/person/" + personID, authtoken);
        PersonIDResult userPID = (PersonIDResult) EncoDecode.deserialize(respData, PersonIDResult.class);
        Person user = new Person(userPID.getFirstName(), userPID.getLastName(), userPID.getFirstName() + " " + userPID.getLastName()
                + " registered!", userPID.getGender(), userPID.getPersonID(), userPID.getMotherID(), userPID.getFatherID(), userPID.getSpouseID());
        if (user.getFirstName() != null) {
            DataCache.clear();
            DataCache.setUser(user);
        }
        PersonResult people = (PersonResult) EncoDecode.deserialize(respData, PersonResult.class);
        PersonIDResult[] personas = new PersonIDResult[people.getData().length];
        for (int j = 0; j < personas.length; ++j) {
            jsonStr = EncoDecode.serialize(people.getData()[j]);
            PersonIDResult pID = (PersonIDResult) EncoDecode.deserialize(jsonStr, PersonIDResult.class);
            personas[j] = pID;
        }
        DataCache.setPeople(personas);
        PersonEventRequest getEvents = new PersonEventRequest(authtoken);
        jsonStr = EncoDecode.serialize(getEvents);
        respData = sendRequest("GET", false, jsonStr, "/event", authtoken);
        EventResult events = (EventResult) EncoDecode.deserialize(respData, EventResult.class);
        EventIDResult[] eventos = new EventIDResult[events.getData().length];
        for (int j = 0; j < eventos.length; ++j) {
            jsonStr = EncoDecode.serialize(events.getData()[j]);
            EventIDResult eID = (EventIDResult) EncoDecode.deserialize(jsonStr, EventIDResult.class);
            eventos[j] = eID;
        }
        DataCache.setEvents(eventos);
        DataCache.endSync();
        return user;
    }

    private static String sendRequest(String reqMethod, boolean doOut, String jsonStr, String extension, String authtoken) {

        // This method shows how to send a POST request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + extension);

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod(reqMethod);
            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(doOut);	// There is a request body

            // Add an auth token to the request in the HTTP "Authorization" header
            if (authtoken != null) {
                http.addRequestProperty("Authorization", authtoken);
            }

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");
            // Connect to the server and send the HTTP request
            http.connect();
            if (doOut) {
                // Get the output stream containing the HTTP request body
                OutputStream reqBody = http.getOutputStream();
                // Write the JSON data to the request body
                writeString(jsonStr, reqBody);
                // Close the request body output stream, indicating that the
                // request is complete
                reqBody.close();
            }
            String respData;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                respData = readString(respBody);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                respData = ("ERROR: " + http.getResponseMessage());
            }
            return respData;
        }
        catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}