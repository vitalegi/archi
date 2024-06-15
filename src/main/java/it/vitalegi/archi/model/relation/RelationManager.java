package it.vitalegi.archi.model.relation;

public class RelationManager {

    protected DirectRelations directRelations;
    protected ImplicitRelations implicitRelations;

    public RelationManager() {
        directRelations = new DirectRelations();
        implicitRelations = new ImplicitRelations();
    }

    public void addRelation(DirectRelation relation) {
        directRelations.addRelation(relation);
        implicitRelations.addRelation(relation);
    }

    public DirectRelations getDirect() {
        return directRelations;
    }

    public ImplicitRelations getImplicit() {
        return implicitRelations;
    }
}
