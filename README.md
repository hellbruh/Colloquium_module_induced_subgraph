# Задание коллоквиума по Комбинаторике и Теории Графов.  
Разработан модуль для [wise-task-graph](https://wisetask.ru/graph/library)   

## Как запустить:  
* Скачать и распаковать / клонировать репозиторий;
* Открыть проект в Intelij Idea;  
* Готово)  

## Чтобы проверить работоспособность тестов 
в файле  _src/test/java/InducedSubgraphPropertyTest.java_  нажать <b>правой клавишей мыши на класс теста и затем   
  _'run InducedSubgraphPropertyTest.java'_</b>

## Загрузить свои тесты 
- На сайте [wise-tasks](https://wisetask.ru/graph/library) создать свой граф (для тестирования понадобятся два графа - original и subgraph;
- Для создания можно воспользоваться [инструкцией](https://docs.google.com/document/d/1kcob6nBooVZGynrmfzEr_0cpB3rNsyKDgW6m7xKGib0/edit?tab=t.0);
- Скачать его в формате json и выбрать опцию <b>для тестирования</b>;
- Поместить файлы в папку _src/test/resources_;
- Написать тест по примеру:
  ```
    @Test
    @DisplayName("Тест-пример")
    public void testExample() throws FileNotFoundException {
        Graph original = FileLoader.loadGraphFromJson("src/test/resources/originalgraphexample.json");
        Graph subgraph = FileLoader.loadGraphFromJson("src/test/resources/subgraphexample.json");

        InducedSubgraphProperty property = new InducedSubgraphProperty();
        assertThat(checkInducedPropertyAndMarking(property, original, subgraph)).isTrue();
    }
  ```
  Если созданный вами подграф является порождённым подмножеством вершин original, то
  ```
  assertThat(checkInducedPropertyAndMarking(property, original, subgraph)).isTrue();
  ```
  иначе
  ```
  assertThat(checkInducedPropertyAndMarking(property, original, subgraph)).isFalse();
  ```
- в файле  _src/test/java/InducedSubgraphPropertyTest.java_  нажать <b>правой клавишей мыши на класс теста и затем   
  _'run InducedSubgraphPropertyTest.java'_</b>;
- Если в терминале "n tests passed", то ваш тест верный и все в порядке)
- В противном случае проверьте корректность тестов и их валидацию. Не расстраивайтесь, всё обязательно получится!

## Определение  

Индуцированный подграф - это граф, образованный из подмножества вершин графа вместе со всеми рёбрами, соединяющими пары вершин из этого подмножества.
  
## Как работает алгоритм этого модуля  
Суть: Проверяет, что subgraph является индуцированным подграфом исходного  original по  множеству вершин из подграфа.  
Алгоритм:
1. Собираем Id вершин из  subgraph (getVertexList()).
2. Проходим по рёбрам original (getEdgeList()):  
   если оба конца лежат в множестве,  
   формируем «ключ» ребра (учитывая направленность) через функцию EdgeKey()  
   и добавляем его в expected;
4. Собираем аналогичный набор «ключей» из subgraph - actual.
5. Сравниваем множества. Совпали -  true, иначе false.  
  
Алгоритм использует память O(|V_s| + |E_o| + |E_s|) подмножества вершин подграфа, ребер оригинала и ребер подграфа.  
Время O(|V_s| + |E_o| + |E_s|)

## References  
- [Инстуркция](https://docs.google.com/document/d/1kcob6nBooVZGynrmfzEr_0cpB3rNsyKDgW6m7xKGib0/edit?tab=t.0);
- [Сайт wise tasks](https://wisetask.ru/graph/library).
