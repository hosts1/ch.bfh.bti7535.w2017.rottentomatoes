package sentimentAnalysis;

import preprocessing.*;

/**
 * Created by hk on 16.12.2017.
 */
public class SentiAnalysis {
    public static SentiWordNet sentiWordNet = new SentiWordNet("sentiwordnet.txt");

    public static TextPolarity textPolarity = new TextPolarity();
    public static TextPurity textPurity = new TextPurity();

}
