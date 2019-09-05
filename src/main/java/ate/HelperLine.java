package ate;

import javafx.scene.shape.Line;

@SuppressWarnings("WeakerAccess")
public class HelperLine extends Line {
    private boolean lineActive = false;
    private boolean lineHidden = false;

    public void setStartXY(double x, double y) {
        setStartX(x);
        setStartY(y);
    }

    public void setEndXY(double x, double y) {
        setEndX(x);
        setEndY(y);
    }

    public void setActive(boolean isActive) {
        lineActive = isActive;
        setVisible(lineActive && !lineHidden);
    }

    public void setHidden(boolean isHidden) {
        lineHidden = isHidden;
        setVisible(lineActive && !lineHidden);
    }

    public HelperLine() { // constructors are not inherited by default
        super();
    }

    public HelperLine(double var1, double var3, double var5, double var7) {
        super(var1, var3, var5, var7);
    }
}