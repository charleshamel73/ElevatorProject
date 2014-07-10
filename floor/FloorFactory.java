
/**
 *Factory Class to build Floor
 * @author charl_000
 */

package floor;

import util.InvalidDataException;

/**
 *
 * @author charl_000
 */
public class FloorFactory {    
    private FloorFactory(){}
    /**
     * Builds the floor object based on floor type
     * @param floorType 0:FloorOfficeImpl
     * @param loc what location the floor is at
     * @return reference to proper Floor Object
     */
    public static Floor build(int floorType,int loc) throws InvalidDataException{
        Floor floorPointer=null;
        switch(floorType){
            case 0:
                floorPointer= new FloorOfficeImpl(loc);
        }
        return floorPointer;
    }
}
