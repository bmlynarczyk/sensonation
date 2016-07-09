package com.sensonation.interfaces;

import com.sensonation.application.BlindScheduler;
import com.sensonation.domain.ScheduledTask;
import com.sensonation.domain.ScheduledTaskName;
import com.sensonation.representation.TaskRepresentation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TaskController {

    private final BlindScheduler blindScheduler;

    public TaskController(BlindScheduler blindScheduler) {
        this.blindScheduler = blindScheduler;
    }


    @RequestMapping(value = "/tasks", method = GET)
    public List<TaskRepresentation> get() {
        return blindScheduler.getScheduledTasks().entrySet().stream()
                .map(this::toRepresentation)
                .collect(Collectors.toList());
    }

    private TaskRepresentation toRepresentation(Map.Entry<ScheduledTaskName, ScheduledTask> entry) {
        return TaskRepresentation.builder()
                .name(entry.getKey().name())
                .executionDate(entry.getValue().getExecutionDate())
                .build();
    }

}
