import java.util.LinkedList;

public class Snake {
    private LinkedList<Position> body;
    private Position direction;

    public Snake(Position startPosition) {
        body = new LinkedList<>();
        body.add(startPosition);
        body.add(new Position(startPosition.getX() - 1, startPosition.getY())); // Kezdeti méret 2
        direction = new Position(1, 0); // Kezdő irány jobbra
    }

    public LinkedList<Position> getBody() {
        return body;
    }

    public void setDirection(Position newDirection) {
        this.direction = newDirection;
    }
    public Position getDirection(){
        return direction;
    }

    public void move() {
        Position newHead = new Position(body.getFirst().getX() + direction.getX(),
                body.getFirst().getY() + direction.getY());
        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow() {
        Position newHead = new Position(body.getFirst().getX() + direction.getX(),
                body.getFirst().getY() + direction.getY());
        body.addFirst(newHead);
    }

    public boolean checkCollisionWithSelf() {
        Position head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
}
