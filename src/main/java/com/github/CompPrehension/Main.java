package com.github.CompPrehension;

import its.model.DomainSolvingModel;
import its.model.definition.DomainModel;
import its.model.definition.ObjectContainer;
import its.model.definition.ObjectRef;
import its.reasoner.LearningSituation;
import its.reasoner.nodes.DecisionTreeReasoner;
import its.reasoner.nodes.DecisionTreeTrace;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            helloWorld();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static DecisionTreeTrace isGoodPetFor(DomainSolvingModel model, String humanName, String petName) {
        DomainModel domain = model.getDomainModel();
        Map<String, ObjectRef> variables = LearningSituation.collectDecisionTreeVariables(model.getDomainModel());
        ObjectContainer objects = domain.getObjects();

        ObjectRef owner = objects.get(humanName).getReference();
        ObjectRef pet = objects.get(petName).getReference();

        variables.put("owner", owner);
        variables.put("pet", pet);

        LearningSituation situation = new LearningSituation(
                model.getDomainModel(),
                variables,
                null
        );

        DecisionTreeTrace trace = DecisionTreeReasoner.solve(
                model.getDecisionTree(),
                situation
        );
        return trace;
    }
    
    private static void helloWorld() throws FileNotFoundException {

        DomainSolvingModel model = new DomainSolvingModel("model", DomainSolvingModel.BuildMethod.LOQI);

        String[] humanNames = {"Alice", "Peter"};
        String[] petNames = {"Basya", "Tuzik", "Sharik", "Ostin"};

        for(String human : humanNames) {
            for(String pet : petNames) {
                System.out.print(human);
                System.out.print(" + ");
                System.out.print(pet);
                System.out.print(" = ");
                System.out.println(isGoodPetFor(model, human, pet).getBranchResult());
            }
        }

    }
}