package org.user;

import org.request.Request;
import org.actor.*;
import org.production.*;

public interface StaffInterface {
    void addProductionSystem(Production p);

    void addActorSystem(Actor a);

    void removeProductionSystem(String name);

    void removeActorSystem(String name);

    void updateProduction(Production p);

    void updateActor(Actor a);

    void resolveRequest(Request request);
}
