package jPlus.async.concurrency;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class IterableImage implements Iterable<Integer> {

    public final static double BASE_TOLERANCE = 0.000007;
    public final double tolerance;

    private final BufferedImage image;
    private final int imageWidth;
    private final int imageHeight;

    public IterableImage(BufferedImage image) {
        this(image, BASE_TOLERANCE);
    }

    public IterableImage(BufferedImage image, double tolerance) {
        super();
        this.image = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        this.tolerance = tolerance > 1 ? 1 : tolerance;
    }

    protected Map<Integer, Integer> getReducedColors() {
        final Map<Integer, Integer> normalMap = new HashMap<>();

        for (Integer rgbI : this) {
            final int rgbINormal = (int) (Math.floor(rgbI * tolerance) / tolerance);
            final Integer prevRGBI = normalMap.get(rgbINormal);
            if (prevRGBI == null) {
                normalMap.put(rgbINormal, rgbI);
            } else {
                final Integer newRGBI =
                        Math.abs(rgbINormal - prevRGBI) < Math.abs(rgbINormal - rgbI)
                                ? prevRGBI
                                : rgbI;
                normalMap.put(rgbINormal, newRGBI);
            }
        }
        return normalMap;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<Integer> {

        private int x = 0, y = 0;

        @Override
        public boolean hasNext() {
            return y < imageHeight - 1;
        }

        @Override
        public Integer next() {
            x += 1;
            if (x >= imageWidth) {
                x = 0;
                y += 1;
            }
            return image.getRGB(x, y);
        }
    }
}
