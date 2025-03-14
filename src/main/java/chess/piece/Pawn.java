package chess.piece;

import chess.enums.Color;
import chess.enums.Direction;
import chess.record.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{

    private Boolean isFirstMove=true;
    private Boolean isCanAttack=false;

    public Pawn(Color color){
        super(color);
    }

    public List<Direction> getForwardDirections() {
        if (isWhite()) {
            return Direction.whitePawnForward();
        }
        return Direction.blackPawnForward();
    }

    public List<Direction> getAttackDirections() {
        return isWhite() ? Direction.whitePawnAttack() : Direction.blackPawnAttack();
    }


    @Override
    public char getSymbol() {
        if(color==Color.WHITE){
            return 'p';
        }
        else{
            return 'P';
        }
    }

    @Override
    public double getPoint() {
        return 1.0;
    }

    @Override
    public boolean canMove(Position target) {
        int dx = target.xPos() - currentPosition.xPos();
        int dy = target.yPos() - currentPosition.yPos();


        if (isFirstMove && (dx == 0 && (dy == (isWhite() ? -2 : 2)))) {
            return true;
        }

        // 이동할 수 있는 기본 방향 목록
        List<Direction> movableDirections = new ArrayList<>(getForwardDirections());

        if (isCanAttack) {
            movableDirections.addAll(getAttackDirections());
        }

        // ✅ 최종적으로 이동 가능한 방향 검사
        for (Direction direction : movableDirections) {
            if (direction.getXDegree() == dx && direction.getYDegree() == dy) {
                return true;
            }
        }

        return false;
    }

    public void setFirstMove(Boolean firstMove) {
        isFirstMove = firstMove;
    }

    public void setCanAttack(Boolean canAttack) {
        isCanAttack = canAttack;
    }
}
