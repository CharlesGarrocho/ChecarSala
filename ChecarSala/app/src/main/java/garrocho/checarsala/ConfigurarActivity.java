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
import android.widget.AdapterView;
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
    Spinner spSalas, spHor, spDia, spCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("[IFSP] Configurar Salas");
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

        spDia = (Spinner) findViewById(R.id.spinner_dia);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.dias, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDia.setAdapter(adapter3);

        spCurso = (Spinner) findViewById(R.id.spinner_cursos);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurso.setAdapter(adapter4);

        dao = new AtividadeDAO(getBaseContext());
        atualizarLista();
        if (!atividades.isEmpty())
            Toast.makeText(ConfigurarActivity.this, "Para Remover: Pressione a Atividade", Toast.LENGTH_SHORT).show();
    }

    public void atualizarLista() {
        atividades = dao.listar();
        ArrayList<String> ats = new ArrayList<String>();

        for (Atividade at : atividades)
            ats.add(at.getSala() + "56765" + at.getCurso() + "56765" + at.getHorario() + "56765" + at.getDia());

        listaAtividades = (ListView)findViewById(R.id.lista_atividades_view);
        AdaptadorAtividades adaptador = new AdaptadorAtividades(this, ats);
        listaAtividades.setAdapter(adaptador);
        listaAtividades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Atividade a = atividades.get(position);
                dao.remover(a.getSala(), a.getHorario(), a.getCurso(),a.getDia());
                Toast.makeText(ConfigurarActivity.this, "Atividade Removida", Toast.LENGTH_SHORT).show();
                atualizarLista();
                MainActivity.modificado = false;
                return true;
            }
        });
    }

    public void adicionar(View comp) {
        String sala = spSalas.getSelectedItem().toString();
        String hora = spHor.getSelectedItem().toString();
        String curso = spCurso.getSelectedItem().toString();
        String dia = spDia.getSelectedItem().toString();

        if (sala.equalsIgnoreCase("Sala") || hora.equalsIgnoreCase("Horário")
                || curso.equalsIgnoreCase("Curso") || dia.equalsIgnoreCase("Dia")) {
            Toast.makeText(ConfigurarActivity.this, "Preencha Todos os Campos", Toast.LENGTH_SHORT).show();
        }
        else {
            spSalas.setSelection(0);
            spHor.setSelection(0);
            spCurso.setSelection(0);
            spDia.setSelection(0);
            dao.adicionar(Integer.valueOf(sala), hora, curso, dia);
            Toast.makeText(ConfigurarActivity.this, "Atividade Adicionada", Toast.LENGTH_SHORT).show();
            atualizarLista();
            MainActivity.modificado = false;
        }
    }
}
