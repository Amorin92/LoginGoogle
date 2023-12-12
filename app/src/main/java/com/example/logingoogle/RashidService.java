package com.example.logingoogle;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RashidService extends AsyncTask<Void, Void, String> {

    private TextView textView;

    public RashidService(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Configuração da URL da API
            String apiUrl = "https://api.tibialabs.com/v2/rashid/city";
            URL url = new URL(apiUrl);

            // Abre a conexão
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                // Lê a resposta da API
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } finally {
                // Fecha a conexão
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            return null; // Handle errors appropriately
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Atualiza o TextView com a cidade de Rashid
        if (result != null) {
            textView.setText(result);
        } else {
            textView.setText("Failed to retrieve Rashid's location.");
        }
    }
}
