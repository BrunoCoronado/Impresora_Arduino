package usac.trext.impresora_arduino;

public class Coordenada {
    float x,y;
    char color, estado;

    public Coordenada(float x, float y, char color,char estado) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.estado=estado;
    }
}
