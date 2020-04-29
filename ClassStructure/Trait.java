package ClassStructure;

import java.util.Random;

public class Trait {
    private boolean patient;//will always serve time if get out of jail free is not an option
    private boolean planner;//will only purchase properties from first two groups owned
    private boolean trainAffinity;//preference for train stations
    private boolean utilityAffinity;//preference for utilities
    private boolean wildCard;//chaotic decision making
    private boolean cautious;//spending proportional to money owned | always chooses to pay over draw
    private boolean investor;//develops properties whenever possible
    private boolean twoSetAffinity;//will always purchase Brown and Deep Blue properties
    private Group plannerGroupA = null;//groups for planner to keep track of
    private Group plannerGroupB = null;
    private Group plannerGroupC = null;

    /**
     * Randomly generates traits for AI agent
     */
    public Trait() {
        Random rand = new Random();
        boolean state;
        for (int i = 0; i < 8; i++) {
            //randomly select activation of trait
            state = rand.nextInt(2) == 1;
            switch (i) {
                case 0:
                    patient = state;
                    break;
                case 1:
                    planner = state;
                    break;
                case 2:
                    trainAffinity = state;
                    break;
                case 3:
                    utilityAffinity = state;
                    break;
                case 4:
                    wildCard = state;
                    break;
                case 5:
                    cautious = state;
                    break;
                case 6:
                    investor = state;
                    break;
                case 7:
                    twoSetAffinity = state;
                    break;
            }
        }
        //maintain mutually exclusive traits
        if (wildCard) {
            cautious = false;//cannot be wild and cautious
            planner = false;
        } else if (cautious) {
            wildCard = false;//cannot be wild and cautious
            investor = false;//cannot obey cautious strategy and investor
        } else if (investor) {
            cautious = false;//cannot obey cautious strategy and investor
        } else if (planner) {
            twoSetAffinity = false;//cannot prefer brown and deep blue and be a planner
            wildCard = false;
        } else if (twoSetAffinity) {
            planner = false;//
        }
    }

    /**
     * displays the set of personality traits that have been selected
     *
     * @return string representation of trait activity
     */
    @Override
    public String toString() {
        return "Trait{" +
                "Patient=" + patient +
                ", Planner=" + planner +
                ", Train affinity=" + trainAffinity +
                ", Utility affinity=" + utilityAffinity +
                ", Wild card=" + wildCard +
                ", Cautious=" + cautious +
                ", Investor=" + investor +
                ", Two set affinity=" + twoSetAffinity +
                '}';
    }

    public boolean isPatient() {
        return patient;
    }

    public boolean isPlanner() {
        return planner;
    }

    public boolean hasTrainAffinity() {
        return trainAffinity;
    }

    public boolean hasUtilityAffinity() {
        return utilityAffinity;
    }

    public boolean isWildCard() {
        return wildCard;
    }

    public boolean isCautious() {
        return cautious;
    }

    public boolean isInvestor() {
        return investor;
    }

    public boolean hasTwoSetAffinity() {
        return twoSetAffinity;
    }

    public Group getGroupA() {
        return plannerGroupA;
    }

    public void setGroupA(Group group) {
        plannerGroupA = group;
    }

    public Group getGroupB() {
        return plannerGroupB;
    }

    public void setGroupB(Group group) {
        plannerGroupB = group;
    }

    public Group getGroupC() {
        return plannerGroupC;
    }

    public void setGroupC(Group group) {
        plannerGroupC = group;
    }

    public void setPatient(boolean patient) {
        this.patient = patient;
    }

    public void setPlanner(boolean planner) {
        this.planner = planner;
    }

    public void setTrainAffinity(boolean trainAffinity) {
        this.trainAffinity = trainAffinity;
    }

    public void setUtilityAffinity(boolean utilityAffinity) {
        this.utilityAffinity = utilityAffinity;
    }

    public void setWildCard(boolean wildCard) {
        this.wildCard = wildCard;
    }

    public void setCautious(boolean cautious) {
        this.cautious = cautious;
    }

    public void setInvestor(boolean investor) {
        this.investor = investor;
    }

    public void setTwoSetAffinity(boolean twoSetAffinity) {
        this.twoSetAffinity = twoSetAffinity;
    }
}