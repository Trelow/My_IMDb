package org.utils;

import java.util.List;

import org.actor.Actor;
import org.exceptions.InvalidCommandException;
import org.production.Production;

public class ItemSelector<T> {

    public static <T> void SelectItem(List<T> items) {
        do {
            try {
                int productionNumber = 0;
                String input = "";

                System.out.print("Write the number of the production you want to see: ");
                input = InputManager.ReadLine();
                productionNumber = InputManager.ToPositiveNumber(input);

                if (productionNumber > 0 && productionNumber <= items.size()) {
                    T contribution = items.get(productionNumber - 1);
                    if (contribution instanceof Production) {
                        Production production = (Production) contribution;
                        production.displayInfo();
                    } else if (contribution instanceof Actor) {
                        Actor actor = (Actor) contribution;
                        actor.displayInfo();
                    }
                    InputManager.WaitForInput();
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }
}
