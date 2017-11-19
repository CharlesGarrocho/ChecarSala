package garrocho.checarsala;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    int botoes[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    AtividadeDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("[IFSP-CJO] Checar Sala");
        dao = new AtividadeDAO(getBaseContext());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        for (int i = 1; i <= 15; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            ((Button) findViewById(id)).setBackgroundResource(android.R.drawable.btn_default);
            ((Button) findViewById(id)).setEnabled(false);
        }
        ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.GONE);
        construirBotoes();
    }

    public void construirBotoes() {
        ArrayList<Atividade> atividades = dao.listar();
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        String hora = sdf.format(new Date());
        hora = "19";

        for (Atividade at : atividades) {
            if (at.getHorario().contains(hora)) {
                int id = getResources().getIdentifier("button" + at.getSala(), "id", getPackageName());
                ((Button) findViewById(id)).setEnabled(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        construirBotoes();
    }

    public void botoes(View view) {
        Button botao = (Button)view;
        String numero = getResources().getResourceEntryName(view.getId());
        numero = numero.substring(6);

        if (botao.getText().toString().equalsIgnoreCase("OK")) {
            botao.setBackgroundColor(Color.RED);
            botao.setText("NO");
            botoes[Integer.valueOf(numero)-1] = 2;
        }
        else if (botao.getText().toString().equalsIgnoreCase("NO")) {
            botao.setBackgroundResource(android.R.drawable.btn_default);
            botao.setText(numero);
            botoes[Integer.valueOf(numero)-1] = 0;
        }
        else {
            botao.setBackgroundColor(Color.GREEN);
            botao.setText("OK");
            botoes[Integer.valueOf(numero)-1] = 1;
        }
        analisar();
    }

    public void analisar() {
        boolean notificar=false;
        for (int i=0; i<15; i++) {
            if (botoes[i] == 2)
                notificar=true;
        }
        if (notificar)
            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.VISIBLE);
        else
            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.GONE);
    }

    public void notificar(View comp) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Email sender = new Email("email", "senha");
        try {
            sender.sendMail("[CAE] Sala Sem Atividade",
                    "Prezado Professor, a sala 1 está sem atividades",
                    "ctgarrocho@gmail.com", "CAE",
                    "ctgarrocho@gmail.com");
        }catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void configurar (View comp) {
        Intent intent = new Intent(this, ConfigurarActivity.class);
        startActivity(intent);
    }
}
