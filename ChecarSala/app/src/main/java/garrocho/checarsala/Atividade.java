package garrocho.checarsala;

public class Atividade {

    private int sala;
    private String descricao, horario;

    public Atividade(){
    }

    public Atividade(int sala, String descricao, String horario) {
        this.sala = sala;
        this.descricao = descricao;
        this.horario = horario;
    }

    public int getSala() {
        return sala;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atividade atividade = (Atividade) o;

        if (sala != atividade.sala) return false;
        if (descricao != null ? !descricao.equals(atividade.descricao) : atividade.descricao != null)
            return false;
        return horario != null ? horario.equals(atividade.horario) : atividade.horario == null;

    }

    @Override
    public int hashCode() {
        int result = sala;
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        result = 31 * result + (horario != null ? horario.hashCode() : 0);
        return result;
    }
}