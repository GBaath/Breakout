package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;
import se.imperialpg.breakout.entity.Ball;
import se.imperialpg.breakout.entity.Block;
import se.imperialpg.breakout.entity.Paddle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionHelperTest {
    private final CollisionHelper collisionHelper = new CollisionHelper();
    private final GameProperties gameProperties = new GameProperties(
            new GameProperties.WindowProperties(800, 600, 60),
            new GameProperties.DifficultyProperties(100, 300, 3, 8)
    );

    @Test
    void reportsCentersOfPaddleAndBlockOverlaps() {
        TrackingBall ball = new TrackingBall(100, 100);
        TrackingBlock block = new TrackingBlock(108, 108);
        Game game = new Game();

        collisionHelper.checkBallIntersections(
                List.of(ball, new Paddle(110, 105), block),
                gameProperties,
                game
        );

        assertEquals(List.of(new Point2D.Double(113, 110.5), new Point2D.Double(112, 112)),
                ball.intersections);
        assertEquals(1, block.hitCount);
    }

    @Test
    void doesNotCallBlockOnHitWithoutIntersection() {
        TrackingBall ball = new TrackingBall(100, 100);
        TrackingBlock block = new TrackingBlock(200, 200);

        collisionHelper.checkBallIntersections(List.of(ball, block), gameProperties, new Game());

        assertEquals(0, block.hitCount);
    }

    @Test
    void reportsScreenEdgeOverlapCenters() {
        TrackingBall ball = new TrackingBall(0, 0);

        collisionHelper.checkBallIntersections(List.of(ball), gameProperties, new Game());

        assertEquals(List.of(new Point2D.Double(8, 0.5), new Point2D.Double(0.5, 8)),
                ball.intersections);
    }

    @Test
    void endsGameWhenBallIntersectsBottomEdge() {
        TrackingBall ball = new TrackingBall(100, 584);
        TrackingGame game = new TrackingGame();

        collisionHelper.checkBallIntersections(List.of(ball), gameProperties, game);

        assertTrue(game.endGameCalled);
        assertEquals(List.of(new Point2D.Double(108, 599.5)), ball.intersections);
    }

    private static final class TrackingBall extends Ball {
        private final List<Point2D.Double> intersections = new ArrayList<>();

        private TrackingBall(double x, double y) {
            super(x, y);
        }

        @Override
        public void onIntersection(double x, double y) {
            intersections.add(new Point2D.Double(x, y));
        }
    }

    private static final class TrackingBlock extends Block {
        private int hitCount;

        private TrackingBlock(double x, double y) {
            super(x, y);
        }

        @Override
        public void onHit() {
            hitCount++;
        }
    }

    private static final class TrackingGame extends Game {
        private boolean endGameCalled;

        @Override
        public void endGame() {
            endGameCalled = true;
        }
    }
}
