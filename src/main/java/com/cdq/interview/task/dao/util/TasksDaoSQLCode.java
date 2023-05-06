package com.cdq.interview.task.dao.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TasksDaoSQLCode {

    public String INSERT_TASK = "INSERT INTO public.tasks(task_id, input, pattern, task_status_code) VALUES ($1, $2, $3, $4)";
    public String UPDATE_TASK = "UPDATE public.tasks SET task_status_code = $1, is_match_found = $2, position = $3, typos = $4 WHERE task_id = $5";

}
