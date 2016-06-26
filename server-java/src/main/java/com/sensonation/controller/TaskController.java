package com.sensonation.controller;

import com.sensonation.application.ScheduledTask;
import com.sensonation.application.ScheduledTaskName;
import com.sensonation.domain.Blind;
import com.sensonation.representation.BlindRepresentation;
import com.sensonation.representation.TaskRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class TaskController {

    private final Map<ScheduledTaskName, ScheduledTask> scheduledTaskStore;

    public TaskController(Supplier<Map<ScheduledTaskName, ScheduledTask>> scheduledTaskStoreProvider) {
        this.scheduledTaskStore = scheduledTaskStoreProvider.get();
    }


    @RequestMapping(value = "/tasks", method = GET)
    public List<TaskRepresentation> get() {
        return scheduledTaskStore.entrySet().stream()
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
