package com.github.CompPrehension;

import its.model.definition.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceCompactor {

    public static void compact(DomainModel model) {
        for (ObjectDef object : model.getObjects()) {
            if(!object.getClassName().equals("TargetWord")) continue;
            interpolateDataForTargetWord(object);
        }
    }

    private static ObjectDef getParentWord(ObjectDef word) {
        if(word.hasRelationshipLink("withParent")) {
            RelationshipLinkStatement parentRel = word.getRelationshipLink("withParent");
            return parentRel.getObjects().getFirst();
        }
        return null;
    }

    private static Set<ObjectDef> getSiblingWords(ObjectDef word) {
        ObjectDef parent = getParentWord(word);
        HashSet<ObjectDef> siblings = new HashSet<ObjectDef>();
        for (ObjectDef object : word.getDomainModel().getObjects()) {
            if(!object.getClassName().equals("TargetWord")) continue;
            if(getParentWord(object) == parent && object != word) siblings.add(object);
        }
        return siblings;
    }

    private static Set<ObjectDef> getAncestors(ObjectDef word) {
        HashSet<ObjectDef> ancestors = new HashSet<ObjectDef>();
        ObjectDef parent = getParentWord(word);
        while (parent != null) {
            ancestors.add(parent);
            parent = getParentWord(parent);
        }
        return ancestors;
    }

    private static void interpolateDataForTargetWord(ObjectDef word) {

        // Заполнить _ancestor
        Set<ObjectDef> ancestors = getAncestors(word);
        if(!ancestors.isEmpty()) {
            for (ObjectDef ancestor : ancestors) {
                RelationshipLinkStatement statement = new RelationshipLinkStatement(
                        word,
                        "_ancestor",
                        List.of(ancestor.getName()),
                        ParamsValues.getEMPTY()
                );
                word.getRelationshipLinks().add(statement);
            }
        }
        Set<ObjectDef> siblings = getSiblingWords(word);
        // Заполнить alwaysRightFrom
        // Заполнить alwaysLeftFrom


        System.out.println(123);
    }
}
