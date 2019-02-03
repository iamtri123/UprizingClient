package uprizing;

public interface ISelectable {

    /**
     * - Draggables
     * - Scrollbar
     * - Sliders
     */

    /**
     * posX += mouseX;
     * posY += mouseY;
     */
    void move(int mouseX, int mouseY, int width, int height);
}