package org.example.behaviour.general;

public class ReproduceBehavior {

//    public GameObject reproduce() {
//final int MINIMUM_HEALTH_FOR_REPRODUCE = 70;
//        OrganismFactory organismFactory = OrganismFactory.getInstance();
//        GameObject sameSpecie = getCell().getResidents().get(getObjectClass()).stream().findAny().orElse(null);
////      TODO: check if cell has available space, not max amount of animals in the current cell, have two parent for child and enough health
//        if (getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie != null && availableSpaceForSpecie()) {
//            // TODO: create new animal and place it into cell
//            GameObject newAnimal = organismFactory.create(getObjectClass());
//            cell.addNewResident(getObjectClass(), newAnimal);
//            return newAnimal;
//        } else {
//           throw new IllegalArgumentException("Species not available");
//        }
//    }
//    private boolean availableSpaceForSpecie() {
//        int amountOfSpecie = getCell().getResidents().get(getObjectClass()).size();
//        return amountOfSpecie < getLimits().getMaxAmount();
//    }
}
