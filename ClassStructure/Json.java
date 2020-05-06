package ClassStructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Helper class to pass objects from json
 */
public class Json {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads card data from files
     * @param src source file
     * @return A list which can be shuffled with collections and converted to an ArrayDeque
     * @throws IOException potential file issues
     */
    public static List<CardEffect> fromJsonToList(String src) throws IOException {
        File in = new File(src);
        List<CardEffect> out = null;
        out = Arrays.asList(objectMapper.readValue(in, CardEffect[].class));
        return out;
    }

    /**
     * Reads board tile data from file
     * @param src source file
     * @return Hash map of iD: Board Tile
     * @throws IOException
     */
    public static HashMap<Integer, BoardTile> fromJsonToTileSet(String src) throws IOException {
        File in = new File(src);
        return objectMapper.readValue(in, TypeFactory.defaultInstance().constructMapType(HashMap.class, Integer.class, BoardTile.class));
    }

}
