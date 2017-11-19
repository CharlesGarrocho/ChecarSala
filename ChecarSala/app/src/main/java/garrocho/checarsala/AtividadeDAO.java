package garrocho.checarsala;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AtividadeDAO {

    private final String TABLE_ATIVIDADES = "Atividade";
    private DBGateway gw;

    public AtividadeDAO(Context ctx){
        gw = DBGateway.getInstance(ctx);
    }

    public boolean adicionar(int sala, String horario, String curso, String dia){
        ContentValues cv = new ContentValues();
        cv.put("sala", sala);
        cv.put("horario", horario);
        cv.put("curso", curso);
        cv.put("dia", dia);
        return gw.getDatabase().insert(TABLE_ATIVIDADES, null, cv) > 0;
    }

    public ArrayList<Atividade> listar(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Atividade ORDER BY sala ASC", null);
        ArrayList<Atividade> atividades = new ArrayList<Atividade>();
        while(cursor.moveToNext()){
            int sala = cursor.getInt(cursor.getColumnIndex("sala"));
            String horario = cursor.getString(cursor.getColumnIndex("horario"));
            String curso = cursor.getString(cursor.getColumnIndex("curso"));
            String dia = cursor.getString(cursor.getColumnIndex("dia"));
            atividades.add(new Atividade(sala, horario, curso, dia));
        }
        return atividades;

    }
}