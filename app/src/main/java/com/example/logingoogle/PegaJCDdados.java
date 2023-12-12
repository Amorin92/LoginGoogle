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

public class PegaJCDdados extends AsyncTask<String, Void, ArrayList<Creature>> {

    private ArrayList<Creature> boostedCreatures = new ArrayList<>();
    private OnDataReadyListener onDataReadyListener;

    public interface OnDataReadyListener {
        void onDataReady(ArrayList<Creature> boostedCreatures);
    }

    public void setOnDataReadyListener(OnDataReadyListener listener) {
        this.onDataReadyListener = listener;
    }

    @Override
    protected ArrayList<Creature> doInBackground(String... params) {
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
                    boostedCreatures = getDados(texto);
                    return boostedCreatures;
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
        return boostedCreatures;
    }

    @Override
    protected void onPostExecute(ArrayList<Creature> creatureList) {
        Log.e("onDataReady", "onDataReady called");
        if (onDataReadyListener != null) {
            onDataReadyListener.onDataReady(boostedCreatures);
        }
    }

    private ArrayList<Creature> getDados(String texto) throws JSONException {
        ArrayList<Creature> creatures = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(texto);

        if (jsonObject.has("creatures")) {
            JSONObject creaturesObject = jsonObject.getJSONObject("creatures");

            // Processar o chefe em destaque (se existir)
            if (creaturesObject.has("boosted")) {
                JSONObject boosted = creaturesObject.getJSONObject("boosted");
                Creature boostedCreature = new Creature();
                boostedCreature.setName(boosted.getString("name"));
                boostedCreature.setRace(boosted.getString("race"));
                boostedCreature.setImageUrl(boosted.getString("image_url"));
                creatures.add(boostedCreature);
            }
        }

        return creatures;
    }

}
