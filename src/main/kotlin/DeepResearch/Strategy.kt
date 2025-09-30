package org.david.DeepResearch

import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import org.david.DeepResearch.Subgraphs.subgraphExecuteDeepSearchSearchOnTopic
import org.david.DeepResearch.Subgraphs.subgraphPlanner
import org.david.DeepResearch.Subgraphs.subgraphWrite


val deepResearchStrategy =strategy<String, String>("deep-research-strategy") {
    var tasks: MutableList<String> = mutableListOf()
    var currentTaskIndex = -1
    val plannerSubgraph by subgraphPlanner()
    val querySubgraph by subgraphExecuteDeepSearchSearchOnTopic()
    val writerSubgraph by subgraphWrite()
    val findings: MutableList<String> = mutableListOf()

    val storeTasks by node<List<String>, Boolean> ("store-tasks") {
        tasks.addAll(it)
    }

    val getAndPrint by node<Boolean, Int>("get-and-print") {
        currentTaskIndex++
        if(currentTaskIndex<tasks.size) {
            print("Starting task ${currentTaskIndex+1} of ${tasks.size}: ${tasks[currentTaskIndex]}")
            currentTaskIndex
        } else {
            -1
        }
    }

    val saveToFindings by node<List<String>, Boolean>("save-to-findings") {
        print(it)
        findings.addAll(it)
        true
    }

    edge(nodeStart forwardTo plannerSubgraph)
    edge(plannerSubgraph forwardTo storeTasks)
    edge(storeTasks forwardTo getAndPrint)
    edge(querySubgraph forwardTo saveToFindings)
    edge(saveToFindings forwardTo getAndPrint)
    edge(getAndPrint forwardTo querySubgraph onCondition {it!=-1} transformed {tasks[currentTaskIndex]})
    edge(getAndPrint forwardTo writerSubgraph onCondition {it==-1} transformed {findings})
    edge(writerSubgraph forwardTo nodeFinish)

}