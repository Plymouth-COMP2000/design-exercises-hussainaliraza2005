package com.example.comp2000restuarantapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseworkApi {

    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";
    private static final String STUDENT_ID = "10929632";

    private VolleySingleton volley;

    public CourseworkApi(Context context) {
        this.volley = VolleySingleton.getInstance(context);
    }

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    // POST create_student/{student_id}
    public void createStudentDatabase(final ApiCallback<String> cb) {
        String url = BASE_URL + "create_student/" + STUDENT_ID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.has("message") ? response.getString("message") : "Success";
                            cb.onSuccess(message);
                        } catch (JSONException e) {
                             cb.onSuccess("Student database action completed.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }

    // POST create_user/{student_id}
    public void createUser(User user, final ApiCallback<String> cb) {
        String url = BASE_URL + "create_user/" + STUDENT_ID;

        JSONObject jsonBody = userToJson(user);
        if (jsonBody == null) {
            cb.onError("Failed to create JSON body");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cb.onSuccess("User created successfully.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }

    // GET read_all_users/{student_id}
    public void readAllUsers(final ApiCallback<List<User>> cb) {
        String url = BASE_URL + "read_all_users/" + STUDENT_ID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<User> userList = new ArrayList<>();
                            if (response.has("users")) {
                                JSONArray array = response.getJSONArray("users");
                                for (int i = 0; i < array.length(); i++) {
                                    User u = jsonToUser(array.getJSONObject(i));
                                    userList.add(u);
                                }
                            }
                            cb.onSuccess(userList);
                        } catch (JSONException e) {
                            cb.onError("JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }

    // GET read_user/{student_id}/{username}
    public void readUser(String username, final ApiCallback<User> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "read_user/" + STUDENT_ID + "/" + encodedUsername;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // The parsing logic is handled in the jsonToUser helper method
                            User u = jsonToUser(response);
                            cb.onSuccess(u);
                        } catch (JSONException e) {
                            cb.onError("JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }

    // PUT update_user/{student_id}/{username}
    public void updateUser(String username, User user, final ApiCallback<User> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "update_user/" + STUDENT_ID + "/" + encodedUsername;

        JSONObject jsonBody = userToJson(user);
        if (jsonBody == null) {
            cb.onError("Failed to create JSON body");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User u = jsonToUser(response);
                            cb.onSuccess(u);
                        } catch (JSONException e) {
                            cb.onSuccess(user);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }

    // DELETE delete_user/{student_id}/{username}
    public void deleteUser(String username, final ApiCallback<String> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "delete_user/" + STUDENT_ID + "/" + encodedUsername;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cb.onSuccess("User deleted successfully.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cb.onError(parseVolleyError(error));
                    }
                });

        volley.addToRequestQueue(request);
    }


    // --- Helper Methods ---

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private JSONObject userToJson(User user) {
        try {
            JSONObject json = new JSONObject();
            json.put("username", user.getUsername());
            json.put("password", user.getPassword());
            json.put("firstname", user.getFirstname());
            json.put("lastname", user.getLastname());
            json.put("email", user.getEmail());
            json.put("contact", user.getContact());
            json.put("usertype", user.getUserType());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User jsonToUser(JSONObject json) throws JSONException {
        // Prefer parsing the nested "user" object if it exists.
        JSONObject userJson = json.has("user") ? json.getJSONObject("user") : json;
        
        User user = new User();
        user.setUsername(userJson.optString("username", ""));
        user.setPassword(userJson.optString("password", ""));
        user.setFirstname(userJson.optString("firstname", ""));
        user.setLastname(userJson.optString("lastname", ""));
        user.setEmail(userJson.optString("email", ""));
        user.setContact(userJson.optString("contact", ""));
        user.setUserType(userJson.optString("usertype", ""));
        return user;
    }

    private String parseVolleyError(VolleyError error) {
        if (error == null) return "Unknown error";
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            String responseBody = new String(response.data, StandardCharsets.UTF_8);
            return "Status: " + response.statusCode + ", Body: " + responseBody;
        }
        return error.getMessage() != null ? error.getMessage() : "Network error occurred";
    }
}