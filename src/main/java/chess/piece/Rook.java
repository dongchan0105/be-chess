package chess.piece;

import chess.enums.Color;
import chess.enums.Direction;
import chess.record.Position;

import java.util.List;

import static chess.util.BoardPositionValidator.isWithinBoard;

public class Rook extends Piece {

    private Boolean isFirstMove = Boolean.TRUE;

    public Rook(Color color) {
        super(color);
    }

    private List<Direction> getMovableDirections() {
        return Direction.linearDirection(); // 상하좌우 이동 가능
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'r' : 'R';
    }

    @Override
    public double getPoint() {
        return 5.0;
    }

    @Override
    public boolean canMove(Position target) {
        for (Direction direction : getMovableDirections()) {
            if (isLinearMove(currentPosition, target, direction)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLinearMove(Position source, Position target, Direction direction) {
        int stepX = direction.getXDegree();
        int stepY = direction.getYDegree();

        int x = source.xPos() + stepX;
        int y = source.yPos() + stepY;

        while (isWithinBoard(new Position(x, y))) {
            if (x == target.xPos() && y == target.yPos()) {
                return true;
            }
            x += stepX;
            y += stepY;
        }
        return false;
    }


}
