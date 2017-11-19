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

    public boolean adicionar(int sala, String descricao, String horario){
        ContentValues cv = new ContentValues();
        cv.put("sala", sala);
        cv.put("descricao", descricao);
        cv.put("horario", horario);
        return gw.getDatabase().insert(TABLE_ATIVIDADES, null, cv) > 0;
    }

    public ArrayList<Atividade> listar(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Atividade ORDER BY sala ASC", null);
        ArrayList<Atividade> atividades = new ArrayList<Atividade>();
        while(cursor.moveToNext()){
            int sala = cursor.getInt(cursor.getColumnIndex("sala"));
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            String horario = cursor.getString(cursor.getColumnIndex("horario"));
            atividades.add(new Atividade(sala,descricao, horario));
        }
        return atividades;

    }
}