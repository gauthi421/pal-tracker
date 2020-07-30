package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(TimeEntryRepository InMemoryTimeEntryRepository) {
        this.timeEntryRepository = InMemoryTimeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry)
    {
        TimeEntry entry = timeEntryRepository.create(timeEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @PutMapping("/time-entries/{entryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable("entryId")long timeEntryId, @RequestBody TimeEntry timeEntry)
    {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntry);
        if(updatedTimeEntry != null)
            return ResponseEntity.ok(updatedTimeEntry);
        else
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list()
    {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping("/time-entries/{entryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("entryId") long timeEntryId)
    {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if(timeEntry != null)
        return ResponseEntity.ok(timeEntry);
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/time-entries/{entryId}")
    public ResponseEntity delete(@PathVariable("entryId") long timeEntryId)
    {
        timeEntryRepository.delete(timeEntryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
