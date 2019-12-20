package nbgame.user;

public interface Movable {
    default void enterArea(Object event) {}
    default  void leaveArea(Object event) {}
    void moveOver(Object event);
    void pressButton(Object event);
    default void releaseButton(Object event) {}
    default void moveItem(Object event) {}
}
