package com.github.CompPrehension;

import its.model.DomainSolvingModel;
import its.model.definition.DomainModel;
import its.model.definition.ObjectContainer;
import its.model.definition.ObjectRef;
import its.reasoner.LearningSituation;
import its.reasoner.nodes.DecisionTreeReasoner;
import its.reasoner.nodes.DecisionTreeTrace;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            helloWorld();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void helloWorld() throws FileNotFoundException {
        DomainSolvingModel model = new DomainSolvingModel("model", DomainSolvingModel.BuildMethod.LOQI);
        DomainModel domain = model.getDomainModel();
        Map<String, ObjectRef> variables = LearningSituation.collectDecisionTreeVariables(model.getDomainModel());
        ObjectContainer objects = domain.getObjects();


        ObjectRef owner = objects.get("Alice").getReference();
        ObjectRef pet = objects.get("Basya").getReference();

        variables.put("owner", owner);
        variables.put("pet", pet);

        LearningSituation situation = new LearningSituation(
                model.getDomainModel(),
                variables,
                null
        );

        DecisionTreeTrace decisionTreeTrace = DecisionTreeReasoner.solve(
                model.getDecisionTree(),
                situation
        );
        System.out.println(decisionTreeTrace.toString());
    }
}