package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the colors of a marker.
 */
public enum Marker {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BLACK;

    /**
     * Returns the path of the image of the marker.
     * @return the path of the image of the marker
     */
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
            case BLACK -> {
                return "images/Markers/CODEX_pion_noir.png";
            }
            default ->
            {
                return null;
            }
        }
    }
}
