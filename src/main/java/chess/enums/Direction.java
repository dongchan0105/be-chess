package chess.enums;

import java.util.Arrays;
import java.util.List;

public enum Direction {
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1),

    NNE(1, 2),
    NNW(-1, 2),
    SSE(1, -2),
    SSW(-1, -2),
    EEN(2, 1),
    EES(2, -1),
    WWN(-2, 1),
    WWS(-2, -1);

    private final int xDegree;
    private final int yDegree;

    Direction(int xDegree, int yDegree) {
        this.xDegree = xDegree;
        this.yDegree = yDegree;
    }

    public int getXDegree() {
        return xDegree;
    }

    public int getYDegree() {
        return yDegree;
    }

    public static List<Direction> linearDirection() {
        return Arrays.asList(NORTH, EAST, SOUTH, WEST);
    }

    public static List<Direction> diagonalDirection() {
        return Arrays.asList(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST);
    }

    public static List<Direction> everyDirection() {
        return Arrays.asList(NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST);
    }

    public static List<Direction> knightDirection() {
        return Arrays.asList(NNE, NNW, SSE, SSW, EEN, EES, WWN, WWS);
    }

    // 🔹 폰의 전진 방향 (공격 X, 빈칸만 가능)
    public static List<Direction> whitePawnForward() {
        return Arrays.asList(SOUTH); // 한 칸 직진
    }

    public static List<Direction> blackPawnForward() {
        return Arrays.asList(NORTH); // 한 칸 직진
    }

    // 🔹 폰의 공격 방향 (대각선만, 상대 기물 있어야 가능)
    public static List<Direction> whitePawnAttack() {
        return Arrays.asList(SOUTHEAST, SOUTHWEST);
    }

    public static List<Direction> blackPawnAttack() {
        return Arrays.asList(NORTHEAST, NORTHWEST);
    }
}
