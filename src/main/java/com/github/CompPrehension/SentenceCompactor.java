package com.github.CompPrehension;

import its.model.definition.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceCompactor {

    public static void compact(DomainModel model) {

        clarifySiblingDependencies(model);

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

    private static void fillInAncestorsFor(ObjectDef word) {
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
    }

    private static Set<ObjectDef> getDependencies(DomainModel model) {
        Set<ObjectDef> dependencies = new HashSet<>();
        for (ObjectDef object : model.getObjects()) {
            if(!object.getClassName().equals("DependencyType")) continue;
            dependencies.add(object);
        }
        return dependencies;
    }

    private static boolean setDependencySiblingsOrder(ObjectDef depBefore, ObjectDef depAfter) {

        // TODO: weak for looped dependencies!

        boolean hasLikeA = false;
        for (RelationshipLinkStatement rel : depBefore.getRelationshipLinks()) {
            if(rel.getRelationshipName().equals("alwaysBefore") && rel.getObjects().contains(depAfter)) {
                hasLikeA = true;
                break;
            }
        }

        boolean hasLikeB = false;
        for (RelationshipLinkStatement rel : depAfter.getRelationshipLinks()) {
            if(rel.getRelationshipName().equals("alwaysAfter") && rel.getObjects().contains(depBefore)) {
                hasLikeB = true;
                break;
            }
        }

        if(!hasLikeA) depBefore.getRelationshipLinks().add(new RelationshipLinkStatement(depBefore, "alwaysBefore", List.of(depAfter.getName()), ParamsValues.getEMPTY()));
        if(!hasLikeB) depAfter.getRelationshipLinks().add(new RelationshipLinkStatement(depBefore, "alwaysAfter", List.of(depBefore.getName()), ParamsValues.getEMPTY()));
        return !(hasLikeA && hasLikeB);
    }

    private static void clarifySiblingDependencies(DomainModel model) {

        Set<ObjectDef> dependencies = getDependencies(model);

        int siblingsUpdated;
        do {
            siblingsUpdated = 0;
            for (ObjectDef rootDep : dependencies) {
                for (RelationshipLinkStatement relation : new HashSet<>(rootDep.getRelationshipLinks())) {
                    if(relation.getRelationshipName().equals("alwaysBefore")) {
                        if(setDependencySiblingsOrder(rootDep, relation.getObjects().getFirst()))
                            siblingsUpdated += 1;
                    } else if (relation.getRelationshipName().equals("alwaysAfter")) {
                        if(setDependencySiblingsOrder(relation.getObjects().getFirst(), rootDep))
                            siblingsUpdated += 1;
                    }
                }
            }
        } while (siblingsUpdated > 0);
    }

    private static void interpolateDataForTargetWord(ObjectDef word) {
        // Заполнить _ancestor
        fillInAncestorsFor(word);
        // Заполнить alwaysRightFrom
        // Заполнить alwaysLeftFrom
    }
}
