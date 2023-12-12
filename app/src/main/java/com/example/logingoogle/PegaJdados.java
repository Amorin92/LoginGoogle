package com.example.logingoogle;

import android.content.Context;

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

public class PegaJdados extends AsyncTask<String, Void, ArrayList<Creature>> {

    private ArrayList<Creature> creatureList = new ArrayList<>();
    private OnDataReadyListener onDataReadyListener;

    public interface OnDataReadyListener {
        void onDataReady(ArrayList<Creature> creatureList);
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
                    creatureList = getDados(texto);
                    return creatureList;
                } else {
                    // Adicione aqui o tratamento para resposta não OK (erro HTTP)
                    Creature errorCreature = new Creature();
                    errorCreature.setErrorCode(httpURLConnection.getResponseCode());
                    errorCreature.setErrorMessage("Resposta vazia da API");
                    creatureList.add(errorCreature);
                    return creatureList;
                }
            } else {
                // Adicione aqui o tratamento para resposta não OK (erro HTTP)
                Creature errorCreature = new Creature();
                errorCreature.setErrorCode(httpURLConnection.getResponseCode());
                errorCreature.setErrorMessage("Erro HTTP: " + httpURLConnection.getResponseMessage());
                creatureList.add(errorCreature);
                return creatureList;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Adicione aqui o tratamento para exceções de URL mal formada
            Creature errorCreature = new Creature();
            errorCreature.setErrorCode(500); // Código para erro interno do servidor
            errorCreature.setErrorMessage("Erro de URL mal formada: " + e.getMessage());
            creatureList.add(errorCreature);
            return creatureList;
        } catch (IOException e) {
            e.printStackTrace();
            // Adicione aqui o tratamento para outras exceções de E/S
            Creature errorCreature = new Creature();
            errorCreature.setErrorCode(500); // Código para erro interno do servidor
            errorCreature.setErrorMessage("Erro de E/S: " + e.getMessage());
            creatureList.add(errorCreature);
            return creatureList;
        } catch (JSONException e) {
            e.printStackTrace();
            // Adicione aqui o tratamento para exceções de JSON
            Creature errorCreature = new Creature();
            errorCreature.setErrorCode(500); // Código para erro interno do servidor
            errorCreature.setErrorMessage("Erro de JSON: " + e.getMessage());
            creatureList.add(errorCreature);
            return creatureList;
        }
    }


    @Override
    protected void onPostExecute(ArrayList<Creature> creatureList) {
        Log.e("onDataReady", "onDataReady called");
        if (onDataReadyListener != null) {
            onDataReadyListener.onDataReady(creatureList);
        }
    }

    private ArrayList<Creature> getDados(String texto) throws JSONException {
        JSONObject jsonObject = new JSONObject(texto);

        if (jsonObject.has("creature")) {
            // Caso a resposta contenha uma única criatura
            Creature creature = new Creature();
            creature.setName(jsonObject.getJSONObject("creature").getString("name"));
            creature.setRace(jsonObject.getJSONObject("creature").getString("race"));
            creature.setImageUrl(jsonObject.getJSONObject("creature").getString("image_url"));
            creatureList.add(creature);
        } else if (jsonObject.has("creatures")) {
            // Caso a resposta contenha uma lista de criaturas
            JSONArray creatureArray = jsonObject.getJSONObject("creatures").getJSONArray("creature_list");
            for (int i = 0; i < creatureArray.length(); i++) {
                JSONObject creatureObject = creatureArray.getJSONObject(i);

                Creature creature = new Creature();
                creature.setName(creatureObject.getString("name"));
                creature.setRace(creatureObject.getString("race"));
                creature.setImageUrl(creatureObject.getString("image_url"));

                creatureList.add(creature);
            }
        }

        return creatureList;
    }



}