package garrocho.checarsala;

public class Atividade {

    private int sala;
    private String horario, curso, dia;

    public Atividade(){
    }

    public Atividade(int sala, String horario, String curso, String dia) {
        this.sala = sala;
        this.horario = horario;
        this.curso = curso;
        this.dia = dia;
    }

    public int getSala() {
        return sala;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}