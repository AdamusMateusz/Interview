package com.cdq.interview.task.dao.util;

import com.cdq.interview.task.model.TasksQuery;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListTasksDaoSQLFactory {
    public static String fromQuery(TasksQuery tasksQuery) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT task_id, input, pattern, task_status_code, is_match_found, position, typos, created_date ");
        stringBuilder.append("FROM public.tasks ");

        if (!tasksQuery.tasksIds().isEmpty()) {
            stringBuilder.append("WHERE task_id in ( ");

            String idsPlaceholders = IntStream.range(0, tasksQuery.tasksIds().size())
                    .map(i -> i + 3)
                    .mapToObj(String::valueOf)
                    .map(s -> "$" + s)
                    .collect(Collectors.joining(", "));

            stringBuilder.append(idsPlaceholders);

            stringBuilder.append(") ");
        }
        stringBuilder.append("ORDER BY id ");

        if (tasksQuery.limit() != null) {
            stringBuilder.append("LIMIT $1 ");
        }

        if (tasksQuery.offset() != null) {
            stringBuilder.append("OFFSET $2");
        }

        return stringBuilder.toString();
    }

}
