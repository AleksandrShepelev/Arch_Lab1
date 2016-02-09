import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Aleksandr on 09.02.2016.
 */
public class SinkFilter extends BaseSinkFilter {

    @Override
    protected String convertToOutput(int id, double measurement) {
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampOutputFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        DecimalFormat attitudeFormat = new DecimalFormat("000000.00000");
        DecimalFormat tempFormat = new DecimalFormat("000.00000");
        DecimalFormat pressureFormat = new DecimalFormat("00.00000");
        DecimalFormat velocityFormat = new DecimalFormat("000000.00000");
        DecimalFormat bankFormat = new DecimalFormat("000000.00000");
        if (id == Frame.TIME_ID) {
            timeStamp.setTimeInMillis(Double.doubleToLongBits(measurement));
        }

        switch (id) {
            case Frame.TIME_ID:
                return timeStampOutputFormat.format(timeStamp.getTime());
            case Frame.VELOCITY_ID:
                return velocityFormat.format(measurement);
            case Frame.ATTITUDE_ID:
                return attitudeFormat.format(measurement);
            case Frame.PRESSURE_ID:
                return pressureFormat.format(measurement);
            case Frame.TEMPERATURE_ID:
                return tempFormat.format(measurement);
            case Frame.BANK_ID:
                return bankFormat.format(measurement);
            default:
                return Double.toString(measurement);
        }
    }

    @Override
    protected String getHeader(int id) {
        switch (id) {
            case Frame.TIME_ID:
                return "Time:";
            case Frame.VELOCITY_ID:
                return "Velosity(sec):";
            case Frame.ATTITUDE_ID:
                return "Attitude(m):";
            case Frame.PRESSURE_ID:
                return "Pressure(psi):";
            case Frame.TEMPERATURE_ID:
                return "Temperature(C):";
            case Frame.BANK_ID:
                return "Bank(m):";
            default:
                return "Undefined header:";
        }
    }

    @Override
    protected String getFileName() {
        return "OutputA.dat";
    }

    @Override
    protected int[] getOutputColumns() {
        int[] output = {Frame.TIME_ID, Frame.TEMPERATURE_ID, Frame.ATTITUDE_ID};
        return output;
    }
}
