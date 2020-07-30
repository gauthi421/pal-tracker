package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    @Autowired
    public TimeEntryController(TimeEntryRepository InMemoryTimeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = InMemoryTimeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry)
    {
        TimeEntry entry = timeEntryRepository.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @PutMapping("/time-entries/{entryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable("entryId")long timeEntryId, @RequestBody TimeEntry timeEntry)
    {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntry);
        if(updatedTimeEntry != null) {
            actionCounter.increment();
            return ResponseEntity.ok(updatedTimeEntry);
        }
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list()
    {
        actionCounter.increment();
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping("/time-entries/{entryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("entryId") long timeEntryId)
    {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if(timeEntry != null) {
            actionCounter.increment();
            return ResponseEntity.ok(timeEntry);
        }
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/time-entries/{entryId}")
    public ResponseEntity delete(@PathVariable("entryId") long timeEntryId)
    {
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        timeEntryRepository.delete(timeEntryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
