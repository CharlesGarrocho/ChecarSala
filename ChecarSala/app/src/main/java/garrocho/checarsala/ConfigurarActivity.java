package garrocho.checarsala;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ConfigurarActivity extends AppCompatActivity {

    AtividadeDAO dao;
    ListView listaAtividades;
    ArrayList<Atividade> atividades = new ArrayList<Atividade>();
    Spinner spSalas, spHor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spSalas = (Spinner) findViewById(R.id.spinner_sala);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.salas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSalas.setAdapter(adapter);

        spHor = (Spinner) findViewById(R.id.spinner_horario);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.horarios, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHor.setAdapter(adapter2);

        dao = new AtividadeDAO(getBaseContext());
        atualizarLista();
    }

    public void atualizarLista() {
        atividades = dao.listar();
        ArrayList<String> ats = new ArrayList<String>();

        for (Atividade at : atividades)
            ats.add(at.getSala() + "56765" + at.getDescricao() + "56765" + at.getHorario());

        listaAtividades = (ListView)findViewById(R.id.lista_atividades_view);
        AdaptadorAtividades adaptador = new AdaptadorAtividades(this, ats);
        listaAtividades.setAdapter(adaptador);
    }

    public void adicionar(View comp) {
        String sala = spSalas.getSelectedItem().toString();
        String hora = spHor.getSelectedItem().toString();
        String descricao = ((EditText)findViewById(R.id.campo_texto_descricao)).getText().toString();
        ((EditText)findViewById(R.id.campo_texto_descricao)).setText("");

        spSalas.setSelection(0);
        spHor.setSelection(0);
        dao.adicionar(Integer.valueOf(sala), descricao, hora);
        atualizarLista();
    }
}
