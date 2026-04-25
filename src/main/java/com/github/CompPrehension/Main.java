package com.github.CompPrehension;

import its.model.DomainSolvingModel;
import its.reasoner.LearningSituation;
import its.reasoner.nodes.DecisionTreeReasoner;
import its.reasoner.nodes.DecisionTreeTrace;

import java.io.FileNotFoundException;
import java.io.FileReader;

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

        LearningSituation situation = new LearningSituation(
                model.getDomainModel(),
                LearningSituation.collectDecisionTreeVariables(model.getDomainModel()),
                null
        );

        DecisionTreeTrace decisionTreeTrace= DecisionTreeReasoner.solve(
                model.getDecisionTree(),
                situation
        );
        System.out.println(decisionTreeTrace.toString());
    }
}