package com.github.CompPrehension;

import its.model.DomainSolvingModel;
import its.model.definition.DomainModel;
import its.model.definition.MetaData;
import its.model.definition.ObjectContainer;
import its.model.definition.ObjectRef;
import its.reasoner.LearningSituation;
import its.reasoner.ReasoningOptions;
import its.reasoner.nodes.DecisionTreeReasoner;
import its.reasoner.nodes.DecisionTreeTrace;
import its.reasoner.nodes.DecisionTreeTraceElement;
import org.apache.jena.base.Sys;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DomainSolvingModel model = new DomainSolvingModel("packedSentence", DomainSolvingModel.BuildMethod.LOQI, false);
        DomainModel domain = model.getDomainModel();
        Map<String, ObjectRef> variables = LearningSituation.collectDecisionTreeVariables(model.getDomainModel());
        ObjectContainer objects = domain.getObjects();

        ObjectRef actSentence = objects.get("input1").getReference();
        variables.put("input", actSentence);

        LearningSituation situation = new LearningSituation(
                domain,
                variables,
                null
        );

        ReasoningOptions opt = new ReasoningOptions();

        DecisionTreeTrace trace = DecisionTreeReasoner.solve(model.getDecisionTree(), situation, opt);

        System.out.println(describeAnswer(trace));
    }

    private static String describeAnswer(DecisionTreeTrace answer) {
        StringBuilder description = new StringBuilder();

        System.out.println("Статус: " + answer.getBranchResult());

        for (DecisionTreeTraceElement<?, ?> element : answer) {
            MetaData meta = element.getNode().getMetadata();
            if(meta.containsAny("msg")) {
                description.append(meta.get("msg"));
            } else {
                description.append("Узел не предоставил объяснение");
            }
            if(element != answer.getLast()) {
                description.append("\n");
            }
        }
        return description.toString();
    }
}