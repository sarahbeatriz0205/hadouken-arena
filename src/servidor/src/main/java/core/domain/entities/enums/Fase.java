package core.domain.entities.enums;

public enum Fase {
    QUARTAS_DE_FINAL,
    SEMIFINAL,
    FINAL;

    public static Fase descobrirFase(int quantidadeDePartidas) {
        switch (quantidadeDePartidas) {
            case 1: return FINAL;
            case 2: return SEMIFINAL;
            case 4: return QUARTAS_DE_FINAL;
        }
        return null;
    }
}
