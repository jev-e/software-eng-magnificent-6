package ClassStructure;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadInDataTest {

    @Test
    public void ReadPotLuck() throws IOException {
        List<CardEffect> potLuckPack = Json.fromJsonToList(new File("PotLuckCardData.json"));
        for(CardEffect item: potLuckPack) {
            System.out.println(item.getCardText());
        }
        System.out.println("\n"+potLuckPack.size());
        assertEquals(potLuckPack.size(),16);
    }

    @Test
    public void ReadOpportunityKnocks() throws IOException {
        List<CardEffect> opportunityKnocksPack = Json.fromJsonToList(new File("OpportunityKnocksCardData.json"));
        for(CardEffect item: opportunityKnocksPack) {
            System.out.println(item.getCardText());
        }
        System.out.println("\n"+opportunityKnocksPack.size());
        assertEquals(opportunityKnocksPack.size(),16);
    }

    @Test
    public void readTileData() throws IOException {
        HashMap<Integer, BoardTile> tileSet = Json.fromJsonToTileSet(new File("BoardTileData.json"));
        System.out.println(tileSet.size());
        BoardTile b;
        for(int i = 0; i < 40; i++) {
            b = tileSet.get(i);
            System.out.println(b.getTitle() + " " + b.iD + " " + b.canPurchase);
        }
    }
}