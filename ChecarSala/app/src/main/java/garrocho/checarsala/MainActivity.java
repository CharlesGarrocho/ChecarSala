package garrocho.checarsala;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    int botoes[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    AtividadeDAO dao;
    HashMap<String, String> coordenadores = new HashMap<String, String>();
    HashMap<String, String> semana = new HashMap<String, String>();
    ArrayList<Atividade> atividades = new ArrayList<Atividade>();
    String diaS, mes, dia, hora;
    static boolean modificado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("[IFSP] Checar Sala");
        dao = new AtividadeDAO(getBaseContext());

        semana.put("Monday", "segunda");
        semana.put("Tuesday", "terca");
        semana.put("Wednesday", "quarta");
        semana.put("Thursday", "quinta");
        semana.put("Friday", "sexta");

        semana.put("segunda", "segunda");
        semana.put("terça", "terca");
        semana.put("quarta", "quarta");
        semana.put("quinta", "quinta");
        semana.put("sexta", "sexta");

        coordenadores.put("matematica", "Maycon Godoi9898ctgar@gmail.com");
        coordenadores.put("pedagogia", "Walas Oliveira9898checarsala@gmail.com");
        coordenadores.put("tads", "Paulo Zeferino9898checarsala@gmail.com");

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
        if (!modificado) {
            atividades = dao.listar();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE-dd-mm-hh");
            String[] data = sdf.format(new Date()).split("-");

            diaS = data[0];
            dia = data[1];
            mes = data[2];
            hora = data[3];
            hora = "19";

            diaS = semana.get(diaS);

            for (int i = 1; i <= 15; i++) {
                int id = getResources().getIdentifier("button" + i, "id", getPackageName());
                ((Button) findViewById(id)).setBackgroundResource(android.R.drawable.btn_default);
                ((Button) findViewById(id)).setText("" + i);
                ((Button) findViewById(id)).setEnabled(false);
            }

            for (Atividade at : atividades) {
                Log.d("CHECAR", at.getDia()+ "-" + diaS);
                if (at.getDia().equalsIgnoreCase(diaS) && at.getHorario().contains(hora)) {
                    int id = getResources().getIdentifier("button" + at.getSala(), "id", getPackageName());
                    ((Button) findViewById(id)).setEnabled(true);
                }
            }
            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.GONE);
            modificado = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("[IFSP] Checar Sala");
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
        for (int i = 1; i <= 15; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            ((Button) findViewById(id)).setEnabled(false);
        }
        ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.botao_configurar)).setVisibility(View.GONE);
        Toast.makeText(this, "Notificando os Coordenadores...", Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final Email sender = new Email("checarsala@gmail.com", "senha");
        for (int i=0; i<15; i++) {
            if (botoes[i] == 2) {
                for (Atividade at : atividades) {
                    if (at.getSala()-1 == i) {
                        final String[] inf = coordenadores.get(at.getCurso().toLowerCase()).split("9898");

                        final int sala = i+1;

                        new AsyncTask<Void, Void, Void>() {
                            @Override public Void doInBackground(Void... arg) {
                                int resposta =-1;

                                try {
                                    resposta = sender.sendMail("[CAE] Sala Sem Atividade",
                                            "Prezado Coordenador " + inf[0]+", a sala " + sala + " está sem atividades neste momento. Não Responda a este email.",
                                            "checarsala@gmail.com", "[IFSP-CJO] CAE",
                                            "checarsala@gmail.com");
                                }catch (Exception error) {
                                    resposta = 3;
                                    error.printStackTrace();
                                }
                                if (resposta == 0) {
                                    runOnUiThread(new Runnable(){

                                        @Override
                                        public void run(){
                                            Toast.makeText(MainActivity.this, "Sala " + sala + ": " + inf[0] + " notificado", Toast.LENGTH_SHORT).show();
                                            habilitarBotoes();
                                            int id = getResources().getIdentifier("button" + sala, "id", getPackageName());
                                            ((Button) findViewById(id)).setBackgroundResource(android.R.drawable.btn_default);
                                            ((Button) findViewById(id)).setText(""+sala);
                                            ((Button) findViewById(id)).setEnabled(false);
                                            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.GONE);
                                            ((Button) findViewById(R.id.botao_configurar)).setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                                else if (resposta==1){
                                    runOnUiThread(new Runnable(){

                                        @Override
                                        public void run(){
                                            Toast.makeText(MainActivity.this, "Sem Conexão Com a Internet", Toast.LENGTH_SHORT).show();
                                            habilitarBotoes();
                                            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.VISIBLE);
                                            ((Button) findViewById(R.id.botao_configurar)).setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                                else {
                                    runOnUiThread(new Runnable(){

                                        @Override
                                        public void run(){
                                            Toast.makeText(MainActivity.this, "Erro Desconhecido", Toast.LENGTH_SHORT).show();
                                            habilitarBotoes();
                                            ((Button) findViewById(R.id.botao_notificar)).setVisibility(View.VISIBLE);
                                            ((Button) findViewById(R.id.botao_configurar)).setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                                return null;}
                        }.execute();

                    }
                }
            }
        }
    }

    public void habilitarBotoes() {
        for (Atividade at : atividades) {
            if (at.getDia().equalsIgnoreCase(diaS) && at.getHorario().contains(hora)) {
                int id = getResources().getIdentifier("button" + at.getSala(), "id", getPackageName());
                ((Button) findViewById(id)).setEnabled(true);
            }
        }
    }

    public void configurar (View comp) {
        NotificacaoChecarSala notificacao = new NotificacaoChecarSala();
        notificacao.notificar(MainActivity.this,"Ola Mundo", 1);
        Intent intent = new Intent(this, ConfigurarActivity.class);
        startActivity(intent);
    }
}
