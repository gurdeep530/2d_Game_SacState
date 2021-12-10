package org.csc133.a5.GameObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GameObjectCollection{

    public List<GameObject> gameObjectCollection;

    public GameObjectCollection()
    {
        gameObjectCollection = new ArrayList<>();
    }

    public ArrayList<GameObject> getFires() {
        return (ArrayList<GameObject>) gameObjectCollection.stream()
                .filter(x->x instanceof Fire)
                .collect(Collectors.toList());
    }
    public ArrayList<GameObject> getBuildings() {
        return (ArrayList<GameObject>) gameObjectCollection.stream()
                .filter(x->x instanceof Building)
                .collect(Collectors.toList());
    }
    public HeliPad getHeliPad() {
        return (HeliPad) gameObjectCollection.stream()
                .filter(x->x instanceof HeliPad)
                .collect(Collectors.toList()).get(0);
    }
    public PlayerHelicopter getPlayerHeli() {
        return (PlayerHelicopter) gameObjectCollection.stream()
                .filter(x->x instanceof PlayerHelicopter)
                .collect(Collectors.toList()).get(0);
    }
    public NonPlayerHelicopter getNonPlayerHeli() {
        return (NonPlayerHelicopter) gameObjectCollection.stream()
                .filter(x->x instanceof NonPlayerHelicopter)
                .collect(Collectors.toList()).get(0);
    }

    public River getRiver() {
        return (River) gameObjectCollection.stream()
                .filter(x->x instanceof River)
                .collect(Collectors.toList()).get(0);
    }

    public ArrayList<GameObject> getGameObjectCollection()
    {
        return (ArrayList<GameObject>) gameObjectCollection;
    }
}
