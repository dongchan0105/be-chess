package chess.gui;

import chess.board.Board;
import chess.enums.Color;
import chess.game.Game;
import chess.piece.Pawn;
import chess.piece.Piece;
import chess.record.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessGameGUI extends JFrame {
    private static final int BOARD_SIZE = 8;
    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private Board board;
    private Game game;
    private Color currentTurn = Color.WHITE;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private String selectedSource = null;

    public ChessGameGUI() {
        board = new Board();
        game = new Game(board);

        setTitle("체스 게임");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 체스판 생성
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);

        // 상태 표시 라벨 (현재 턴)
        statusLabel = new JLabel("현재 턴: 흰색(White)", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // 점수 및 종료 버튼 패널 추가
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 점수 표기 라벨
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        updateScore(); // 초기 점수 설정
        bottomPanel.add(scoreLabel, BorderLayout.NORTH);

        // 게임 종료 버튼
        JButton endButton = new JButton("게임 종료");
        endButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(endButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeBoard(JPanel boardPanel) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setPreferredSize(new Dimension(70, 70));
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 20));

                // 배경색 설정 (체스판 패턴)
                if ((row + col) % 2 == 0) {
                    buttons[row][col].setBackground(java.awt.Color.LIGHT_GRAY);
                } else {
                    buttons[row][col].setBackground(java.awt.Color.DARK_GRAY);
                }

                // 클릭 이벤트 추가
                int finalRow = row;
                int finalCol = col;
                buttons[row][col].addActionListener(e -> handleMove(finalRow, finalCol));

                boardPanel.add(buttons[row][col]);
            }
        }
        updateBoard();
    }

    private void handleMove(int row, int col) {
        String position = convertToChessNotation(row, col);
        if (selectedSource == null) {
            // 첫 클릭: 이동할 기물 선택
            if (game.isValidTurn(position, currentTurn)) {
                selectedSource = position;
                statusLabel.setText("이동할 위치를 선택하세요.");

                highlightMovablePositions(position);
            } else {
                statusLabel.setText("⚠️ 본인의 기물만 선택할 수 있습니다.");
            }
        } else {
            // 두 번째 클릭: 이동 실행
            try {
                game.move(selectedSource, position);
                updateBoard();
                updateScore(); // 점수 업데이트
                switchTurn();
                statusLabel.setText("현재 턴: " + (currentTurn == Color.WHITE ? "흰색(White)" : "검은색(Black)"));
            } catch (IllegalArgumentException ex) {
                statusLabel.setText("⚠️ " + ex.getMessage());
            }
            selectedSource = null; // 선택 초기화
        }
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void updateBoard() {
        Piece[][] boardState = board.getBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = boardState[row][col];
                buttons[row][col].setText(piece.getColor() == Color.NOCOLOR ? "" : String.valueOf(piece.getSymbol()));
            }
        }
    }

    private void updateScore() {
        double whiteScore = game.calculatePoint(Color.WHITE);
        double blackScore = game.calculatePoint(Color.BLACK);
        scoreLabel.setText(String.format("현재 점수 - 흰색: %.1f  |  검은색: %.1f", whiteScore, blackScore));
    }

    private String convertToChessNotation(int row, int col) {
        char file = (char) ('a' + col);
        int rank = BOARD_SIZE - row;
        return file + String.valueOf(rank);
    }

    private void highlightMovablePositions(String source) {
        resetHighlightedPositions(); // 기존 강조 해제

        Position sourcePosition = Board.getPosition(source);
        Piece piece = board.getBoard()[sourcePosition.yPos()][sourcePosition.xPos()];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position targetPosition = new Position(col, row);

                game.specialValidateOfPawn(targetPosition,piece);

                // 이동 가능 여부 확인
                if (piece.canMove(targetPosition)&&game.validateTargetHasSameColor(sourcePosition,targetPosition)) {
                    try {
                        buttons[row][col].setBackground(java.awt.Color.PINK); // 이동 가능 위치 표시
                    } catch (IllegalArgumentException ignored) {
                        // 경로에 기물이 있으면 무시
                    }
                }
            }
        }
    }


    private void resetHighlightedPositions() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // 체스판 패턴을 유지하면서 색상 되돌리기
                if ((row + col) % 2 == 0) {
                    buttons[row][col].setBackground(java.awt.Color.LIGHT_GRAY);
                } else {
                    buttons[row][col].setBackground(java.awt.Color.DARK_GRAY);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameGUI gui = new ChessGameGUI();
            gui.setVisible(true);
        });
    }
}
