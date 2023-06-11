import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class RandomPointGenerator {


    public static Point nextRandom(){

        Random random = new Random();
        int randomNumberX = random.nextInt(1080);
        int randomNumberY = random.nextInt(1960);
        return new Point(randomNumberX,randomNumberY);

    }


}
