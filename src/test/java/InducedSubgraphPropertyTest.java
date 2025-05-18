import ru.leti.wise.task.graph.model.Graph;
import ru.leti.wise.task.graph.model.Vertex;
import ru.leti.wise.task.graph.model.Edge;
import ru.leti.wise.task.graph.model.Color;
import ru.leti.wise.task.graph.util.FileLoader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InducedSubgraphPropertyTest {

    private boolean checkInducedPropertyAndMarking(InducedSubgraphProperty property, Graph original, Graph subgraph) {
        boolean isInduced = property.run(original, subgraph);

        // Если подграф не является индуцированным — возвращаем false
        if (!isInduced) {
            return false;
        }


        // Собираем ID всех вершин из подграфа во множество
        Set<Integer> vertexIds = new HashSet<>();
        for (Vertex v : subgraph.getVertexList()) {
            vertexIds.add(v.getId());
        }

        // Проверяем, что все ребра между вершинами подграфа в оригинальном графе покрашены в красный
        for (Edge e : original.getEdgeList()) {
            if (vertexIds.contains(e.getSource()) && vertexIds.contains(e.getTarget())) {
                if (e.getColor() != Color.RED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    @DisplayName("Тест корректного индуцированного подграфа")
    public void testCorrectInducedSubgraphFromFile() throws FileNotFoundException {
        // Загружаем оригинальный граф и корректный индуцированный подграф
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph validSubgraph = FileLoader.loadGraphFromJson("src/test/resources/valid_induced_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, validSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест подграфа с отсутствующим ребром")
    public void testMissingEdgeSubgraphFromFile() throws FileNotFoundException {
        // Загружаем оригинальный граф и подграф с отсутствующим ребром
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph invalidSubgraph = FileLoader.loadGraphFromJson("src/test/resources/invalid_induced_subgraph_missing_edge.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, invalidSubgraph)).isFalse();
    }

    @Test
    @DisplayName("Тест подграфа с лишним ребром")
    public void testExtraEdgeSubgraphFromFile() throws FileNotFoundException {
        // Загружаем оригинальный граф и подграф с лишним ребром
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph invalidSubgraph = FileLoader.loadGraphFromJson("src/test/resources/invalid_induced_subgraph_extra_edge.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, invalidSubgraph)).isFalse();
    }

    @Test
    @DisplayName("Тест подграфа из одной вершины")
    public void testSingleVertexSubgraphFromFile() throws FileNotFoundException {
        // Загружаем оригинальный граф и подграф из одной вершины
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph singleVertexSubgraph = FileLoader.loadGraphFromJson("src/test/resources/single_vertex_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, singleVertexSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест пустого подграфа")
    public void testEmptySubgraphFromFile() throws FileNotFoundException {
        // Загружаем оригинальный граф и пустой подграф
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph emptySubgraph = FileLoader.loadGraphFromJson("src/test/resources/empty_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, emptySubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест несоответствия направленности графов")
    public void testDirectednessFromFile() throws FileNotFoundException {
        // Загружаем направленный оригинальный граф и ненаправленный подграф
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/original_graph.txt");
        Graph undirectedSubgraph = FileLoader.loadGraphFromJson("src/test/resources/undirected_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, undirectedSubgraph)).isFalse();
    }

    @Test
    @DisplayName("Тест подграфа-компоненты связности")
    public void testConnectedComponentSubgraph() throws FileNotFoundException {
        // Граф с несколькими компонентами связности
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/multi_component_graph.txt");
        // Подграф — один из компонентов связности
        Graph componentSubgraph = FileLoader.loadGraphFromJson("src/test/resources/component_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, componentSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест с изолированными вершинами")
    public void testIsolatedVerticesSubgraph() throws FileNotFoundException {
        // Загружаем граф с изолированными вершинами и подграф, содержащий только их
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/graph_with_isolated_vertices.txt");
        Graph isolatedSubgraph = FileLoader.loadGraphFromJson("src/test/resources/isolated_vertices_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, isolatedSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест с петлями")
    public void testLoopEdgesSubgraph() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/graph_with_loops.txt");
        Graph loopSubgraph = FileLoader.loadGraphFromJson("src/test/resources/loop_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, loopSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест подграфа с отсутствующей петлей")
    public void testMissingLoopSubgraph() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/graph_with_loops.txt");
        Graph missingLoopSubgraph = FileLoader.loadGraphFromJson("src/test/resources/missing_loop_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, missingLoopSubgraph)).isFalse();
    }

    @Test
    @DisplayName("Тест вложенных индуцированных подграфов")
    public void testNestedInducedSubgraphs() throws FileNotFoundException {
        // Оригинальный граф
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/large_graph.txt");
        // Первый индуцированный подграф
        Graph firstSubgraph = FileLoader.loadGraphFromJson("src/test/resources/first_level_subgraph.txt");
        // Второй индуцированный подграф (подграф первого)
        Graph secondSubgraph = FileLoader.loadGraphFromJson("src/test/resources/second_level_subgraph.txt");

        InducedSubgraphProperty property1 = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property1, original, firstSubgraph)).isTrue();

        InducedSubgraphProperty property2 = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property2, original, secondSubgraph)).isTrue();

        InducedSubgraphProperty nestedProperty = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(nestedProperty, firstSubgraph, secondSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест подграфа с вершинами, имеющими одинаковые идентификаторы, но разные свойства")
    public void testVertexPropertiesSubgraph() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/graph_with_vertex_properties.txt");
        Graph modifiedPropertiesSubgraph = FileLoader.loadGraphFromJson("src/test/resources/modified_properties_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        // Проверка должна основываться только на идентификаторах вершин и ребер,
        // а не на их дополнительных свойствах
        assertThat(checkInducedPropertyAndMarking(property, original, modifiedPropertiesSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест с графом, содержащим веса ребер")
    public void testWeightedGraphSubgraph() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/weighted_graph.txt");
        Graph weightedSubgraph = FileLoader.loadGraphFromJson("src/test/resources/weighted_subgraph.txt");
        Graph weightModifiedSubgraph = FileLoader.loadGraphFromJson("src/test/resources/weight_modified_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        // Подграф с правильной структурой и оригинальными весами
        assertThat(checkInducedPropertyAndMarking(property, original, weightedSubgraph)).isTrue();

        // Подграф с правильной структурой, но измененными весами
        assertThat(checkInducedPropertyAndMarking(property, original, weightModifiedSubgraph)).isTrue();
    }

    @Test
    @DisplayName("Тест с двудольным графом")
    public void testBipartiteGraphSubgraph() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/bipartite_graph.txt");
        Graph bipartiteSubgraph = FileLoader.loadGraphFromJson("src/test/resources/bipartite_subgraph.txt");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, bipartiteSubgraph)).isTrue();
    }

//    @Test
//    @DisplayName("Тест подграфа с лишним ребром")
//    public void testAnya() throws FileNotFoundException {
//        // Загружаем оригинальный граф и подграф с лишним ребром
//        Graph original = FileLoader.loadGraphFromJson("src/test/resources/originaltestAnya.json");
//        Graph invalidSubgraph = FileLoader.loadGraphFromJson("src/test/resources/subgraphtestAnya.json");
//
//        InducedSubgraphProperty property = new InducedSubgraphProperty();
//        assertThat(checkInducedPropertyAndMarking(property, original, invalidSubgraph)).isFalse();
//    }
}
