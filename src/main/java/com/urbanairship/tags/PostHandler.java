package com.urbanairship.tags;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class PostHandler implements HttpHandler {
    // processes POST request

    // get instance of Users
    Users users = Users.getInstance();

    String response;
    int responseCode = 200;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // parse request
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query);

        // send response
        httpExchange.sendResponseHeaders(responseCode, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void parseQuery(String query) {

        if (query != null) {

            // parse JSON
            final JSONObject obj = new JSONObject(query);

            String userName = obj.getString("user");

            Set<String> tagsToAdd = new HashSet<>();
            for (Object tag : obj.getJSONArray("add")) {
                tagsToAdd.add(tag.toString());
            }

            Set<String> tagsToRemove = new HashSet<>();
            for (Object tag : obj.getJSONArray("remove")) {
                tagsToRemove.add(tag.toString());
            }

            users.update(userName, tagsToAdd, tagsToRemove);
            response = users.getResponse(userName);

        } else {
            response = getErrorJSON("query is empty");
            responseCode = 400;
        }
    }

    private String getErrorJSON(String error) {
        return new JSONObject()
                .put("error", error)
                .toString()
                + "\n";
    }

}
