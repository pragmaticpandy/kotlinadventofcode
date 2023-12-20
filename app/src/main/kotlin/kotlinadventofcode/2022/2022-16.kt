// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day
import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.min

const val numMinutesWithElephant = 26

class `2022-16` : Day {

    override fun runPartOneNoUI(input: String): String {
        val graph = Graph(parse(input))

        val priorityQueue: PriorityQueue<Plan> =
            PriorityQueue(1) { a: Plan, b: Plan ->
                b.heuristic(graph).compareTo(a.heuristic(graph))
            }

        priorityQueue.offer(Plan(listOf(Travel("AA"))))
        var maxPressureReleased = 0
        while (priorityQueue.isNotEmpty()) {
            val plan = priorityQueue.poll()
            plan.getNextPlans(graph).forEach {
                if (it.actions.size > 30 || it.openedValves.size == graph.valveIdsWithFlow.size) {
                    maxPressureReleased = max(maxPressureReleased, it.getPressureReleased(graph))
                } else {
                    if (it.heuristic(graph) > maxPressureReleased) priorityQueue.offer(it)
                }
            }
        }

        return maxPressureReleased.toString()
    }

    /**
     * Very slow. Finds a solution that is likely to be best in a couple minutes, but it's not
     * guaranteed to be the best one. Then it takes like 45 minutes total to verify that it actually
     * got the best one.
     * See previous commit for the commented/lots of println version for debugging.
     * I think the way to go here for improvement is to actually model the cost as the valves that
     * are shut, and then use dijkstra's
     */
    override fun runPartTwoNoUI(input: String): String {
        val graph = Graph(parse(input))

        val priorityQueue: PriorityQueue<ElephantPlan> =
            PriorityQueue(1_000_000) { a: ElephantPlan, b: ElephantPlan ->
                b.heuristic(graph).compareTo(a.heuristic(graph))
            }

        /**
         * Populate with some equal number of valves per partner to start.
         * Doing this helps with the heuristic which is overly optimistic when one partner has done nothing yet.
         * 2 or 3 seems best for my input (though 3 is too large for the example since it only has 6 non-zero valves)
         */
        val numValvesToPopulateForEach = 2

        val populateQueue = mutableListOf(ElephantPlan(listOf(Travel("AA")), listOf(Travel("AA"))))
        while (populateQueue.isNotEmpty()) {
            val plan = populateQueue.removeFirst()
            for (myNextValve in graph.valveIdsWithFlow - plan.openedValves) {
                for (elephantNextValve in graph.valveIdsWithFlow - plan.openedValves - myNextValve) {
                    val newPlan =
                        plan
                            .plusNewActionsForMe(
                                graph.getShortestPathFrom(plan.myActions.last().valveId to myNextValve) +
                                    Open(myNextValve))
                            .plusNewActionsForElephant(
                                graph.getShortestPathFrom(plan.elephantActions.last().valveId to elephantNextValve) +
                                    Open(elephantNextValve))

                    if (newPlan.openedValves.size == 2 * numValvesToPopulateForEach) {
                        priorityQueue.offer(newPlan)
                    } else {
                        populateQueue.add(newPlan)
                    }

                }
            }
        }

        var maxPressureReleased = 0
        while (priorityQueue.isNotEmpty()) {
            val plan = priorityQueue.poll()
            if ((plan.myActions.size > numMinutesWithElephant && plan.elephantActions.size > numMinutesWithElephant) ||
                plan.openedValves.size == graph.valveIdsWithFlow.size) {

                maxPressureReleased = max(maxPressureReleased, plan.getPressureReleased(graph))
            } else {
                if (plan.heuristic(graph) > maxPressureReleased) {
                    plan.getNextPlans(graph).forEach {
                        priorityQueue.offer(it)
                    }
                }
            }
        }

        return maxPressureReleased.toString()
    }

    private data class ElephantPlan(
        val myActions: List<Action>,
        val elephantActions: List<Action>,
        val openedValves: Set<String> = (myActions + elephantActions)
            .filterIsInstance<Open>()
            .map { it.valveId }
            .toSet()
    ) {

        fun plusNewActionsForMe(newActions: List<Action>): ElephantPlan {
            return ElephantPlan(
                myActions + newActions,
                elephantActions,
                openedValves + newActions.filterIsInstance<Open>().map { it.valveId })
        }

        fun plusNewActionsForElephant(newActions: List<Action>): ElephantPlan {
            return ElephantPlan(
                myActions,
                elephantActions + newActions,
                openedValves + newActions.filterIsInstance<Open>().map { it.valveId })
        }

        fun getPressureReleased(graph: Graph): Int {
            var flow = 0
            var pressureReleased = 0
            for (i in 1..numMinutesWithElephant) {
                pressureReleased += flow
                if (i in myActions.indices && myActions[i] is Open) {
                    flow += graph.valvesById[myActions[i].valveId]!!.flowRate
                }
                if (i in elephantActions.indices && elephantActions[i] is Open) {
                    flow += graph.valvesById[elephantActions[i].valveId]!!.flowRate
                }
            }

            return pressureReleased
        }

        fun heuristic(graph: Graph): Int {
            val openValvesQueue: PriorityQueue<Valve> =
                PriorityQueue(15) { a: Valve, b: Valve -> b.flowRate.compareTo(a.flowRate) }

            (graph.valveIdsWithFlow - openedValves)
                .map { graph.valvesById[it]!! }
                .forEach { openValvesQueue.add(it) }

            val myHeuristicActions: MutableList<Action> = mutableListOf(myActions[0])
            val elephantHeuristicActions: MutableList<Action> = mutableListOf(elephantActions[0])
            for (i in 1..numMinutesWithElephant) {
                if (i in myActions.indices) myHeuristicActions.add(myActions[i])
                else if (openValvesQueue.isEmpty()) break
                else {
                    /**
                     * best possible scenario is alternating between min moves and one open
                     * with the highest flow rate valves first
                     */
                    if ((1..graph.minStepsBetweenValvesWithFlow).all { myHeuristicActions[i - it] is Travel })
                        myHeuristicActions.add(Open(openValvesQueue.poll().id))
                    else myHeuristicActions.add(Travel(openValvesQueue.peek().id))
                }

                if (i in elephantActions.indices) elephantHeuristicActions.add(elephantActions[i])
                else if (openValvesQueue.isEmpty()) break
                else {
                    /**
                     * best possible scenario is alternating between min moves and one open
                     * with the highest flow rate valves first
                     */
                    if ((1..graph.minStepsBetweenValvesWithFlow).all { elephantHeuristicActions[i - it] is Travel })
                        elephantHeuristicActions.add(Open(openValvesQueue.poll().id))
                    else elephantHeuristicActions.add(Travel(openValvesQueue.peek().id))
                }
            }

            return ElephantPlan(myHeuristicActions, elephantHeuristicActions).getPressureReleased(graph)
        }

        /**
         * Basically choose from the positive-flow valves we haven't been to yet.
         */
        fun getNextPlans(graph: Graph): Iterable<ElephantPlan> {
            val result: MutableList<ElephantPlan> = mutableListOf()
            if (myActions.size <= numMinutesWithElephant)
                result +=
                    Plan(myActions, openedValves)
                        .getNextPlans(graph)
                        .map { ElephantPlan(it.actions, elephantActions, it.openedValves) }

            if (elephantActions.size <= numMinutesWithElephant)
                result +=
                    Plan(elephantActions, openedValves)
                        .getNextPlans(graph)
                        .map { ElephantPlan(myActions, it.actions, it.openedValves) }

            return result
        }
    }

    private data class Plan(
        val actions: List<Action>,
        val openedValves: Set<String> = actions
            .filterIsInstance<Open>()
            .map { it.valveId }
            .toSet()
    ) {

        fun plus(newActions: List<Action>): Plan {
            return Plan(
                actions + newActions,
                openedValves + newActions.filterIsInstance<Open>().map { it.valveId })
        }

        fun getPressureReleased(graph: Graph): Int {
            var flow = 0
            var pressureReleased = 0
            for (i in 1..30) {
                pressureReleased += flow
                if (i in actions.indices && actions[i] is Open) {
                    flow += graph.valvesById[actions[i].valveId]!!.flowRate
                }
            }

            return pressureReleased
        }

        fun heuristic(graph: Graph): Int {
            val openValvesQueue: PriorityQueue<Valve> =
                PriorityQueue(15) { a: Valve, b: Valve -> b.flowRate.compareTo(a.flowRate) }

            (graph.valveIdsWithFlow - openedValves)
                .map { graph.valvesById[it]!! }
                .forEach { openValvesQueue.add(it) }

            val heuristicActions: MutableList<Action> = mutableListOf(actions[0])
            for (i in 1..30) {
                if (i in actions.indices) heuristicActions.add(actions[i])
                else if (openValvesQueue.isEmpty()) break
                else {
                    /**
                     * best possible scenario is alternating between one move and one open
                     * with the highest flow rate valves first
                     */
                    if (heuristicActions[i - 1] is Travel) heuristicActions.add(Open(openValvesQueue.poll().id))
                    else heuristicActions.add(Travel(openValvesQueue.peek().id))
                }
            }

            return Plan(heuristicActions).getPressureReleased(graph)
        }

        /**
         * Basically choose from the positive-flow valves we haven't been to yet.
         */
        fun getNextPlans(graph: Graph): Iterable<Plan> {
            return (graph.valveIdsWithFlow - openedValves)
                .map { plus(graph.getShortestPathFrom(actions.last().valveId to it) + Open(it)) }
        }
    }

    private sealed interface Action {
        val valveId: String
    }

    private data class Travel(override val valveId: String) : Action
    private data class Open(override val valveId: String) : Action

    private class Graph(val valves: Iterable<Valve>) {
        val valveIdsWithFlow = valves.filter { it.flowRate > 0 }.map { it.id }.toSet()
        val valvesById = valves.associateBy { it.id }
        val shortestPaths: MutableMap<Pair<Valve, Valve>, List<Travel>> = mutableMapOf()
        val minStepsBetweenValvesWithFlow: Int by lazy {
            var minSteps = Int.MAX_VALUE
            valveIdsWithFlow.forEach { a ->
                (valveIdsWithFlow - a).forEach { b ->
                    minSteps = min(minSteps, getShortestPathFrom(a to b).size)
                }
            }

            minSteps
        }

        fun getShortestPathFrom(pair: Pair<String, String>): List<Travel> {
            val a = valvesById[pair.first]!!
            val b = valvesById[pair.second]!!
            return shortestPaths.getOrPut(a to b) load@{
                val queue: MutableList<List<Valve>> = mutableListOf(listOf(a))
                while (queue.isNotEmpty()) {
                    val path = queue.removeFirst()
                    path.last().connectedValves.map { valvesById[it]!! }.forEach { valve ->
                        val newPath = path + valve
                        if (valve == b) {
                            return@load newPath.subList(1, newPath.size).map { Travel(it.id) }
                        }
                        queue.add(newPath)
                    }
                }

                throw Exception("not found")
            }
        }
    }


    private data class Valve(val id: String, val flowRate: Int, val connectedValves: Set<String>)

    private fun parse(input: String): List<Valve> {
        val grammar = object : Grammar<List<Valve>>() {

            /*
             * Tokens must be declared by themselves—i.e. they must be a declared property in this object.
             * Declaration order will be used in the case that multiple tokens match.
             */
            val newlineLit by literalToken("\n")
            val valveLit by literalToken("Valve ")
            val hasFlowLit by literalToken(" has flow rate=")
            val valveIdSeparator by literalToken(", ")
            val tunnelsLit by regexToken("; tunnels? leads? to valves? ")
            val valveIdRegex by regexToken("[A-Z][A-Z]")
            val positiveIntRegex by regexToken("\\d+")

            /*
             * Intermediate parsers.
             */
            val positiveInt by positiveIntRegex use { text.toInt() }
            val valveId by valveIdRegex map { it.text }
            val valve by skip(valveLit) and valveId and skip(hasFlowLit) and positiveInt and
                skip(tunnelsLit) and separatedTerms(valveId, valveIdSeparator) map {
                Valve(it.t1, it.t2, it.t3.toSet())
            }

            /*
             * Root parser.
             */
            override val rootParser by separatedTerms(valve, newlineLit)
        }

        return grammar.parseToEnd(input)
    }

    override val defaultInput = """Valve EJ has flow rate=25; tunnel leads to valve MC
Valve WC has flow rate=0; tunnels lead to valves OW, RU
Valve NP has flow rate=0; tunnels lead to valves VR, KL
Valve AA has flow rate=0; tunnels lead to valves QT, AP, EZ, AK, XV
Valve VO has flow rate=6; tunnels lead to valves KM, RF, HS, LJ, IA
Valve CB has flow rate=0; tunnels lead to valves UI, UP
Valve TE has flow rate=18; tunnel leads to valve JT
Valve CZ has flow rate=0; tunnels lead to valves UP, OW
Valve LJ has flow rate=0; tunnels lead to valves DV, VO
Valve UP has flow rate=7; tunnels lead to valves SK, CB, CZ
Valve FP has flow rate=0; tunnels lead to valves OW, RE
Valve KM has flow rate=0; tunnels lead to valves SE, VO
Valve DV has flow rate=0; tunnels lead to valves LJ, UM
Valve FL has flow rate=0; tunnels lead to valves AH, TS
Valve VR has flow rate=24; tunnels lead to valves DM, TF, NP
Valve IA has flow rate=0; tunnels lead to valves VS, VO
Valve RF has flow rate=0; tunnels lead to valves VO, JF
Valve RT has flow rate=0; tunnels lead to valves UM, SE
Valve RU has flow rate=0; tunnels lead to valves AR, WC
Valve SE has flow rate=4; tunnels lead to valves GU, KM, CX, RT
Valve MC has flow rate=0; tunnels lead to valves EJ, AR
Valve TF has flow rate=0; tunnels lead to valves AH, VR
Valve CX has flow rate=0; tunnels lead to valves SE, TO
Valve GL has flow rate=11; tunnels lead to valves UY, KL, CY
Valve GU has flow rate=0; tunnels lead to valves SE, EZ
Valve VS has flow rate=0; tunnels lead to valves XN, IA
Valve EZ has flow rate=0; tunnels lead to valves AA, GU
Valve GK has flow rate=0; tunnels lead to valves FI, HZ
Valve JT has flow rate=0; tunnels lead to valves TE, XN
Valve DM has flow rate=0; tunnels lead to valves VR, HZ
Valve AR has flow rate=16; tunnels lead to valves UI, RU, MC
Valve XN has flow rate=9; tunnels lead to valves XP, JT, VS, GT, CY
Valve CY has flow rate=0; tunnels lead to valves XN, GL
Valve QT has flow rate=0; tunnels lead to valves UM, AA
Valve KL has flow rate=0; tunnels lead to valves GL, NP
Valve SK has flow rate=0; tunnels lead to valves XV, UP
Valve OW has flow rate=12; tunnels lead to valves CZ, WC, FP
Valve AK has flow rate=0; tunnels lead to valves AA, HS
Valve XV has flow rate=0; tunnels lead to valves AA, SK
Valve GT has flow rate=0; tunnels lead to valves XN, UM
Valve FI has flow rate=0; tunnels lead to valves JF, GK
Valve UY has flow rate=0; tunnels lead to valves JF, GL
Valve UM has flow rate=5; tunnels lead to valves DV, GT, RT, QT
Valve IQ has flow rate=0; tunnels lead to valves HZ, AH
Valve JF has flow rate=10; tunnels lead to valves RF, FI, UY, RE, TS
Valve TS has flow rate=0; tunnels lead to valves JF, FL
Valve AH has flow rate=23; tunnels lead to valves IQ, FL, TF
Valve HS has flow rate=0; tunnels lead to valves AK, VO
Valve HZ has flow rate=20; tunnels lead to valves IQ, DM, GK
Valve TO has flow rate=15; tunnel leads to valve CX
Valve XP has flow rate=0; tunnels lead to valves AP, XN
Valve AP has flow rate=0; tunnels lead to valves XP, AA
Valve RE has flow rate=0; tunnels lead to valves JF, FP
Valve UI has flow rate=0; tunnels lead to valves AR, CB"""
}