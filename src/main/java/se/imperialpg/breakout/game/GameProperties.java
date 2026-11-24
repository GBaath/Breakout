package se.imperialpg.breakout.game;

public record GameProperties(int width,int height,int fps) {

    public long deltaTime(){
        return 1000 / (long)fps;
    }
}
