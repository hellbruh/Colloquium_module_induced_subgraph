package ru.leti;

import ru.leti.wise.task.plugin.graph.GraphProperty;
import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.graph.model.Color;        // ← добавили импорт цвета

import java.util.HashSet;
//import java.util.List;
import java.util.Set;

/**
 * Проверяет, что subgraph является индуцированным подграфом
 * исходного  original по  множеству вершин из подграфа.
 * Алгоритм
 * 1 Собираем Id-шники вершин из  subgraph.
 * 2 Проходим по рёбрам original; если оба конца лежат в множестве,
 *   формируем «ключ» ребра (учитывая направленность) и добавляем его в expected.
 * 3 Собираем аналогичный набор «ключей» из subgraph - actual.
 * 4 Сравниваем множества. Совпали -  true, иначе false.
 * Алгоритм использует память 𝑂(|E_s|) подмножества ребер.
 * Время O(|V_s| + |E_o| + |E_s|)
 **/

public class InducedSubgraphProperty implements GraphProperty {

    private final Graph original;

    public InducedSubgraphProperty(Graph original) {
        this.original = original;
    }

    // Возвращает хэш-представление ребра. Для ненаправленного графа порядок
    // вершин упорядочивается, чтобы (u,v) == (v,u).
    private static long edgeKey(int u, int v, boolean directed) {
        if (!directed && v < u) {
            int t = u; u = v; v = t;
        }
        return (((long) u) << 32) | (v & 0xFFFFFFFFL);
    }

    @Override
    public boolean run(Graph subgraph) {
        if (original.isDirect() != subgraph.isDirect()) {
            return false;
        }
        boolean directed = original.isDirect();

        // Собираем id вершин подграфа
        Set<Integer> verts = new HashSet<>();
        for (Vertex v : subgraph.getVertexList()) {
            verts.add(v.getId());
        }

        // Ожидаемые рёбра (из original)
        Set<Long> expected = new HashSet<>();
        for (Edge e : original.getEdgeList()) {
            int u = e.getSource();
            int v = e.getTarget();
            if (verts.contains(u) && verts.contains(v)) {
                expected.add(edgeKey(u, v, directed));
            }
        }

        // Фактические рёбра (из subgraph)
        Set<Long> actual = new HashSet<>();
        for (Edge e : subgraph.getEdgeList()) {
            actual.add(edgeKey(e.getSource(), e.getTarget(), directed));
        }

        //проверка равенства наборов рёбер графа и подграфа между вершинами подграфа
        boolean isInduced = expected.equals(actual);
        //если подграф является порожденным (=индуцированным) графа, то ребра подграфа подсвечиваются в оригинальном графе красным
        if (isInduced) {
            for (Edge e : original.getEdgeList()) {
                if (verts.contains(e.getSource()) && verts.contains(e.getTarget())) {
                    e.setColor(Color.RED);      // отмечаем ребро красным
                }
            }
        }

        return isInduced;
    }
}
