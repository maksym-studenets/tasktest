package co.inventorsoft.tasktest.handler;

import co.inventorsoft.tasktest.model.TaskScheduleRule;
import co.inventorsoft.tasktest.provider.CsvTaskDataProvider;
import co.inventorsoft.tasktest.provider.DateProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoTaskHandlerTest {

    @Mock
    private ActionHandler actionHandler;

    @Mock
    private CsvTaskDataProvider csvTaskDataProvider;

    @Mock
    private DateProvider dateProvider;

    @InjectMocks
    private DemoTaskHandler demoTaskHandler;

    @ParameterizedTest
    @MethodSource("actionExecutedOnceArgsProvider")
    void actionExecutedOnceTest(LocalDateTime currentDateTime, List<TaskScheduleRule> taskScheduleRules) {

        when(dateProvider.getCurrentDateTime(any(ZoneId.class)))
                .thenReturn(currentDateTime);

        when(csvTaskDataProvider.getTaskScheduleRules())
                .thenReturn(taskScheduleRules);

        demoTaskHandler.executeDemoTask();

        InOrder order = inOrder(dateProvider, actionHandler, csvTaskDataProvider);
        order.verify(dateProvider).getCurrentDateTime(ZoneId.of("Africa/Lagos"));
        order.verify(csvTaskDataProvider).getTaskScheduleRules();
        order.verify(actionHandler).performAction();
        order.verifyNoMoreInteractions();
    }

    private static Stream<Arguments> actionExecutedOnceArgsProvider() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 10, 18, 10, 30),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 16),
                                new TaskScheduleRule(LocalTime.of(12, 0), 24)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 15, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 2),
                                new TaskScheduleRule(LocalTime.of(12, 0), 20)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 19, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 1),
                                new TaskScheduleRule(LocalTime.of(12, 0), 84),
                                new TaskScheduleRule(LocalTime.of(10, 30), 42)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("actionExecutedTwiceArgsProvider")
    void actionExecutedTwiceTest(LocalDateTime currentDateTime, List<TaskScheduleRule> taskScheduleRules) {

        when(dateProvider.getCurrentDateTime(any(ZoneId.class)))
                .thenReturn(currentDateTime);

        when(csvTaskDataProvider.getTaskScheduleRules())
                .thenReturn(taskScheduleRules);

        demoTaskHandler.executeDemoTask();

        InOrder order = inOrder(dateProvider, actionHandler, csvTaskDataProvider);
        order.verify(dateProvider).getCurrentDateTime(ZoneId.of("Africa/Lagos"));
        order.verify(csvTaskDataProvider).getTaskScheduleRules();
        order.verify(actionHandler, times(2)).performAction();
        order.verifyNoMoreInteractions();
    }

    private static Stream<Arguments> actionExecutedTwiceArgsProvider() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 10, 18, 10, 30),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 16),
                                new TaskScheduleRule(LocalTime.of(12, 0), 24),
                                new TaskScheduleRule(LocalTime.of(10, 30), 24)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 15, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 6),
                                new TaskScheduleRule(LocalTime.of(12, 0), 34),
                                new TaskScheduleRule(LocalTime.of(10, 30), 18)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 19, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 33),
                                new TaskScheduleRule(LocalTime.of(12, 0), 33),
                                new TaskScheduleRule(LocalTime.of(10, 30), 96)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("actionNotExecutedArgsProvider")
    void actionNotExecutedTest(LocalDateTime currentDateTime, List<TaskScheduleRule> taskScheduleRules) {

        when(dateProvider.getCurrentDateTime(any(ZoneId.class)))
                .thenReturn(currentDateTime);

        when(csvTaskDataProvider.getTaskScheduleRules())
                .thenReturn(taskScheduleRules);

        demoTaskHandler.executeDemoTask();

        InOrder order = inOrder(dateProvider, actionHandler, csvTaskDataProvider);
        order.verify(dateProvider).getCurrentDateTime(ZoneId.of("Africa/Lagos"));
        order.verify(csvTaskDataProvider).getTaskScheduleRules();
        order.verifyNoMoreInteractions();
        verifyNoInteractions(actionHandler);
    }

    private static Stream<Arguments> actionNotExecutedArgsProvider() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 10, 18, 10, 30),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 40), 16),
                                new TaskScheduleRule(LocalTime.of(12, 0), 16),
                                new TaskScheduleRule(LocalTime.of(10, 30), 96),
                                new TaskScheduleRule(LocalTime.of(10, 30), 15)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 15, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 5),
                                new TaskScheduleRule(LocalTime.of(12, 0), 2),
                                new TaskScheduleRule(LocalTime.of(11, 30), 42)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 19, 10, 30, 2),
                        List.of(
                                new TaskScheduleRule(LocalTime.of(10, 30), 65),
                                new TaskScheduleRule(LocalTime.of(12, 0), 36),
                                new TaskScheduleRule(LocalTime.of(10, 30), 30)
                        )
                )
        );
    }

}