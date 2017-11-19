package garrocho.checarsala;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAtividades extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> atividades;

    public AdaptadorAtividades(Activity context, ArrayList<String> atividades) {
        super(context, R.layout.item_atividade, atividades);
        this.context=context;
        this.atividades = atividades;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View itemLista = inflater.inflate(R.layout.item_atividade, null,true);

        TextView rotuloSalaAtiv = (TextView) itemLista.findViewById(R.id.rotulo_sala_atividade);
        TextView rotuloHorario = (TextView) itemLista.findViewById(R.id.rotulo_horario);

        String[] atividade = atividades.get(position).split("56765");
        rotuloSalaAtiv.setText("Sala " + atividade[0] + ": " + atividade[1]);
        rotuloHorario.setText("Horário: " + atividade[2] + " - " + atividade[3]);

        return itemLista;

    };
}
