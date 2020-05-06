package ClassStructure;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadInDataTest {

    @Test
    public void ReadPotLuck() throws IOException {
        List<CardEffect> potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        for(CardEffect item: potLuckPack) {
            System.out.println(item.getCardText());
        }
        System.out.println("\n"+potLuckPack.size());
        assertEquals(potLuckPack.size(),16);
    }

    @Test
    public void ReadOpportunityKnocks() throws IOException {
        List<CardEffect> opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
        for(CardEffect item: opportunityKnocksPack) {
            System.out.println(item.getCardText());
        }
        System.out.println("\n"+opportunityKnocksPack.size());
        assertEquals(opportunityKnocksPack.size(),16);
    }

    @Test
    public void readTileData() throws IOException {
        HashMap<Integer, BoardTile> tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        System.out.println(tileSet.size());
        BoardTile b;
        for(int i = 0; i < 40; i++) {
            b = tileSet.get(i);
            System.out.println(b.getTitle() + " " + b.iD + " " + b.canPurchase);
        }
    }

    @Test
    public void confirmDataIntegrity() throws IOException {
        HashMap<Integer, BoardTile> tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        Property testProp = (Property)tileSet.get(1);
        assertEquals(testProp.getiD(),1);
        assertEquals(testProp.getTitle(),"Crapper Street");
        assertEquals(testProp.getGroup(),Group.BROWN);
        assertEquals(testProp.getCost(),60);
        assertEquals(testProp.getRent(),2);
        int[] testRents = {10,30,90,160};
        assertArrayEquals(testProp.getBuildingRents(), testRents);
        assertEquals(testProp.getHotelRent(),250);

        Property testProp2 = (Property)tileSet.get(37);
        assertEquals(testProp2.getiD(),37);
        assertEquals(testProp2.getTitle(),"Hawking Way");
        assertEquals(testProp2.getGroup(),Group.DEEP_BLUE);
        assertEquals(testProp2.getCost(),350);
        assertEquals(testProp2.getRent(),35);
        int[] testRents2 = {175,500,1100,1300};
        assertArrayEquals(testProp2.getBuildingRents(), testRents2);
        assertEquals(testProp2.getHotelRent(),1500);
    }

    @Test
    public void canBuySetting() throws IOException {
        HashMap<Integer, BoardTile> tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        BoardTile b;
        for(int i = 0; i < 40; i++) {
            b = tileSet.get(i);
            if(b instanceof Property) {
                assertTrue(b.canPurchase);
            }else if(b instanceof Station) {
                assertTrue(b.canPurchase);
            }else if(b instanceof Utility) {
                assertTrue(b.canPurchase);
            }else{
                assertFalse(b.canPurchase);
            }
        }
    }
}