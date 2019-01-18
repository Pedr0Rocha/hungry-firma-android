package hungryfirma.pedrorocha.com.hungryfirma;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {

    public static String getParsedTimestamp(long timestamp) {
        Timestamp stamp = new Timestamp(timestamp);
        Date date = new Date(stamp.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
        return sdf.format(date);
    }

}
