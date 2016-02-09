import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidar on 09.02.2016.
 */
public class Utils {

    public static ArrayList<Frame> InsertIntoSortedList(ArrayList<Frame> list, Frame element, int sortParam){

        boolean isInserted = false;

        for (int i=0; i < list.size(); i++){
           if(list.get(i).getData().get(sortParam) > element.getData().get(sortParam)){
               list.add(element);
               isInserted=true;
               break;
           }else {
               continue;
           }
        }

        if (!isInserted) {
            list.add(element);
        }
        return list;
    }
}
