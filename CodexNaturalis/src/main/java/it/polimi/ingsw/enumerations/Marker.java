package it.polimi.ingsw.enumerations;

public enum Marker {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BLACK;

    public String getPath()
    {
        switch (this)
        {
            case RED -> {
                return "images/Markers/CODEX_pion_rouge.png";
            }
            case BLUE -> {
                return "images/Markers/CODEX_pion_bleu.png";
            }
            case YELLOW -> {
                return "images/Markers/CODEX_pion_jaune.png";
            }
            case GREEN -> {
                return "images/Markers/CODEX_pion_vert.png";
            }
            default ->
            {
                return null;
            }
        }
    }
}
