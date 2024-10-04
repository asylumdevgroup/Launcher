/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class PastebinPoster {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final String MCLGS_URL = "https://api.mclo.gs/1/log";

    public static void paste(String code, PasteCallback callback) {
        PasteProcessor processor = new PasteProcessor(code, callback);
        Thread thread = new Thread(processor);
        thread.start();
    }

    public static interface PasteCallback {
        public void handleSuccess(String url);
        public void handleError(String err);
    }
    
    private static class PasteProcessor implements Runnable {
        private String code;
        private PasteCallback callback;
        
        public PasteProcessor(String code, PasteCallback callback) {
            this.code = code;
            this.callback = callback;
        }
        
        @Override
        public void run() {
            HttpURLConnection conn = null;
            OutputStream out = null; 
            InputStream in = null;
            
            try {
                URL url = new URL(MCLGS_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setInstanceFollowRedirects(false);
                conn.setDoOutput(true);
                out = conn.getOutputStream();
                
                out.write(("content=" + URLEncoder.encode(code, "utf-8")).getBytes());
                out.flush();
                out.close();
                
                if (conn.getResponseCode() == 200) {     
                    in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    // Handle JSON response
                    String result = response.toString();
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.getBoolean("success")) {
                        String url2 = jsonResponse.getString("url");
                        callback.handleSuccess(url2);
                    } else {
                        String error = jsonResponse.getString("error");
                        callback.handleError(error);
                    }
                } else {
                    callback.handleError("An error occurred while uploading the text.");
                }
            } catch (IOException | JSONException e) {
                callback.handleError(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        
    }
    
}
