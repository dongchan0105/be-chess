package chess.piece;

import chess.enums.Color;
import chess.enums.Direction;
import chess.record.Position;

import java.util.List;

import static chess.util.BoardPositionValidator.isWithinBoard;

public class King extends Piece {

    private Boolean isFirstMove=Boolean.TRUE;

    public King(Color color) {
        super(color);
    }


    @Override
    public char getSymbol() {
        return isWhite() ? 'k' : 'K';
    }

    @Override
    public double getPoint() {
        return 0.0;
    }

    private List<Direction> getMovableDirections(){
        return Direction.everyDirection();
    }

    @Override
    public boolean canMove(Position target) {
        if (!isWithinBoard(target)) {
            return false;
        }

        int dx = target.xPos() - currentPosition.xPos();
        int dy = target.yPos() - currentPosition.yPos();

        for (Direction direction : getMovableDirections()) {
            if (direction.getXDegree() == dx && direction.getYDegree() == dy) {
                return true;
            }
        }
        return false;
    }


}
