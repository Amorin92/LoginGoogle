package com.example.logingoogle;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class PegaBdados extends AsyncTask<String, Void, ArrayList<Bosses>> {

    private ArrayList<Bosses> bossList = new ArrayList<>();
    private OnDataReadyListener onDataReadyListener;

    public interface OnDataReadyListener {
        void onDataReady(ArrayList<Bosses> bossList);
    }

    public void setOnDataReadyListener(OnDataReadyListener listener) {
        this.onDataReadyListener = listener;
    }

    @Override
    protected ArrayList<Bosses> doInBackground(String... params) {
        String urlString = params[0];
        URL url;

        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resposta = httpURLConnection.getInputStream();
                String texto = new Scanner(resposta).useDelimiter("\\A").next();
                Log.e("Resposta da API:", texto);
                if (texto != null) {
                    bossList = getDados(texto);
                    return bossList;
                } else {
                    return null;
                }
            } else {
                // Adicione aqui o tratamento para resposta não OK (erro HTTP)
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bossList;
    }

    @Override
    protected void onPostExecute(ArrayList<Bosses> creatureList) {
        Log.e("onDataReady", "onDataReady called");
        if (onDataReadyListener != null) {
            onDataReadyListener.onDataReady(bossList);
        }
    }

    private ArrayList<Bosses> getDados(String texto) throws JSONException {
        JSONObject jsonObject = new JSONObject(texto);

        if (jsonObject.has("boostable_bosses")) {
            JSONObject boostableBosses = jsonObject.getJSONObject("boostable_bosses");

            // Processar o chefe em destaque (se existir)
            if (boostableBosses.has("boosted")) {
                JSONObject boosted = boostableBosses.getJSONObject("boosted");
                Bosses boostedBoss = new Bosses();
                boostedBoss.setName(boosted.getString("name"));
                boostedBoss.setImageUrl(boosted.getString("image_url"));
                bossList.add(boostedBoss);
            }

            // Processar a lista de chefes boostable
            if (boostableBosses.has("boostable_boss_list")) {
                JSONArray bossArray = boostableBosses.getJSONArray("boostable_boss_list");
                for (int i = 0; i < bossArray.length(); i++) {
                    JSONObject creatureObject = bossArray.getJSONObject(i);

                    Bosses bosses = new Bosses();
                    bosses.setName(creatureObject.getString("name"));
                    bosses.setImageUrl(creatureObject.getString("image_url"));

                    bossList.add(bosses);
                }
            }
        }

        return bossList;
    }

}
