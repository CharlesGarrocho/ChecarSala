package garrocho.checarsala;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

public class ServicoNotificacao extends Service {
    public ServicoNotificacao() {
    }

    public class ServicoBinder extends Binder {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AtividadeDAO dao = new AtividadeDAO(this);
        ArrayList<Atividade> atividades = dao.listar();

        return super.onStartCommand(intent ,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServicoBinder();
    }
}
