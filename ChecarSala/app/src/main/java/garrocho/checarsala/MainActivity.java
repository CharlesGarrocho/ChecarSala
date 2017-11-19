package garrocho.checarsala;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int botoes[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        for (int i=1; i<=15; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            ((Button)findViewById(id)).setBackgroundResource(android.R.drawable.btn_default);
        }
        ((Button)findViewById(R.id.botao_notificar)).setEnabled(false);
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
            ((Button)findViewById(R.id.botao_notificar)).setEnabled(true);
        else
            ((Button)findViewById(R.id.botao_notificar)).setEnabled(false);
    }

    public void configurar (View comp) {
        Intent intent = new Intent(this, ConfigurarActivity.class);
        startActivity(intent);
    }
}
