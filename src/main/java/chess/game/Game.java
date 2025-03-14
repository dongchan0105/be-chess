package chess.game;

import chess.board.Board;
import chess.enums.Color;
import chess.piece.Pawn;
import chess.piece.Piece;
import chess.record.Position;
import chess.util.BoardPositionValidator;


public class Game {

    private Piece[][] board;
    private static final int BOARD_SIZE = 8;

    public Game(Board board) {
        this.board=board.getBoard();
    }

    private static Position getPosition(String location) {
        int xPos= location.charAt(0)-'a';
        int y=Character.getNumericValue(location.charAt(1));
        int yPos=BOARD_SIZE-y;
        return new Position(xPos,yPos);
    }

    public void move(String location, Piece piece){
        Position position = getPosition(location);
        if(!BoardPositionValidator.isWithinBoard(position)){
            throw new IllegalArgumentException("체스 보드판의 범위를 벗어난 값을 입력했습니다.");
        }
        piece.setCurrentPosition(position);
        board[position.yPos()][position.xPos()] = piece;
    }

    public void move(String sourcePotion, String targetPotion){
        Position sourcePosition = getPosition(sourcePotion);
        Position targetPosition = getPosition(targetPotion);
        validateSuitableMove(sourcePosition, targetPosition);
        validatePieceMoveAndChangePosition(sourcePosition, targetPosition);
    }

    private void validateSuitableMove(Position sourcePosition, Position targetPosition){
        try{
            validateTargetHasSameColor(sourcePosition, targetPosition);
            isPathClear(sourcePosition, targetPosition);
            if(!BoardPositionValidator.isWithinBoard(targetPosition))
                throw new IllegalArgumentException("체스 보드판의 범위를 벗어난 값을 입력했습니다.");
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean isPathClear(Position source, Position target) {
        int dx = Integer.compare(target.xPos(), source.xPos()); // x 방향 이동 (-1, 0, 1)
        int dy = Integer.compare(target.yPos(), source.yPos()); // y 방향 이동 (-1, 0, 1)

        int x = source.xPos() + dx;
        int y = source.yPos() + dy;

        while (x != target.xPos() || y != target.yPos()) {
            if (board[y][x] != null && board[y][x].getColor() != Color.NOCOLOR) {
                throw new IllegalArgumentException("중간에 기물을 통과할 수 없습니다."); // 중간에 기물이 있으면 이동 불가
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    public boolean validateTargetHasSameColor(Position sourcePosition, Position targetPosition){
        if(board[sourcePosition.yPos()][sourcePosition.xPos()].getColor() == board[targetPosition.yPos()][targetPosition.xPos()].getColor()){
            throw new IllegalArgumentException("같은 색상의 말로는 이동할 수 없습니다.");
        }
        return true;
    }

    private void validatePieceMoveAndChangePosition(Position sourcePosition, Position targetPosition) {
        Piece sourcePiece = board[sourcePosition.yPos()][sourcePosition.xPos()];

        specialValidateOfPawn(targetPosition, sourcePiece);

        if(!sourcePiece.canMove(targetPosition)){
            throw new IllegalArgumentException("해당 위치로 이동할 수 없는 기물입니다.");
        }

        // 이동 처리
        sourcePiece.setCurrentPosition(targetPosition);
        board[targetPosition.yPos()][targetPosition.xPos()] = sourcePiece;
        board[sourcePosition.yPos()][sourcePosition.xPos()] = Piece.createBlank();

        if (sourcePiece instanceof Pawn) {
        ((Pawn) sourcePiece).setFirstMove(false);
            ((Pawn) sourcePiece).setCanAttack(false);
        }

    }

    public void specialValidateOfPawn(Position targetPosition, Piece sourcePiece) {
        if(sourcePiece instanceof Pawn){
            Pawn pawn = (Pawn) sourcePiece;

            Color currentColor = sourcePiece.getColor();
            Color opponentColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
            boolean isEnemyPiece = board[targetPosition.yPos()][targetPosition.xPos()].getColor() == opponentColor;

            // 🔹 공격 가능한 경우 isCanAttack 활성화
            if (isEnemyPiece) {
                pawn.setCanAttack(true);
            } else {
                pawn.setCanAttack(false);
            }
        }
    }

    public double calculatePoint(Color color) {
        double point = 0;

        for (int col = 0; col < BOARD_SIZE; col++) {
            int count = 0;
            for (int row = 0; row < BOARD_SIZE; row++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color && piece instanceof Pawn) {
                    count++;
                }
            }
            if (count >= 2) point += count * 0.5;
            else point += count;
        }

        // 폰이 아닌 다른 기물들의 점수 합산
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color && !(piece instanceof Pawn)) {
                    point += piece.getPoint();
                }
            }
        }

        return point;
    }

    public boolean isValidTurn(String source, Color currentTurn) {
        Piece piece = Board.findPiece(source);
        if (piece == null) {
            return false;
        }

        return piece.getColor() == currentTurn;
    }


}
