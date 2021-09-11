package ru.javaops.masterjava.matrix;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//количество прогреваний, после которого результаты jvm можно будет принимать во внимание и начинать измерять
@Warmup(iterations = 10)
//количество самих измерений, после чего все усредняется
@Measurement(iterations = 10)
//Разные режимы, можно почитать в доке, провалившись в класс и загрузив сорцы
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
//Означает, что производится одна итерация запуска со всеми настройками. По-умолчанию итераций 10, что было
// бы более правильно и эксперимент был бы чище, но дольше ждать. Также, для чистоты эксперимента, лучше запускать
// не через IntellijIDEA плагин, а отдельно. Погрешность с использованием IDEA плагина в среднем = 2,2%.
// Также можно запускать через среду, реализовав метод main.
// Чтобы запускать не через среду, а через мавен, то в помник необходимо добавить плагин maven-shade-plugin. При
// добавлении плагина в maven, реализовывать метод main нет необходимости.
// плагин привязан к phase package, поэтому для его установки достаточно запустить package.
@Fork(1)
//Интервал, после которого отвалимся. В данном случае, 5 минут
@Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
public class MatrixBenchmark {

    //размер матрицы, можно задавать разное количество. Аннотация @Param относится как раз к аннотации класса @State,
    //
    @Param({/*"100", */"1000"})
    private int matrixSize;

    //класс с аннотацией @State
    //класс, если вложен, должен иметь модификатор static.
    //класс должен быть public
    //класс должен иметь только конструктор без параметров
    @State(Scope.Thread)
    public static class Example {

    }

    private static final int THREAD_NUMBER = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    private static int[][] matrixA;
    private static int[][] matrixB;

    //аннотация, помечающая метод инициализации (аналогично как в junit) матриц
    //МОЖЕТ РАСПОЛАГАТЬСЯ ВНУТРИ КЛАССА С АННОТАЦИЕЙ @State
    //Принимает 3 параметра: Trial, Iteration, Invocation
    @Setup
    public void setUp() {
        matrixA = MatrixUtil.create(matrixSize);
        matrixB = MatrixUtil.create(matrixSize);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MatrixBenchmark.class.getSimpleName())
                //значения ниже дублируются из аннотаций класса
                .threads(1)
                .forks(1)
                .timeout(TimeValue.minutes(5))
                .build();
        new Runner(options).run();
    }
    /**
     * аннотация, помечающая метод, в котором будет производиться сам бенчмарк. В таких методах ОБЯЗАТЕЛЬНО
     * возвращать результат, иначе измерять по сути ничего не будем
     * Также оптимизатор может использовать константы для оптимизации кода. Если значения в код не подставляются
     * динамически, то компилятор опустит эти константы. Чтобы компилятор не оптимизировал константы, необходимо
     * использовать их из объекта с аннотацией State.
     * либо 2 вариант: использовать класс Blackhole bh в параметре метода.
     * Который делает consume для нашего кода и ничего не возвращает. Консьюмов может быть много. Пример:
     *
     *     @Benchmark
     *     public int[][] singleThreadMultiply(Blackhole bh) {
     *          bh.consume(MatrixUtil.singleThreadMultiplyOpt(matrixA, matrixB));
     *     }
     *
     * OR
     *
     *     @Benchmark
     *     public void testMethod(MyState state, Blackhole blackhole) {
     *         int sum1 = state.a + state.b;
     *         int sum2 = state.a + state.a + state.b + state.b;
     *
     *         blackhole.consume(sum1);
     *         blackhole.consume(sum2);
     *     }
     *
     * OR
     *
     *     @State(Scope.Thread)
     *     public static class MyState {
     *         public int a = 1;
     *         public int b = 2;
     *     }
     *
     *
     *     @Benchmark
     *     public int testMethod(MyState state) {
     *         int sum = state.a + state.b;
     *         return sum;
     *     }
     * Для того, чтобы метод не проходил бенчмарк, достаточно убрать аннотацию.
     * Вывод: Score - вывод в аннотации @OutputTimeUnit
     */

    @Benchmark
    public int[][] singleThreadMultiply() {
        return MatrixUtil.singleThreadMultiplyOpt(matrixA, matrixB);
    }

    //аннотация, помечающая метод, в котором будет производиться сам бенчмарк
    @Benchmark
    public int[][] concurrentMultiply() throws ExecutionException, InterruptedException {
        return MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
    }

    //аннотация, помечающая метод, в котором содержится логика для корректного завершения программы, то, что выполнится
    //после выполнения самого бенчмарка
    //МОЖЕТ РАСПОЛАГАТЬСЯ ВНУТРИ КЛАССА С АННОТАЦИЕЙ @State
    //Принимает 3 параметра: Trial, Iteration, Invocation
    @TearDown
    public void tearDown() {
        executor.shutdown();
    }
}
