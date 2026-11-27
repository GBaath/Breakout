package se.imperialpg.breakout.game;

import se.imperialpg.breakout.entity.Ball;

import java.util.List;

public class CollisionHelper {
    public void checkBallIntersections(List<GameObject> objects, GameProperties gameProperties, Game game) {
        Ball ball = null;
        for (GameObject object : objects) {
            if (object instanceof Ball gameBall) {
                ball = gameBall;
                break;
            }
        }
        if (ball == null) {
            return;
        }

        for (GameObject object : objects) {
            if (object instanceof BallCollider collider && checkIntersection(ball, object)) {
                collider.onHit();
            }
        }

        int screenWidth = gameProperties.windowProperties().width();
        int screenHeight = gameProperties.windowProperties().height();
        checkIntersection(ball, 0, 0, screenWidth, 1);
        checkIntersection(ball, 0, 0, 1, screenHeight);
        checkIntersection(ball, screenWidth - 1, 0, 1, screenHeight);
        if (checkIntersection(ball, 0, screenHeight - 1, screenWidth, 1)) {
            game.endGame();
        }
    }

    private boolean checkIntersection(Ball ball, GameObject other) {
        return checkIntersection(ball, other.x, other.y, other.width, other.height);
    }

    private boolean checkIntersection(Ball ball, double x, double y, int width, int height) {
        if (ball.x >= x + width
                || ball.x + ball.width <= x
                || ball.y >= y + height
                || ball.y + ball.height <= y) {
            return false;
        }

        double intersectionLeft = Math.max(ball.x, x);
        double intersectionRight = Math.min(ball.x + ball.width, x + width);
        double intersectionTop = Math.max(ball.y, y);
        double intersectionBottom = Math.min(ball.y + ball.height, y + height);
        ball.onIntersection(
                (intersectionLeft + intersectionRight) / 2.0,
                (intersectionTop + intersectionBottom) / 2.0
        );
        return true;
    }
}
