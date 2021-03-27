package com.example.recordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button greenBtn, yellowBtn, redBtn, vistaBtn, confBtn;
    private EditText recText, xText, yText;
    private BufferedWriter bwriter;
    private int posX, posY, r, g,b;
    String datos;
    //ArrayList<Notes> conf; no funcionó
    private boolean confirmar, confvista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greenBtn = findViewById(R.id.greenBtn);
        yellowBtn = findViewById(R.id.yellowBtn);
        redBtn = findViewById(R.id.redBtn);
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        vistaBtn = findViewById(R.id.vistaBtn);
        confBtn = findViewById(R.id.confBtn);
        recText = findViewById(R.id.recText);

        //conf = new ArrayList<Notes>();


        greenBtn.setOnClickListener(
                v -> {
                    r= 50;
                    g=205;
                    b=50;
                }
        );
        yellowBtn.setOnClickListener(
                v -> {
                    r= 255;
                    g=255;
                    b=102;
                }
        );
        redBtn.setOnClickListener(
                v -> {
                    r= 178;
                    g=34;
                    b=34;
                }
        );
        vistaBtn.setOnClickListener(
                v -> {
                    //Para recibir los datos y volverlos de int a string
                    posX = Integer.parseInt(xText.getText().toString());
                    posY = Integer.parseInt(yText.getText().toString());
                    //normal porque ya es string
                    datos = recText.getText().toString();
                    confvista = true;
                    enviar();
                }
        );
        confBtn.setOnClickListener(
                v -> {
                    confirmar = true;
                    confvista = false;
                    enviar();
                }
        );
        new Thread(
                ()-> {
                    // Ponemos la IP del server y el puerto donde el servidor escucha
                    try {
                        Socket socket = new Socket("192.168.1.2", 5000);
                        InputStream is = socket.getInputStream();
                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        bwriter = new BufferedWriter(osw);

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
    }
    public void enviar() { //este es para la Vista previa
        Gson gson = new Gson();
        Notes bola = new Notes(posX, posY,r,g,b,40, datos, confirmar, confvista);
        String esto = gson.toJson(bola);

        new Thread(()-> {
            try {
                bwriter.write(esto+"\n");
                bwriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}